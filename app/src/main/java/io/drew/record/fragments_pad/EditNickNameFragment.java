package io.drew.record.fragments_pad;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditNickNameFragment extends BaseFragment {

    @BindView(R.id.et_nicknake)
    protected EditText et_nicknake;
    @BindView(R.id.btn_submit)
    protected TextView btn_submit;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private AuthInfo.UserBean userInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_edit_nick_name;
    }

    @Override
    protected void initData() {
        userInfo = EduApplication.instance.authInfo.getUser();

    }

    @Override
    protected void initView() {
        title.setText("编辑昵称");
        et_nicknake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0 && !userInfo.getNickname().equals(s)) {
                    btn_submit.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_btn_submit));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.btn_submit, R.id.relay_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relay_back:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.btn_submit:
                String newName = et_nicknake.getText().toString().trim();
                if (TextUtils.isEmpty(newName)) {
                    ToastManager.showDiyShort("请先填写您的昵称");
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("nickname", newName);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            new JSONObject(map).toString());
                    RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class)
                            .userUpdate(body).enqueue(new BaseCallback<>(result -> {
                        ToastManager.showDiyShort("修改成功");

                        MessageEvent messageEvent = new MessageEvent(ConfigConstant.EB_UPDATE_NICKNAME);
                        messageEvent.setMessage(newName);
                        EventBus.getDefault().post(messageEvent);
                        getParentFragmentManager().popBackStack();
                    }, throwable -> {
                        MyLog.e("数据异常，请稍后再试" + throwable.getMessage());
                    }));
                }

                break;
        }
    }
}
