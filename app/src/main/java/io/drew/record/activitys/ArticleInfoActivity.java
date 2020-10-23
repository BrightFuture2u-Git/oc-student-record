package io.drew.record.activitys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.adapters.CommentAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.dialog.SureDialog;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.service.bean.response.CommentsInfo;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.util.SoftKeyBoardListener;
import io.drew.record.view.CreateImgDialog;
import io.drew.record.view.CustomLoadView;
import io.drew.record.view.MyNineGridLayout;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ArticleInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.recycleView_comment)
    protected RecyclerView recycleView_comment;
    @BindView(R.id.et_input)
    protected EditText et_input;
    @BindView(R.id.iv_like)
    protected ImageView iv_like;
    @BindView(R.id.iv_collection)
    protected ImageView iv_collection;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;
    @BindView(R.id.iv_delect)
    protected ImageView iv_delect;
    @BindView(R.id.iv_share)
    protected ImageView iv_share;

    protected ImageView iv_head;
    protected MyNineGridLayout layout_nine_grid;
    protected TextView tv_name;
    protected TextView tv_time;
    protected TextView tv_title_comment;
    protected TextView tv_content;
    protected TextView tv_like;
    protected ImageView iv_head_like;
    protected ImageView iv_head1;
    protected ImageView iv_head2;
    protected ImageView iv_head3;
    protected ImageView tv_more;
    protected LinearLayout ll_more;
    //    private Articles.RecordsBean recordsBean;
    private int articleId;
    private LinearLayoutManager layoutManager;
    private AppService appService;
    private Articles.RecordsBean articleInfo;
    private CommentsInfo commentsInfo;
    private CommentAdapter commentAdapter;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;
    private View headView;
    private String loacalUuid;
    private int position;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_article_info;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        articleId = intent.getExtras().getInt("articleId");
        position = intent.getExtras().getInt("position");
        loacalUuid = (String) MySharedPreferencesUtils.get(this, ConfigConstant.SP_USER_ID, "");
        if (articleId <= 0) finish();
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        appService.getArticleInfo(articleId).enqueue(new BaseCallback<>(result -> {
            if (result != null) {
                articleInfo = result;
                reflash();
            }
        }, throwable -> {
            MyLog.e("帖子详情获取失败" + throwable.getMessage());
        }));

        getComments();
    }

    private void reflash() {
        GlideUtils.loadUserHead(this,articleInfo.getHeadImage(),iv_head);
        tv_name.setText(articleInfo.getNickname());
        tv_time.setText(articleInfo.getFormatTime());
        tv_content.setText(articleInfo.getContent());
        tv_like.setText(articleInfo.getLikeNum() + "人点赞");
        iv_head_like.setImageResource(articleInfo.getIsLiked() == 1 ? R.drawable.ic_article_liked : R.drawable.ic_article_like);
        iv_like.setImageResource(articleInfo.getIsLiked() == 1 ? R.drawable.ic_article_liked : R.drawable.ic_article_like);
        iv_collection.setImageResource(articleInfo.getIsCollected() == 1 ? R.drawable.ic_item_collectioned : R.drawable.ic_item_collection);

        layout_nine_grid.setSpacing(5);
        layout_nine_grid.setUrlList(articleInfo.getImageList());


        List<String> heads = articleInfo.getUserHeadList();
        if (heads != null && heads.size() > 0) {
            if (heads.size() == 1) {
                GlideUtils.loadImg(this,heads.get(0),iv_head1);
                iv_head1.setVisibility(View.VISIBLE);
                iv_head2.setVisibility(View.GONE);
                iv_head3.setVisibility(View.GONE);
                tv_more.setVisibility(View.GONE);
            } else if (heads.size() == 2) {
                GlideUtils.loadImg(this,heads.get(0),iv_head1);
                GlideUtils.loadImg(this,heads.get(1),iv_head2);
                iv_head1.setVisibility(View.VISIBLE);
                iv_head2.setVisibility(View.VISIBLE);
                iv_head3.setVisibility(View.GONE);
                tv_more.setVisibility(View.GONE);
            } else if (heads.size() == 3) {
                GlideUtils.loadImg(this,heads.get(0),iv_head1);
                GlideUtils.loadImg(this,heads.get(1),iv_head2);
                GlideUtils.loadImg(this,heads.get(2),iv_head3);
                iv_head1.setVisibility(View.VISIBLE);
                iv_head2.setVisibility(View.VISIBLE);
                iv_head3.setVisibility(View.VISIBLE);
                if (articleInfo.getLikeNum() > 3) {
                    tv_more.setVisibility(View.VISIBLE);
                } else {
                    tv_more.setVisibility(View.GONE);
                }

            } else {
                GlideUtils.loadImg(this,heads.get(0),iv_head1);
                GlideUtils.loadImg(this,heads.get(1),iv_head2);
                GlideUtils.loadImg(this,heads.get(2),iv_head3);
                iv_head1.setVisibility(View.VISIBLE);
                iv_head2.setVisibility(View.VISIBLE);
                iv_head3.setVisibility(View.VISIBLE);
                tv_more.setVisibility(View.VISIBLE);
            }
            ll_more.setVisibility(View.VISIBLE);
        } else {
            ll_more.setVisibility(View.GONE);
        }

        if (loacalUuid.equals(articleInfo.getUserId())) {
            iv_delect.setVisibility(View.VISIBLE);
            iv_share.setVisibility(View.VISIBLE);
        } else {
            iv_delect.setVisibility(View.INVISIBLE);
            iv_share.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void initView() {
//        initActionBar("社区", true);
        hideActionBar();
        headView = getLayoutInflater().inflate(R.layout.head_article_info, null);
        iv_head = headView.findViewById(R.id.iv_head);
        layout_nine_grid = headView.findViewById(R.id.layout_nine_grid);
        tv_name = headView.findViewById(R.id.tv_name);
        tv_time = headView.findViewById(R.id.tv_time);
        tv_title_comment = headView.findViewById(R.id.tv_title_comment);
        tv_content = headView.findViewById(R.id.tv_content);
        tv_like = headView.findViewById(R.id.tv_like);
        iv_head_like = headView.findViewById(R.id.iv_head_like);
        iv_head1 = headView.findViewById(R.id.iv_head1);
        iv_head2 = headView.findViewById(R.id.iv_head2);
        iv_head3 = headView.findViewById(R.id.iv_head3);
        tv_more = headView.findViewById(R.id.tv_more);
        ll_more = headView.findViewById(R.id.ll_more);

        tv_like.setOnClickListener(this);
        iv_head_like.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        relay_back.setOnClickListener(this);
        iv_delect.setOnClickListener(this);
        iv_share.setOnClickListener(this);

        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“发送”键*/
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (EduApplication.instance.authInfo == null) {
                        goLogin();
                        return true;
                    }
                    String content = et_input.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastManager.showDiyShort("请输入回复内容");
                        return true;
                    }
                    SoftKeyBoardListener.hideInput(io.drew.record.activitys.ArticleInfoActivity.this);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("articleId", String.valueOf(articleId));
                    map.put("content", content);
                    map.put("id", "0");
                    map.put("status", "0");
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            new JSONObject(map).toString());
                    appService.comment(body).enqueue(new BaseCallback<>(result -> {
                        currentPage = 1;
                        getComments();
                        et_input.setText("");
                        et_input.setHint("说点什么吧～");
                    }, throwable -> {
                        MyLog.e("点赞失败" + throwable.getMessage());
                    }));
                    return true;
                }
                return false;
            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentAdapter = new CommentAdapter(this, R.layout.item_comment, new ArrayList<>());
        recycleView_comment.setLayoutManager(layoutManager);
        recycleView_comment.setAdapter(commentAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) recycleView_comment.getItemAnimator()).setSupportsChangeAnimations(false);
        //        // 打开或关闭加载更多功能（默认为true）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMore(true);
        // 是否自定加载下一页（默认为true）
        commentAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 当数据不满一页时，是否继续自动加载（默认为true）
        commentAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
//        // 所有数据加载完成后，是否允许点击（默认为false）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(false);
//        // 是否处于加载中
//        articlesAdapter.getLoadMoreModule().isLoading();
//        // 预加载的位置（默认为1）
        commentAdapter.getLoadMoreModule().setPreLoadNumber(1);
        commentAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        commentAdapter.addHeaderView(headView);

        commentAdapter.addChildClickViewIds(R.id.tv_like, R.id.iv_like);
        commentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentsInfo.RecordsBean comment = (CommentsInfo.RecordsBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_like:
                    case R.id.iv_like:
                        appService.likeComment(comment.getId()).enqueue(new BaseCallback<>(result -> {
                            if ("true".equals(result)) {
                                comment.setIsLiked(1);
                                comment.setLikeNum(comment.getLikeNum() + 1);
                            } else {
                                comment.setIsLiked(0);
                                comment.setLikeNum(comment.getLikeNum() - 1);
                            }
                            commentAdapter.notifyItemChanged(position + 1);
                        }, throwable -> {
                            MyLog.e("点赞失败" + throwable.getMessage());
                        }));
                        break;
                }
            }
        });
        //隐藏加载完成tip
        commentAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadView(false));

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                iv_collection.setVisibility(View.GONE);
                iv_like.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                iv_collection.setVisibility(View.VISIBLE);
                iv_like.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getComments() {
        appService.getCommentList(articleId, currentPage, PAGE_SIZE).enqueue(new BaseCallback<>(commentsInfo -> {
            if (commentsInfo != null) {
                this.commentsInfo = commentsInfo;
                tv_title_comment.setText("评论（" + commentsInfo.getTotal() + "）");
                if (currentPage == 1) {
                    commentAdapter.setNewData(commentsInfo.getRecords());
                } else {
                    commentAdapter.addData(commentsInfo.getRecords());
                }
                if (currentPage < commentsInfo.getPages()) {
                    currentPage += 1;
                    commentAdapter.getLoadMoreModule().loadMoreComplete();
                } else {
                    commentAdapter.getLoadMoreModule().loadMoreEnd();
                }
            }

        }, throwable -> {
            MyLog.e("评论列表获取失败" + throwable.getMessage());
            commentAdapter.getLoadMoreModule().loadMoreFail();
        }));
    }

    private void loadMore() {
        if (commentsInfo != null && commentsInfo.getPages() >= currentPage) {
            getComments();
        } else {
            MyLog.e("loadMore--------没有更多数据了");
        }
    }

    public void onClick(View view) {
        if (view.getId() != R.id.relay_back && EduApplication.instance.authInfo == null) {
            goLogin();
            return;
        }
        switch (view.getId()) {
            case R.id.tv_like:
            case R.id.iv_like:
            case R.id.iv_head_like:
                appService.like(articleId).enqueue(new BaseCallback<>(result -> {
                    if ("true".equals(result)) {
                        articleInfo.setIsLiked(1);
                        articleInfo.setLikeNum(articleInfo.getLikeNum() + 1);
                        articleInfo.getUserHeadList().add(articleInfo.getHeadImage());
                    } else {
                        articleInfo.setIsLiked(0);
                        articleInfo.setLikeNum(articleInfo.getLikeNum() - 1);
                        articleInfo.getUserHeadList().remove(articleInfo.getHeadImage());
                    }
                    reflash();
                }, throwable -> {
                    MyLog.e("点赞失败" + throwable.getMessage());
                }));
                break;

            case R.id.iv_collection:
                appService.collect(articleId).enqueue(new BaseCallback<>(result -> {
                    if ("true".equals(result)) {
                        articleInfo.setIsCollected(1);
                        ToastManager.showDiyShort("已收藏");
                    } else {
                        articleInfo.setIsCollected(0);
                        ToastManager.showDiyShort("已取消收藏");
                    }
                    iv_collection.setImageResource(articleInfo.getIsCollected() == 1 ? R.drawable.ic_item_collectioned : R.drawable.ic_item_collection);
                }, throwable -> {
                    MyLog.e("收藏失败" + throwable.getMessage());
                }));
                break;
            case R.id.relay_back:
                SoftKeyBoardListener.hideInput(io.drew.record.activitys.ArticleInfoActivity.this);
                finish();
                break;
            case R.id.iv_delect:
                SureDialog sureDialog = new SureDialog(io.drew.record.activitys.ArticleInfoActivity.this, "确定删除帖子");
                sureDialog.setOnSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sureDialog.dismiss();
                        appService.delect(articleId).enqueue(new BaseCallback<>(result -> {
                            ToastManager.showDiyShort("删除成功");
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            intent.setAction("action_delect_article");
                            LocalBroadcastManager.getInstance(io.drew.record.activitys.ArticleInfoActivity.this).sendBroadcast(intent);
                            finish();
                        }, throwable -> {
                            MyLog.e("删除帖子失败" + throwable.getMessage());
                        }));
                    }
                });
                sureDialog.show();
                break;
            case R.id.iv_share:
                CreateImgDialog.showCreateImgDialog(io.drew.record.activitys.ArticleInfoActivity.this, articleInfo);
                break;

        }
    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
//                hideSoftInput(v.getWindowToken());
                SoftKeyBoardListener.hideInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

}
