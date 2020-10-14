package io.drew.record.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.adapters.RecordWorksAdapter;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.MyRecordWorks;
import io.drew.record.util.AppBarStateChangeListener;
import io.drew.record.util.AppUtil;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.ShotUtils;
import io.drew.record.util.WxShareUtil;
import io.drew.record.view.CustomLoadView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 我的录播课作品集
 */
public class MyRecordWorksActivity extends BaseActivity {

    private int currentPage = 1;
    private int pageSize = 10;
    @BindView(R.id.recycleView)
    protected RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_submit)
    protected Button btn_submit;
    @BindView(R.id.appbar)
    protected AppBarLayout appbar;
    @BindView(R.id.toolbar)
    protected androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.collapsing_toolbarLayout)
    protected CollapsingToolbarLayout collapsing_toolbarLayout;

    private int unUploadWorkLectures;
    private MyRecordWorks myWorks;
    private int lectureNum_NoProduct;
    private RecordWorksAdapter worksAdapter;
    private LinearLayoutManager layoutManager;
    private boolean afterUploadWork = false;//是否是生成作品跳转过来
    public static final int REQUEST_RECORD_EDIT = 775;
    public static final int REQUEST_RECORD_ADD = 774;
    private MediaPlayer mediaPlayer;
    private GifDrawable gifDrawable_current;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_works;
    }

    @Override
    protected void initData() {
        afterUploadWork = getIntent().getBooleanExtra("afterUploadWork", false);
        lectureNum_NoProduct = getIntent().getIntExtra("lectureNum_NoProduct", 0);
        if (lectureNum_NoProduct > 0) {
            btn_submit.setText("有" + lectureNum_NoProduct + "门课程可上传作品");
        } else {
            btn_submit.setVisibility(View.GONE);
        }
        getUnUploadLecturesNum();
    }

    @Override
    protected void initView() {
        hideActionBar();
        toolbar.setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        lp.topMargin = AppUtil.getStatusBarHeight(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case IDLE:
                        toolbar.setNavigationIcon(R.drawable.ic_back);
                        break;
                    case COLLAPSED:
                        toolbar.setNavigationIcon(R.drawable.ic_back);
                        toolbar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                customStatusBar();
                            }
                        }, 400);
                        break;
                    case EXPANDED:
                        toolbar.setNavigationIcon(R.drawable.ic_back_white);
                        setStatusBarFullTransparent();
                        break;
                }

            }
        });
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        worksAdapter = new RecordWorksAdapter(this, R.layout.item_mywork_record, new ArrayList<>());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(worksAdapter);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                myWorks = null;
                getWorks();
            }
        });
        //不显示动画了，也就解决了闪烁问题
        ((SimpleItemAnimator) recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        worksAdapter.getLoadMoreModule().setAutoLoadMore(true);
        worksAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        worksAdapter.getLoadMoreModule().setPreLoadNumber(1);
        worksAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        View empty = LayoutInflater.from(this).inflate(R.layout.view_empty_myworks, null);
        TextView tv_empty = empty.findViewById(R.id.tv_empty);
        tv_empty.setText("在上课后第二天中午12:00前上传作品\n老师会在当天晚上20:00前点评哦");
        worksAdapter.setEmptyView(empty);
        worksAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadView(false));
        worksAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(MyRecordWorksActivity.this, ImageActivity.class);
                Bundle bundle = new Bundle();
                List<String> urls = new ArrayList<>();
                urls.add(worksAdapter.getData().get(position).getProductImage());
                bundle.putSerializable("data", (Serializable) urls);
                bundle.putInt("index", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        worksAdapter.addChildClickViewIds(R.id.iv_edit, R.id.iv_share, R.id.relay_audio_des, R.id.relay_audio_comment, R.id.shadow);
        worksAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyRecordWorks.RecordsBean recordsBean = (MyRecordWorks.RecordsBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_edit:
                        Intent intent = new Intent();
                        intent.setClass(MyRecordWorksActivity.this, UploadMyRecordWorkActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("record", (Serializable) worksAdapter.getData().get(position));
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_RECORD_EDIT);
                        break;
                    case R.id.iv_share:
                        shareTypeDialog(worksAdapter.getItem(position));
                        break;
                    case R.id.relay_audio_des:
                        GifImageView gifImageView = view.findViewById(R.id.gif_des);
                        playAudio(gifImageView, recordsBean.getProductVoice());
                        break;
                    case R.id.relay_audio_comment:
                        GifImageView gifImageView_comment = view.findViewById(R.id.gif_comment);
                        playAudio(gifImageView_comment, recordsBean.getReviewVoice());
                        break;
                    case R.id.shadow:
                        Intent intent_shadow = new Intent();
                        intent_shadow.setClass(MyRecordWorksActivity.this, ImageActivity.class);
                        Bundle bundle_shadow = new Bundle();
                        List<String> urls = new ArrayList<>();
                        urls.add(worksAdapter.getData().get(position).getProductImage());
                        bundle_shadow.putSerializable("data", (Serializable) urls);
                        bundle_shadow.putInt("index", 0);
                        intent_shadow.putExtras(bundle_shadow);
                        startActivity(intent_shadow);
                        break;
                }
            }
        });

        getWorks();
    }

    /**
     * 播放语音
     */
    private void playAudio(GifImageView gifImageView, String audioPath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying()) {
                stopPlayAudio();
                return;
            }
        }
        GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        if (gifDrawable_current != null) {
            gifDrawable_current.stop();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayAudio();
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            gifDrawable.start();
            gifDrawable_current = gifDrawable;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放语音
     */
    private void stopPlayAudio() {
        if (gifDrawable_current != null) {
            gifDrawable_current.stop();
            gifDrawable_current = null;
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayAudio();
    }

    private void getUnUploadLecturesNum() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class)
                .getUnUploadRecordLectureNum().enqueue(new BaseCallback<>(num -> {
            if (num != null) {
                unUploadWorkLectures = num;
                if (num > 0) {
                    btn_submit.setText("有" + num + "门课程可上传作品");
                    btn_submit.setVisibility(View.VISIBLE);
                } else {
                    btn_submit.setVisibility(View.GONE);
                }
            } else {
                btn_submit.setVisibility(View.GONE);
            }
        }, throwable -> {
            btn_submit.setVisibility(View.GONE);
            refreshLayout.finishRefresh(false);
            MyLog.e("未上传作品录播课程获取失败" + throwable.getMessage());
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_EDIT || requestCode == REQUEST_RECORD_ADD) {
            if (resultCode == RESULT_OK) {
                lectureNum_NoProduct--;
                if (lectureNum_NoProduct > 0) {
                    btn_submit.setText("有" + lectureNum_NoProduct + "门课程可上传作品");
                } else {
                    btn_submit.setVisibility(View.GONE);
                }
                currentPage = 1;
                myWorks = null;
                getWorks();
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        MyRecordWorks.RecordsBean shareRecord = (MyRecordWorks.RecordsBean) extras.getSerializable("record");
                        afterUploadWork = true;
                        shareTypeDialog(shareRecord);
                    }
                }
            }
        }
    }

    private void getWorks() {
        if (EduApplication.instance.currentKid == null) {
            return;
        }
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class)
                .getMyRecordWorks(currentPage, pageSize).enqueue(new BaseCallback<>(works -> {
            if (works != null) {
                myWorks = works;
                if (currentPage == 1) {
                    refreshLayout.finishRefresh(true);
                    if (myWorks.getRecords().size() > 0) {
                        worksAdapter.setNewData(myWorks.getRecords());
                    } else {
                        worksAdapter.setNewData(null);
                    }
                } else {
                    worksAdapter.addData(myWorks.getRecords());
                }
                if (currentPage < myWorks.getPages()) {
                    currentPage += 1;
                    worksAdapter.getLoadMoreModule().loadMoreComplete();
                } else {
                    worksAdapter.getLoadMoreModule().loadMoreEnd();
                }
                recycleView.scrollToPosition(0);
            }
        }, throwable -> {
            refreshLayout.finishRefresh(false);
            MyLog.e("我的作品获取失败" + throwable.getMessage());
        }));
    }

    private void loadMore() {
        if (myWorks != null && myWorks.getPages() >= currentPage) {
            getWorks();
        } else {
            MyLog.e("loadMore--------没有更多数据了");
        }
    }

    public void shareTypeDialog(MyRecordWorks.RecordsBean recordsBean) {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view;
        if (afterUploadWork) {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_share_type_uploaded, null);
            afterUploadWork = false;
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.dialog_share_type, null);
        }

        TextView tv_share = view.findViewById(R.id.tv_share);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                createImgDialog(recordsBean);
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = AppUtil.getScreenWidth(this);
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public void createImgDialog(MyRecordWorks.RecordsBean recordsBean) {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_create_img, null);
        ImageView iv_head = view.findViewById(R.id.iv_head);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_time = view.findViewById(R.id.tv_time);
        ImageView iv_work = view.findViewById(R.id.iv_work);
        TextView tv_lecture_name = view.findViewById(R.id.tv_lecture_name);
        RelativeLayout realy_container = view.findViewById(R.id.realy_container);

        GlideUtils.loadBabyHead(this, "", iv_head);
        tv_name.setText(recordsBean.getStudentName());
        tv_time.setText("创作于" + recordsBean.getProductTime());
        GlideUtils.loadImg(this, recordsBean.getProductImage(), iv_work);
        tv_lecture_name.setText(recordsBean.getLectureName());

        TextView tv_wechat = view.findViewById(R.id.tv_wechat);
        TextView tv_pyq = view.findViewById(R.id.tv_pyq);
        TextView tv_save = view.findViewById(R.id.tv_save);

        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(realy_container, 1);
            }
        });
        tv_pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(realy_container, 2);
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(realy_container, 3);
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = AppUtil.getScreenWidth(this);
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void share(View view, int type) {
        ShotUtils.viewShot(view, new ShotUtils.ShotCallback() {
            @Override
            public void onShotComplete(String savePath, Bitmap bitmap) {
                switch (type) {
                    case 1://好友
                        WxShareUtil.getInstance().shareImage(MyRecordWorksActivity.this, bitmap, 1);
                        break;
                    case 2://朋友圈
                        WxShareUtil.getInstance().shareImage(MyRecordWorksActivity.this, bitmap, 2);
                        break;
                    case 3:
                        if (ShotUtils.saveImageToGallery(MyRecordWorksActivity.this, bitmap)) {
                            ToastManager.showDiyShort("已成功保存至相册");
                        } else {
                            ToastManager.showDiyShort("保存失败");
                        }
                        break;
                }
            }
        });

    }

    @OnClick({R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                Intent intent = new Intent();
                intent.setClass(MyRecordWorksActivity.this, UploadMyRecordWorkActivity.class);
                startActivityForResult(intent, REQUEST_RECORD_ADD);
                break;
        }
    }
}
