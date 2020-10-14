package io.drew.record.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.activitys.RecordCourseInfoActivity;
import io.drew.record.adapters.HomeExperienceRecordAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.HomeRecords;
import io.drew.record.util.AppUtil;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/7 6:42 PM
 * 录播课首页
 */
public class RecordFragment extends BaseFragment {

    @BindView(R.id.recycleView_experience)
    protected RecyclerView recycleView_experience;
    @BindView(R.id.recycleView_hot)
    protected RecyclerView recyclerView_hot;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_title)
    protected TextView tv_title;

    private HomeRecords homeRecords;
    private LinearLayoutManager linearLayoutManager_experience;
    private LinearLayoutManager linearLayoutManager_hot;
    private HomeExperienceRecordAdapter experienceRecordAdapter;
    private HomeExperienceRecordAdapter hotRecordAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_record;
    }

    @Override
    protected void initData() {
        getHomeRecords();
    }

    @Override
    protected void initView() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_title.getLayoutParams();
        lp.topMargin = AppUtil.getStatusBarHeight(context);
        linearLayoutManager_experience = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        linearLayoutManager_hot = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        experienceRecordAdapter = new HomeExperienceRecordAdapter(context, R.layout.item_record_experience, null);
        hotRecordAdapter = new HomeExperienceRecordAdapter(context, R.layout.item_record_experience, null);
        recycleView_experience.setLayoutManager(linearLayoutManager_experience);
        recyclerView_hot.setLayoutManager(linearLayoutManager_hot);
        recycleView_experience.setAdapter(experienceRecordAdapter);
        recyclerView_hot.setAdapter(hotRecordAdapter);

        View footer = View.inflate(context, R.layout.view_footer, null);
        hotRecordAdapter.addFooterView(footer);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHomeRecords();
            }
        });
        experienceRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeRecords.RecordCourseBean bean = (HomeRecords.RecordCourseBean) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("courseId", bean.getId());
                intent.setClass(context, RecordCourseInfoActivity.class);
                startActivity(intent);
            }
        });
        hotRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeRecords.RecordCourseBean bean = (HomeRecords.RecordCourseBean) adapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("courseId", bean.getId());
                intent.setClass(context, RecordCourseInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getHomeRecords() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).
                getHomeRecord().enqueue(new BaseCallback<>(data -> {
            if (data != null) {
                refreshLayout.finishRefresh();
                homeRecords = data;
                experienceRecordAdapter.setNewData(homeRecords.getExperienceList());
                hotRecordAdapter.setNewData(homeRecords.getHotList());
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
            MyLog.e("首页录播课数据获取失败" + throwable.getMessage());
        }));
    }
}
