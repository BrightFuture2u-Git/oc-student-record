package io.drew.record.fragments_pad;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.adapters.RecordWorkLectureAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.dialog.LoadingDialog;
import io.drew.record.interfaces.OnOssFileUploadListener;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.MyRecordWorks;
import io.drew.record.service.bean.response.StsInfo;
import io.drew.record.service.bean.response.UnUploadRecordLecture;
import io.drew.record.util.AppUtil;
import io.drew.record.util.GlideUtils;
import io.drew.record.util.OSSManager;
import io.drew.record.util.TimeUtil;
import io.drew.record.view.GlideEngine;
import io.drew.record.view.RundProgressBar;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class UploadMyRecordWorkFragment extends BaseFragment {

    @BindView(R.id.tv_lectureName)
    protected TextView tv_lectureName;
    @BindView(R.id.tv_reload)
    protected TextView tv_reload;
    @BindView(R.id.line_add_work)
    protected LinearLayout line_add_work;
    @BindView(R.id.iv_choose)
    protected ImageView iv_choose;
    @BindView(R.id.btn_submit)
    protected Button btn_submit;
    @BindView(R.id.iv_add)
    protected ImageView iv_add;

    @BindView(R.id.line_audio)
    protected LinearLayout line_audio;
    @BindView(R.id.relay_audioed)
    protected RelativeLayout relay_audioed;
    @BindView(R.id.iv_audio)
    protected ImageView iv_audio;
    @BindView(R.id.tv_audio_time)
    protected TextView tv_audio_time;
    @BindView(R.id.tv_audio_status)
    protected TextView tv_audio_status;
    @BindView(R.id.tv_time_des)
    protected TextView tv_time_des;
    @BindView(R.id.gif_des)
    protected GifImageView gif_des;
    @BindView(R.id.et_name_baby)
    protected EditText et_name_baby;
    @BindView(R.id.rundProgressbar)
    protected RundProgressBar rundProgressbar;

    private List<UnUploadRecordLecture> unUploadRecordLectures;
    private int index_choose = -1;

    private String path;
    private LoadingDialog loadingDialog;
    private StsInfo stsInfo;
    private AppService appService;
    private static final int REQUEST_DRESS = 999;
    private MyRecordWorks.RecordsBean recordsBean;//编辑对象

    private boolean isAudio = false;//是否录音中
    private MediaRecorder mMediaRecorder;
    private String audioSaveDir;//录音文件目录
    private String audioPath;//录音文件路径
    private int count = 0;//当前录音时间,秒
    private int max_audio_second = 300;//录音最长时间，秒
    private int min_audio_second = 2;//录音最长时间，秒
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count == max_audio_second) {
                stopRecord();
            }
            count++;
            tv_audio_time.setText(TimeUtil.getVideoTime(count * 1000));
            rundProgressbar.setProgress(count / (max_audio_second / 100));
            handler.postDelayed(runnable, 1000);
        }
    };
    private GifDrawable gifDrawable;
    private MediaPlayer mediaPlayer;
    private String name_baby = "";
    private String url_img_oss = "";
    private String url_voice_oss = "";
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    public UploadMyRecordWorkFragment() {
    }

    public UploadMyRecordWorkFragment(MyRecordWorks.RecordsBean recordsBean) {
        this.recordsBean = recordsBean;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_upload_my_record_work;
    }

    @Override
    protected void initData() {
        audioSaveDir = context.getCacheDir().getAbsolutePath();
        appService = RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class);
        if (recordsBean == null) {
            line_audio.setVisibility(View.VISIBLE);
            relay_audioed.setVisibility(View.GONE);
            RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class)
                    .getUnUploadRecordLectures().enqueue(new BaseCallback<>(unUploadRecordLectures -> {
                if (unUploadRecordLectures != null) {
                    this.unUploadRecordLectures = unUploadRecordLectures;
                    if (unUploadRecordLectures.size() == 1) {
                        index_choose = 0;
                        tv_lectureName.setText(unUploadRecordLectures.get(index_choose).getLectureName());
                        tv_lectureName.setTextColor(Color.BLACK);
                    }
                }
            }, throwable -> {
                MyLog.e("我的作品获取失败" + throwable.getMessage());
            }));
        } else {//编辑模式
            index_choose = 0;
            tv_lectureName.setText(recordsBean.getLectureName());
            tv_lectureName.setTextColor(Color.BLACK);

            path = recordsBean.getProductImage();
            iv_choose.setVisibility(View.VISIBLE);
            tv_reload.setVisibility(View.VISIBLE);
            line_add_work.setVisibility(View.GONE);

            name_baby = recordsBean.getStudentName();
            et_name_baby.setText(name_baby);
            url_img_oss = recordsBean.getProductImage();
            if (!TextUtils.isEmpty(recordsBean.getProductVoice())) {
                audioPath = recordsBean.getProductVoice();
                relay_audioed.setVisibility(View.VISIBLE);
                line_audio.setVisibility(View.GONE);
                count = Integer.parseInt(recordsBean.getProductVoiceSeconds());
                tv_time_des.setText(TimeUtil.getVideoTime(count * 1000));
            } else {
                relay_audioed.setVisibility(View.GONE);
                line_audio.setVisibility(View.VISIBLE);
            }

            GlideUtils.loadRoundCornerImg(context, path, iv_choose, 5f);
            btn_submit.setEnabled(true);
        }

    }

    @Override
    protected void initView() {
        title.setText("上传作品");
        if (recordsBean != null) {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_submit));
            btn_submit.setEnabled(true);
        } else {
            btn_submit.setEnabled(false);
        }


        et_name_baby.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name_baby = s.toString();
                if (index_choose >= 0 && !TextUtils.isEmpty(name_baby) && !TextUtils.isEmpty(path)) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_submit));
                    btn_submit.setEnabled(true);
                }
            }
        });
        gifDrawable = (GifDrawable) gif_des.getDrawable();
        gifDrawable.stop();

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        if (event.getCode() == ConfigConstant.EB_WORK_DRESSED) {
            path = event.getMessage();
            if (path != null) {
                iv_choose.setVisibility(View.VISIBLE);
                tv_reload.setVisibility(View.VISIBLE);
                line_add_work.setVisibility(View.GONE);

                GlideUtils.loadRoundCornerImg(context, path, iv_choose, 5f);
                if (index_choose >= 0) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_submit));
                    btn_submit.setEnabled(true);
                }
            }
        }
    }


    @OnClick({R.id.relay_back, R.id.line_choose, R.id.line_add_work, R.id.btn_submit, R.id.tv_reload
            , R.id.iv_audio, R.id.relay_audio_des, R.id.iv_delect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relay_back:
                getParentFragmentManager().popBackStack();
                break;
            case R.id.line_choose:
                if (recordsBean == null) {
                    showLectureChooseDialog();
                }
                break;
            case R.id.line_add_work:
            case R.id.tv_reload:
                if (index_choose < 0) {
                    ToastManager.showDiyShort("请先选择课程");
                } else {
                    choosePicDialog();
                }
                break;
            case R.id.iv_audio:
                if (isAudio) {
                    stopRecord();
                } else {
                    startRecordAudio();
                }
                break;
            case R.id.relay_audio_des:
                if (gifDrawable.isPlaying()) {
                    gifDrawable.stop();
                    stopPlayAudio();
                } else {
                    gifDrawable.start();
                    playAudio();
                }
                break;
            case R.id.iv_delect:
                stopPlayAudio();
                relay_audioed.setVisibility(View.GONE);
                line_audio.setVisibility(View.VISIBLE);
                audioPath = "";
                url_voice_oss = "";
                count = 0;
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(name_baby)) {
                    ToastManager.showDiyShort("请先选输入宝宝名字");
                } else {
                    if (TextUtils.isEmpty(path)) {
                        ToastManager.showDiyShort("请先选择图片");
                    } else {
                        if (isAudio) {
                            stopRecord();
                        }
                        getSts(path);
                    }
                }
                break;
        }
    }

    /**
     * 播放语音
     */
    private void playAudio() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayAudio();
            }
        });
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放语音
     */
    private void stopPlayAudio() {
        if (gifDrawable != null && gifDrawable.isPlaying()) {
            gifDrawable.stop();
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    /**
     * 开始录音
     */
    private void startRecordAudio() {
        count = 0;
        iv_audio.setImageResource(R.drawable.ic_audioing);
        tv_audio_status.setText("录音中");
        rundProgressbar.setProgress(0);
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            String fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".aac";
            audioPath = audioSaveDir + "/" + fileName;
            MyLog.d("保存路径=" + audioPath);
            mMediaRecorder.setOutputFile(audioPath);
            mMediaRecorder.setMaxDuration(max_audio_second * 1000);
            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        MyLog.d("2分钟时间已经满了");
                        stopRecord();
                        relay_audioed.setVisibility(View.VISIBLE);
                        line_audio.setVisibility(View.GONE);
                        count = max_audio_second;
                        tv_time_des.setText("02:00");
                    }
                }
            });
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isAudio = true;
            runnable.run();
        } catch (IllegalStateException e) {
            MyLog.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            MyLog.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    public void stopRecord() {
        try {
            isAudio = false;
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            handler.removeCallbacks(runnable);
            iv_audio.setImageResource(R.drawable.ic_audio);
            tv_audio_time.setText("00:00");
            tv_audio_status.setText("点击话筒,开始录音");
            if (count <= min_audio_second) {
                audioPath = "";
                ToastManager.showDiyShort("录音时间不得短于2秒");
            } else {
                relay_audioed.setVisibility(View.VISIBLE);
                line_audio.setVisibility(View.GONE);
                tv_time_des.setText(TimeUtil.getVideoTime(count * 1000));
            }
            rundProgressbar.setProgress(0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void showLectureChooseDialog() {
        if (unUploadRecordLectures == null || unUploadRecordLectures.size() <= 0) {
            ToastManager.showDiyShort("数据异常，请稍后重试");
            return;
        }
        int[] position_index = new int[1];
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lecture_choose, null);
        GridView gridView = view.findViewById(R.id.gridView);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        TextView tv_sure = view.findViewById(R.id.tv_sure);
        RecordWorkLectureAdapter kidAdapter = new RecordWorkLectureAdapter();
        kidAdapter.setData(unUploadRecordLectures);
        gridView.setAdapter(kidAdapter);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index_choose = position_index[0];
                dialog.dismiss();
                tv_lectureName.setText(unUploadRecordLectures.get(index_choose).getLectureName());
                tv_lectureName.setTextColor(Color.BLACK);
                if (!TextUtils.isEmpty(path) && iv_choose.getVisibility() == View.VISIBLE) {
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.shape_bg_btn_submit));
                    btn_submit.setEnabled(true);
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position_index[0] = position;
                kidAdapter.checkItem(position);

            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
//        lp.width = AppUtil.getScreenWidth(getActivity());
        lp.width = (AppUtil.getScreenWidth(getActivity()) - getContext().getResources().getDimensionPixelOffset(R.dimen.dp_47)) / 2;
        dialogWindow.setAttributes(lp);

        dialog.show();
        kidAdapter.checkItem(0);
    }

    public void choosePicDialog() {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_get_picture_type, null);
        TextView tv_camera = view.findViewById(R.id.tv_camera);
        TextView tv_gallery = view.findViewById(R.id.tv_gallery);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PictureSelector.create(UploadMyRecordWorkFragment.this)
                        .openCamera(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                LocalMedia localImg = result.get(0);
                                if (localImg.getRealPath() == null) {
                                    path = localImg.getPath();
                                } else {
                                    path = localImg.getRealPath();
                                }
                                MyLog.e("选择的图片=" + path);

                                showPre();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PictureSelector.create(UploadMyRecordWorkFragment.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.WeChatstyle)
                        .isWeChatStyle(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(1)
                        .isCamera(false)
                        .enableCrop(false)
                        .withAspectRatio(1, 1)
                        .isCompress(false)// 是否压缩
//                        .compressQuality(10)// 图片压缩后输出质量 0~ 100
//                        .minimumCompressSize(500)// 小于500kb的图片不压缩
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
                                path = url;
                                showPre();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = (AppUtil.getScreenWidth(getActivity()) - getContext().getResources().getDimensionPixelOffset(R.dimen.dp_47)) / 2;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void showPre() {
        if (isAdded()) {
            new WorkDressFragment(path, 2).myShow(getChildFragmentManager(), "dress");
        } else {
            ToastManager.showDiyShort("程序异常，请稍后重试");
        }
    }


    private void getSts(String url) {
        if (isHttpUrl(url) && isHttpUrl(audioPath)) {
            url_img_oss = url;
            url_voice_oss = audioPath;
            addProduct();
//            if (TextUtils.isEmpty(audioPath)) { //没有录音
//                addProduct();
//            } else {
//                if (isHttpUrl(audioPath)) {
//                    url_voice_oss = audioPath;
//                    addProduct();
//                } else {
//                    OSSManager.instance().uploadVoice(UploadMyRecordWorkActivity.this, stsInfo, getVoiceName(audioPath), audioPath, new OnOssFileUploadListener() {
//                        @Override
//                        public void onUploadSuccess(String url) {
//                            url_voice_oss = url;
//                            loadingDialog.dismiss();
//                            addProduct();
//                        }
//
//                        @Override
//                        public void onUploadFail() {
//                            loadingDialog.dismiss();
//                            ToastManager.showDiyShort("上传失败，请稍后再试");
//                        }
//                    });
//                }
//            }
        } else {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();
            appService.getSts().enqueue(new BaseCallback<>(stsInfo -> {
                loadingDialog.dismiss();
                if (stsInfo != null) {
                    this.stsInfo = stsInfo;
                    if (isHttpUrl(url)) {
                        url_img_oss = url;
                        if (TextUtils.isEmpty(audioPath)) { //没有录音
                            addProduct();
                        } else {
                            if (isHttpUrl(audioPath)) {
                                url_voice_oss = url;
                                addProduct();
                            } else {
                                loadingDialog.show();
                                OSSManager.instance().uploadVoice(context, stsInfo, getVoiceName(audioPath), audioPath, new OnOssFileUploadListener() {
                                    @Override
                                    public void onUploadSuccess(String url) {
                                        url_voice_oss = url;
                                        loadingDialog.dismiss();
                                        addProduct();
                                    }

                                    @Override
                                    public void onUploadFail() {
                                        loadingDialog.dismiss();
                                        ToastManager.showDiyShort("上传失败，请稍后再试");
                                    }
                                });
                            }
                        }
                    } else {
                        upLoadImg(url);
                    }

                }
            }, throwable -> {
                loadingDialog.dismiss();
                MyLog.e("Sts获取失败" + throwable.getMessage());
                ToastManager.showDiyShort("上传失败，请稍后再试");
            }));
        }

    }

    private boolean isHttpUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        } else {
            return url.contains("https://");
        }
    }

    private void upLoadImg(String imageUrl) {
        MyLog.e("开始上传------" + imageUrl);
        loadingDialog.show();
        OSSManager.instance().upload(context, stsInfo, getFileName(imageUrl), imageUrl, new OnOssFileUploadListener() {
            @Override
            public void onUploadSuccess(String url) {
                MyLog.e(url + "上传图片成功");
                loadingDialog.dismiss();
                url_img_oss = url;
                if (TextUtils.isEmpty(audioPath)) { //没有录音
                    addProduct();
                } else {
                    if (isHttpUrl(audioPath)) {
                        url_voice_oss = url;
                        addProduct();
                    } else {
                        loadingDialog.show();
                        OSSManager.instance().uploadVoice(context, stsInfo, getVoiceName(audioPath), audioPath, new OnOssFileUploadListener() {
                            @Override
                            public void onUploadSuccess(String url) {
                                url_voice_oss = url;
                                loadingDialog.dismiss();
                                addProduct();
                            }

                            @Override
                            public void onUploadFail() {
                                loadingDialog.dismiss();
                                ToastManager.showDiyShort("上传失败，请稍后再试");
                            }
                        });
                    }
                }
            }

            @Override
            public void onUploadFail() {
                loadingDialog.dismiss();
                ToastManager.showDiyShort("上传失败，请稍后再试");
            }
        });

    }


    private void addProduct() {
        HashMap<String, String> map = new HashMap<>();
        if (recordsBean != null) {
            map.put("courseId", recordsBean.getCourseId());
            map.put("lectureId", recordsBean.getLectureId());
        } else {
            map.put("courseId", String.valueOf(unUploadRecordLectures.get(index_choose).getCourseId()));
            map.put("lectureId", unUploadRecordLectures.get(index_choose).getLectureId());
        }
        map.put("product", url_img_oss);
        map.put("studentName", name_baby);
        map.put("voiceSeconds", String.valueOf(count));
        map.put("voiceUrl", url_voice_oss);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                new JSONObject(map).toString());
        appService.addRecordProduct(body).enqueue(new BaseCallback<>(result -> {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (result != null) {
                ToastManager.showDiyShort("上传成功");
                getParentFragmentManager().popBackStack();
                MessageEvent messageEvent = new MessageEvent(ConfigConstant.EB_RECORD_WORK_UPLOADED);
                messageEvent.setObjectMessage(result);
                EventBus.getDefault().post(messageEvent);

            } else {
                ToastManager.showDiyShort("上传失败");
            }
        }, throwable -> {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            MyLog.e("数据异常，请稍后再试" + throwable.getMessage());
        }));
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

    private String getVoiceName(String localPath) {
        String contact = EduApplication.instance.authInfo.getUser().getPhone();
        String name = "";
        int start = localPath.lastIndexOf("/");
        if (start != -1) {
            name = localPath.substring(start + 1);
        } else {
            return null;
        }
        return "voice_" + contact + "_" + name;
    }
}
