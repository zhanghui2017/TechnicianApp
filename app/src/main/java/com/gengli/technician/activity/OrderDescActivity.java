package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.OrderDescAdapter;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.HorizontalListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维修描述
 */
public class OrderDescActivity extends Activity implements View.OnClickListener {

    private HorizontalListView order_desc_title_view;
    private ImageView order_desc_back_img;
    private EditText edit_order_desc;
    private OrderDescAdapter adapter;
    private String[] types = {"液压系统", "电气", "机械", "泵送", "速凝剂添加系统", "底盘系统", "润滑系统", "其它系统"};
    private String[] typesTMP = {"yeya", "dianqi", "jixie", "bengsong", "suningji", "dipan", "runhua", "qita"};
    private TextView text_order_desc_comit_bt;
    //    private List<Map<String, String>> descList;
    private Map<String, String> descMap;
    int clickPosition = 0;
    private Order order;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_desc);
        order = (Order) getIntent().getSerializableExtra("commit_order");
        preferences = getSharedPreferences("order_desc", Activity.MODE_PRIVATE);
        initView();

    }

    private void initView() {
        descMap = new HashMap<>();

        text_order_desc_comit_bt = (TextView) findViewById(R.id.text_order_desc_comit_bt);
        text_order_desc_comit_bt.setOnClickListener(this);
        order_desc_back_img = (ImageView) findViewById(R.id.order_desc_back_img);
        order_desc_back_img.setOnClickListener(this);
        edit_order_desc = (EditText) findViewById(R.id.edit_order_desc);
        edit_order_desc.setOnClickListener(this);
        order_desc_title_view = (HorizontalListView) findViewById(R.id.order_desc_title_view);
        adapter = new OrderDescAdapter(this, types);
        order_desc_title_view.setAdapter(adapter);
        order_desc_title_view.setOnItemClickListener(new OnTitleItemListClick());


        String contentTMP = preferences.getString(typesTMP[0], "");
        if (!TextUtils.isEmpty(contentTMP)) {
            edit_order_desc.setText(contentTMP);
        }
    }

    public class OnTitleItemListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (!TextUtils.isEmpty(edit_order_desc.getText().toString())) {
                descMap.put(types[clickPosition], edit_order_desc.getText().toString());
                LogUtils.showLogD("之前position是：" + types[clickPosition] + "   " + edit_order_desc.getText().toString());
            }
            clickPosition = position;
            adapter.changeSelected(position);
            adapter.notifyDataSetChanged();
            edit_order_desc.setText("");
            edit_order_desc.setFocusable(false);


            String content = descMap.get(types[position]);
            if (!TextUtils.isEmpty(content)) {
                edit_order_desc.setText(content);
            } else {
                String contentTMP = preferences.getString(typesTMP[position], "");
                LogUtils.showLogD("............" + contentTMP);
                if (!TextUtils.isEmpty(contentTMP)) {
                    edit_order_desc.setText(contentTMP);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_desc_back_img:
                finish();
                break;
            case R.id.edit_order_desc:
                edit_order_desc.setFocusable(true);//设置输入框可聚集
                edit_order_desc.setFocusableInTouchMode(true);//设置触摸聚焦
                edit_order_desc.requestFocus();//请求焦点
                edit_order_desc.findFocus();//获取焦点
                break;

            case R.id.text_order_desc_comit_bt:
                if (!TextUtils.isEmpty(edit_order_desc.getText().toString())) {
                    descMap.put(types[clickPosition], edit_order_desc.getText().toString());
                }
                for (String key : descMap.keySet()) {
                    String value = descMap.get(key);
                    LogUtils.showLogD(key + "     " + value);
                }
                addDesc();
                break;
        }
    }


    public void addDesc() {
        SharedPreferences.Editor editor = preferences.edit();
        String url = ServicePort.REPAIR_PROBLEM_ADD;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("rid", order.getId());
        for (String key : descMap.keySet()) {
            if (key.equals(types[0])) {
                editor.putString("yeya", descMap.get(key));
                map.put("yeya", descMap.get(key));
            }
            if (key.equals(types[1])) {
                editor.putString("dianqi", descMap.get(key));
                map.put("dianqi", descMap.get(key));
            }
            if (key.equals(types[2])) {
                editor.putString("jixie", descMap.get(key));
                map.put("jixie", descMap.get(key));
            }
            if (key.equals(types[3])) {
                editor.putString("bengsong", descMap.get(key));
                map.put("bengsong", descMap.get(key));
            }
            if (key.equals(types[4])) {
                editor.putString("suningji", descMap.get(key));
                map.put("suningji", descMap.get(key));
            }
            if (key.equals(types[5])) {
                editor.putString("dipan", descMap.get(key));
                map.put("dipan", descMap.get(key));
            }
            if (key.equals(types[6])) {
                editor.putString("runhua", descMap.get(key));
                map.put("runhua", descMap.get(key));
            }
            if (key.equals(types[7])) {
                editor.putString("qita", descMap.get(key));
                map.put("qita", descMap.get(key));
            }
        }
        editor.commit();
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        for (String key : descMap.keySet()) {
            if (key.equals(types[0])) {
                params.put("yeya", descMap.get(key));
            }
            if (key.equals(types[1])) {
                params.put("dianqi", descMap.get(key));
            }
            if (key.equals(types[2])) {
                params.put("jixie", descMap.get(key));
            }
            if (key.equals(types[3])) {
                params.put("bengsong", descMap.get(key));
            }
            if (key.equals(types[4])) {
                params.put("suningji", descMap.get(key));
            }
            if (key.equals(types[5])) {
                params.put("dipan", descMap.get(key));
            }
            if (key.equals(types[6])) {
                params.put("runhua", descMap.get(key));
            }
            if (key.equals(types[7])) {
                params.put("qita", descMap.get(key));
            }
        }
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String responce = o.toString();
                LogUtils.showLogD("----->提交维修描述返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            LogUtils.showCenterToast(OrderDescActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
        }
    };
}
