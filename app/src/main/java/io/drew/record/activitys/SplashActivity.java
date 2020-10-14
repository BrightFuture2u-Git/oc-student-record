package io.drew.record.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.activitys_pad.HomeActivity_Pad;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.dialog.CustomDialog;
import io.drew.record.service.AppService;
import io.drew.record.util.AppUtil;
import io.drew.record.util.MySharedPreferencesUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SplashActivity extends BaseActivity {
    private final int REQUEST_CODE_RTC = 101;
    private Handler handler = new Handler();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        hideActionBar();
        if (AppUtil.checkAndRequestAppPermission(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_CODE_RTC)) {
            autoLogin();
        }
    }

    private void toHome() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUtil.isPad(SplashActivity.this)) {
                    startActivity(HomeActivity_Pad.class);
                } else {
                    startActivity(HomeActivity.class);
                }
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    private void autoLogin() {
        String phone = (String) MySharedPreferencesUtils.get(this, ConfigConstant.SP_ACCOUNT, "");
        if (TextUtils.isEmpty(phone)) {
            MyLog.d("本地无账号，不自动登录");
            toHome();
        } else {
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
            RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).loginV1(body).enqueue(new BaseCallback<>(authInfo -> {
                if (authInfo != null) {
                    initLogin(authInfo);
                    toHome();
                }
            }, throwable -> {
                if (throwable.getMessage().equals("账号不存在")) {
                    ToastManager.showDiyShort(throwable.getMessage());
                    startActivity(RegistActivity.class);
                    finish();
                } else {
                    toHome();
                }
            }));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                showPermissionDialog();
                return;
            }
        }
        switch (requestCode) {
            case REQUEST_CODE_RTC:
                autoLogin();
                break;
        }
    }

    public void showPermissionDialog() {
        CustomDialog customDialog = new CustomDialog(this, "权限申请");
        customDialog.setContent("为保证程序正常运行，请允许画了么拥有「存储，相机，麦克风」等权限");
        customDialog.setSureText("去设置");
        customDialog.setOnSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        customDialog.setOnCancleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                SplashActivity.this.finish();
            }
        });
        customDialog.show();
    }
}
