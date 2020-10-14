package io.drew.record.fragments_pad;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
import io.drew.record.adapters.HomeRecordPadAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.HomeRecords;

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


    private HomeRecords homeRecords;
    private LinearLayoutManager linearLayoutManager_experience;
    private LinearLayoutManager linearLayoutManager_hot;
    private HomeRecordPadAdapter experienceRecordAdapter;
    private HomeRecordPadAdapter hotRecordAdapter;

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
        linearLayoutManager_experience = new GridLayoutManager(context, 3);
        linearLayoutManager_hot = new GridLayoutManager(context, 3);
        experienceRecordAdapter = new HomeRecordPadAdapter(context, R.layout.item_record_experience, null);
        hotRecordAdapter = new HomeRecordPadAdapter(context, R.layout.item_record_experience, null);
        recycleView_experience.setLayoutManager(linearLayoutManager_experience);
        recyclerView_hot.setLayoutManager(linearLayoutManager_hot);
        recycleView_experience.setAdapter(experienceRecordAdapter);
        recyclerView_hot.setAdapter(hotRecordAdapter);

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
                if (!TextUtils.isEmpty(bean.getId())) {
                    new RecordCourseInfoFragment(bean.getId()).show(getParentFragmentManager(), "recordInfo");
                }
            }
        });
        hotRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeRecords.RecordCourseBean bean = (HomeRecords.RecordCourseBean) adapter.getData().get(position);
                if (!TextUtils.isEmpty(bean.getId())) {
                    new RecordCourseInfoFragment(bean.getId()).show(getParentFragmentManager(), "recordInfo");
                }
            }
        });
    }

    private void getHomeRecords() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).
                getHomeRecord().enqueue(new BaseCallback<>(data -> {
            if (data != null) {
                refreshLayout.finishRefresh();
                homeRecords = data;
                if (homeRecords.getExperienceList() == null || homeRecords.getExperienceList().size() <= 1) {
                    HomeRecords.RecordCourseBean bean = new HomeRecords.RecordCourseBean();
                    homeRecords.getExperienceList().add(bean);
                }
                experienceRecordAdapter.setNewData(homeRecords.getExperienceList());
                if (homeRecords.getHotList() == null || homeRecords.getHotList().size() <= 1) {
                    HomeRecords.RecordCourseBean bean = new HomeRecords.RecordCourseBean();
                    homeRecords.getHotList().add(bean);
                }
                hotRecordAdapter.setNewData(homeRecords.getHotList());
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
            MyLog.e("首页录播课数据获取失败" + throwable.getMessage());
        }));
    }
}
