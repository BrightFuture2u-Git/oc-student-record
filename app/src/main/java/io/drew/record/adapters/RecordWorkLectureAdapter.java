package io.drew.record.adapters;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/4 10:27 AM
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.UnUploadRecordLecture;

public class RecordWorkLectureAdapter extends BaseAdapter {

    private List<UnUploadRecordLecture> data = new ArrayList<>();
    private int checked_index;

    public void setData(List<UnUploadRecordLecture> data) {
        this.data = data;
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
        UnUploadRecordLecture lecture = data.get(position);

        menuItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture_check, null);
        ImageView iv_status = menuItem.findViewById(R.id.iv_status);
        TextView tv_name = menuItem.findViewById(R.id.tv_name);
        TextView tv_time = menuItem.findViewById(R.id.tv_time);
        tv_name.setText(lecture.getLectureName());
        tv_time.setText("开放时间：" + lecture.getOpenTime());
        if (checked_index == position) {
            iv_status.setImageResource(R.drawable.ic_check_circle_blue_22);
        } else {
            iv_status.setImageResource(R.drawable.oval_unchecked);
        }
        return menuItem;
    }
}
