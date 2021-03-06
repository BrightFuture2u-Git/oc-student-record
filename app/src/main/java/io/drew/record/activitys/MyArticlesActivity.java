package io.drew.record.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.adapters.ArticlesAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.dialog.CommentDialog;
import io.drew.record.dialog.SureDialog;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.util.SoftKeyBoardListener;
import io.drew.record.view.CreateImgDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 我的动态
 */
public class MyArticlesActivity extends BaseActivity {
    @BindView(R.id.article_recycleView)
    protected RecyclerView article_recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    private ArticlesAdapter articlesAdapter;
    private LinearLayoutManager layoutManager;
    private AppService appService;
    private Articles articles;
    private int currentPage = 1;
    private int pageSize = 10;

    private EditText et_input;
    private View itemView;
    private Dialog commentInputDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_articles;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        getArticles();
    }

    @Override
    protected void initView() {
        initActionBar("我的动态", true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        articlesAdapter = new ArticlesAdapter(this, R.layout.item_articles, new ArrayList<>());
        article_recycleView.setLayoutManager(layoutManager);
        article_recycleView.setAdapter(articlesAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) article_recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        article_recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        // 是否自定加载下一页（默认为true）
        articlesAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 当数据不满一页时，是否继续自动加载（默认为true）
        articlesAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
//        // 预加载的位置（默认为1）
        articlesAdapter.getLoadMoreModule().setPreLoadNumber(1);
        articlesAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        View empty = getLayoutInflater().inflate(R.layout.view_empty, null);
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        articlesAdapter.setEmptyView(empty);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                articles = null;
                getArticles();
            }
        });
        articlesAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Articles.RecordsBean recordsBean = (Articles.RecordsBean) adapter.getData().get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("articleId", recordsBean.getId());
                intent.setClass(MyArticlesActivity.this, ArticleInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        articlesAdapter.addChildClickViewIds(R.id.iv_share, R.id.iv_delect, R.id.tv_comment, R.id.tv_like);
        articlesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Articles.RecordsBean recordsBean = articlesAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_share:
                        CreateImgDialog.showCreateImgDialog(MyArticlesActivity.this, recordsBean);
                        break;
                    case R.id.iv_delect:
                        SureDialog sureDialog = new SureDialog(MyArticlesActivity.this, "确定删除帖子");
                        sureDialog.setOnSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sureDialog.dismiss();
                                appService.delect(recordsBean.getId()).enqueue(new BaseCallback<>(result -> {
                                    articlesAdapter.getData().remove(recordsBean);
                                    articlesAdapter.notifyItemChanged(position);
                                    ToastManager.showDiyShort("删除成功");
                                }, throwable -> {
                                    MyLog.e("删除帖子失败" + throwable.getMessage());
                                }));
                            }
                        });
                        sureDialog.show();
                        break;
                    case R.id.tv_comment:
                        showInput(MyArticlesActivity.this, position);
                        break;
                    case R.id.tv_like:
                        appService.like(recordsBean.getId()).enqueue(new BaseCallback<>(result -> {
                            if ("true".equals(result)) {
                                recordsBean.setIsLiked(1);
                                recordsBean.setLikeNum(recordsBean.getLikeNum() + 1);
                            } else {
                                recordsBean.setIsLiked(0);
                                recordsBean.setLikeNum(recordsBean.getLikeNum() - 1);
                            }
                            articlesAdapter.notifyItemChanged(position);
                        }, throwable -> {
                            MyLog.e("点赞失败" + throwable.getMessage());
                        }));
                        break;
                }
            }
        });

        SoftKeyBoardListener.setListener(MyArticlesActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                MyLog.e("键盘高度=" + height);
            }

            @Override
            public void keyBoardHide(int height) {
                if (commentInputDialog != null && commentInputDialog.isShowing()) {
                    commentInputDialog.dismiss();
                }
            }
        });
    }

    private void showInput(Context context, int position) {
        itemView = article_recycleView.getChildAt(position - layoutManager.findFirstVisibleItemPosition());
        commentInputDialog = new CommentDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        commentInputDialog.setContentView(R.layout.dialog_comment_input);
        et_input = commentInputDialog.findViewById(R.id.et_input);
        TextView tv_send = commentInputDialog.findViewById(R.id.tv_send);
        ScrollView scrollView = commentInputDialog.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    SoftKeyBoardListener.hideSoftKeyBoardDialog(MyArticlesActivity.this);
                return true;
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastManager.showDiyShort("请输入回复内容");
                } else {
                    SoftKeyBoardListener.hideSoftKeyBoardDialog(MyArticlesActivity.this);
                    Articles.RecordsBean recordsBean = articlesAdapter.getData().get(position);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("articleId", String.valueOf(recordsBean.getId()));
                    map.put("content", content);
                    map.put("id", "0");
                    map.put("status", "0");
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            new JSONObject(map).toString());
                    appService.comment(body).enqueue(new BaseCallback<>(result -> {
                        et_input.setText("");
                        et_input.setHint("说点什么吧～");
                        recordsBean.setCommentNum(recordsBean.getCommentNum() + 1);
                        articlesAdapter.notifyItemChanged(position);
                        ToastManager.showDiyShort("评论成功");
                    }, throwable -> {
                        MyLog.e("评论失败" + throwable.getMessage());
                    }));
                }
            }
        });
        LinearLayout line_item_comment = commentInputDialog.findViewById(R.id.line_item_comment);
        commentInputDialog.show();
        et_input.requestFocus();

        article_recycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] position = new int[2];
                int[] position_item = new int[2];
                line_item_comment.getLocationOnScreen(position);
                itemView.getLocationOnScreen(position_item);
                int ss = position_item[1] + itemView.getHeight() - position[1];
                article_recycleView.smoothScrollBy(0, ss);
            }
        }, 300);
    }

    private void getArticles() {
        appService.getMyAllArticles(currentPage, pageSize).enqueue(new BaseCallback<>(articles -> {
            if (articles != null) {
                this.articles = articles;
                MyLog.e("画廊列表获取成功");
                if (currentPage == 1) {
                    refreshLayout.finishRefresh(true);
                    articlesAdapter.setNewData(articles.getRecords());
                } else {
                    articlesAdapter.addData(articles.getRecords());
                }
                if (currentPage < articles.getPages()) {
                    currentPage += 1;
                    articlesAdapter.getLoadMoreModule().loadMoreComplete();
                } else {
                    articlesAdapter.getLoadMoreModule().loadMoreEnd();
                }
            }

        }, throwable -> {
            MyLog.e("画廊列表获取失败" + throwable.getMessage());
            articlesAdapter.getLoadMoreModule().loadMoreFail();
            refreshLayout.finishRefresh(false);
        }));
    }

    private void loadMore() {
        if (articles != null && articles.getPages() >= currentPage) {
            getArticles();
        } else {
            MyLog.e("loadMore--------没有更多数据了");
        }
    }
}
