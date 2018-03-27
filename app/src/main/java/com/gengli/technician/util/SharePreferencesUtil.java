package com.gengli.technician.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @ClassName: DatasUtil
 * @Description: TODO 工具类，获取数据
 */
public class SharePreferencesUtil {


    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharePreferencesUtil mSharedPreferencesUtil;

    public SharePreferencesUtil(Context context, String dataName) {
        mPreferences = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static SharePreferencesUtil getInstance(Context context, String dataName) {
        if (mSharedPreferencesUtil == null) {
            mSharedPreferencesUtil = new SharePreferencesUtil(context, dataName);
        }
        return mSharedPreferencesUtil;
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public String getString(String key) {
        return mSharedPreferencesUtil.getString(key);
    }

    public int getInt(String key) {
        return mSharedPreferencesUtil.getInt(key);
    }

    public void removeItem(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

}
