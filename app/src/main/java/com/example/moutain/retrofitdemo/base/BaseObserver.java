package com.example.moutain.retrofitdemo.base;

import android.app.Activity;
import android.widget.Toast;

import com.example.moutain.retrofitdemo.utils.LogUtil;

import java.lang.ref.WeakReference;

import rx.Observer;

/**
 * Created by MOUTAIN on 2017/2/10.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    /**
     * 这儿为什么使用弱引用：
     * 防止Observer持有activity引用导致GC无法及时回收Activity
     */

    protected final WeakReference<Activity> mRef;

    public BaseObserver(Activity activity) {
        mRef = new WeakReference<Activity>(activity);
    }

    @Override
    public void onCompleted() {
        Activity activity = mRef.get();
        if(activity instanceof BaseActivity){
            //在Activity上取消loading页面
        }
    }

    @Override
    public void onError(Throwable e) {
            // 显示加载失败页面
        Activity activity = mRef.get();
        Toast.makeText(activity.getApplicationContext(),"服务器开小差了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {
        Activity activity = mRef.get();
        if (activity!=null&&!activity.isFinishing()) {
            AbstractResponse abstractResponse = (AbstractResponse) t;

            if (abstractResponse.successed()) {
                try {
                    handleData(t);
                } catch (Exception e) {
                    handleException(e,activity);
                }
            } else {
                /**
                 * 根据不同的code提示不同的信息
                 */
                handleRespErrCode(abstractResponse.getCode(),activity);
            }


        }
    }

    public abstract void handleData(T t);
    public void handleRespErrCode(String code,Activity activity){
        switch (code){
            case "1":
                Toast.makeText(activity.getApplicationContext(),"登陆状态异常",Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(activity.getApplicationContext(), "终端签名异常", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void handleException(Exception e,Activity activity){
        LogUtil.v("okhttp",e.getMessage().toString());
        Toast.makeText(activity.getApplicationContext(),"数据解析异常",Toast.LENGTH_SHORT).show();
    }
}
