package io.drew.record.fragments_pad;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;

import butterknife.BindView;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.adapters.MyOrderAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.dialog.LogisticsDialog;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.RecordOrders;
import io.drew.record.view.CustomLoadView;

/**
 * 我的订单
 */
public class MyOrdersFragment extends BaseFragment {
    @BindView(R.id.article_recycleView)
    protected RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private MyOrderAdapter myOrderAdapter;
    private LinearLayoutManager layoutManager;
    private AppService appService;
    private RecordOrders recordOrders;
    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        getOrders();
    }

    @Override
    protected void initView() {
        title.setText("我的订单");
        relay_back.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        myOrderAdapter = new MyOrderAdapter(context, R.layout.item_order, new ArrayList<>());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myOrderAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        myOrderAdapter.getLoadMoreModule().setAutoLoadMore(true);
        myOrderAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        myOrderAdapter.getLoadMoreModule().setPreLoadNumber(1);
        myOrderAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (recordOrders != null && recordOrders.getPagination().getPageCount() >= currentPage) {
                    getOrders();
                } else {
                    MyLog.e("loadMore--------没有更多数据了");
                }
            }
        });
        View empty = getLayoutInflater().inflate(R.layout.view_empty, null);
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        myOrderAdapter.setEmptyView(empty);
        myOrderAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadView(false));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOrders();
            }
        });
        myOrderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecordOrders.RecordOrder order = (RecordOrders.RecordOrder) adapter.getData().get(position);
                new RecordCourseInfoFragment(2,order.getId()).show(getParentFragmentManager(),"recordInfo");
            }
        });
        myOrderAdapter.addChildClickViewIds(R.id.tv_logistics);
        myOrderAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_logistics) {
                    RecordOrders.RecordOrder order = (RecordOrders.RecordOrder) adapter.getData().get(position);
                    new LogisticsDialog(context, order).show();
                }
            }
        });
    }


    private void getOrders() {
        appService.getMyOrders(currentPage, pageSize).enqueue(new BaseCallback<>(orders -> {
            if (orders != null) {
                this.recordOrders = orders;
                if (currentPage == 1) {
                    refreshLayout.finishRefresh(true);
                    myOrderAdapter.setNewData(recordOrders.getList());
                } else {
                    myOrderAdapter.addData(recordOrders.getList());
                }
                if (currentPage < recordOrders.getPagination().getPageCount()) {
                    currentPage += 1;
                    myOrderAdapter.getLoadMoreModule().loadMoreComplete();
                } else {
                    myOrderAdapter.getLoadMoreModule().loadMoreEnd();
                }
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
        }));
    }
}
