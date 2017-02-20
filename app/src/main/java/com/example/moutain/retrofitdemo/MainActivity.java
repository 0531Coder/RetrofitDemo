package com.example.moutain.retrofitdemo;

import android.os.Bundle;
import android.widget.TextView;

import com.example.moutain.retrofitdemo.base.BaseActivity;
import com.example.moutain.retrofitdemo.base.BaseObserver;
import com.example.moutain.retrofitdemo.network.Api.AddressApi;
import com.example.moutain.retrofitdemo.network.response.AreaResponse;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.moutain.retrofitdemo.R.id.tv_content;

public class MainActivity extends BaseActivity {

    private TextView tv_content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        tv_content1 = (TextView) findViewById(tv_content);
        getApi(AddressApi.class).getAreaList("0xtahxgdc7aaq8wznkrwd4j3ha1tcy31a1h4hyv4xkkasuvwtghvxukn4ejafmfmwkradukdslnapkyxdi7wtkogeaklar3weet7m0fmdgfncxmeqaptrt40dmcdhk7d","").subscribeOn(Schedulers.io()).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<AreaResponse>(this) {
            @Override
            public void handleData(AreaResponse areaResponse) {
                    tv_content1.setText(areaResponse.toString());
            }
        });

       /* getApi(AddressApi.class).getDouban("","").subscribeOn(Schedulers.io()).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseObserver<DouBanResponse>(this) {
            @Override
            public void handleData(DouBanResponse douBanResponse) {
                tv_content1.setText(douBanResponse.toString());
            }
        });*/
    }
}
