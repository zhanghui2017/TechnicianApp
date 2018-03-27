package com.gengli.technician.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;

/**
 * @ClassName: DatasUtil
 * @Description: TODO 工具类，获取数据
 */
public class DatasUtil {

    /**
     * 判断是否已登录
     */
    public static boolean isLogin(Context context) {
        boolean loginState = false;
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        boolean state = preferences.getBoolean("LoginState", false);
        LogUtils.showLogD("---->登录状态---->" + state);
        if (state == true) {
            loginState = true;
        }
        return loginState;
    }

    public static boolean isTechLogin(Context context) {
        boolean isTechState = true;
        if (isLogin(context)) {
            String companyId = getUserInfo(context, "company_id");
            if (!TextUtils.isEmpty(companyId)) {
                String str = companyId.substring(0, 1);
                if (str.equals("9")) {
                    isTechState = true;
                } else {
                    isTechState = false;
                }
            }
        }
        return isTechState;
    }

    /**
     * 清除用户信息
     */
    public static void cleanUserData(Context context, boolean isExit) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove("LoginState");
        editor.remove("sessionid");
        editor.remove("mobile");
        editor.remove("nickname");
        editor.remove("gender");
        editor.remove("prov");
        editor.remove("prov_name");
        editor.remove("city");
        editor.remove("city_name");
        editor.remove("birthday");
        editor.remove("avatar");
        if (isExit) {
            editor.remove("account");
            editor.remove("password");
        }
        editor.commit();
    }


    /**
     * TODO 获取用户USER 存储信息
     */
    public static String getUserInfo(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }


    /**
     * TODO 获取用户USER 存储信息
     */
    public static boolean getUserInfoBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }
    /**
     * TODO 获取用户USER 存储信息
     */
    public static int getUserInfoInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        int value = preferences.getInt(key, -1);
        return value;
    }

    /**
     * TODO 更新User数据
     */
    public static void changeUserState(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void changeUserString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("User", Activity.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * TODO 搜索关键词记录
     */
    public static void storeSearchKeyWord(Context context, String strArray) {
        SharedPreferences sp = context.getSharedPreferences("SearchKeyWord", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("keyWordHistroy", strArray.toString());
        editor.commit();
    }

    /**
     * TODO 获取关键词记录
     */
    public static List<String> getSearchKeyWord(Context context) {
        SharedPreferences sp = context.getSharedPreferences("SearchKeyWord", Context.MODE_PRIVATE);
        String str = sp.getString("keyWordHistroy", "");
        List<String> list = new ArrayList<String>();
        if (!str.equals("")) {
            System.out.println("list is not null");
            list = StringUtil.getListString(str);
            return list;
        }
        return list;
    }

    /**
     * TODO 清除关键词记录
     */
    public static void clearSearchKeyWord(Context context) {
        SharedPreferences sp = context.getSharedPreferences("SearchKeyWord", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove("keyWordHistroy");
//		editor.clear();
        editor.commit();
        System.out.println("搜索关键词清除完毕！");
    }

}
