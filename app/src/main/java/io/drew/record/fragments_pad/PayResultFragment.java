package io.drew.record.fragments_pad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.base.BaseCallback;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.util.ShotUtils;
import io.drew.record.util.WxShareUtil;

/**
 * 支付结果页面
 */
public class PayResultFragment extends BaseDialogFragment {

    @BindView(R.id.line_success)
    protected LinearLayout line_success;
    @BindView(R.id.btn_success)
    protected Button btn_success;
    @BindView(R.id.tv_tip)
    protected TextView tv_tip;
    @BindView(R.id.download)
    protected Button download;

    @BindView(R.id.line_fail)
    protected LinearLayout line_fail;
    @BindView(R.id.btn_repay)
    protected Button btn_repay;
    @BindView(R.id.btn_cancel)
    protected Button btn_cancel;

    private boolean success = false;
    private int payType = 0;
    private String orderId;
    private String orderToken;

    public PayResultFragment() {

    }

    public PayResultFragment(boolean success, int payType, String orderId, String orderToken) {
        this.success = success;
        this.payType = payType;
        this.orderId = orderId;
        this.orderToken = orderToken;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initData() {
        getPayStatus();
    }

    @Override
    protected void initView() {
        line_success.setVisibility(View.GONE);
        line_fail.setVisibility(View.GONE);
        SpannableString spanString = new SpannableString("请务必通过二维码添加老师微信，获取课程安排");
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#625DFB"));
                ds.setUnderlineText(false);

            }
        }, 8, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tip.setText(spanString);
    }

    @OnClick({R.id.btn_success, R.id.btn_cancel, R.id.btn_repay, R.id.download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_success:
            case R.id.btn_cancel:
                EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_ORDER_CANCEL));
                dismiss();
                break;
            case R.id.btn_repay:
                MessageEvent event = new MessageEvent(ConfigConstant.EB_REPAY);
                event.setIntMessage(payType);
                EventBus.getDefault().post(event);
                dismiss();
                break;
            case R.id.download:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr_add_teacher);
                if (ShotUtils.saveImageToGallery(mContext, bitmap)) {
                    ToastManager.showDiyShort("已成功保存至相册");
                    download.setText("已成功保存至相册");
                    download.setEnabled(false);
                    WxShareUtil.getInstance().launchWeChat(mContext);
                } else {
                    ToastManager.showDiyShort("保存失败");
                }
                break;
        }
    }

    /**
     * 查询支付状态
     */
    private void getPayStatus() {
//        showLoadingDialig();
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).paymentReview(orderId, orderToken).enqueue(new BaseCallback<>(payStatus -> {
//            cancleLoadingDialig();
            if (payStatus != null) {
                if (payStatus.getAns().equals("1")) {
                    line_success.setVisibility(View.VISIBLE);
                    line_fail.setVisibility(View.GONE);
                } else {
                    line_success.setVisibility(View.GONE);
                    line_fail.setVisibility(View.VISIBLE);
                }
            } else {
                line_success.setVisibility(View.GONE);
                line_fail.setVisibility(View.VISIBLE);
            }
        }, throwable -> {
//            cancleLoadingDialig();
            MyLog.e("查询支付状态失败" + throwable.getMessage());
            line_success.setVisibility(View.GONE);
            line_fail.setVisibility(View.VISIBLE);
        }));
    }
}
