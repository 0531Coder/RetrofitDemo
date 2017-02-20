package com.example.moutain.retrofitdemo.core;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class BaseApp extends MultiDexApplication {

    public static String cacheDir;
    public static Context mAppContext = null;
    public static String Appsign = "sheyuanhui";


    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    public static Context getApplication() {
        return mAppContext;
    }

    public static Context getContext() {
        return mAppContext;
    }


}
