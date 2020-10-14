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

import io.drew.base.MyLog;
import io.drew.record.R;
import io.drew.record.util.AppUtil;

public class UpdateDialog extends Dialog {

    private TextView button_confirm;//定义取消与确认按钮
    private LinearLayout line_close;
    private TextView tv;//定义标题文字
    private TextView tv_version;//定义标题文字

    //构造方法
    public UpdateDialog(Context context, String title, String version, boolean isForceUpdate) {
        super(context, R.style.mdialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, null);
        tv = view.findViewById(R.id.tv_content);
        tv_version = view.findViewById(R.id.tv_version);
        line_close = view.findViewById(R.id.line_close);
        line_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button_confirm = view.findViewById(R.id.tv_dialog_confirm);
        tv.setText(title);
        tv_version.setText(version);
        if (isForceUpdate) {
            MyLog.d("当前是强制更新");
        } else {
            MyLog.d("当前是普通更新");
        }
        if (isForceUpdate) {
            line_close.setVisibility(View.GONE);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        } else {
            line_close.setVisibility(View.VISIBLE);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }

        setContentView(view);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        if (AppUtil.isPad(getContext())) {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_160);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_250);
        } else {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_320);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_500);
        }

        getWindow().setAttributes(params);
    }

    public void setOnSureListener(View.OnClickListener listener) {
        button_confirm.setOnClickListener(listener);
    }
}
