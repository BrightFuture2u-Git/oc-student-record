package io.drew.record.fragments_pad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.VerticalLoginActivity;
import io.drew.record.model.MessageEvent;
import io.drew.record.util.AppUtil;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/28 1:36 AM
 */
public abstract class BaseDialogFragment extends DialogFragment {
    protected Context mContext;
    protected View view;
    private TextView title;
    private RelativeLayout relay_back;
    private ImageView iv_back;
    private TextView tv_right;
    private int screenType = 0;//0默认,1全屏，2半屏,3窄边


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialog);
        super.onCreate(savedInstanceState);
    }

    /**
     * @param manager
     * @param tag     解决Can not perform this action after onSaveInstanceState
     */
    public void myShow(@NonNull FragmentManager manager, @Nullable String tag) {
//        if (!isAdded()) return;//for Fragment has not been attached yet
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isRegisterEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        screenType = getScreenType();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(getLayoutResId(), container, false);
        title = view.findViewById(R.id.title);
        relay_back = view.findViewById(R.id.relay_back);
        iv_back = view.findViewById(R.id.iv_back);
        tv_right = view.findViewById(R.id.tv_right);
        ButterKnife.bind(this, view);
        initData();
        initView();

        if (relay_back != null) {
            relay_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        getDialog().setCanceledOnTouchOutside(getCanceledOnTouchOutside());
        if (getDialog() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            view.setPadding(0, 60, 0, 0);
            view.requestLayout();
        }
        return view;
    }

    public void initActionBar(String actionBarTitle) {
        if (title != null) {
            title.setText(actionBarTitle);
        }
    }

    public void setActionBarRight(String text, View.OnClickListener listener) {
        if (tv_right != null) {
            tv_right.setText(text);
            showRight(true);
            setRightOnClickListener(listener);
        }
    }

    public void setActionBarRightColor(String color) {
        if (tv_right != null) tv_right.setTextColor(Color.parseColor(color));
    }

    public void setRightIcon(int resId) {
        if (tv_right != null) {
            tv_right.setBackgroundResource(resId);
        }
    }

    public void showRight(boolean show) {
        if (show) {
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightOnClickListener(View.OnClickListener listener) {
        if (tv_right != null) {
            tv_right.setOnClickListener(listener);
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        switch (screenType) {
            case 0:
                params.width = (AppUtil.getScreenWidth(getActivity()) - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_44)) * 2 / 5 + getResources().getDimensionPixelSize(R.dimen.dp_10);
                break;
            case 1:
                params.width = AppUtil.getScreenWidth(getActivity()) - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_43);
                break;
            case 2:
                params.width = (AppUtil.getScreenWidth(getActivity()) - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_44)) / 2;
                break;
            case 3:
                params.width = (AppUtil.getScreenWidth(getActivity())) / 3;
                break;
        }
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (screenType == 1) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        getDialog().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.RIGHT;
        window.getAttributes().windowAnimations = R.style.DialogFragmentAnim;
        window.setAttributes(windowParams);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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

    protected int getScreenType() {
        return 0;
    }

    protected boolean getCanceledOnTouchOutside() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(MessageEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusMessage(MessageEvent event) {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void goLogin() {
        if (AppUtil.isPad(getContext())) {
            new VerticalLoginFragment().show(getParentFragmentManager(), "login");
        } else {
            EduApplication.instance.startActivity(new Intent(EduApplication.instance, VerticalLoginActivity.class));
        }
    }
}
