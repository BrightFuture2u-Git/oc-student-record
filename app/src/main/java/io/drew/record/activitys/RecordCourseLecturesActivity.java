package io.drew.record.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.adapters.RecordLecturesAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.RecordCourseInfo;
import io.drew.record.service.bean.response.RecordCourseLecture;

/**
 * 录播课课程课节详情
 */
public class RecordCourseLecturesActivity extends BaseActivity {
    @BindView(R.id.article_recycleView)
    protected RecyclerView article_recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    private RecordLecturesAdapter recordLecturesAdapter;
    private LinearLayoutManager layoutManager;
    private AppService appService;
    private List<RecordCourseLecture> recordCourseLectures;

    private RecordCourseInfo recordCourseInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_collections;
    }

    @Override
    protected void initData() {
        recordCourseInfo = (RecordCourseInfo) getIntent().getExtras().getSerializable("recordCourseInfo");
        if (recordCourseInfo == null) {
            ToastManager.showDiyShort("数据异常");
            finish();
            return;
        }
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        getLectures();
    }

    @Override
    protected void initView() {
        initActionBar(recordCourseInfo != null ? recordCourseInfo.getName() : "", true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recordLecturesAdapter = new RecordLecturesAdapter(this, R.layout.item_record_lecture, new ArrayList<>());
        article_recycleView.setLayoutManager(layoutManager);
        article_recycleView.setAdapter(recordLecturesAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) article_recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        article_recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        View empty = getLayoutInflater().inflate(R.layout.view_empty, null);
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recordLecturesAdapter.setEmptyView(empty);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getLectures();
            }
        });
        recordLecturesAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecordCourseLecture recordsBean = (RecordCourseLecture) adapter.getData().get(position);
                if (recordsBean.getIsOpen() == 1) {
                    Intent intent = new Intent();
                    intent.setClass(RecordCourseLecturesActivity.this, RecordVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lecture", recordsBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    private void getLectures() {
        appService.getRecordCourseLectures(recordCourseInfo.getId()).enqueue(new BaseCallback<>(lectures -> {
            if (lectures != null) {
                this.recordCourseLectures = lectures;
                refreshLayout.finishRefresh(true);
                recordLecturesAdapter.setNewData(recordCourseLectures);
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
        }));
    }
}
