package io.drew.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Log统一管理类
 */
public class MyLog {

    private MyLog() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
//    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "KKK";

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void log(String str) {
        if (isDebug) {
            int max_str_length = 2001 - TAG.length();
            //大于4000时
            while (str.length() > max_str_length) {
                Log.d(TAG, str.substring(0, max_str_length));
                str = str.substring(max_str_length);
            }
            //剩余部分
            Log.d(TAG, str);
        }
    }
}
