package io.drew.record.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.nativeclass.CacheConfig;
import com.aliyun.player.source.VidAuth;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.model.RecordVideoTag;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.OssRecord;
import io.drew.record.service.bean.response.RecordCourseLecture;
import io.drew.record.util.TimeUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 录播课播放页
 */
public class RecordVideoActivity extends BaseActivity {

    @BindView(R.id.surface_view)
    protected SurfaceView surface_view;
    @BindView(R.id.tv_totalTime)
    protected TextView tv_totalTime;
    @BindView(R.id.tv_currentTime)
    protected TextView tv_currentTime;
    @BindView(R.id.seekBar)
    protected AppCompatSeekBar seekBar;
    @BindView(R.id.relay_top)
    protected RelativeLayout relay_top;
    @BindView(R.id.relay_bottom)
    protected RelativeLayout relay_bottom;
    @BindView(R.id.relay_complete)
    protected LinearLayout relay_complete;
    @BindView(R.id.iv_back)
    protected ImageView iv_back;
    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.iv_play)
    protected ImageView iv_play;
    @BindView(R.id.tv_restart)
    protected TextView tv_restart;
    @BindView(R.id.tv_exit)
    protected TextView tv_exit;
    //    private String url = "https://image.brightfuture360.com/course/20200828153325tPgj2uNlIHRBoekfrz.webm";
//    private String url = "http://vod.hualeme.com/sv/c30cea6-17448deb1af/c30cea6-17448deb1af.mp4?auth_key=1599032982-048bcfed2fb84d949ebe6c8fc1ca9b84-0-a02f409f2e9393496f23e63ff61457dd";
    private OssRecord ossRecord;
    private RecordCourseLecture lectureInfo;
    private AliPlayer aliyunVodPlayer;
    private boolean isPlaying = false;
    private boolean isFinished = false;
    private RecordVideoTag recordVideoTag;
    private int time_watch = 0;

    private static final int HIDE_MENU = 1;

    private final NoLeakHander hander = new NoLeakHander(this);

    private static class NoLeakHander extends Handler {
        private final WeakReference<RecordVideoActivity> mActivity;

        public NoLeakHander(RecordVideoActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            RecordVideoActivity activity = mActivity.get();
            switch (msg.what) {
                case HIDE_MENU:
                    activity.hideMenu();
                    break;
            }
        }
    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            time_watch += 1;
            MyLog.e("计时器" + time_watch);
            hander.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //禁止截屏录屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
    }

    private void hideMenu() {
        relay_top.setVisibility(View.INVISIBLE);
        relay_bottom.setVisibility(View.INVISIBLE);
    }

    private void showMenu(boolean autoHide) {
        relay_top.setVisibility(View.VISIBLE);
        relay_bottom.setVisibility(View.VISIBLE);
        if (autoHide)
            hander.sendEmptyMessageDelayed(HIDE_MENU, 1000 * 4);
    }

    @OnClick({R.id.iv_back, R.id.iv_play, R.id.surface_view, R.id.tv_restart, R.id.tv_exit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_exit:
                finish();
                break;
            case R.id.surface_view:
                if (relay_top.getVisibility() != View.VISIBLE) {
                    showMenu(true);
                }
                break;
            case R.id.iv_play:
                if (isPlaying) {
                    aliyunVodPlayer.pause();
                } else {
                    aliyunVodPlayer.start();
                }
                break;
            case R.id.tv_restart:
                relay_complete.setVisibility(View.INVISIBLE);
                aliyunVodPlayer.seekTo(0);
                aliyunVodPlayer.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        hander.removeCallbacks(timeRunnable);
        addWatchRecord();
        if (aliyunVodPlayer != null) aliyunVodPlayer.release();
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record_video;
    }

    @Override
    protected void initData() {
        lectureInfo = (RecordCourseLecture) getIntent().getSerializableExtra("lecture");
        if (lectureInfo == null) {
            ToastManager.showDiyShort("数据异常");
            finish();
            return;
        }
        recordVideoTag = new RecordVideoTag(lectureInfo.getId());
        tv_title.setText(lectureInfo.getName());
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).recordLectureAuth(Integer.parseInt(lectureInfo.getId())).enqueue(new BaseCallback<>(oss -> {
            if (oss != null) {
                this.ossRecord = oss;
                createVideoData();
            }
        }, throwable -> {
            MyLog.e("获取课程播放授权接口失败" + throwable.getMessage());
        }));
    }

    @Override
    protected void initView() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyLog.e("seekBar.getProgress()=" + seekBar.getProgress());
                aliyunVodPlayer.seekTo(seekBar.getProgress() * 1000);
            }
        });
        setLoadingDialogCancelable(true);
        surface_view.setSecure(true);//设置后录屏会成为黑屏
        relay_complete.setVisibility(View.INVISIBLE);
        initPlayer();

    }

    private void initPlayer() {
        aliyunVodPlayer = AliPlayerFactory.createAliPlayer(getApplicationContext());
        surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.redraw();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                aliyunVodPlayer.setDisplay(null);
            }
        });
        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                //播放完成事件
                MyLog.e("onCompletion");
                isPlaying = false;
                isFinished = true;
                iv_play.setImageResource(R.drawable.icon_play);
                relay_complete.setVisibility(View.VISIBLE);
                hideMenu();
                hander.removeCallbacks(timeRunnable);
                addWatchRecord();
            }
        });
        aliyunVodPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                //出错事件
                cancleLoadingDialig();
                ToastManager.showDiyShort("加载异常errorCode=" + errorInfo.getCode());
                MyLog.e("onError:" + errorInfo.getMsg());
            }
        });
        aliyunVodPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功事件
                MyLog.e("onPrepared");
                tv_totalTime.setText(TimeUtil.getVideoTime(aliyunVodPlayer.getDuration()));
                seekBar.setMax((int) (aliyunVodPlayer.getDuration() / 1000));
            }
        });
        aliyunVodPlayer.setOnRenderingStartListener(new IPlayer.OnRenderingStartListener() {
            @Override
            public void onRenderingStart() {
                //首帧渲染显示事件
                MyLog.e("首帧渲染显示事件");
            }
        });
        aliyunVodPlayer.setOnInfoListener(new IPlayer.OnInfoListener() {
            @Override
            public void onInfo(InfoBean infoBean) {
                //其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
                if (infoBean.getCode() == InfoCode.AutoPlayStart) {
                    MyLog.d("自动播放开始");
                } else if (infoBean.getCode() == InfoCode.CurrentPosition) {
                    tv_currentTime.setText(TimeUtil.getVideoTime(infoBean.getExtraValue()));
                    seekBar.setProgress((int) (infoBean.getExtraValue() / 1000));
                } else if (infoBean.getCode() == InfoCode.BufferedPosition) {
//                    MyLog.d("缓冲位置=" + infoBean.getExtraMsg() + "/" + infoBean.getExtraValue());
                    seekBar.setSecondaryProgress((int) (infoBean.getExtraValue() / 1000));
                }
            }
        });
        aliyunVodPlayer.setOnLoadingStatusListener(new IPlayer.OnLoadingStatusListener() {
            @Override
            public void onLoadingBegin() {
                //缓冲开始。
                MyLog.e("onLoadingBegin");
                showLoadingDialig(true);
            }

            @Override
            public void onLoadingProgress(int percent, float kbps) {
                //缓冲进度
                MyLog.e("onLoadingProgress---" + percent + "/" + kbps);
            }

            @Override
            public void onLoadingEnd() {
                //缓冲结束
                MyLog.e("onLoadingEnd");
                cancleLoadingDialig();
            }
        });
        aliyunVodPlayer.setOnSeekCompleteListener(new IPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                //拖动结束
                MyLog.e("onSeekComplete");
            }
        });
        aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(int newState) {
                //播放器状态改变事件
                switch (newState) {
                    case IPlayer.initalized:
                        MyLog.d("初始化成功");
                        break;
                    case IPlayer.prepared:
                        MyLog.d("准备完成");
                        break;
                    case IPlayer.started:
                        MyLog.d("开始播放");
                        if (TextUtils.isEmpty(recordVideoTag.getStartTime())) {
                            recordVideoTag.setStartTime(TimeUtil.getCurrentStringTime());
                        }
                        hander.postDelayed(timeRunnable, 1000);
                        cancleLoadingDialig();
                        isPlaying = true;
                        isFinished = false;
                        iv_play.setImageResource(R.drawable.icon_pause);
                        break;
                    case IPlayer.paused:
                        MyLog.d("暂停");
                        isPlaying = false;
                        iv_play.setImageResource(R.drawable.icon_play);
                        hander.removeCallbacks(timeRunnable);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMenu(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying) {
            showMenu(false);
            aliyunVodPlayer.pause();
        }

    }

    private void createVideoData() {


        VidAuth vidAuth = new VidAuth();
        vidAuth.setPlayAuth(ossRecord.getPlayAuth());
        vidAuth.setRegion(ossRecord.getRegion());
        vidAuth.setVid(ossRecord.getVideoId());

        aliyunVodPlayer.setDataSource(vidAuth);//设置播放源
        aliyunVodPlayer.setAutoPlay(true);//自动播放,自动播放的时候将不会回调onPrepared回调，而会回调onInfo回调。

        CacheConfig cacheConfig = new CacheConfig();
        //开启缓存功能
        cacheConfig.mEnable = true;
        //能够缓存的单个文件最大时长。超过此长度则不缓存
        cacheConfig.mMaxDurationS = 100;
        //缓存目录的位置
        cacheConfig.mDir = getCacheDir().getAbsolutePath();
        //缓存目录的最大大小。超过此大小，将会删除最旧的缓存文件
        cacheConfig.mMaxSizeMB = 200;
        //设置缓存配置给到播放器
        aliyunVodPlayer.setCacheConfig(cacheConfig);
        aliyunVodPlayer.prepare();//准备播放
        showLoadingDialig(true);
    }


    private void addWatchRecord() {
        if (recordVideoTag == null || TextUtils.isEmpty(recordVideoTag.getStartTime())) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseLectureId", Integer.parseInt(recordVideoTag.getCourseLectureId()));
        map.put("endTime", TimeUtil.getCurrentStringTime());
        map.put("startTime", recordVideoTag.getStartTime());
        map.put("viewSeconds", time_watch);

        time_watch = 0;
        recordVideoTag.setStartTime("");
        recordVideoTag.setViewSeconds(0);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new JSONObject(map).toString());
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).addWatchRecord(body).enqueue(new BaseCallback<>(result -> {
            if (result) {
                MyLog.e("观看记录上传成功");
            } else {
                MyLog.e("观看记录上传失败");
            }
        }, throwable -> {
            MyLog.e("观看记录上传失败");
            MyLog.e("获取课程播放授权接口失败" + throwable.getMessage());
        }));
    }

}