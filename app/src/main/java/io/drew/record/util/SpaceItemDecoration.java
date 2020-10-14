package io.drew.record.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/26 7:21 PM
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
//        outRect.left = space;
//        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
//        if (parent.getChildLayoutPosition(view) %2==1) {
//            outRect.left = 40;
//            outRect.bottom = 40;
//            outRect.left = 40;
//            outRect.top = 40;
//        }
        outRect.set(space,space,space,space);
    }

}
