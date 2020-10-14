package io.drew.record.fragments_pad;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FeedbackFragment extends BaseDialogFragment {

    @BindView(R.id.et_content)
    protected EditText et_content;
    @BindView(R.id.et_phone)
    protected EditText et_phone;
    @BindView(R.id.btn_submit)
    protected Button btn_submit;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private String content;
    private String phone;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected int getScreenType() {
        return 2;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        title.setText("意见反馈");
        relay_back.setVisibility(View.VISIBLE);
        relay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_submit.setClickable(false);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
                enableLogin();
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
                        et_content.setText("");
                        et_phone.setText("");
                    }, throwable -> {
                        MyLog.e("意见反馈" + throwable.getMessage());
                    }));
                }

                break;
        }
    }

    private void enableLogin() {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(content)) {
            btn_submit.setClickable(false);
            btn_submit.setBackground(getContext().getDrawable(R.drawable.shape_bg_btn_input));
        } else {
            btn_submit.setClickable(true);
            btn_submit.setBackground(getContext().getDrawable(R.drawable.shape_bg_btn_submit));
        }
    }
}
