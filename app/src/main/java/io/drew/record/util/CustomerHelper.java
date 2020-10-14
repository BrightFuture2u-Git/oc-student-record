package io.drew.record.util;

import android.app.Activity;
import android.content.Intent;

import com.m7.imkfsdk.KfStartHelper;
import com.moor.imkf.IMChatManager;

import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.activitys.VerticalLoginActivity;
import io.drew.record.service.bean.response.AuthInfo;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @Author: KKK
 * @CreateDate: 2020/6/11 11:33 AM
 */
public class CustomerHelper {
    private static CustomerHelper customerHelper;
    private static KfStartHelper helper;
    private static Activity mActivity;

    public static CustomerHelper getInstance(Activity activity) {
        mActivity = activity;
        if (customerHelper == null) {
            customerHelper = new CustomerHelper();
        }
        if (helper == null) {
            helper = new KfStartHelper(activity);
        }
        return customerHelper;
    }

    public void goToCustomer(AuthInfo.UserBean userBean) {
        if (userBean != null) {
            IMChatManager.getInstance().quitSDk();
            helper.initSdkChat(ConfigConstant.CUSTOMER_7MOOR, userBean.getNickname(), userBean.getId());
        } else {
//            helper.initSdkChat(ConfigConstant.CUSTOMER_7MOOR, AppUtil.getDevicesUUID(mActivity), AppUtil.getDevicesUUID(mActivity));
            if (AppUtil.isPad(mActivity)){
                //todo
//                new VerticalLoginFragment().show(mActivity.);
            }else {
                Intent intent = new Intent();
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(EduApplication.instance, VerticalLoginActivity.class);
                EduApplication.instance.startActivity(intent);
            }

        }
    }


}
