package io.drew.record.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.alipay.PayResult;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AddressList;
import io.drew.record.service.bean.response.AliPayOrder;
import io.drew.record.service.bean.response.RecordCourseInfo;
import io.drew.record.service.bean.response.WxPayOrder;
import io.drew.record.util.TagHelper;
import io.drew.record.util.WxShareUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 确认订单
 */
public class SureOrderActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    protected TextView tv_name;
    @BindView(R.id.tv_price)
    protected TextView tv_price;
    @BindView(R.id.tv_time)
    protected TextView tv_time;
    @BindView(R.id.tv_create_address)
    protected TextView tv_create_address;
    @BindView(R.id.line_wechat)
    protected LinearLayout line_wechat;
    @BindView(R.id.iv_status_wechat)
    protected ImageView iv_status_wechat;
    @BindView(R.id.line_alipay)
    protected LinearLayout line_alipay;
    @BindView(R.id.iv_status_alipay)
    protected ImageView iv_status_alipay;
    @BindView(R.id.tv_price_pay)
    protected TextView tv_price_pay;
    @BindView(R.id.btn_pay)
    protected Button btn_pay;
    @BindView(R.id.relay_no_address)
    protected RelativeLayout relay_no_address;
    @BindView(R.id.relay_address)
    protected RelativeLayout relay_address;

    @BindView(R.id.tv_user_name)
    protected TextView tv_user_name;
    @BindView(R.id.tv_user_phone)
    protected TextView tv_user_phone;
    @BindView(R.id.tv_address_default)
    protected TextView tv_address_default;
    @BindView(R.id.tv_edit)
    protected TextView tv_edit;

    private RecordCourseInfo recordCourseInfo;
    private AddressList addressList;
    private AddressList.Address addressChoosed;
    private int payType;
    private AliPayOrder aliPayOrder;
    private WxPayOrder wxPayOrder;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Intent intent = new Intent();
                    intent.setClass(SureOrderActivity.this, PayResultActivity.class);
                    intent.putExtra("payType", payType);
                    intent.putExtra("orderId", aliPayOrder.getOrderId());
                    intent.putExtra("orderToken", aliPayOrder.getToken());
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        MyLog.e("支付成功");
                        intent.putExtra("success", true);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyLog.e("支付失败");
                        intent.putExtra("success", false);
                    }
                    startActivity(intent);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sure_order;
    }

    @Override
    protected void initData() {
        recordCourseInfo = (RecordCourseInfo) getIntent().getExtras().getSerializable("recordCourseInfo");
        if (recordCourseInfo == null) {
            ToastManager.showDiyShort("数据异常");
            finish();
        }
        getAddressList();
    }

    private void getAddressList() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).getAddressList(1, 100).enqueue(new BaseCallback<>(list -> {
            if (list != null) {
                addressList = list;
                if (addressList.getList() == null || addressList.getList().size() <= 0) {
                    relay_no_address.setVisibility(View.VISIBLE);
                    relay_address.setVisibility(View.GONE);
                } else {
                    relay_no_address.setVisibility(View.GONE);
                    relay_address.setVisibility(View.VISIBLE);
                    addressChoosed = addressList.getList().get(0);
                    tv_user_name.setText(addressChoosed.getName());
                    tv_user_phone.setText(addressChoosed.getPhone());
                    tv_address_default.setText(addressChoosed.getDistrict() + addressChoosed.getAddress());
                }
            }
        }, throwable -> {
            MyLog.e("收件地址列表获取失败" + throwable.getMessage());
        }));
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_ADDRESS_CHOOSE:
                relay_no_address.setVisibility(View.GONE);
                relay_address.setVisibility(View.VISIBLE);
                addressChoosed = (AddressList.Address) event.getObjectMessage();
                tv_user_name.setText(addressChoosed.getName());
                tv_user_phone.setText(addressChoosed.getPhone());
                tv_address_default.setText(addressChoosed.getDistrict() + addressChoosed.getAddress());
                break;
            case ConfigConstant.EB_ADDRESS_ADDED:
            case ConfigConstant.EB_ADDRESS_EDITED:
            case ConfigConstant.EB_ADDRESS_REMOVE:
                getAddressList();
                break;
            case ConfigConstant.EB_REPAY:
                int type = event.getIntMessage();
                if (type == 2) {
                    createAliPayOrder();
                } else if (type == 1) {
                    createWeChatOrder();
                }
                break;
            case ConfigConstant.EB_ORDER_CANCEL:
                finish();
                break;
            case ConfigConstant.EB_WX_PAYED:
                Intent intent = new Intent();
                intent.setClass(SureOrderActivity.this, PayResultActivity.class);
                intent.putExtra("payType", 1);
                intent.putExtra("orderId", wxPayOrder.getOrderId());
                intent.putExtra("orderToken", wxPayOrder.getToken());
                if ((Boolean) event.getObjectMessage()) {
                    intent.putExtra("success", true);
                } else {
                    intent.putExtra("success", false);
                }
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void initView() {
        initActionBar("确认订单", true);
        tv_name.setText(recordCourseInfo.getName());
        tv_price.setText("¥" + recordCourseInfo.getPriceStr());
        tv_price_pay.setText("¥" + recordCourseInfo.getPriceStr());
        tv_time.setText("开课时间：" + recordCourseInfo.getStartTime() + " | " + "共 " + recordCourseInfo.getLectureNum() + " 节课");
    }

    @OnClick({R.id.tv_create_address, R.id.tv_edit, R.id.line_wechat, R.id.line_alipay, R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create_address:
                startActivity(AddAddressActivity.class);
                break;
            case R.id.tv_edit:
                if (addressList != null && addressList.getList().size() > 0) {
                    Intent intent = new Intent();
                    intent.setClass(SureOrderActivity.this, AddressListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addressList", (Serializable) addressList.getList());
                    bundle.putBoolean("fromOrder", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.line_wechat:
                choosePayType(1);
                break;
            case R.id.line_alipay:
                choosePayType(2);
                break;
            case R.id.btn_pay:
                if (payType < 1) {
                    ToastManager.showDiyShort("请先选择支付方式");
                } else {
                    if (addressChoosed == null) {
                        ToastManager.showDiyShort("请先选择收货地址");
                    } else {
                        if (payType == 1) {
                            createWeChatOrder();
                        } else if (payType == 2) {
                            createAliPayOrder();
                        }
                        TagHelper.getInstance().submitEvent("btn_pay", "pay_page");
                    }
                }
                break;
        }
    }

    private void choosePayType(int type) {
        payType = type;
        if (type == 1) {
            iv_status_wechat.setImageResource(R.drawable.ic_checked);
            iv_status_alipay.setImageResource(R.drawable.ic_unchecked);
        } else if (type == 2) {
            iv_status_wechat.setImageResource(R.drawable.ic_unchecked);
            iv_status_alipay.setImageResource(R.drawable.ic_checked);
        }
    }

    private void createWeChatOrder() {
        if (!WxShareUtil.getInstance().isWxAppInstalled(this)) {
            ToastManager.showDiyShort("请先安装微信");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("categoryId", 2);
        map.put("commodityId", recordCourseInfo.getId());//课程id
        map.put("paymentChannel", 13);//支付渠道, 微信 h5:11, pc:12, app:13, 支付宝 h5:21, pc:22, app:23
        map.put("studentId", EduApplication.instance.currentKid.getId());
        map.put("userAddressId", addressChoosed.getId());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new JSONObject(map).toString());
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).createCourseOrderWxApp(body).enqueue(new BaseCallback<>(order -> {
            if (order != null) {
                wxPayOrder = order;
                payByWeChat(order);
            }
        }, throwable -> {
            MyLog.e("创建订单失败" + throwable.getMessage());
        }));
    }

    private void payByWeChat(WxPayOrder wxPayOrder) {
        IWXAPI api = WxShareUtil.getInstance().api;
        PayReq request = new PayReq();
//        request.appId = "wx7bf3be6b0d78edd7";
        request.appId = wxPayOrder.getAppId();
        request.partnerId = wxPayOrder.getPartnerId();
        request.prepayId = wxPayOrder.getPrepayId();
        request.packageValue = wxPayOrder.getPackageX();
        request.nonceStr = wxPayOrder.getNonceStr();
        request.timeStamp = wxPayOrder.getTimeStamp();
        request.sign = wxPayOrder.getPaySign();
        api.sendReq(request);
    }

    /**
     * 创建aliPay订单
     */
    private void createAliPayOrder() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("categoryId", 2);
        map.put("commodityId", recordCourseInfo.getId());//课程id
        map.put("paymentChannel", 23);//支付渠道, 微信 h5:11, pc:12, app:13, 支付宝 h5:21, pc:22, app:23
        map.put("studentId", EduApplication.instance.currentKid.getId());
        map.put("userAddressId", addressChoosed.getId());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new JSONObject(map).toString());
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).createCourseOrder(body).enqueue(new BaseCallback<>(order -> {
            if (order != null) {
                aliPayOrder = order;
                payByAliPay(order);
            }
        }, throwable -> {
            MyLog.e("创建订单失败" + throwable.getMessage());
        }));
    }

    /**
     * 支付宝支付
     *
     * @param aliPayOrder
     */
    private void payByAliPay(AliPayOrder aliPayOrder) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(SureOrderActivity.this);
                Map<String, String> result = alipay.payV2(aliPayOrder.getAns(), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }
}
