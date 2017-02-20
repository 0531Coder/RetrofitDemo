package com.example.moutain.retrofitdemo.base;

import android.app.Activity;

import com.example.moutain.retrofitdemo.network.RetrofitInstance;

/**
 * Created by MOUTAIN on 2017/2/10.
 */

public class BaseActivity extends Activity {
    public <T> T getApi(Class<T> cls){
        return RetrofitInstance.getInstance().create(cls);
    }
}
