package io.drew.record;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tuzhenlei.crashhandler.CrashHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.drew.base.LogManager;
import io.drew.base.MyLog;
import io.drew.base.PreferenceManager;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.activitys.SplashActivity;
import io.drew.record.service.bean.response.AuthInfo;
import io.drew.record.util.MySharedPreferencesUtils;
import io.drew.record.view.MyFreshHeader;

import static io.drew.record.ConfigConstant.UMENG_APP_ID;

public class EduApplication extends Application {

    public static EduApplication instance;
    public AuthInfo authInfo;
    public AuthInfo.UserBean.StudentListBean currentKid;
    public int currentKid_index;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MyFreshHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    /**
     * Application和每一个activity中重写 getResource 方法，防止系统字体影响
     */
    @Override
    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PreferenceManager.init(this);
        ToastManager.init(this);
        LogManager.setTagPre("kkk");

        if (BuildConfig.DEBUG) {
            CrashHandler.getInstance().init(this, true, false, 0, SplashActivity.class);
        } else {
            //友盟
            UMConfigure.setLogEnabled(false);
            UMConfigure.init(this, UMENG_APP_ID, "QingYouZi", UMConfigure.DEVICE_TYPE_PHONE, "");
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);// 选用AUTO页面采集模式
        }

        handleSSLHandshake();
        getAndroiodScreenProperty();
        initLocalData();

    }

    private void initLocalData() {
        try {
            String json_authInfo = (String) MySharedPreferencesUtils.get(this, ConfigConstant.SP_AUTH_INFO, "");
//            currentKid_index = (int) MySharedPreferencesUtils.get(this, ConfigConstant.SP_CURRENT_KID_INDEX, 0);
            if (TextUtils.isEmpty(json_authInfo)) {
                MyLog.e("本地无用户数据");
            } else {
                authInfo = new Gson().fromJson(json_authInfo, AuthInfo.class);
                if (authInfo.getUser().getStudentList() != null && authInfo.getUser().getStudentList().size() > 0) {
                    currentKid = authInfo.getUser().getStudentList().get(currentKid_index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MySharedPreferencesUtils.clear(this);
            MyLog.e("本地用户数据异常---清除本地用户数据");
        }
        String token = (String) MySharedPreferencesUtils.get(this, ConfigConstant.SP_TOKEN, "");
        RetrofitManager.instance(token);
    }

    public static void handleSSLHandshake() {
        //Glide加载hppts图片失败
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        MyLog.d("屏幕宽度（像素）：" + width);
        MyLog.d("屏幕高度（像素）：" + height);
        MyLog.d("屏幕密度density（0.75 / 1.0 / 1.5）：" + density);
        MyLog.d("屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        MyLog.d("屏幕宽度（dp）：" + screenWidth);
        MyLog.d("屏幕高度（dp）：" + screenHeight);
    }
}
