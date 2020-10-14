package io.drew.record.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.drew.base.MyLog;
import io.drew.record.EduApplication;
import io.drew.record.activitys.VerticalLoginActivity;
import io.drew.record.fragments_pad.VerticalLoginFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.util.AppUtil;

public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isRegisterEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            MyLog.e("添加监听---" + getClass().getName());
        }

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(MessageEvent event) {
        MyLog.e("basefragment---onEventBusMessage" + event.getCode());
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusMessage(MessageEvent event) {
        MyLog.e("basefragemnt---onStickyEventBusMessage" + event.getCode());
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(MessageEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(MessageEvent event) {

    }


    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initData();

    protected abstract void initView();

    public boolean canShow() {
        if (context != null) {
            if (context instanceof Activity) {
                Activity mActivity = (Activity) context;
                if (mActivity.isFinishing() || mActivity.isDestroyed()) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected void runOnUiThread(Runnable runnable) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        }
    }

    public void goLogin() {
        if (AppUtil.isPad(context)) {
            new VerticalLoginFragment().show(getParentFragmentManager(), "login");
        } else {
            Intent intent = new Intent();
            intent.setClass(EduApplication.instance, VerticalLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            EduApplication.instance.startActivity(intent);
        }

    }
}
