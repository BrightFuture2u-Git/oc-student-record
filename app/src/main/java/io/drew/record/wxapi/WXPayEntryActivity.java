package io.drew.record.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import io.drew.base.MyLog;
import io.drew.record.ConfigConstant;
import io.drew.record.model.MessageEvent;
import io.drew.record.util.WxShareUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WxShareUtil.getInstance().api;
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("kkk", "onPayFinish, errCode = " + resp.errCode);
        Log.d("kkk", "onPayFinish, errStr = " + resp.errStr);
        Log.d("kkk", "onPayFinish, openId = " + resp.openId);
        MessageEvent event=new MessageEvent(ConfigConstant.EB_WX_PAYED);
        switch (resp.errCode) {
            case 0://成功，展示成功页面
                event.setObjectMessage(true);
                EventBus.getDefault().post(event);
                break;
            case -1://错误，展示成功页面
                event.setObjectMessage(false);
                EventBus.getDefault().post(event);
                break;
            case -2://用户取消
                event.setObjectMessage(false);
                EventBus.getDefault().post(event);
                MyLog.d("用户已取消支付");
                break;
        }
        finish();
    }
}