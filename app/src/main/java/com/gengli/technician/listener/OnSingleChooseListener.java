package com.gengli.technician.listener;

import com.gengli.technician.bean.DateBean;

import android.view.View;

/**
 * 日期点击接口
 */
public interface OnSingleChooseListener {
    /**
     * @param view
     * @param date
     */
    void onSingleChoose(View view, DateBean date);
}
