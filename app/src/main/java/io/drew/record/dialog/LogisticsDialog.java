package io.drew.record.dialog;

/**
 * 物流信息dialog
 *
 * @Author: KKK
 * @CreateDate: 2020/4/26 7:42 PM
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import io.drew.record.R;
import io.drew.record.service.bean.response.RecordOrders;
import io.drew.record.util.AppUtil;

public class LogisticsDialog extends Dialog {

    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_position;
    private TextView tv_address;
    private TextView tv_sure;

    //构造方法
    public LogisticsDialog(Context context, RecordOrders.RecordOrder order) {
        super(context, R.style.mdialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logistics, null);
        tv_sure = view.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_position = view.findViewById(R.id.tv_position);
        tv_address = view.findViewById(R.id.tv_address);

        tv_name.setText(order.getConsigneeName());
        tv_phone.setText(order.getConsigneePhone());
        tv_position.setText(order.getDistrict());
        tv_address.setText(order.getAddress());
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(view);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        if (AppUtil.isPad(getContext())) {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_160);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_200);
        } else {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_320);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_400);
        }
        getWindow().setAttributes(params);
    }
}
