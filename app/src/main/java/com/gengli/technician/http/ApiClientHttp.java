package com.gengli.technician.http;

import android.content.Context;
import android.util.Log;

import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.ImageUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.MD5Utils;
import com.gengli.technician.util.SystemMsgUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiClientHttp {
    private static final String TAG = "test";
    private static FinalHttp finalHttp = new FinalHttp();
    private static String SKEY = "69G4M}Dvc>k+U0BpBXb+s[{oWj{^sHY7";
    private static String KEY = "Zsxm@MhdWdcx#Tdj";
    private static String loc = "0,0";
    private static String os = "android";
    private static AjaxParams params;


    public String getSign(Context context, String timestamp, Map<String, String> funs) {
        String cip = SystemMsgUtil.getIPAddress(context);
        String sv = String.valueOf(SystemMsgUtil.getVersionName(context));
        String sessionid = DatasUtil.getUserInfo(context, "sessionid");
        if (sessionid == "") {
            sessionid = "111";
        }
        StringBuilder signBuilder = new StringBuilder();
        Map<String, String> signs = new HashMap<>();
        signs.put("cip", cip);
        signs.put("sv", sv);
        signs.put("os", os);
        signs.put("sessionid", sessionid);
        signs.put("loc", loc);
        signs.put("key", KEY);
        signs.put("timestamp", timestamp);
        Set<Map.Entry<String, String>> entrySet = funs.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            signs.put(entry.getKey(), entry.getValue());
        }
        List<Map.Entry<String, String>> mapList = new ArrayList<>(signs.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> map1, Map.Entry<String, String> map2) {
                return map1.getKey().compareTo(map2.getKey());
            }
        });
        for (int i = 0; i < mapList.size(); i++) {
            Map.Entry<String, String> entrys = mapList.get(i);
//            LogUtils.showLogD(" ------------->" + entrys.getKey() + "-------------" + entrys.getValue());
            signBuilder.append(entrys.getValue());
        }

        String sign = MD5Utils.toMd5(MD5Utils.toMd5(signBuilder.toString()) + SKEY);
        return sign;
    }

    public AjaxParams getParam(Context context, Map<String, String> funs) {
        String timestamp = System.currentTimeMillis() / 1000 + "";
        String cip = SystemMsgUtil.getIPAddress(context);
        String sv = String.valueOf(SystemMsgUtil.getVersionName(context));
        String sessionid = DatasUtil.getUserInfo(context, "sessionid");
        if (sessionid == "") {
            sessionid = "111";
        }

        params = new AjaxParams();
        params.put("cip", cip);
        params.put("key", KEY);
        params.put("loc", loc);
        params.put("sessionid", sessionid);
        params.put("sign", getSign(context, timestamp, funs));
        params.put("sv", sv);
        params.put("os", os);
        params.put("timestamp", timestamp);

        return params;
    }

    /**
     * get请求 无AjaxParams params
     *
     * @param url
     * @param callBack
     */
    public void Get(Context context, String url, AjaxParams params, AjaxCallBack<?> callBack) {
        finalHttp.configTimeout(10000);// 超时时间
        Log.i(TAG, "Get连接接口地址--->" + FinalHttp.getUrlWithQueryString(url, params));

        finalHttp.get(url, params, callBack);
    }

//    /**
//     * get请求 无AjaxParams params
//     *
//     * @param url
//     * @param callBack
//     */
//    public void Get(String deviceId, String url, AjaxCallBack<?> callBack) {
//        String temp = System.currentTimeMillis() + "";
//        String sign = MD5Utils.toMd5(sign_header + temp);
//        String[] headerValue = {appid, temp, sign, deviceId, "true"};
//        finalHttp.configTimeout(10000);// 超时时间
//        Log.i(TAG, "Get连接接口地址--->" + FinalHttp.getUrlWithQueryString(url, null));
//
//        for (int i = 0; i < headerName.length; i++) {
//            finalHttp.addHeader(headerName[i], headerValue[i]);
//        }
//
//        finalHttp.get(url, callBack);
//    }
//

    /**
     * post请求 无AjaxParams params
     *
     * @param url
     * @param callBack
     */
    public void Post(String url, AjaxParams params, AjaxCallBack<?> callBack) {
        finalHttp.configTimeout(10000);// 超时时间
        Log.i(TAG, "Get连接接口地址--->" + FinalHttp.getUrlWithQueryString(url, params));

        finalHttp.post(url, params, callBack);
    }


    public void download(String url,String apkPath, AjaxCallBack<File> callBack) {
        finalHttp.configTimeout(10000);// 超时时间
        finalHttp.download(url, apkPath, callBack);
    }
}
