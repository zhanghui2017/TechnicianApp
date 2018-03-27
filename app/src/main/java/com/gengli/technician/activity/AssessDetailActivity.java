package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Assess;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AssessDetailActivity extends Activity {

    private ImageView assess_detail_back_bt;
    private TextView assess_detail_text_1;
    private TextView assess_detail_text_2;
    private TextView assess_detail_text_3;
    private TextView assess_detail_text_4;
    private TextView assess_detail_text_5;
    private TextView assess_detail_text_6;
    private TextView assess_detail_text_7;
    private TextView assess_detail_text_8;
    private TextView assess_detail_text_9;
    private TextView assess_detail_text_10;
    private TextView assess_detail_text_11;
    private TextView assess_detail_text_12;
    private TextView assess_detail_text_13;
    private TextView assess_detail_text_14;
    private TextView assess_detail_text_15;
    private TextView assess_detail_text_16;
    private TextView assess_detail_text_17;
    private TextView assess_detail_text_18;
    private TextView assess_detail_text_19;
    private TextView assess_detail_text_20;
    private TextView assess_detail_text_21;
    private TextView assess_detail_text_22;
    private TextView assess_detail_text_23;
    private TextView assess_detail_text_24;
    private TextView assess_detail_text_25;
    private TextView assess_detail_text_26;
    private TextView assess_detail_text_27;
    private TextView assess_detail_text_28;
    private TextView assess_detail_text_29;
    private TextView assess_detail_text_30;

    private int id;
    private String day;
    private String name;
    private String address;
    private String sale_name;
    private String sale_phone;
    private String lead_name;
    private String lead_phone;
    private String product_model;
    private String order_id;
    private String time_arrive;
    private String time_service;
    private String data_kcgy;
    private String data_ycjb;
    private String data_gddy;
    private String data_gfkyj;
    private String data_jindong;
    private String data_shuini;
    private String data_sha;
    private String data_leibie;
    private String data_shizi;
    private String data_shui;
    private String data_suningji;
    private String data_jianshuini;
    private String kaohe_gztd;
    private String kaohe_szgt;
    private String kaohe_jssp;
    private String kaohe_pjsp;
    private String kaohe_yssb;
    private String kaohe_rcwh;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_detail);
        id = getIntent().getIntExtra("tmp_id", -1);
        LogUtils.showLogD("---------" + id);

        assess_detail_back_bt = (ImageView) findViewById(R.id.assess_detail_back_bt);
        assess_detail_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        assess_detail_text_1 = (TextView) findViewById(R.id.assess_detail_text_1);
        assess_detail_text_2 = (TextView) findViewById(R.id.assess_detail_text_2);
        assess_detail_text_3 = (TextView) findViewById(R.id.assess_detail_text_3);
        assess_detail_text_4 = (TextView) findViewById(R.id.assess_detail_text_4);
        assess_detail_text_5 = (TextView) findViewById(R.id.assess_detail_text_5);
        assess_detail_text_6 = (TextView) findViewById(R.id.assess_detail_text_6);
        assess_detail_text_7 = (TextView) findViewById(R.id.assess_detail_text_7);
        assess_detail_text_8 = (TextView) findViewById(R.id.assess_detail_text_8);
        assess_detail_text_9 = (TextView) findViewById(R.id.assess_detail_text_9);
        assess_detail_text_10 = (TextView) findViewById(R.id.assess_detail_text_10);
        assess_detail_text_11 = (TextView) findViewById(R.id.assess_detail_text_11);
        assess_detail_text_12 = (TextView) findViewById(R.id.assess_detail_text_12);
        assess_detail_text_13 = (TextView) findViewById(R.id.assess_detail_text_13);
        assess_detail_text_14 = (TextView) findViewById(R.id.assess_detail_text_14);
        assess_detail_text_15 = (TextView) findViewById(R.id.assess_detail_text_15);
        assess_detail_text_16 = (TextView) findViewById(R.id.assess_detail_text_16);
        assess_detail_text_17 = (TextView) findViewById(R.id.assess_detail_text_17);
        assess_detail_text_18 = (TextView) findViewById(R.id.assess_detail_text_18);
        assess_detail_text_19 = (TextView) findViewById(R.id.assess_detail_text_19);
        assess_detail_text_20 = (TextView) findViewById(R.id.assess_detail_text_20);
        assess_detail_text_21 = (TextView) findViewById(R.id.assess_detail_text_21);
        assess_detail_text_22 = (TextView) findViewById(R.id.assess_detail_text_22);
        assess_detail_text_23 = (TextView) findViewById(R.id.assess_detail_text_23);
        assess_detail_text_24 = (TextView) findViewById(R.id.assess_detail_text_24);
        assess_detail_text_25 = (TextView) findViewById(R.id.assess_detail_text_25);
        assess_detail_text_26 = (TextView) findViewById(R.id.assess_detail_text_26);
        assess_detail_text_27 = (TextView) findViewById(R.id.assess_detail_text_27);
        assess_detail_text_28 = (TextView) findViewById(R.id.assess_detail_text_28);
        assess_detail_text_29 = (TextView) findViewById(R.id.assess_detail_text_29);
        assess_detail_text_30 = (TextView) findViewById(R.id.assess_detail_text_30);

        getAssessDetail();
    }


    private void getAssessDetail() {
        String url = ServicePort.EVALUATE_DETAIL;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        AjaxParams params = api.getParam(this, map);
        params.put("id", id + "");
        api.Post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->获取评定表详情返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            day = results.getString("day");
                            name = results.getString("name");
                            address = results.getString("address");
                            sale_name = results.getString("sale_name");
                            sale_phone = results.getString("sale_phone");
                            lead_name = results.getString("lead_name");
                            lead_phone = results.getString("lead_phone");
                            product_model = results.getString("product_model");
                            order_id = results.getString("order_id");
                            time_arrive = results.getString("time_arrive");
                            time_service = results.getString("time_service");
                            data_kcgy = results.getString("data_kcgy");
                            data_ycjb = results.getString("data_ycjb");
                            data_gddy = results.getString("data_gddy");
                            data_gfkyj = results.getString("data_gfkyj");
                            data_jindong = results.getString("data_jindong");
                            data_shuini = results.getString("data_shuini");
                            data_sha = results.getString("data_sha");
                            data_leibie = results.getString("data_leibie");
                            data_shizi = results.getString("data_shizi");
                            data_shui = results.getString("data_shui");
                            data_suningji = results.getString("data_suningji");
                            data_jianshuini = results.getString("data_jianshuini");
                            kaohe_gztd = results.getString("kaohe_gztd");
                            kaohe_szgt = results.getString("kaohe_szgt");
                            kaohe_jssp = results.getString("kaohe_jssp");
                            kaohe_pjsp = results.getString("kaohe_pjsp");
                            kaohe_yssb = results.getString("kaohe_yssb");
                            kaohe_rcwh = results.getString("kaohe_rcwh");
                            note = results.getString("note");
                            handle.sendEmptyMessage(1);
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(3);
                            LogUtils.showCenterToast(AssessDetailActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        } else {
                            LogUtils.showCenterToast(AssessDetailActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                assess_detail_text_1.setText(day);
                assess_detail_text_2.setText(name);
                assess_detail_text_3.setText(address);
                assess_detail_text_4.setText(sale_name);
                assess_detail_text_5.setText(sale_phone);
                assess_detail_text_6.setText(lead_name);
                assess_detail_text_7.setText(lead_phone);
                assess_detail_text_8.setText(product_model);
                assess_detail_text_9.setText(order_id);
                assess_detail_text_10.setText(time_arrive);
                assess_detail_text_11.setText(time_service);
                assess_detail_text_12.setText(data_kcgy);
                assess_detail_text_13.setText(data_ycjb);
                assess_detail_text_14.setText(data_gddy);
                assess_detail_text_15.setText(data_gfkyj);
                assess_detail_text_16.setText(data_jindong);
                assess_detail_text_17.setText(data_shuini);
                assess_detail_text_18.setText(data_sha);
                assess_detail_text_19.setText(data_leibie);
                assess_detail_text_20.setText(data_shizi);
                assess_detail_text_21.setText(data_shui);
                assess_detail_text_22.setText(data_suningji);
                assess_detail_text_23.setText(data_jianshuini);
                assess_detail_text_24.setText(kaohe_gztd);
                assess_detail_text_25.setText(kaohe_szgt);
                assess_detail_text_26.setText(kaohe_jssp);
                assess_detail_text_27.setText(kaohe_pjsp);
                assess_detail_text_28.setText(kaohe_yssb);
                assess_detail_text_29.setText(kaohe_rcwh);
                assess_detail_text_30.setText(note);
            } else if (msg.what == 3) {
                startActivity(new Intent(AssessDetailActivity.this, LoginActivity.class));
            }
        }
    };
}
