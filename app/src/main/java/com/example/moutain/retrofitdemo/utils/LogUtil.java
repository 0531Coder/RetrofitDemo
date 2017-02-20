package com.example.moutain.retrofitdemo.utils;

import android.util.Log;
import com.example.moutain.retrofitdemo.core.AppConfig;

/**
 * Created by MOUTAIN on 2017/2/10.
 */

public class LogUtil {
    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    private static final String TAG = "way";
    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (AppConfig.isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (AppConfig.isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (AppConfig.isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (AppConfig.isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (AppConfig.isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (AppConfig.isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (AppConfig.isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (AppConfig.isDebug)
            Log.i(tag, msg);
    }
}

