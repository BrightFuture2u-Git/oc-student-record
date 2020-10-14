package io.drew.record.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.HomeRecords;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class HomeExperienceRecordAdapter extends BaseQuickAdapter<HomeRecords.RecordCourseBean, BaseViewHolder> {

    private Context mContext;

    public HomeExperienceRecordAdapter(Context context, int layoutResId, List<HomeRecords.RecordCourseBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeRecords.RecordCourseBean bean) {
        TextView tv_price_old = baseViewHolder.getView(R.id.tv_price_old);

        baseViewHolder.setText(R.id.tv_name, bean.getName());
        baseViewHolder.setText(R.id.tv_tags, bean.getTags());
        baseViewHolder.setText(R.id.tv_time, "开课时间：" + bean.getStartTime());
        baseViewHolder.setText(R.id.tv_price_new, "¥" + bean.getPriceStr());
        baseViewHolder.setText(R.id.tv_num_order, "已有 " + bean.getBuyNum() + " 人报名");
        if (bean.getOriginalPrice() > 0) {
            tv_price_old.setVisibility(View.VISIBLE);
            tv_price_old.setText("原价¥" + bean.getOriginalPriceStr());
        } else {
            tv_price_old.setVisibility(View.INVISIBLE);
        }

    }
}
