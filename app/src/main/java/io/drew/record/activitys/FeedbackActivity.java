package io.drew.record.activitys;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.et_content)
    protected EditText et_content;
    @BindView(R.id.et_phone)
    protected EditText et_phone;
    @BindView(R.id.btn_submit)
    protected Button btn_submit;

    private AuthInfo.UserBean userInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {
        userInfo = EduApplication.instance.authInfo.getUser();
        if (userInfo == null) {
            finish();
            return;
        }
    }

    @Override
    protected void initView() {
        initActionBar("意见反馈", true);
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String content = et_content.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(phone)) {
                    ToastManager.showDiyShort("请先输入您的问题和联系方式");
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("contact", phone);
                    map.put("content", content);
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            new JSONObject(map).toString());
                    AppService appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
                    appService.feedback(body).enqueue(new BaseCallback<>(result -> {
                        ToastManager.showDiyShort("提交成功");
                        finish();
                    }, throwable -> {
                        MyLog.e("意见反馈" + throwable.getMessage());
                    }));
                }

                break;
        }
    }
}
