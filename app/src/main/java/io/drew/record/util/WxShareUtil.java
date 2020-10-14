package io.drew.record.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.record.ConfigConstant;
import io.drew.record.R;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/9 4:12 PM
 */
public class WxShareUtil {
    private static WxShareUtil instance;
    // IWXAPI 是第三方app和微信通信的openApi接口
    public IWXAPI api;
    private static final int THUMB_SIZE = 150;

    public static WxShareUtil getInstance() {
        if (instance == null) {
            instance = new WxShareUtil();
        }
        return instance;
    }

    public void launchWeChat(Context context) {
        if (!isWxAppInstalled(context)) {
            ToastManager.showDiyShort("当前还未安装微信");
            return;
        }
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    /**
     * 是否安装微信
     *
     * @param mContext
     * @return
     */
    public boolean isWxAppInstalled(Context mContext) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(mContext, null);
        wxApi.registerApp(ConfigConstant.WX_APP_ID);
        return wxApi.isWXAppInstalled();
    }

    public void regToWx(Activity activity) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(activity, ConfigConstant.WX_APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(ConfigConstant.WX_APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(ConfigConstant.WX_APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    /**
     * type=1 好友
     * type=2 朋友圈
     */
    public void shareImage(Context context, Bitmap bmp, int type) {
        if (bmp==null){
            MyLog.e("分享图片为null");
            return;
        }
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }

//        req.userOpenId = getOpenId();
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void showShareDialog(Activity context, Bitmap bitmap) {
        if (bitmap == null) return;
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        TextView tv_wechat = view.findViewById(R.id.tv_wechat);
        TextView tv_pyq = view.findViewById(R.id.tv_pyq);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(context, bitmap, 1);
                dialog.dismiss();
            }
        });
        tv_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(context, bitmap, 2);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = AppUtil.getScreenWidth(context);
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
