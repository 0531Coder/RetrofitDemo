package com.example.moutain.retrofitdemo.utils;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author 朱厚飞 E-mail:zhuhoufei@yeah.net
 * @version 创建时间：2016年3月29日 下午5:33:17
 *          类说明
 */
public class VerifyUtil {

    /**
     * 不匹配{"code":200,"businessCode":0,"description":"","modelData":{"aaa":"aaa"}}
     * 只匹配{"code":"200","businessCode":"0","description":"","modelData":""}
     *
     * @param json
     * @return
     * @author ZhuHouFei
     * @date 2016年3月29日下午6:15:25
     */
    public static boolean stringJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            return false;
        }
        json = json.substring(1, json.length() - 1);
        json = json.trim();
        if (json.length() == 0) {
            return false;
        }
        for (String kv : json.split(",")) {
            String[] strs = kv.split(":");
            if (strs.length != 2) {
                return false;
            }
            for (String str : strs) {
                if (!(str.startsWith("\"") && str.endsWith("\""))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean verify(Map<String, String> data, String signKey) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        String datastr = coverMap2String(data) + signKey;
        System.out.println("datastr:" + datastr);
        String md5Sign = MD5Utils.getMD5(datastr);
        String sign = data.get("sign");
        if (md5Sign.equalsIgnoreCase(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成短信签名
     *
     * @return
     */
    public static String genSign(Map<String, String> data, String signKey) {
        String datastr = coverMap2String(data) + signKey;
        return MD5Utils.getMD5(datastr.toLowerCase());
    }

    public static String coverMap2String(Map<String, String> data) {
        return coverMap2String(data, false);
    }

    /**
     * asd=12&bcbd=12323
     *
     * @param data
     * @param isLowerCase 是否转换成小写
     * @return
     * @author ZhuHouFei
     * @date 2016年1月15日下午7:20:19
     */
    public static String coverMap2String(Map<String, String> data, boolean isLowerCase) {
        TreeMap<String, String> tree = new TreeMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            if ("sign".equals(en.getKey().trim())) {
                continue;
            }
            if (isLowerCase) {
                tree.put(en.getKey(), en.getValue().toLowerCase());
            } else {
                tree.put(en.getKey(), en.getValue());
            }
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            sf.append(en.getKey() + "=" + en.getValue() + "&");
        }
        if (sf.length() == 0) {
            return "";
        }
        return sf.substring(0, sf.length() - 1);
    }

    /**
     * 生成v3版签名
     *
     * @return
     */
    public static String genSignV3(Map<String, String> data, String signKey) {
        String datastr = coverMap2String(data, true) + signKey;
//        PLog.i("okhttp", "sign:" + datastr);
        return MD5Utils.getSignMD5(MD5Utils.getSignMD5(datastr).toLowerCase());
    }

    /**
     * 校验v3版签名是否正确
     *
     * @param data
     * @param signKey
     * @return
     */
    public static boolean verifyV3(Map<String, String> data, String signKey) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        String md5Sign = genSignV3(data, signKey);
        String sign = data.get("sign");
        if (md5Sign.equalsIgnoreCase(sign)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
//        String str = "{\"username\":zs}";
//        System.out.println(stringJson(str));
//
//        Map<String, String> data = new HashMap<>();
//        data.put("123","123");
//        String sign = genSignV3(data,"");
//        System.out.println(sign);

//        PLog.i("sign", MD5Utils.getSignMD5(MD5Utils.getSignMD5("deviceType=1&jiguangToken=sdf&mobile=sdf&password=124345&v=3.0sheyuanhui")));
    }
}
