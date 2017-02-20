package com.example.moutain.retrofitdemo.network.response;

import com.example.moutain.retrofitdemo.model.Areas;
import com.example.moutain.retrofitdemo.base.AbstractResponse;

/**
 * Created by MOUTAIN on 2016/9/6.
 */
public class AreaResponse extends AbstractResponse{

    private Areas data;

    public Areas getData() {
        return data;
    }

    public void setData(Areas data) {
        this.data = data;
    }
}