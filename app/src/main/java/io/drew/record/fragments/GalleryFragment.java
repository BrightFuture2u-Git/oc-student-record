package io.drew.record.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.ArticleInfoActivity;
import io.drew.record.activitys.HomeActivity;
import io.drew.record.activitys.MyArticlesActivity;
import io.drew.record.activitys.MyCollectionsActivity;
import io.drew.record.activitys.MyMessageActivity;
import io.drew.record.adapters.ArticlesAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.dialog.CommentDialog;
import io.drew.record.dialog.SureDialog;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.service.bean.response.MessageCount;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.util.SoftKeyBoardListener;
import io.drew.record.view.CreateImgDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import q.rorbin.badgeview.QBadgeView;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/4 5:11 PM
 * 创作分享
 */
public class GalleryFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.article_recycleView)
    protected RecyclerView article_recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;
    private AppService appService;
    private int currentPage = 1;
    private int pageSize = 10;
    private Articles articles;
    private ArticlesAdapter articlesAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayout line_item_comment;
    private EditText et_input;
    private View itemView;
    private Dialog commentInputDialog;

    private MessageCount messageCount;
    private LinearLayout line_myarticles;
    private LinearLayout line_mymsg;
    private TextView tv_title_new_message;
    private LinearLayout line_mycollection;

    private QBadgeView badge;
    //只有两个标记同时满足，才进行数据加载
    private BroadcastReceiver localReceiverl;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onEventBusMessage(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_LOGIN:
                if (articlesAdapter != null) {
                    articlesAdapter.loacalUserId = (String) MySharedPreferencesUtils.get(context, ConfigConstant.SP_USER_ID, "");
                }
                getMessageData();
                break;
            case ConfigConstant.EB_NEW_ARTICLE:
                currentPage = 1;
                articles = null;
                getArticles();
                break;
        }
    }

    private void getMessageData() {
        appService.getNewMessageCount().enqueue(new BaseCallback<>(messageCount -> {
            if (messageCount != null) {
                this.messageCount = messageCount;
                if (messageCount.getLikeNum() + messageCount.getCommentNum() > 0) {
                    badge.setBadgeNumber(-1);
                } else {
                    badge.hide(false);
                }
            }
        }, throwable -> {
            MyLog.e("新消息总数获取失败" + throwable.getMessage());
        }));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_gallery;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(localReceiverl);
    }

    @Override
    protected void initData() {
        localReceiverl = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("action_new_article")) {
                    currentPage = 1;
                    articles = null;
                    getArticles();
                }
                if (intent.getAction().equals("action_delect_article")) {
                    int position = intent.getIntExtra("position", 0);
                    articlesAdapter.remove(position);
                }
            }
        };
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action_new_article");
        intentFilter.addAction("action_delect_article");
        localBroadcastManager.registerReceiver(localReceiverl, intentFilter);
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);

        if (EduApplication.instance.authInfo != null) {
            getMessageData();
        }
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initView() {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        articlesAdapter = new ArticlesAdapter(getContext(), R.layout.item_articles, new ArrayList<>());
        article_recycleView.setLayoutManager(layoutManager);
        article_recycleView.setAdapter(articlesAdapter);
        article_recycleView.setItemViewCacheSize(5);
        articlesAdapter.getLoadMoreModule().setPreLoadNumber(4);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) article_recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        articlesAdapter.getLoadMoreModule().setAutoLoadMore(true);
        articlesAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        articlesAdapter.getLoadMoreModule().setPreLoadNumber(1);
        articlesAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        View head = getLayoutInflater().inflate(R.layout.head_gallery, null);
        line_myarticles = head.findViewById(R.id.line_myarticles);
        line_mymsg = head.findViewById(R.id.line_mymsg);
        tv_title_new_message = head.findViewById(R.id.tv_title_new_message);
        line_mycollection = head.findViewById(R.id.line_mycollection);

        line_myarticles.setOnClickListener(this);
        line_mymsg.setOnClickListener(this);
        line_mycollection.setOnClickListener(this);

        articlesAdapter.addHeaderView(head);
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
                Articles.RecordsBean bean = (Articles.RecordsBean) adapter.getData().get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("articleId", bean.getId());
                bundle.putInt("position", position);
                intent.setClass(context, ArticleInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        articlesAdapter.addChildClickViewIds(R.id.iv_share, R.id.iv_delect, R.id.iv_collection, R.id.tv_comment, R.id.tv_like);
        articlesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (EduApplication.instance.authInfo == null) {
                    goLogin();
                    return;
                }
                Articles.RecordsBean recordsBean = articlesAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_share:
                        CreateImgDialog.showCreateImgDialog((HomeActivity) context, recordsBean);
                        break;
                    case R.id.iv_delect:
                        SureDialog sureDialog = new SureDialog(getContext(), "确定删除帖子");
                        sureDialog.setOnSureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sureDialog.dismiss();
                                appService.delect(recordsBean.getId()).enqueue(new BaseCallback<>(result -> {
                                    articlesAdapter.getData().remove(recordsBean);
                                    articlesAdapter.notifyItemChanged(position + 1);
                                    ToastManager.showDiyShort("删除成功");
                                }, throwable -> {
                                    MyLog.e("删除帖子失败" + throwable.getMessage());
                                }));
                            }
                        });
                        sureDialog.show();
                        break;
                    case R.id.iv_collection:
                        if (recordsBean.getIsCollected() == 1) {
                            SureDialog collectDialog = new SureDialog(getContext(), "确定取消收藏");
                            collectDialog.setOnSureListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    collectDialog.dismiss();
                                    appService.collect(recordsBean.getId()).enqueue(new BaseCallback<>(result -> {
                                        if ("false".equals(result)) {
                                            recordsBean.setIsCollected(0);
                                            articlesAdapter.notifyItemChanged(position + 1);
                                        }
                                    }, throwable -> {
                                        MyLog.e("取消收藏失败" + throwable.getMessage());
                                    }));
                                }
                            });
                            collectDialog.show();
                        } else {
                            appService.collect(recordsBean.getId()).enqueue(new BaseCallback<>(result -> {
                                if ("true".equals(result)) {
                                    recordsBean.setIsCollected(1);
                                    ToastManager.showDiyShort("已收藏");
                                }
                                articlesAdapter.notifyItemChanged(position + 1);
                            }, throwable -> {
                                MyLog.e("收藏失败" + throwable.getMessage());
                            }));
                        }

                        break;
                    case R.id.tv_comment:
                        showInput(getActivity(), position);
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
                            articlesAdapter.notifyItemChanged(position + 1);
                        }, throwable -> {
                            MyLog.e("点赞失败" + throwable.getMessage());
                        }));
                        break;
                }
            }
        });

        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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

        badge = new QBadgeView(getActivity());
        badge.setBadgeBackgroundColor(Color.parseColor("#F24724"));
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.bindTarget(tv_title_new_message);
    }


    private void getArticles() {
        appService.getArticles(currentPage, pageSize, 1).enqueue(new BaseCallback<>(articles -> {
            if (articles != null) {
                this.articles = articles;
                MyLog.e("画廊列表获取成功");
                if (currentPage == 1) {
                    refreshLayout.finishRefresh();
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
            refreshLayout.finishRefresh(false);
            MyLog.e("画廊列表获取失败" + throwable.getMessage());
            articlesAdapter.getLoadMoreModule().loadMoreFail();
        }));
    }

    private void loadMore() {
        if (articles != null && articles.getPages() >= currentPage) {
            getArticles();
        } else {
            MyLog.e("loadMore--------没有更多数据了");
        }
    }

    private void showInput(Context context, int position) {
        itemView = article_recycleView.getChildAt(position + 1 - layoutManager.findFirstVisibleItemPosition());
        commentInputDialog = new CommentDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        commentInputDialog.setContentView(R.layout.dialog_comment_input);
        et_input = commentInputDialog.findViewById(R.id.et_input);
        TextView tv_send = commentInputDialog.findViewById(R.id.tv_send);
        ScrollView scrollView = commentInputDialog.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    SoftKeyBoardListener.hideSoftKeyBoardDialog(getActivity());
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
                    SoftKeyBoardListener.hideSoftKeyBoardDialog(getActivity());
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
                        articlesAdapter.notifyItemChanged(position + 1);
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

    @Override
    public void onClick(View view) {
        if (EduApplication.instance.authInfo == null) {
            goLogin();
            return;
        }
        switch (view.getId()) {
            case R.id.line_myarticles:
                startActivity(new Intent(context, MyArticlesActivity.class));
                break;
            case R.id.line_mymsg:
                badge.hide(false);
                Intent messageIntent = new Intent();
                messageIntent.setClass(context, MyMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("messageCount", messageCount);
                messageIntent.putExtras(bundle);
                startActivity(messageIntent);
                break;
            case R.id.line_mycollection:
                startActivity(new Intent(context, MyCollectionsActivity.class));
                break;
        }
    }
}
