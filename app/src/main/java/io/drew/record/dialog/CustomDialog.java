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
import android.widget.TextView;

import io.drew.record.R;
import io.drew.record.util.AppUtil;

public class CustomDialog extends Dialog {

    private TextView button_cancel, button_confirm;//定义取消与确认按钮
    private TextView tv_title;//定义标题文字
    private TextView tv_content;

    //构造方法
    public CustomDialog(Context context, String title) {
        super(context, R.style.mdialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom, null);
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        button_cancel = view.findViewById(R.id.tv_dialog_cancel);
        button_confirm = view.findViewById(R.id.tv_dialog_confirm);
        tv_title.setText(title);
        setCancelable(false);
        setContentView(view);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        if (AppUtil.isPad(context)) {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_150);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_100);
        } else {
            params.width = context.getResources().getDimensionPixelSize(R.dimen.dp_300);
            params.height = context.getResources().getDimensionPixelSize(R.dimen.dp_190);
        }

        getWindow().setAttributes(params);
    }


    public void setContent(String content) {
        tv_content.setText(content);
    }

    public void setSureText(String sure) {
        button_confirm.setText(sure);
    }

    public void setCancelText(String cancel) {
        button_cancel.setText(cancel);
    }

    //退出监听
    public void setOnSureListener(View.OnClickListener listener) {
        button_confirm.setOnClickListener(listener);
    }//退出监听

    public void setOnCancleListener(View.OnClickListener listener) {
        button_cancel.setOnClickListener(listener);
    }
}
