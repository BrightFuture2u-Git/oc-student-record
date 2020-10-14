package io.drew.record.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.drew.base.ToastManager;
import io.drew.record.R;
import io.drew.record.service.bean.response.Articles;
import io.drew.record.util.AppUtil;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.ShotUtils;
import io.drew.record.util.WxShareUtil;

/**
 * 创作分享-分享
 *
 * @Author: KKK
 * @CreateDate: 2020/8/13 5:46 PM
 */
public class CreateImgDialog {
    public static void showCreateImgDialog(Activity context, Articles.RecordsBean recordsBean) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        int picSize = recordsBean.getImageList().size();
        View view = null;
        switch (picSize) {
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_create_img_article_1, null);
                ImageView iv_work = view.findViewById(R.id.iv_work);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(0),iv_work);
                break;
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_create_img_article_2, null);
                ImageView iv_work_1 = view.findViewById(R.id.iv_work_1);
                ImageView iv_work_2 = view.findViewById(R.id.iv_work_2);

                GlideUtils.loadImg(context,recordsBean.getImageList().get(0),iv_work_1);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(1),iv_work_2);
                break;
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_create_img_article_3, null);
                ImageView iv_work_3_1 = view.findViewById(R.id.iv_work_1);
                ImageView iv_work_3_2 = view.findViewById(R.id.iv_work_2);
                ImageView iv_work_3_3 = view.findViewById(R.id.iv_work_3);

                GlideUtils.loadImg(context,recordsBean.getImageList().get(0),iv_work_3_1);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(1),iv_work_3_2);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(2),iv_work_3_3);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.dialog_create_img_article_4, null);
                ImageView iv_work_4_1 = view.findViewById(R.id.iv_work_1);
                ImageView iv_work_4_2 = view.findViewById(R.id.iv_work_2);
                ImageView iv_work_4_3 = view.findViewById(R.id.iv_work_3);
                ImageView iv_work_4_4 = view.findViewById(R.id.iv_work_4);

                GlideUtils.loadImg(context,recordsBean.getImageList().get(0),iv_work_4_1);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(1),iv_work_4_2);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(2),iv_work_4_3);
                GlideUtils.loadImg(context,recordsBean.getImageList().get(3),iv_work_4_4);
                break;
        }
        ImageView iv_head = view.findViewById(R.id.iv_head);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_time = view.findViewById(R.id.tv_time);

        TextView tv_content = view.findViewById(R.id.tv_content);
        RelativeLayout realy_container = view.findViewById(R.id.realy_container);

        GlideUtils.loadUserHead(context,recordsBean.getHeadImage(),iv_head);
        tv_name.setText(recordsBean.getNickname() + "");
        tv_time.setText(recordsBean.getCreateTime());
        tv_content.setText(recordsBean.getContent());

        TextView tv_wechat = view.findViewById(R.id.tv_wechat);
        TextView tv_pyq = view.findViewById(R.id.tv_pyq);
        TextView tv_save = view.findViewById(R.id.tv_save);

        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(context, realy_container, 1);
            }
        });
        tv_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(context, realy_container, 2);
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(context, realy_container, 3);
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        if (AppUtil.isPad(context)){
            lp.width = (AppUtil.getScreenWidth(context) - context.getResources().getDimensionPixelOffset(R.dimen.dp_44)) / 2;
//            Window window = getDialog().getWindow();
//            if (screenType == 0) {
////            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                    | View.SYSTEM_UI_FLAG_IMMERSIVE
////                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
////            window.getDecorView().setSystemUiVisibility(uiOptions);
//            } else {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//            WindowManager.LayoutParams windowParams = window.getAttributes();
            lp.gravity = Gravity.RIGHT;
            dialogWindow.getAttributes().windowAnimations = R.style.DialogFragmentAnim;
            dialogWindow.setAttributes(lp);
        }else {
            lp.width = AppUtil.getScreenWidth(context);
        }

        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private static void share(Context context, View view, int type) {
        ShotUtils.viewShot(view, new ShotUtils.ShotCallback() {
            @Override
            public void onShotComplete(String savePath, Bitmap bitmap) {
                switch (type) {
                    case 1://好友
                        WxShareUtil.getInstance().shareImage(context, bitmap, 1);
                        break;
                    case 2://朋友圈
                        WxShareUtil.getInstance().shareImage(context, bitmap, 2);
                        break;
                    case 3:
                        if (ShotUtils.saveImageToGallery(context, bitmap)) {
                            ToastManager.showDiyShort("已成功保存至相册");
                        } else {
                            ToastManager.showDiyShort("保存失败");
                        }
                        break;
                }
            }
        });

    }
}
