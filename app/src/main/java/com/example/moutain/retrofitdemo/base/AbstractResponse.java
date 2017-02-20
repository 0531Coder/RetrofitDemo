package com.example.moutain.retrofitdemo.base;

/**
 * Created by MOUTAIN on 2017/2/10.
 */

public class AbstractResponse {
    String code;
    String msg;
    String timestamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 判断返回的数据是否正常
     * @return
     */
    public boolean successed() {
        if (code != null) {
            return code.equals("0");
        }
        return false;
    }
}
