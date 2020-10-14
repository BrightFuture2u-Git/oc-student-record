package io.drew.record.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.VerticalLoginActivity;
import io.drew.record.dialog.LoadingDialog;
import io.drew.record.fragments_pad.VerticalLoginFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.AppUtil;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.widget.EyeProtection;

public abstract class BaseActivity extends AppCompatActivity {

    private EyeProtection.EyeProtectionView eyeProtectionView;
    public ActionBar actionBar;
    public TextView tv_title;
    public TextView tv_right;
    public RelativeLayout relay_back;
    public ImageView iv_back;
    public RelativeLayout relay_actionbar;
    private LoadingDialog loadingDialog;

    @Override
    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppUtil.isPad(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } /*else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            View view = getLayoutInflater().inflate(R.layout.my_action_bar, null);
            actionBar.setCustomView(view);
            actionBar.setElevation(0);
            Toolbar parent = (Toolbar) view.getParent();
            parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
            parent.setContentInsetsAbsolute(0, 0);

//            actionBar.setDisplayShowCustomEnabled(true);
//            View view = LayoutInflater.from(this).inflate(R.layout.my_action_bar, null);
            relay_actionbar = view.findViewById(R.id.relay_actionbar);
            tv_title = view.findViewById(R.id.title);
            tv_right = view.findViewById(R.id.tv_right);
            iv_back = view.findViewById(R.id.iv_back);
            relay_back = view.findViewById(R.id.relay_back);
            relay_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            actionBar.setCustomView(view, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT));
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initData();
        initView();
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(MessageEvent event) {
        MyLog.e("baseactivity---onEventBusMessage" + event.getCode());
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusMessage(MessageEvent event) {
        MyLog.e("baseactivity---onEventBusMessage" + event.getCode());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void setLoadingDialogCancelable(boolean cancelable) {
        if (loadingDialog != null) loadingDialog.setCancelable(cancelable);
    }

    public void showLoadingDialig() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }public void showLoadingDialig(boolean cancelable) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this,cancelable);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void cancleLoadingDialig() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.cancel();
            }
        }

    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initData();

    protected abstract void initView();

    public void initActionBar(String title, boolean showBack) {
        setTitle(title);
        showBack(showBack);
    }

    public void hideActionBar() {
        if (actionBar != null) actionBar.hide();
    }

    public void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }
    }

    public void setActionBarRight(String title, View.OnClickListener listener) {
        if (tv_right != null) {
            tv_right.setText(title);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(listener);
        }
    }

    public void setActionBarRightColor(String color) {
        if (tv_right != null) {
            tv_right.setTextColor(Color.parseColor(color));
        }
    }

    public void showBack(boolean showBack) {
        if (showBack && iv_back != null) {
            iv_back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (EyeProtection.isNeedShow()) {
            showEyeProtection();
        } else {
            dismissEyeProtection();
        }
    }

    protected void showEyeProtection() {
        if (eyeProtectionView == null) {
            eyeProtectionView = new EyeProtection.EyeProtectionView(this);
        }
        if (eyeProtectionView.getParent() == null) {
            addContentView(eyeProtectionView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        eyeProtectionView.setVisibility(View.VISIBLE);
    }

    protected void dismissEyeProtection() {
        if (eyeProtectionView != null) {
            eyeProtectionView.setVisibility(View.GONE);
        }
    }

    protected void removeFromParent(View view) {
        if (view == null) return;
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
    }

    /**
     * 全透状态栏
     */
    public void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void customStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void goLogin() {
        if (AppUtil.isPad(this)) {
            new VerticalLoginFragment().show(getSupportFragmentManager(), "login");
        } else {
            startActivity(VerticalLoginActivity.class);
        }

    }

    /**
     * 登录成功，服务器将有最近课程的孩子优先排序
     *
     * @param authInfo
     */
    public void initLogin(AuthInfo authInfo) {
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_AUTH_INFO, new Gson().toJson(authInfo));
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_ACCOUNT, authInfo.getUser().getPhone());
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_NICKNAME, authInfo.getUser().getNickname());
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_TOKEN, authInfo.getToken());
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_REFRESH_TOKEN, authInfo.getRefreshToken());
        MySharedPreferencesUtils.put(this, ConfigConstant.SP_USER_ID, authInfo.getUser().getId());
        RetrofitManager.instance(authInfo.getToken());

        EduApplication.instance.authInfo = authInfo;
        if (authInfo.getUser().getStudentList() != null && authInfo.getUser().getStudentList().size() > 0) {
            EduApplication.instance.currentKid_index = 0;
//            EduApplication.instance.currentKid_index = (int) MySharedPreferencesUtils.get(this, ConfigConstant.SP_CURRENT_KID_INDEX, 0);
//            if (EduApplication.instance.currentKid_index + 1 > authInfo.getUser().getStudentList().size()) {
//                EduApplication.instance.currentKid_index = 0;
//                MySharedPreferencesUtils.put(this, ConfigConstant.SP_CURRENT_KID_INDEX, 0);
//            }
            EduApplication.instance.currentKid = authInfo.getUser().getStudentList().get(EduApplication.instance.currentKid_index);
        }
        MyLog.e("登录成功");
        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_LOGIN));
    }

    public boolean isVip() {
        return EduApplication.instance.authInfo != null
                && EduApplication.instance.authInfo.getUser() != null
                && EduApplication.instance.authInfo.getUser().getHadHuaLeMeVip() == 1;
    }

    public boolean isLogin() {
        return EduApplication.instance.authInfo != null;
    }
}
