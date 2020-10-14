package io.drew.record.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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
public class HomeRecordPadAdapter extends BaseQuickAdapter<HomeRecords.RecordCourseBean, BaseViewHolder> {

    private Context mContext;

    public HomeRecordPadAdapter(Context context, int layoutResId, List<HomeRecords.RecordCourseBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeRecords.RecordCourseBean bean) {
        RelativeLayout relay_empty = baseViewHolder.getView(R.id.relay_empty);
        RelativeLayout relay_content = baseViewHolder.getView(R.id.relay_content);
        if (TextUtils.isEmpty(bean.getId())) {
            relay_content.setVisibility(View.GONE);
            relay_empty.setVisibility(View.VISIBLE);
        } else {
            relay_content.setVisibility(View.VISIBLE);
            relay_empty.setVisibility(View.GONE);
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
}
