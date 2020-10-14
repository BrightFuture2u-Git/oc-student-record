package io.drew.record.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/26 7:21 PM
 */
public class HomeSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    public HomeSpaceItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (AppUtil.isPad(mContext)) {
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            } else {
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            }
        } else {
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_15);
                outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_20);
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_20);
            } else {
                outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
                outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_20);
                outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_15);
                outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_20);
            }
        }

    }

}
