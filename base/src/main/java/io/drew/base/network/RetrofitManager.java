package io.drew.base.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.drew.base.BuildConfig;
import io.drew.base.MyLog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static RetrofitManager instance;

    private OkHttpClient client;

    private String token = null;

    private RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);//超时时间大于10，超时荣耀刷新会继续超时
//        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("auth", this.token)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) MyLog.log("Http:" + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//设置日志打印等级
        builder.addInterceptor(loggingInterceptor);
        client = builder.build();
    }

    public static RetrofitManager instance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        if (instance.token == null)
            instance.token = "";
        return instance;
    }

    public static RetrofitManager instance(String token) {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        instance.token = token;
        return instance;
    }

    public <T> T getService(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())

                .build();
        return retrofit.create(tClass);
    }

    public static class Callback<T extends ResponseBody> implements retrofit2.Callback<T> {
        private int code;
        private io.drew.base.Callback<T> callback;

        public Callback(int code, @NonNull io.drew.base.Callback<T> callback) {
            this.code = code;
            this.callback = callback;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.errorBody() != null) {
                try {
                    callback.onFailure(new Throwable(response.errorBody().string()));
                } catch (IOException e) {
                    callback.onFailure(e);
                }
            } else {
                T body = response.body();
                if (body == null) {
                    callback.onFailure(new Throwable("response body is null"));
                } else {
                    if (body.code != code) {
                        callback.onFailure(new BusinessException(body.code, body.msg.toString()));
                    } else {
                        callback.onSuccess(body);
                    }
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            callback.onFailure(t);
        }
    }

}
