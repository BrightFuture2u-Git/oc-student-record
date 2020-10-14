package io.drew.record.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.activitys.AddressListActivity;
import io.drew.record.activitys.HomeActivity;
import io.drew.record.activitys.MyOrdersActivity;
import io.drew.record.activitys.MyRecordLecturesActivity;
import io.drew.record.activitys.MyRecordWorksActivity;
import io.drew.record.activitys.SettingsActivity;
import io.drew.record.activitys.UserProfileActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.view.DrewGuide;
import q.rorbin.badgeview.QBadgeView;


public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_head)
    protected ImageView iv_head;
    @BindView(R.id.tv_nickname)
    protected TextView tv_nickname;
    @BindView(R.id.tv_phone)
    protected TextView tv_phone;
    @BindView(R.id.tv_edit_profile)
    protected TextView tv_edit_profile;

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    private AppService appService;
    private AuthInfo.UserBean userBean;
    private HomeActivity homeActivity;
    private QBadgeView badge;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        homeActivity = (HomeActivity) getActivity();
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        if (EduApplication.instance.authInfo != null) {
            userBean = EduApplication.instance.authInfo.getUser();
            refreshUserInfo();
            appService.getUserInfo().enqueue(new BaseCallback<>(userInfo -> {
                if (userInfo != null) {
                    this.userBean = userInfo;
                    MySharedPreferencesUtils.put(context, ConfigConstant.SP_USER_INFO, new Gson().toJson(userInfo));
                    if (EduApplication.instance.currentKid == null && userInfo.getStudentList() != null && userInfo.getStudentList().size() > 0) {
                        EduApplication.instance.currentKid = userInfo.getStudentList().get(0);
                        EduApplication.instance.currentKid_index = 0;
                        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_KID_CHANGE));
                    }
                    refreshUserInfo();
                }
                refreshLayout.finishRefresh();
            }, throwable -> {
                refreshLayout.finishRefresh(false);
                setUnLoginState();
                MyLog.e("用户详情获取失败" + throwable.getMessage());
            }));
        } else {
            setUnLoginState();
            refreshLayout.finishRefresh();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 未登录状态
     */
    private void setUnLoginState() {
        iv_head.setImageResource(R.drawable.head_user_placeholder);
        tv_nickname.setText("未登录");
        tv_phone.setText("点击登录");
        tv_edit_profile.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_UPDATE_NICKNAME:
                userBean.setNickname(event.getMessage());
                tv_nickname.setText(event.getMessage());
                break;
            case ConfigConstant.EB_UPDATE_HEAD:
                userBean.setHeadImage(event.getMessage());
                GlideUtils.loadImg(context, userBean.getHeadImage(), iv_head);
                break;
            case ConfigConstant.EB_KID_CHANGE:
            case ConfigConstant.EB_LOGIN:
            case ConfigConstant.EB_LOGOUT:
                initData();
                break;
        }
    }

    private void refreshUserInfo() {
        GlideUtils.loadUserHead(context, userBean.getHeadImage(), iv_head);
        tv_nickname.setText(userBean.getNickname());
        tv_phone.setText("手机号:" + userBean.getPhone());
        tv_edit_profile.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.tv_edit_profile, R.id.iv_setting, R.id.tv_nickname,
            R.id.tv_phone, R.id.line_record_myAddress, R.id.line_record_myOrders, R.id.line_my_record_lecture
            , R.id.line_record_myWorks})
    public void onClick(View view) {
        if (EduApplication.instance.authInfo == null) {
            if (view.getId() != R.id.iv_setting
                    && view.getId() != R.id.iv_customer
            ) {
                homeActivity.goLogin();
                return;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userBean);
        switch (view.getId()) {
            case R.id.line_my_record_lecture:
                homeActivity.startActivity(MyRecordLecturesActivity.class);
                break;
            case R.id.line_record_myWorks:
                homeActivity.startActivity(MyRecordWorksActivity.class);
                break;
            case R.id.line_record_myOrders:
                homeActivity.startActivity(MyOrdersActivity.class);
                break;
            case R.id.line_record_myAddress:
                homeActivity.startActivity(AddressListActivity.class);
                break;
            case R.id.tv_edit_profile:
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_setting:
                Intent intent_setting = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent_setting);
                break;
        }
    }

    /**
     * 切换宝宝引导
     *
     * @param target
     */
    private void showChangeGuideView(View target) {
        target.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] locations = new int[2];
                target.getLocationOnScreen(locations);
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(target)
                        .setAlpha(150)
                        .setHighTargetCorner(context.getResources().getDimensionPixelOffset(R.dimen.dp_20));
                builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {

                    }

                    @Override
                    public void onDismiss() {
                        SharedPreferences.Editor editor = homeActivity.sharedPreferences.edit();
                        editor.putBoolean(ConfigConstant.SP_GUIDE_CHANGE_BABY_MINE, true);
                        editor.apply();
                    }
                });
                int x = context.getResources().getDimensionPixelOffset(R.dimen.dp_6);
                builder.addComponent(new DrewGuide(3, homeActivity, target, x));
                Guide guide = builder.createGuide();
                guide.show(homeActivity);
            }
        }, 500);

    }
}
