package io.drew.record.fragments_pad;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.utils.MoorUtils;
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
import io.drew.record.activitys_pad.HomeActivity_Pad;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.MySharedPreferencesUtils;
import q.rorbin.badgeview.QBadgeView;


public class MineFragmentMain extends BaseFragment {

    @BindView(R.id.iv_head)
    protected ImageView iv_head;
    @BindView(R.id.tv_nickname)
    protected TextView tv_nickname;
    @BindView(R.id.tv_phone)
    protected TextView tv_phone;
    @BindView(R.id.line_edit)
    protected LinearLayout line_edit;

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;

    @BindView(R.id.line_tab_record_content)
    protected LinearLayout line_tab_record_content;
    @BindView(R.id.line_myLectures_record)
    protected LinearLayout line_myLectures_record;
    @BindView(R.id.line_setting_record)
    protected LinearLayout line_setting_record;
    @BindView(R.id.line_customer_record)
    protected LinearLayout line_customer_record;
    @BindView(R.id.line_addres)
    protected LinearLayout line_addres;
    @BindView(R.id.line_orders)
    protected LinearLayout line_orders;
    @BindView(R.id.line_myWorks_record)
    protected LinearLayout line_myWorks_record;

    private AppService appService;
    private AuthInfo.UserBean userBean;
    private HomeActivity_Pad homeActivity;
    private QBadgeView badge;
    private View currentItem = null;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        homeActivity = (HomeActivity_Pad) getActivity();
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

            getUnReadCount();
        } else {
            setUnLoginState();
            refreshLayout.finishRefresh();
        }


    }

    private void checkItem(View view) {
        if (currentItem != null) {
            currentItem.setBackgroundColor(Color.WHITE);
        }
        if (view != null && view != line_edit) {
            view.setBackgroundColor(Color.parseColor("#F3F3F3"));
        }

        currentItem = view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void setUnLoginState() {
        iv_head.setImageResource(R.drawable.head_user_placeholder);
        tv_nickname.setText("未登录");
        tv_phone.setText("点击登录");
        line_edit.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取未读消息数示例
     */
    private void getUnReadCount() {
        if (MoorUtils.isInitForUnread(context)) {
            IMChatManager.getInstance().getMsgUnReadCountFromService(new IMChatManager.HttpUnReadListen() {
                @Override
                public void getUnRead(int acount) {
                    MyLog.e("未读消息数量=" + acount);
//                    if (acount > 0) {
//                        badge = new QBadgeView(getActivity());
//                        badge.setBadgePadding(3, true);//设置大小
//                        badge.setBadgeBackgroundColor(Color.parseColor("#F24724"));
//                        badge.setBadgeGravity(Gravity.END | Gravity.TOP);
//                        badge.bindTarget(iv_customer);
//                        badge.setBadgeNumber(-1);
//                    }
                }
            });
        } else {
            MyLog.e("未读消息还未初始化");
        }
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
        checkItem(line_myLectures_record);
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
        line_edit.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.line_edit,
            R.id.tv_phone, R.id.line_myLectures_record, R.id.line_myWorks_record,
            R.id.line_orders, R.id.line_addres, R.id.line_customer_record, R.id.line_setting_record})
    public void onClick(View view) {
        if (EduApplication.instance.authInfo == null) {
            if (view.getId() != R.id.line_setting_record) {
                homeActivity.goLogin();
                return;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userBean);
        switch (view.getId()) {
            case R.id.line_myLectures_record:
                checkItem(line_myLectures_record);
                MessageEvent messageEvent_record_lectures = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_record_lectures.setMessage("myRecordLectures");
                EventBus.getDefault().post(messageEvent_record_lectures);
                break;
            case R.id.line_myWorks_record:
                checkItem(line_myWorks_record);
                MessageEvent messageEvent_recordWorks = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_recordWorks.setMessage("recordWorks");
                EventBus.getDefault().post(messageEvent_recordWorks);
                break;
            case R.id.line_orders:
                checkItem(line_orders);
                MessageEvent messageEvent_orders = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_orders.setMessage("orders");
                EventBus.getDefault().post(messageEvent_orders);
                break;
            case R.id.line_addres:
                checkItem(line_addres);
                MessageEvent messageEvent_address_record = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_address_record.setMessage("address");
                EventBus.getDefault().post(messageEvent_address_record);
                break;
            case R.id.line_customer_record:
                checkItem(line_customer_record);
//                if (badge != null) badge.setBadgeNumber(0);
//                CustomerHelper.getInstance(getActivity()).goToCustomer(userBean);
                MessageEvent messageEvent_customer_record = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_customer_record.setMessage("customer");
                EventBus.getDefault().post(messageEvent_customer_record);
                break;
            case R.id.line_setting_record:
                checkItem(line_setting_record);
                MessageEvent messageEvent_setting_record = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_setting_record.setMessage("setting");
                EventBus.getDefault().post(messageEvent_setting_record);
                break;
            case R.id.line_edit:
                checkItem(line_edit);
                MessageEvent messageEvent_edit = new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent_edit.setMessage("edit");
                EventBus.getDefault().post(messageEvent_edit);
                break;
        }
    }
}
