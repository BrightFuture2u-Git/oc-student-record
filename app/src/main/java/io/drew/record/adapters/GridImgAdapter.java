package io.drew.record.adapters;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/4 10:27 AM
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import io.drew.record.R;
import io.drew.record.interfaces.OnImgItemClickListener;
import io.drew.record.util.GlideUtils;

public class GridImgAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<>();
    private OnImgItemClickListener onImgItemClickListener;

    private Context mContext;

    public GridImgAdapter(Context context) {
        mContext = context;
    }

    public void setItemOnClickListener(OnImgItemClickListener onImgItemClickListener) {
        this.onImgItemClickListener = onImgItemClickListener;
    }

    public void setData(List<String> mData) {
        data = mData;
        if (data != null && data.size() < 4) {
            data.add("");
        }
    }

    public void delectItem(int position) {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View menuItem;
        String url = data.get(position);
        if (TextUtils.isEmpty(url)) {
            menuItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_add, null);
        } else {
            menuItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, null);
            ImageView iv_img = menuItem.findViewById(R.id.iv_img);
            LinearLayout line_delect = menuItem.findViewById(R.id.line_delect);
            if (onImgItemClickListener != null) {
                line_delect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImgItemClickListener.onImgItemClick(position);
                    }
                });
            }

            GlideUtils.loadImg(mContext,url,iv_img);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(Gravity.CENTER);
        menuItem.setLayoutParams(layoutParams);
        return menuItem;
    }
}
