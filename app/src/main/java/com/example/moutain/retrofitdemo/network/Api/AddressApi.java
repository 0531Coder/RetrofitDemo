package com.example.moutain.retrofitdemo.network.Api;

import com.example.moutain.retrofitdemo.network.response.AreaResponse;
import com.example.moutain.retrofitdemo.network.response.DouBanResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by MOUTAIN on 2016/11/4.
 */

public interface AddressApi {

    //获取地区列表
    @FormUrlEncoded
    @POST("farm/areaList")
    Observable<AreaResponse> getAreaList(@Field("userToken") String UserToken, @Field("parentId") String parentId);

    //豆瓣Api
    @FormUrlEncoded
    @POST("v2/book/1220562")
    Observable<DouBanResponse> getDouban(@Field(" ") String UserToken, @Field("") String parentId);
}
