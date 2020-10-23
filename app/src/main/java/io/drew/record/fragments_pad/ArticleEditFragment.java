package io.drew.record.fragments_pad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.R;
import io.drew.record.adapters.GridImgAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.dialog.LoadingDialog;
import io.drew.record.interfaces.OnImgClickListener;
import io.drew.record.interfaces.OnImgItemClickListener;
import io.drew.record.interfaces.OnOssFileUploadListener;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.LocalImg;
import io.drew.record.service.bean.response.StsInfo;
import io.drew.record.util.BitmapUtil;
import io.drew.record.util.OSSManager;
import io.drew.record.util.SoftKeyBoardListener;
import io.drew.record.view.GlideEngine;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ArticleEditFragment extends DialogFragment implements OnImgClickListener {

    private static final int IMG_MAX_NUM = 4;
    @BindView(R.id.tv_num_tezt)
    protected TextView tv_num_tezt;
    @BindView(R.id.et_content)
    protected EditText et_content;
    @BindView(R.id.gridView)
    protected GridView gridView;
    @BindView(R.id.tv_cancle)
    protected TextView tv_cancle;
    @BindView(R.id.tv_submit)
    protected TextView tv_submit;
    private String content;

    private List<LocalImg> urls = new ArrayList<>();
    private AppService appService;
    private StsInfo stsInfo;
    private LoadingDialog loadingDialog;
    private GridImgAdapter gridImgAdapter;

    protected View view;
    private String url;//传递过来的图片地址

    public ArticleEditFragment() {
    }

    public ArticleEditFragment(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//圆角需要先透明背景
        view = inflater.inflate(R.layout.fragment_article_edit, container, false);
        ButterKnife.bind(this, view);
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        initView();
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(MessageEvent event) {
        if (event != null && event.getCode() == ConfigConstant.EB_WORK_DRESSED) {
            String path = event.getMessage();
            if (path != null) {
                LocalImg localImg = new LocalImg();
                localImg.localPath = path;
                localImg.compressedPath = path;
                urls.add(localImg);
                gridImgAdapter.setData(getUrls());
                gridImgAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_264);
        params.height = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_285);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        loadingDialog = new LoadingDialog(getContext());
        gridImgAdapter = new GridImgAdapter(getContext());
        gridImgAdapter.setData(getUrls());
        gridView.setAdapter(gridImgAdapter);
        gridImgAdapter.setItemOnClickListener(new OnImgItemClickListener() {
            @Override
            public void onImgItemClick(int position) {
                urls.remove(position);
                gridImgAdapter.setData(getUrls());
                gridImgAdapter.notifyDataSetChanged();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == urls.size()) {
                    if (urls.size() < IMG_MAX_NUM) {
                        PictureSelector.create(ArticleEditFragment.this)
                                .openGallery(PictureMimeType.ofImage())
                                .theme(R.style.WeChatstyle)
                                .isWeChatStyle(true)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .maxSelectNum(IMG_MAX_NUM - urls.size())
                                .isCamera(true)
                                .isCompress(true)// 是否压缩
                                .compressQuality(80)// 图片压缩后输出质量 0~ 100
                                .minimumCompressSize(2000)// 小于500kb的图片不压缩
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(List<LocalMedia> images) {
                                        if (images.size() == 1) {
                                            if (images.get(0).isCompressed()){
                                                new WorkDressFragment(images.get(0).getCompressPath()).myShow(getChildFragmentManager(), "dress");
                                            }else {
                                                new WorkDressFragment(images.get(0).getRealPath()).myShow(getChildFragmentManager(), "dress");
                                            }
                                        } else {
                                            for (LocalMedia media : images) {
                                                LocalImg localImg = new LocalImg();
                                                localImg.localPath = media.getRealPath()==null?media.getPath():media.getRealPath();
                                                localImg.compressedPath = BitmapUtil.compressImage(localImg.localPath);
                                                urls.add(localImg);
                                            }
                                            gridImgAdapter.setData(getUrls());
                                            gridImgAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }
                }
            }
        });
        et_content.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
                if (wordNum.length() > 0) {
                    tv_submit.setTextColor(Color.parseColor("#6A48F5"));
                } else {
                    tv_submit.setTextColor(Color.parseColor("#CDCDCD"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                if (s != null)
                    tv_num_tezt.setText("" + s.length() + "/200");
            }
        });

        if (!TextUtils.isEmpty(url)) {
            LocalImg localImg = new LocalImg();
            localImg.compressedPath = url;
            localImg.localPath = url;
            localImg.ossUrl = url;

            urls.add(localImg);
            gridImgAdapter.setData(getUrls());
            gridImgAdapter.notifyDataSetChanged();
        }
    }

    private List<String> getUrls() {
        List<String> u = new ArrayList<>();
        for (LocalImg localImg : urls) {
            u.add(localImg.localPath);
        }
        return u;
    }

    private List<String> getOssUrls() {
        List<String> u = new ArrayList<>();
        for (LocalImg localImg : urls) {
            if (!"add".equals(localImg.ossUrl)) {
                u.add(localImg.ossUrl);
            }
        }
        return u;
    }

    private void submit() {
        SoftKeyBoardListener.hideInput(getActivity());
        content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastManager.showDiyShort("请先输入内容");
        } else if (urls.size() <= 0) {
            send();
        } else {
            getSts();
        }
    }

    private void send() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String url : getOssUrls()) {
            jsonArray.put(url);
        }
        try {
            jsonObject.put("content", content);
            jsonObject.put("imageList", jsonArray);
            jsonObject.put("type", "string");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        appService.addArticle(body).enqueue(new BaseCallback<>(result -> {
            ToastManager.showDiyShort("发送成功");
            Intent intent = new Intent();
            intent.setAction("action_new_article");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            dismiss();
        }, throwable -> {
            MyLog.e("帖子发送失败" + throwable.getMessage());
        }));
    }

    private void getSts() {
        loadingDialog.show();
        appService.getSts().enqueue(new BaseCallback<>(stsInfo -> {
            if (stsInfo != null) {
                this.stsInfo = stsInfo;
                MyLog.e("getSts()=" + stsInfo.getAK());
                upLoadImgs();
            }
        }, throwable -> {
            loadingDialog.dismiss();
            MyLog.e("Sts获取失败" + throwable.getMessage());
            ToastManager.showDiyShort("发送失败，请稍后再试");
        }));
    }

    private void upLoadImgs() {
        LocalImg localImg = getUnUploadImg();
        if (localImg == null) {
            MyLog.e("所有图片都已经上传成功");
            loadingDialog.dismiss();
            send();
        } else {
            MyLog.e("开始上传------" + localImg.compressedPath);
            OSSManager.instance().upload(getContext(), stsInfo, getFileName(localImg.localPath), localImg.compressedPath, new OnOssFileUploadListener() {
                @Override
                public void onUploadSuccess(String url) {
                    MyLog.e(url + "上传成功");
                    localImg.ossUrl = url;
                    upLoadImgs();
                }

                @Override
                public void onUploadFail() {
                    loadingDialog.dismiss();
                    ToastManager.showDiyShort("发送失败，请稍后再试");
                }
            });
        }
    }

    private LocalImg getUnUploadImg() {
        for (LocalImg localImg : urls) {
            if (!localImg.hasUpload()) return localImg;
        }
        return null;
    }

    private String getFileName(String localPath) {
        String contact = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("account", "");
        String name = "";
        int start = localPath.lastIndexOf("/");
        if (start != -1) {
            name = localPath.substring(start + 1);
        } else {
            return null;
        }
        return "fourm_" + contact + "_" + System.currentTimeMillis() + "_" + name;
    }

    @Override
    public void onImgClick(int position, String url, List<String> urlList) {
        ToastManager.showDiyShort("点击了图片" + position);
    }

}
