package io.drew.record.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import io.drew.record.R;
import io.drew.record.service.bean.response.AddressList;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/12 5:50 PM
 */
public class AddressAdapter extends BaseQuickAdapter<AddressList.Address, BaseViewHolder> {

    private Context mContext;

    public AddressAdapter(Context context, int layoutResId, List<AddressList.Address> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AddressList.Address bean) {
        TextView tv_tag_default = baseViewHolder.getView(R.id.tv_tag_default);

        baseViewHolder.setText(R.id.tv_user_name, bean.getName());
        baseViewHolder.setText(R.id.tv_user_phone, bean.getPhone());
        baseViewHolder.setText(R.id.tv_address, bean.getDistrict()+bean.getAddress());
        if (bean.getIsDefault() == 1) {
            tv_tag_default.setVisibility(View.VISIBLE);
        } else {
            tv_tag_default.setVisibility(View.GONE);
        }

    }
}
