package io.drew.record.fragments;

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
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
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
import io.drew.record.activitys.ArticleInfoActivity;
import io.drew.record.adapters.CommentedAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.fragments_pad.ArticleInfoFragment;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.CommentedListInfo;
import io.drew.record.util.AppUtil;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/20 6:08 PM
 */
public class CommentTabFragment extends BaseFragment {
    @BindView(R.id.article_recycleView)
    protected RecyclerView article_recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    private AppService appService;
    private CommentedListInfo articles;
    private int currentPage = 1;
    private int pageSize = 10;

    private CommentedAdapter articlesAdapter;
    private LinearLayoutManager layoutManager;

    //这个构造方法是便于各导航同时调用一个fragment
    public CommentTabFragment() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        getArticles();
    }

    @Override
    protected void initView() {
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        articlesAdapter = new CommentedAdapter(context, R.layout.item_commented, new ArrayList<>());
        article_recycleView.setLayoutManager(layoutManager);
        article_recycleView.setAdapter(articlesAdapter);
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) article_recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        article_recycleView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));

//        // 打开或关闭加载更多功能（默认为true）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMore(true);
        // 是否自定加载下一页（默认为true）
        articlesAdapter.getLoadMoreModule().setAutoLoadMore(true);
        // 当数据不满一页时，是否继续自动加载（默认为true）
        articlesAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
//        // 所有数据加载完成后，是否允许点击（默认为false）
//        articlesAdapter.getLoadMoreModule().setEnableLoadMoreEndClick(false);
//        // 是否处于加载中
//        articlesAdapter.getLoadMoreModule().isLoading();
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
        articlesAdapter.addChildClickViewIds(R.id.line_article);
        articlesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentedListInfo.RecordsBean currentBean = (CommentedListInfo.RecordsBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.line_article:
                        if (AppUtil.isPad(context)){
                            new ArticleInfoFragment(currentBean.getArticleId(), position).show(getParentFragmentManager(), "articleInfo");
                        }else {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("articleId", currentBean.getArticleId());
                            intent.setClass(context, ArticleInfoActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        break;
                }
            }
        });
    }

    private void getArticles() {
        appService.getCommentedList(currentPage, pageSize).enqueue(new BaseCallback<>(articles -> {
            if (articles != null) {
                this.articles = articles;
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
            refreshLayout.finishRefresh(false);
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
}