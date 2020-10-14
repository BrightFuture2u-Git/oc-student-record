package io.drew.record.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class BgAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private Context mContext;
    private int position_checked = -1;

    public void check(int position) {
        position_checked = position;
        notifyDataSetChanged();
    }

    public BgAdapter(Context context, int layoutResId, List<Integer> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Integer bean) {
        RelativeLayout relay_container = baseViewHolder.getView(R.id.relay_container);
        ImageView iv_img = baseViewHolder.getView(R.id.iv_img);

        if (baseViewHolder.getAdapterPosition() == position_checked) {
            relay_container.setBackground(mContext.getResources().getDrawable(R.drawable.bg_checked));
        } else {
            relay_container.setBackgroundColor(Color.TRANSPARENT);
        }
        iv_img.setImageResource(bean);

    }
}
