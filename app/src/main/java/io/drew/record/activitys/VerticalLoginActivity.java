package io.drew.record.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.AppUtil;
import io.drew.record.util.SoftKeyBoardListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VerticalLoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    protected EditText et_phone;
    @BindView(R.id.btn_login)
    protected Button btn_login;
    @BindView(R.id.tv_service)
    protected TextView tv_service;

    private AppService appService;
    private String phone;
    private AuthInfo authInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vertical_login;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
    }

    @OnClick({R.id.btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SoftKeyBoardListener.hideInput(VerticalLoginActivity.this);
                login();
                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShort("请先填写手机号");
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("account", phone);
            object.put("password", 123456);
            object.put("resolutionRatio", AppUtil.getDeviceResolutionRatio(this));
            object.put("source", AppUtil.isPad(this) ? "android_pad" : "android");
            object.put("systemVersion", "ANDROID_" + AppUtil.getSystemVersion());
            object.put("terminal", AppUtil.getDeviceBrand() + "_" + AppUtil.getSystemModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                object.toString());
        appService.loginV1(body).enqueue(new BaseCallback<>(authInfo -> {
            if (authInfo != null) {
                this.authInfo = authInfo;
                initLogin(authInfo);
                finish();
            }
        }, throwable -> {
            if (throwable.getMessage().equals("账号不存在")) {
                ToastManager.showDiyShort(throwable.getMessage());
                startActivity(RegistActivity.class);
                finish();
            }
        }));
    }

    @Override
    protected void initView() {
        setActionBarRight("去注册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegistActivity.class);
                finish();
            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString();
                enableLogin();
            }
        });
        btn_login.setClickable(false);
        SpannableString spanString = new SpannableString(getString(R.string.user_rule_content_login2));
        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("url", ConfigConstant.url_privacy);
                intent.setClass(VerticalLoginActivity.this, WebViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#7350F5"));
                ds.setUnderlineText(false);
            }
        }, 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spanString2 = new SpannableString(getString(R.string.user_rule_content_login4));
        spanString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("url", ConfigConstant.url_user_agreement);
                intent.setClass(VerticalLoginActivity.this, WebViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#7350F5"));
                ds.setUnderlineText(false);

            }
        }, 0, spanString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_service.setText(getString(R.string.user_rule_content_login1));
        tv_service.append(spanString);
        tv_service.append(getString(R.string.user_rule_content_login3));
        tv_service.append(spanString2);
        tv_service.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void enableLogin() {
        if (!TextUtils.isEmpty(phone)) {
            btn_login.setClickable(true);
            btn_login.setBackground(getDrawable(R.drawable.shap_blue_30));
        } else {
            btn_login.setClickable(false);
            btn_login.setBackground(getDrawable(R.drawable.shape_bg_btn_input));
        }
    }
}
