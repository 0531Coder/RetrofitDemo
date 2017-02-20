package com.example.moutain.retrofitdemo.network;

import android.text.TextUtils;

import com.example.moutain.retrofitdemo.core.AppConfig;
import com.example.moutain.retrofitdemo.core.BaseApp;
import com.example.moutain.retrofitdemo.utils.Util;
import com.example.moutain.retrofitdemo.utils.VerifyUtil;
import com.example.moutain.retrofitdemo.utils.VersionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MOUTAIN on 2017/2/6.
 */

public class RetrofitInstance {

    private static RetrofitInstance instance;
    private final Retrofit retrofit;

    private RetrofitInstance() {
        retrofit = new Retrofit.Builder().
                client(initOkhttp()).
                baseUrl(AppConfig.BaseUrl).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                build();
    }

    private OkHttpClient initOkhttp() {
        /**
         * 初始化oKHttp，添加日志拦截器
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (AppConfig.isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        /**
         * 添加缓存机制
         */
        File cacheFile = new File(BaseApp.cacheDir, "/NetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!Util.isNetworkReachable(BaseApp.getContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                //response如果出现问题可能是返回的头部信息的问题remove掉应该就可以了
                if (Util.isNetworkReachable(BaseApp.getContext())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
        /**
         * 添加公共请求参数
         */
        Interceptor addParamsInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
              /*
                注掉的是普通的追加参数，但是项目中用到了遍历请求的参数
                Request originalRequest = chain.request();
                Request request;
                String method = originalRequest.method();
                Headers headers = originalRequest.headers();
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("platform", "android")
                        .addQueryParameter("version", "1.0.0")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);*/
                HashMap<String, String> paramsMap = new HashMap<>();
                FormBody.Builder newFormBody = new FormBody.Builder();
                if (request.body() instanceof FormBody) {
                    FormBody oldFormBody = (FormBody) request.body();
                    for (int i = 0; i < oldFormBody.size(); i++) {
                        newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                        String value = oldFormBody.encodedValue(i);
                        if (!TextUtils.isEmpty(value)) {
                            value = URLDecoder.decode(oldFormBody.encodedValue(i), "utf-8");
                        }
                        paramsMap.put(oldFormBody.encodedName(i), value);
                    }

                }
                paramsMap.put("v", VersionUtils.getVersion(BaseApp.getContext()));
                String sign = VerifyUtil.genSignV3(paramsMap, BaseApp.Appsign);
                newFormBody.addEncoded("sign", sign);
                newFormBody.addEncoded("v", VersionUtils.getVersion(BaseApp.getContext()));
                requestBuilder.method(request.method(), newFormBody.build());
                return chain.proceed(requestBuilder.build());
            }
        };
        builder.addInterceptor(addParamsInterceptor);

        /**
         * 设置超时和重连机制
         */
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    /**
     * 双重锁单例模式
     *
     * @return
     */
    public static RetrofitInstance getInstance() {
        if (instance == null) {
            synchronized (RetrofitInstance.class) {
                if (instance == null) {
                    instance = new RetrofitInstance();
                }
            }
        }
        return instance;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

}
