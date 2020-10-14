package io.drew.record.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.RecordCourseLecture;
import io.drew.record.util.GlideUtils;
import io.drew.record.view.ResizableRoundImageView;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class RecordLecturesAdapter extends BaseQuickAdapter<RecordCourseLecture, BaseViewHolder> {

    private Context mContext;

    public RecordLecturesAdapter(Context context, int layoutResId, List<RecordCourseLecture> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecordCourseLecture recordsBean) {
        TextView tv_start_time = baseViewHolder.getView(R.id.tv_start_time);
        TextView tv_num = baseViewHolder.getView(R.id.tv_num);
        ResizableRoundImageView iv_cover = baseViewHolder.getView(R.id.iv_cover);
        ImageView iv_lock = baseViewHolder.getView(R.id.iv_lock);
        TextView tv_name = baseViewHolder.getView(R.id.tv_name);
        TextView tv_watched_times = baseViewHolder.getView(R.id.tv_watched_times);

        GlideUtils.loadImg(mContext, recordsBean.getIcon(), iv_cover);
        tv_num.setText("第" + (baseViewHolder.getAdapterPosition() + 1) + "集");
        tv_name.setText(recordsBean.getName());
        tv_start_time.setText("开课时间：" + recordsBean.getOpenTime());
        if (recordsBean.getIsOpen() == 1) {
            tv_watched_times.setText("已看过" + recordsBean.getViewTimes() + "次");
            iv_cover.showMask(false);
            iv_lock.setVisibility(View.GONE);
        } else {
            tv_watched_times.setText("未开课");
            iv_cover.showMask(true);
            iv_lock.setVisibility(View.VISIBLE);
        }
    }

}
