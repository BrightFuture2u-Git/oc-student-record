package io.drew.record.fragments_pad;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.WebViewActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.dialog.SureDialog;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.Version;
import io.drew.record.util.AppUtil;
import io.drew.record.util.DataCleanManager;
import io.drew.record.util.MySharedPreferencesUtils;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class SettingsFragment extends BaseFragment {


    @BindView(R.id.tv_size)
    protected TextView tv_size;
    @BindView(R.id.tv_newVersion)
    protected TextView tv_newVersion;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;
    @BindView(R.id.btn_logout)
    protected Button btn_logout;

    private Version version;
    private Badge badge;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initData() {
        try {
            tv_size.setText(DataCleanManager.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkUpdate(false);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_LOGIN:
                btn_logout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void checkUpdate(boolean showUpdate) {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).
                getNewVersion("android", AppUtil.getLocalVersionName(getContext())).enqueue(new BaseCallback<>(version -> {
            if (version != null) {
                this.version = version;
                if (version.getVersion().equals(AppUtil.getLocalVersionName(getContext()))) {
                    tv_newVersion.setText("已是最新版本(" + version.getVersion() + ")");
                    if (showUpdate) ToastManager.showDiyShort("已是最新版本");
                } else {
                    tv_newVersion.setText("有新版本可升级(" + version.getVersion() + ")");
                    badge.bindTarget(tv_newVersion);

                    if (showUpdate) {
                        AppDialogConfig config = new AppDialogConfig();
                        config.setTitle("发现新版本" + version.getVersion())
                                .setOk("升级")
                                .setContent(version.getDescription())
                                .setOnClickOk(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AppUpdater.Builder()
                                                .serUrl(version.getUrl())
                                                .build(context)
                                                .start();
                                        AppDialog.INSTANCE.dismissDialog();
                                    }
                                });
                        AppDialog.INSTANCE.showDialog(context, config);
                    }
                }
            } else {
//                ToastManager.showDiyShort("已是最新版本");
                tv_newVersion.setText("已是最新版本(" + AppUtil.getLocalVersionName(context) + ")");
            }
        }, throwable -> {
            MyLog.e("最新版本获取失败" + throwable.getMessage());
        }));
    }

    @Override
    protected void initView() {
        title.setText("设置");
        relay_back.setVisibility(View.GONE);
        badge = new QBadgeView(getContext());
        badge.setBadgePadding(3, true);//设置大小
        badge.setBadgeBackgroundColor(Color.parseColor("#F24724"));
        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
        badge.setBadgeNumber(-1);
        if (EduApplication.instance.authInfo == null) {
            btn_logout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.relay_termsOfService, R.id.relay_privacy_policy, R.id.relay_clear, R.id.relay_checkUpdate, R.id.btn_logout
            , R.id.relay_feedback, R.id.relay_about, R.id.line_contactUs})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relay_termsOfService:
                Intent intent_termOfService = new Intent();
                intent_termOfService.putExtra("url", ConfigConstant.url_user_agreement);
                intent_termOfService.setClass(context, WebViewActivity.class);
                startActivity(intent_termOfService);
                break;
            case R.id.relay_privacy_policy:
                Intent intent_privacy_policy = new Intent();
                intent_privacy_policy.putExtra("url", ConfigConstant.url_privacy);
                intent_privacy_policy.setClass(context, WebViewActivity.class);
                startActivity(intent_privacy_policy);
                break;
            case R.id.relay_clear:
                if (DataCleanManager.clearAllCache(context)) {
                    ToastManager.showDiyShort("清理成功");
                    tv_size.setText("0M");
                } else {
                    MyLog.e("缓存清理异常");
                }
                break;
            case R.id.relay_checkUpdate:
                checkUpdate(true);
                break;
            case R.id.relay_feedback:
                if (EduApplication.instance.authInfo == null) {
                    goLogin();
                } else {
                    new FeedbackFragment().show(getParentFragmentManager(), "feedback");
                }

                break;
            case R.id.relay_about:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigConstant.url_about+ "?phone=" + MySharedPreferencesUtils.get(context, ConfigConstant.SP_ACCOUNT, ""))));
                break;
            case R.id.line_contactUs:
                callDialog();
                break;
            case R.id.btn_logout:
                SureDialog sureDialog = new SureDialog(getContext(), "确定退出？");
                sureDialog.setOnSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MySharedPreferencesUtils.clear(context);
                        RetrofitManager.instance("");
                        EduApplication.instance.authInfo = null;
                        EduApplication.instance.currentKid = null;
                        EduApplication.instance.currentKid_index = -1;
                        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_LOGOUT));
                        sureDialog.dismiss();
                        btn_logout.setVisibility(View.GONE);
                    }
                });
                sureDialog.show();
                break;
        }
    }

    public void callDialog() {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_call, null);
        Button btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
