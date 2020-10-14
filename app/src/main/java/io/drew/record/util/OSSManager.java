package io.drew.record.util;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.EduApplication;
import io.drew.record.base.BaseCallback;
import io.drew.record.interfaces.OnOssFileUploadListener;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.StsInfo;

/**
 * @Author: KKK
 * @CreateDate: 2020/5/15 3:49 PM
 */
public class OSSManager {
    public static OSSManager instance;
    private OSS oss;

    public static OSSManager instance() {
        if (instance == null) {
            synchronized (OSSManager.class) {
                if (instance == null)
                    instance = new OSSManager();
            }
        }
        return instance;
    }

    public void init(Context context, StsInfo stsInfo) {
        String endpoint = "http://" + stsInfo.getOssEndpoint();
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                stsInfo.getAK(),
                stsInfo.getKS(),
                stsInfo.getToken());

        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(10); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//        OSSLog.enableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv

        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    public void upload(Context context, StsInfo stsInfo, String objectKey, String uploadFilePath, OnOssFileUploadListener onOssFileUploadListener) {
        if (oss == null) {
            init(context, stsInfo);
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("bf-images", objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                MyLog.e("currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url = "https://bf-images." + stsInfo.getOssEndpoint() + "/" + objectKey;
                MyLog.e("图片上传成功" + url);
                onOssFileUploadListener.onUploadSuccess(url);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                ToastManager.showDiyShort("发送失败，请稍后再试");
                MyLog.e("上传图片异常");
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    MyLog.e("ErrorCode" + serviceException.getErrorCode());
                    MyLog.e("RequestId" + serviceException.getRequestId());
                    MyLog.e("HostId" + serviceException.getHostId());
                    MyLog.e("RawMessage" + serviceException.getRawMessage());
                }
                onOssFileUploadListener.onUploadFail();
            }
        });
//        task.waitUntilFinished();
    }

    public void uploadVoice(Context context, StsInfo stsInfo, String objectKey, String uploadFilePath, OnOssFileUploadListener onOssFileUploadListener) {
        if (oss == null) {
            init(context, stsInfo);
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("bf-voice", objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                MyLog.e("currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url = "https://voice.brightfuture360.com/" + objectKey;
//                String url = "https://bf-voice." + stsInfo.getOssEndpoint() + "/" + objectKey;
                MyLog.e("语音上传成功" + url);
                onOssFileUploadListener.onUploadSuccess(url);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                ToastManager.showDiyShort("发送失败，请稍后再试");
                MyLog.e("上传语音异常");
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    MyLog.e("ErrorCode" + serviceException.getErrorCode());
                    MyLog.e("RequestId" + serviceException.getRequestId());
                    MyLog.e("HostId" + serviceException.getHostId());
                    MyLog.e("RawMessage" + serviceException.getRawMessage());
                }
                onOssFileUploadListener.onUploadFail();
            }
        });
//        task.waitUntilFinished();
    }

    /**
     * 上传日志文件
     */
    public void uploadLogFile() {
        try {
            RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).getSts().enqueue(new BaseCallback<>(stsInfo -> {
                if (stsInfo != null) {
                    if (oss == null) {
                        init(EduApplication.instance, stsInfo);
                    }
                    String uploadFilePath = EduApplication.instance.getFilesDir() + "/agorasdk.log";
                    String contact = EduApplication.instance.authInfo.getUser().getPhone();
                    // 构造上传请求。
                    PutObjectRequest put = new PutObjectRequest("bf-logs", "android_" + contact + "_" + DateUtils.getStringToday(), uploadFilePath);

                    // 异步上传时可以设置进度回调。
                    put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                        @Override
                        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                            MyLog.d("kkk", "currentSize: " + currentSize + " totalSize: " + totalSize);
                        }
                    });

                    oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                            MyLog.d("kkk", "LogFile----UploadSuccess");
                            MyLog.d("kkk", result.getETag());
                            MyLog.d("kkk", result.getRequestId());
                        }

                        @Override
                        public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                            // 请求异常。
                            if (clientExcepion != null) {
                                // 本地异常，如网络异常等。
                                clientExcepion.printStackTrace();
                            }
                            if (serviceException != null) {
                                // 服务异常。
                                MyLog.e("kkk-ErrorCode" + serviceException.getErrorCode());
                                MyLog.e("kkk-RequestId" + serviceException.getRequestId());
                                MyLog.e("kkk-HostId" + serviceException.getHostId());
                                MyLog.e("RawMessage" + serviceException.getRawMessage());
                            }
                        }
                    });
                }
            }, throwable -> {
                MyLog.e("uploadLogFile_Sts获取失败" + throwable.getMessage());
            }));
        } catch (Exception e) {

        }

    }
}
