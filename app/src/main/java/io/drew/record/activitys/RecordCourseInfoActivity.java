package io.drew.record.activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.RecordCourseInfo;
import io.drew.record.util.AppUtil;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.RichHtmlUtil;
import io.drew.record.util.TagHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 录播课详情页
 */
public class RecordCourseInfoActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    protected NestedScrollView scrollView;
    @BindView(R.id.iv_lecture)
    protected ImageView iv_lecture;
    @BindView(R.id.tv_lectureName)
    protected TextView tv_lectureName;
    @BindView(R.id.tv_ages)
    protected TextView tv_ages;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.tv_lectureTime)
    protected TextView tv_lectureTime;
    @BindView(R.id.btn_order)
    protected Button btn_order;

    @BindView(R.id.relay_top2)
    protected RelativeLayout line_top2;
    @BindView(R.id.relay_actionbar)
    protected RelativeLayout relay_actionbar;
    @BindView(R.id.relay_head)
    protected RelativeLayout relay_head;

    @BindView(R.id.tv_learn)
    protected TextView tv_learn;
    @BindView(R.id.tv_hadCourseWare)
    protected TextView tv_hadCourseWare;
    @BindView(R.id.tv_seeForever)
    protected TextView tv_seeForever;

    @BindView(R.id.tv_collect)
    protected TextView tv_collect;

    @BindView(R.id.btn_share)
    protected ImageView btn_share;
    @BindView(R.id.iv_share)
    protected ImageView iv_share;
    @BindView(R.id.realy_share)
    protected RelativeLayout realy_share;
    @BindView(R.id.relay_back2)
    protected RelativeLayout relay_back2;
    @BindView(R.id.line_tags)
    protected LinearLayout line_tags;

    @BindView(R.id.tab)
    protected CommonTabLayout tab;
    @BindView(R.id.line_tab)
    protected LinearLayout line_tab;

    @BindView(R.id.tv_price_new)
    protected TextView tv_price_new;
    @BindView(R.id.tv_price_old)
    protected TextView tv_price_old;
    @BindView(R.id.webView)
    protected WebView webView;
    //    @BindView(R.id.tv_des)
//    protected TextView tv_des;
    @BindView(R.id.tv_menu)
    protected TextView tv_menu;
    @BindView(R.id.tv_comments)
    protected TextView tv_comments;

    @BindView(R.id.line_watch)
    protected LinearLayout line_watch;

    private String courseId;
    private RecordCourseInfo recordCourseInfo;
    private String[] titles = {"课程简介", "课程目录", "学员评价"};
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private int screenHeight;
    private int statusBarHeight;

    private boolean tagFlag = false;
    private int lastTagIndex = 0;
    private boolean content2NavigateFlagInnerLock = false;

    private int height;//偏移量


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record_courseinfo;
    }

    private void getDataInfo() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).getRecordCourseInfo(String.valueOf(courseId)).enqueue(new BaseCallback<>(recordCourseInfo -> {
            if (recordCourseInfo != null) {
                this.recordCourseInfo = recordCourseInfo;
                GlideUtils.loadImg(this, recordCourseInfo.getCoverImage(), iv_lecture);
                tv_lectureName.setText(recordCourseInfo.getName());
                tv_lectureTime.setText("共 " + recordCourseInfo.getLectureNum() + " 节课");
                title.setText(recordCourseInfo.getName());
                tv_ages.setText(recordCourseInfo.getMinAge() + "~" + recordCourseInfo.getMaxAge() + "岁");
                tv_price_new.setText("¥" + recordCourseInfo.getPriceStr());
                tv_price_old.setText("原价¥" + recordCourseInfo.getOriginalPriceStr());
                tv_learn.setText("已有 " + recordCourseInfo.getBuyNum() + " 人报名");
                if (recordCourseInfo.getIsCollect() == 1) {
                    Drawable collect_drawable = getResources().getDrawable(R.drawable.ic_lecture_collectioned);
                    tv_collect.setCompoundDrawablesWithIntrinsicBounds(collect_drawable, null, null, null);
                } else {
                    Drawable collect_drawable = getResources().getDrawable(R.drawable.ic_collection_lecture);
                    tv_collect.setCompoundDrawablesWithIntrinsicBounds(collect_drawable, null, null, null);
                }
                if (recordCourseInfo.getHadCourseWare() == 1) {
                    tv_hadCourseWare.setVisibility(View.VISIBLE);
                } else {
                    tv_hadCourseWare.setVisibility(View.GONE);
                }
                if (recordCourseInfo.getSeeForever() == 1) {
                    tv_seeForever.setVisibility(View.VISIBLE);
                } else {
                    tv_seeForever.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(recordCourseInfo.getIntroduction())) {
//                    new RichHtmlUtil(tv_des, recordCourseInfo.getIntroduction());
                    webView.loadDataWithBaseURL(null, recordCourseInfo.getIntroduction(), "text/html", "utf-8", null);
//                    webView.loadUrl("https://image.brightfuture360.com/course/20200828153325tPgj2uNlIHRBoekfrz.webm");
                }
                if (!TextUtils.isEmpty(recordCourseInfo.getCatalog())) {
                    new RichHtmlUtil(tv_menu, recordCourseInfo.getCatalog());
                }
                if (!TextUtils.isEmpty(recordCourseInfo.getEvaluation())) {
                    new RichHtmlUtil(tv_comments, recordCourseInfo.getEvaluation());
                }

                if (recordCourseInfo.getIsBuy() == 1) {
                    line_watch.setVisibility(View.VISIBLE);
                    btn_order.setVisibility(View.GONE);
                } else {
                    line_watch.setVisibility(View.GONE);
                    btn_order.setVisibility(View.VISIBLE);
                }
            }
        }, throwable -> {
            MyLog.e("课程详情获取失败" + throwable.getMessage());
            if ("课程不存在".equals(throwable.getMessage())) {
                finish();
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataInfo();
    }

    @Override
    protected void initData() {
        courseId = getIntent().getStringExtra("courseId");
        screenHeight = AppUtil.getScreenHeight(this);
        statusBarHeight = AppUtil.getStatusBarHeight(this);
//        getDataInfo();
    }

    @Override
    protected void initView() {
        hideActionBar();
        setStatusBarFullTransparent();
        relay_back2.setOnClickListener(this::onClick);
        line_tab.setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        //不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        for (int i = 0; i < titles.length; i++) {
            int finalI = i;
            CustomTabEntity tabEntity = new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return titles[finalI];
                }

                @Override
                public int getTabSelectedIcon() {
                    return 0;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            };
            tabs.add(tabEntity);
        }
        tab.setTabData(tabs);
        tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tagFlag = false;
                switch (position) {
                    case 0:
                        scrollView.scrollTo(0, webView.getTop() + height);
                        break;
                    case 1:
                        scrollView.scrollTo(0, tv_menu.getTop() + height);
                        break;
                    case 2:
                        scrollView.scrollTo(0, tv_comments.getTop() + height);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) relay_actionbar.getLayoutParams();
        lp.topMargin = AppUtil.getStatusBarHeight(this);
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            tagFlag = true;
            int[] position = new int[2];
            int[] position_des = new int[2];
            int[] position_menu = new int[2];
            int[] position_comment = new int[2];
            int[] position_line_tab = new int[2];
            relay_back2.getLocationOnScreen(position);
            webView.getLocationOnScreen(position_des);
            tv_menu.getLocationOnScreen(position_menu);
            tv_comments.getLocationOnScreen(position_comment);
            line_tab.getLocationOnScreen(position_line_tab);
            if (position[1] < (-relay_back2.getHeight())) {
                relay_actionbar.setVisibility(View.VISIBLE);
                line_tab.setVisibility(View.VISIBLE);
                height = relay_head.getHeight() - statusBarHeight - relay_actionbar.getHeight() - line_tab.getHeight();
                customStatusBar();
            } else {
                relay_actionbar.setVisibility(View.GONE);
                line_tab.setVisibility(View.GONE);
                setStatusBarFullTransparent();
            }

            if (line_tab.getVisibility() == View.VISIBLE) {

                if (scrollY >= tv_comments.getTop() + height) {
                    refreshContent2NavigationFlag(2);
                } else if (scrollY >= tv_menu.getTop() + height) {
                    refreshContent2NavigationFlag(1);
                } else {
                    refreshContent2NavigationFlag(0);
                }
            }
        });
    }

    /**
     * 刷新标签
     *
     * @param currentTagIndex 当前模块位置
     */
    private void refreshContent2NavigationFlag(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
                tab.setCurrentTab(currentTagIndex);
                MyLog.d("tab.setCurrentTab" + currentTagIndex);
            }
        }
        lastTagIndex = currentTagIndex;
    }

    @OnClick({R.id.relay_back, R.id.relay_back2, R.id.realy_share, R.id.tv_collect, R.id.btn_share, R.id.btn_order
            , R.id.line_watch})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relay_back:
            case R.id.relay_back2:
                finish();
                break;
            case R.id.realy_share:
            case R.id.btn_share:
                ToastManager.showDiyShort("share");
//                WxShareUtil.getInstance().showShareDialog(RecordCourseInfoActivity.this, shareBitmap);
                break;
            case R.id.tv_collect:
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        String.format("{\"itemId\": \"%s\",\"itemType\": \"%s\"}", courseId, "ai"));
                RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).collect(body).enqueue(new BaseCallback<>(result -> {
                    if (result.equals("collect")) {
                        Drawable collect_drawable = getResources().getDrawable(R.drawable.ic_lecture_collectioned);
                        tv_collect.setCompoundDrawablesWithIntrinsicBounds(collect_drawable, null, null, null);
                    } else {
                        Drawable collect_drawable = getResources().getDrawable(R.drawable.ic_collection_lecture);
                        tv_collect.setCompoundDrawablesWithIntrinsicBounds(collect_drawable, null, null, null);
                    }
                }, throwable -> {
                    MyLog.e("课程收藏失败" + throwable.getMessage());
                }));
                break;
            case R.id.btn_order:
                if (EduApplication.instance.authInfo == null) {
                    goLogin();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(RecordCourseInfoActivity.this, SureOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("recordCourseInfo", recordCourseInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    TagHelper.getInstance().submitEvent("btn_buy", "course_page");
                }
                break;
            case R.id.line_watch:
                Intent intent = new Intent();
                intent.setClass(RecordCourseInfoActivity.this, RecordCourseLecturesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recordCourseInfo", recordCourseInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
