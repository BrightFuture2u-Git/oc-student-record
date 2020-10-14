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
public class TeachersSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    public TeachersSpaceItemDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_8);
        } else {
            outRect.left = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
            outRect.bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            outRect.right = mContext.getResources().getDimensionPixelSize(R.dimen.dp_0);
            outRect.top = mContext.getResources().getDimensionPixelSize(R.dimen.dp_8);
        }
    }
}
