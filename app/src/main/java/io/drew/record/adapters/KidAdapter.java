package io.drew.record.adapters;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/4 10:27 AM
 */

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.drew.record.R;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.GlideUtils;

public class KidAdapter extends BaseAdapter {

    private List<AuthInfo.UserBean.StudentListBean> data = new ArrayList<>();
    private int checked_index;
    private Context mContext;

    public KidAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<AuthInfo.UserBean.StudentListBean> data, int checked_index) {
        AuthInfo.UserBean.StudentListBean add = new AuthInfo.UserBean.StudentListBean();
        add.setId("");

        if (data != null) {
            this.data.addAll(data);
        }
        this.data.add(add);
        this.checked_index = checked_index;
    }

    public void checkItem(int position) {
        checked_index = position;
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
        AuthInfo.UserBean.StudentListBean studentListBean = data.get(position);
        if (TextUtils.isEmpty(studentListBean.getId())) {
            menuItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kid_add, null);
        } else {
            menuItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kid, null);
            CircleImageView iv_head = menuItem.findViewById(R.id.iv_head);
            GlideUtils.loadBabyHead(mContext,studentListBean.getAvatar(),iv_head);
            ImageView iv_checked = menuItem.findViewById(R.id.iv_checked);
            TextView tv_name = menuItem.findViewById(R.id.tv_name);
            tv_name.setText(studentListBean.getRealName());
            if (checked_index == position) {
                iv_checked.setVisibility(View.VISIBLE);
                iv_head.setBorderColor(Color.parseColor("#6A48F5"));
                iv_head.setBorderWidth(4);
            } else {
                iv_checked.setVisibility(View.INVISIBLE);
                iv_head.setBorderWidth(0);
            }
        }

        return menuItem;
    }
}
