package io.drew.record.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.adapters.MyRecordLecturesAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.MyRecordLecture;
import io.drew.record.service.bean.response.RecordCourseInfo;
import io.drew.record.view.CustomLoadView;

/**
 * 我的录播课课程
 */
public class MyRecordLecturesActivity extends BaseActivity {
    @BindView(R.id.article_recycleView)
    protected RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    private MyRecordLecturesAdapter recordLecturesAdapter;
    private LinearLayoutManager layoutManager;
    private AppService appService;
    private MyRecordLecture myRecordLecture;

    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_orders;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        getLectures();
    }

    @Override
    protected void initView() {
        initActionBar("我的课程", true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recordLecturesAdapter = new MyRecordLecturesAdapter(this, R.layout.item_my_record_lecture, new ArrayList<>());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(recordLecturesAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

//        // 打开或关闭加载更多功能（默认为true）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMore(true);
        // 是否自定加载下一页（默认为true）
        recordLecturesAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 当数据不满一页时，是否继续自动加载（默认为true）
        recordLecturesAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
//        // 所有数据加载完成后，是否允许点击（默认为false）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(false);
//        // 是否处于加载中
//        articlesAdapter.getLoadMoreModule().isLoading();
//        // 预加载的位置（默认为1）
        recordLecturesAdapter.getLoadMoreModule().setPreLoadNumber(1);
        recordLecturesAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        View empty = getLayoutInflater().inflate(R.layout.view_empty_button, null);
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Button btn_empty = empty.findViewById(R.id.btn_empty);
        btn_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_FIND_RECORD_COURSE));
            }
        });
        recordLecturesAdapter.setEmptyView(empty);
        //隐藏加载完成tip
        recordLecturesAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadView(false));

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getLectures();
            }
        });
        recordLecturesAdapter.addChildClickViewIds(R.id.tv_watch);
        recordLecturesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyRecordLecture.RecordsBean recordsBean = (MyRecordLecture.RecordsBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_watch:
                        RecordCourseInfo recordCourseInfo = new RecordCourseInfo();
                        recordCourseInfo.setId(recordsBean.getId());
                        recordCourseInfo.setName(recordsBean.getName());

                        Intent intent = new Intent();
                        intent.setClass(MyRecordLecturesActivity.this, RecordCourseLecturesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("recordCourseInfo", recordCourseInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        recordLecturesAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyRecordLecture.RecordsBean recordsBean = (MyRecordLecture.RecordsBean) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("courseId", recordsBean.getId());
                intent.setClass(MyRecordLecturesActivity.this, RecordCourseInfoActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getLectures() {
        appService.getRecordLectures(currentPage, pageSize).enqueue(new BaseCallback<>(lectures -> {
            if (lectures != null) {
                this.myRecordLecture = lectures;
                if (currentPage == 1) {
                    refreshLayout.finishRefresh(true);
                    recordLecturesAdapter.setNewData(lectures.getRecords());
                } else {
                    recordLecturesAdapter.addData(lectures.getRecords());
                }
                if (currentPage < lectures.getPages()) {
                    currentPage += 1;
                    recordLecturesAdapter.getLoadMoreModule().loadMoreComplete();
                } else {
                    recordLecturesAdapter.getLoadMoreModule().loadMoreEnd();
                }
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
        }));
    }

    private void loadMore() {
        if (myRecordLecture != null && myRecordLecture.getPages() >= currentPage) {
            getLectures();
        } else {
            MyLog.e("loadMore--------没有更多数据了");
        }
    }
}
