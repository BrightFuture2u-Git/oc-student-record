package io.drew.record.dialog;

/**
 * @Author: KKK
 * @CreateDate: 2020/4/26 7:42 PM
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.drew.record.R;
import io.drew.record.util.AppUtil;

public class SureDialog extends Dialog {

    private TextView button_confirm;//定义取消与确认按钮
    private LinearLayout line_close;
    private TextView tv;//定义标题文字

    //构造方法
    public SureDialog(Context context, String title) {
        super(context, R.style.mdialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        tv = view.findViewById(R.id.tv_content);
        line_close = view.findViewById(R.id.line_close);
        line_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button_confirm = view.findViewById(R.id.tv_dialog_confirm);
        tv.setText(title);
        setCancelable(true);
        setContentView(view);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        if (AppUtil.isPad(getContext())) {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_160);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_160);
        } else {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_320);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_320);
        }

        getWindow().setAttributes(params);
    }


    //退出监听
    public void setOnSureListener(View.OnClickListener listener) {
        button_confirm.setOnClickListener(listener);
    }
}
