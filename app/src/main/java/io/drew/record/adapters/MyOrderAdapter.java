package io.drew.record.adapters;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.RecordOrders;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class MyOrderAdapter extends BaseQuickAdapter<RecordOrders.RecordOrder, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public MyOrderAdapter(Context context, int layoutResId, List<RecordOrders.RecordOrder> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecordOrders.RecordOrder recordsBean) {
        TextView tv_order_time = baseViewHolder.getView(R.id.tv_order_time);
        TextView tv_name = baseViewHolder.getView(R.id.tv_name);
        TextView tv_start_time = baseViewHolder.getView(R.id.tv_start_time);
        TextView tv_price = baseViewHolder.getView(R.id.tv_price);

        tv_order_time.setText("下单时间:" + recordsBean.getCreateTime());
        tv_name.setText(recordsBean.getName());
        tv_start_time.setText("开课时间：" + recordsBean.getStartTime() + " | 共 " + recordsBean.getLectureNum() + " 节课");
        tv_price.setText("¥" + recordsBean.getPriceStr());
    }

}
