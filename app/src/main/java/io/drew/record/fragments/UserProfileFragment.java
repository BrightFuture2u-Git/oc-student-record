package io.drew.record.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

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
import io.drew.record.dialog.LoadingDialog;
import io.drew.record.interfaces.OnOssFileUploadListener;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.service.bean.response.StsInfo;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.OSSManager;
import io.drew.record.view.GlideEngine;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserProfileFragment extends BaseFragment {

    @BindView(R.id.tv_nickname)
    protected TextView tv_nickname;
    @BindView(R.id.tv_phone)
    protected TextView tv_phone;
    @BindView(R.id.iv_head)
    protected ImageView iv_head;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private AuthInfo.UserBean userInfo;
    private LoadingDialog loadingDialog;
    private StsInfo stsInfo;
    private AppService appService;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void initData() {
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        userInfo = EduApplication.instance.authInfo.getUser();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case ConfigConstant.EB_UPDATE_NICKNAME:
                userInfo.setNickname(event.getMessage());
                tv_nickname.setText(event.getMessage());
                break;
        }
    }

    @Override
    protected void initView() {
        title.setText("个人资料");
        relay_back.setVisibility(View.GONE);
        loadingDialog = new LoadingDialog(getContext());
        tv_nickname.setText(userInfo.getNickname());
        tv_phone.setText(userInfo.getPhone());
        GlideUtils.loadImg(context,userInfo.getHeadImage(),iv_head);
    }

    @OnClick({R.id.relay_head, R.id.relay_nickname})
    protected void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        switch (v.getId()) {
            case R.id.relay_head:
                PictureSelector.create(UserProfileFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.WeChatstyle)
                        .isWeChatStyle(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(1)
                        .isCamera(false)
                        .enableCrop(true)
                        .withAspectRatio(1, 1)
                        .isCompress(true)// 是否压缩
                        .compressQuality(10)// 图片压缩后输出质量 0~ 100
                        .minimumCompressSize(500)// 小于500kb的图片不压缩
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> images) {
                                String url;
                                LocalMedia localImg = images.get(0);
                                MyLog.e("选择的图片=" + localImg.getRealPath());
                                MyLog.e("选择的图片=" + localImg.getCutPath());
                                MyLog.e("选择的图片=" + localImg.getCompressPath());
                                if (localImg.isCompressed()) {
                                    url = localImg.getCompressPath();
                                } else {
                                    if (localImg.isCut()) {
                                        url = localImg.getCutPath();
                                    } else {
                                        url = localImg.getPath();
                                    }
                                }
                                getSts(url);

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            case R.id.relay_nickname:
                MessageEvent messageEvent=new MessageEvent(ConfigConstant.EB_PAD_CHANGE_TAB);
                messageEvent.setMessage("editNickName");
                EventBus.getDefault().post(messageEvent);
                break;
        }
    }

    private void getSts(String url) {
        loadingDialog.show();
        appService.getSts().enqueue(new BaseCallback<>(stsInfo -> {
            if (stsInfo != null) {
                this.stsInfo = stsInfo;
                MyLog.e("getSts()=" + stsInfo.getAK());
                upLoadImg(url);
            }
        }, throwable -> {
            loadingDialog.dismiss();
            MyLog.e("Sts获取失败" + throwable.getMessage());
            ToastManager.showDiyShort("上传失败，请稍后再试");
        }));
    }

    private void upLoadImg(String imageUrl) {
        MyLog.e("开始上传------" + imageUrl);
        OSSManager.instance().upload(context, stsInfo, getFileName(imageUrl), imageUrl, new OnOssFileUploadListener() {
            @Override
            public void onUploadSuccess(String url) {
                MyLog.e(url + "上传成功");
                loadingDialog.dismiss();
                HashMap<String, String> map = new HashMap<>();
                map.put("headImage", url);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        new JSONObject(map).toString());
                appService.userUpdate(body).enqueue(new BaseCallback<>(result -> {
                    userInfo.setHeadImage(url);
                    GlideUtils.loadImg(context,userInfo.getHeadImage(),iv_head);
                    MessageEvent messageEvent = new MessageEvent(ConfigConstant.EB_UPDATE_HEAD);
                    messageEvent.setMessage(url);
                    EventBus.getDefault().post(messageEvent);

                    ToastManager.showDiyShort("修改成功");
                }, throwable -> {
                    MyLog.e("数据异常，请稍后再试" + throwable.getMessage());
                }));
            }

            @Override
            public void onUploadFail() {
                loadingDialog.dismiss();
                ToastManager.showDiyShort("上传失败，请稍后再试");
            }
        });

    }

    private String getFileName(String localPath) {
        String contact = context.getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("account", "");
        String name = "";
        int start = localPath.lastIndexOf("/");
        if (start != -1) {
            name = localPath.substring(start + 1);
        } else {
            return null;
        }
        return "fourm_" + contact + "_" + System.currentTimeMillis() + "_" + name;
    }
}
