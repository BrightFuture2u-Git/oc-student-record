package io.drew.record.fragments_pad;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.activitys.WebViewActivity;
import io.drew.record.activitys_pad.HomeActivity_Pad;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.AppUtil;
import io.drew.record.util.SoftKeyBoardListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegistFragment extends DialogFragment {

    @BindView(R.id.et_phone)
    protected EditText et_phone;
    @BindView(R.id.et_code)
    protected EditText et_code;
    @BindView(R.id.tv_getCode)
    protected TextView tv_getCode;
    @BindView(R.id.btn_login)
    protected Button btn_login;
    @BindView(R.id.tv_service)
    protected TextView tv_service;
    @BindView(R.id.tv_cancle)
    protected TextView tv_cancle;
    @BindView(R.id.tv_toLogin)
    protected TextView tv_toLogin;

    private AppService appService;
    private String phone;
    private String code;
    private AuthInfo authInfo;
    protected View view;
    private HomeActivity_Pad homeActivity_pad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//圆角需要先透明背景
        homeActivity_pad = (HomeActivity_Pad) getActivity();
        view = inflater.inflate(R.layout.fragment_regist, container, false);
        ButterKnife.bind(this, view);
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        initView();
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_189);
        params.height = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_228);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    @OnClick({R.id.btn_login, R.id.tv_getCode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SoftKeyBoardListener.hideInput(getActivity());
                login();
                break;
            case R.id.tv_getCode:
                if (TextUtils.isEmpty(phone)) {
                    ToastManager.showDiyShort("请先输入手机号");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phone", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                appService.getVerificationCode(body).enqueue(new BaseCallback<>(result -> {
                    if (result) {
                        new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_getCode.setText(millisUntilFinished / 1000 + "s");
                            }

                            @Override
                            public void onFinish() {
                                tv_getCode.setText("获取验证码");
                                tv_getCode.setClickable(true);
                            }
                        }.start();
                        ToastManager.showDiyShort("验证码已发送，请注意查收");
                    } else {
                        ToastManager.showDiyShort("验证码发送失败");
                        tv_getCode.setClickable(true);
                    }
                }, throwable -> {
                    MyLog.e("验证码获取失败" + throwable.getMessage());
                }));
                tv_getCode.setClickable(false);
                break;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(phone)) {
            ToastManager.showShort("请先填写手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastManager.showShort("请先填写验证码");
            return;
        }
        if (BuildConfig.DEBUG) {
            code = "666666";
        }
        JSONObject object = new JSONObject();
        try {
            object.put("phone", phone);
            object.put("code", code);
            object.put("resolutionRatio", AppUtil.getDeviceResolutionRatio(getActivity()));
            object.put("source", AppUtil.isPad(getActivity()) ? "android_pad" : "android");
            object.put("systemVersion", "ANDROID_" + AppUtil.getSystemVersion());
            object.put("terminal", AppUtil.getDeviceBrand() + "_" + AppUtil.getSystemModel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                object.toString());
        appService.loginByPhone(body).enqueue(new BaseCallback<>(authInfo -> {
            if (authInfo != null) {
                this.authInfo = authInfo;
                homeActivity_pad.initLogin(authInfo);
                dismiss();
            }
        }, throwable -> {
            MyLog.e("登录失败" + throwable.getMessage());
        }));
    }

    private void initView() {

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new VerticalLoginFragment().show(getParentFragmentManager(), "login");
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
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code = s.toString();
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
                intent.setClass(getContext(), WebViewActivity.class);
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
                intent.setClass(getContext(), WebViewActivity.class);
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
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
            btn_login.setClickable(true);
            btn_login.setBackground(getActivity().getDrawable(R.drawable.shap_blue_30));
        } else {
            btn_login.setClickable(false);
            btn_login.setBackground(getActivity().getDrawable(R.drawable.shape_bg_btn_input));
        }
    }
}
