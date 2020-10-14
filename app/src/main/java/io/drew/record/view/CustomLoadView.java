package io.drew.record.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.loadmore.LoadMoreStatus;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/15 9:03 PM
 */
public class CustomLoadView extends BaseLoadMoreView {


    private boolean needEndView = true;

    public CustomLoadView(boolean needEndView) {
        this.needEndView = needEndView;
    }

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_complete_view);
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_end_view);
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_load_fail_view);
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.getView(R.id.load_more_loading_view);
    }


    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_load_more, viewGroup, false);
    }

    @Override
    public void convert(@NotNull BaseViewHolder holder, int position, @NotNull LoadMoreStatus loadMoreStatus) {
        switch (loadMoreStatus) {
            case Complete:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.VISIBLE);
                getLoadFailView(holder).setVisibility(View.GONE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case Loading:
                getLoadingView(holder).setVisibility(View.VISIBLE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.GONE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case Fail:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.VISIBLE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case End:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.GONE);
                if (needEndView) {
                    getLoadEndView(holder).setVisibility(View.VISIBLE);
                } else {
                    getLoadEndView(holder).setVisibility(View.GONE);
                }

                break;
        }
    }
}
