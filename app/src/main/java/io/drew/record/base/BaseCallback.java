package io.drew.record.base;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.drew.base.Callback;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.BusinessException;
import io.drew.base.network.RetrofitManager;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.VerticalLoginActivity;
import io.drew.record.activitys_pad.HomeActivity_Pad;
import io.drew.record.service.bean.ResponseBody;
import io.drew.record.util.AppUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BaseCallback<T> extends RetrofitManager.Callback<ResponseBody<T>> {

    public BaseCallback(@NonNull SuccessCallback<T> callback) {
        super(0, new Callback<ResponseBody<T>>() {
            @Override
            public void onSuccess(ResponseBody<T> res) {
                callback.onSuccess(res.data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                checkError(throwable);
            }
        });
    }

    public BaseCallback(@NonNull SuccessCallback<T> success, @Nullable FailureCallback failure) {
        super(0, new Callback<ResponseBody<T>>() {
            @Override
            public void onSuccess(ResponseBody<T> res) {
                success.onSuccess(res.data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.d("onFailure-----" + throwable.getMessage());
                checkError(throwable);
                if (failure != null) {
                    failure.onFailure(throwable);
                }
            }
        });
    }

    private static void checkError(Throwable throwable) {
        String message = throwable.getMessage();
        if (throwable instanceof BusinessException) {
            int code = ((BusinessException) throwable).getCode();
            if (code == 401) {
                if (AppUtil.isPad(EduApplication.instance)){
                    Intent intent=new Intent();
                    intent.setClass(EduApplication.instance, HomeActivity_Pad.class);
                    intent.putExtra("code",code);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    EduApplication.instance.startActivity(intent);
                }else {
                    EduApplication.instance.startActivity(new Intent(EduApplication.instance, VerticalLoginActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
                }

                return;
            }
            if (TextUtils.isEmpty(message)) {
                message = EduApplication.instance.getString(R.string.request_error, code);
            }
        }
        if (!TextUtils.isEmpty(message)) {
            if (message.contains("SSL handshake timed out")) {
                message = "网络故障，请切换网络！";
            }else if (message.contains("No address associated with hostname")){
                message="网络连接出错，请检查网络";
            }
            ToastManager.showShort(message);
        }
    }

    public interface SuccessCallback<T> {
        void onSuccess(T data);
    }

    public interface FailureCallback {
        void onFailure(Throwable throwable);
    }

}
