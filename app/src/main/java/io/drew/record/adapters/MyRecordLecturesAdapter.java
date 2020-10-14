package io.drew.record.adapters;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.MyRecordLecture;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class MyRecordLecturesAdapter extends BaseQuickAdapter<MyRecordLecture.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public MyRecordLecturesAdapter(Context context, int layoutResId, List<MyRecordLecture.RecordsBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyRecordLecture.RecordsBean recordsBean) {
        TextView tv_name = baseViewHolder.getView(R.id.tv_name);
        TextView tv_start_time = baseViewHolder.getView(R.id.tv_start_time);
        TextView tv_price = baseViewHolder.getView(R.id.tv_price);

        tv_name.setText(recordsBean.getName());
        tv_start_time.setText("开课时间：" + recordsBean.getStartTime());
        tv_price.setText("¥" + recordsBean.getPriceStr());
    }

}
