package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.GridImgAdapter;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.HorizontalListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderBeginActivity extends Activity implements View.OnClickListener {

    private TextView order_begin_id_text;
    private TextView order_begin_level_text;
    private TextView order_begin_time_text;
    private TextView order_begin_machine_text;
    private TextView order_begin_trouble_text;
    private TextView order_begin_company_text;
    private TextView order_begin_address_text;
    private TextView order_begin_express_address_text;
    private TextView order_begin_name_phone_text;
    private TextView order_begin_model_text;
    private TextView order_begin_buy_time_text;
    private HorizontalListView order_begin_fitting_horizontal_list;
    private GridImgAdapter adapter;
    private List<String> imgList;
    private Order order;
    private ImageView order_begin_back_img;
    private TextView order_begin_confirm;

    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_begin);
        order = (Order) getIntent().getSerializableExtra("BEGIN_ORDER");
        getRepairDetail();
        imgList = new ArrayList<>();
        init();
    }

    public void init() {
        order_begin_confirm = (TextView) findViewById(R.id.order_begin_confirm);
        order_begin_confirm.setOnClickListener(this);
        order_begin_back_img = (ImageView) findViewById(R.id.order_begin_back_img);
        order_begin_back_img.setOnClickListener(this);
        order_begin_model_text = (TextView) findViewById(R.id.order_begin_model_text);
        order_begin_id_text = (TextView) findViewById(R.id.order_begin_id_text);
        order_begin_level_text = (TextView) findViewById(R.id.order_begin_level_text);
        order_begin_time_text = (TextView) findViewById(R.id.order_begin_time_text);
        order_begin_machine_text = (TextView) findViewById(R.id.order_begin_machine_text);
        order_begin_trouble_text = (TextView) findViewById(R.id.order_begin_trouble_text);
        order_begin_company_text = (TextView) findViewById(R.id.order_begin_company_text);
        order_begin_address_text = (TextView) findViewById(R.id.order_begin_address_text);
        order_begin_express_address_text = (TextView) findViewById(R.id.order_begin_express_address_text);
        order_begin_buy_time_text = (TextView) findViewById(R.id.order_begin_buy_time_text);
        order_begin_name_phone_text = (TextView) findViewById(R.id.order_begin_name_phone_text);
        order_begin_fitting_horizontal_list = (HorizontalListView) findViewById(R.id.order_begin_fitting_horizontal_list);

        order_begin_id_text.setText(order.getId());
        order_begin_level_text.setText(order.getLevel());
        order_begin_time_text.setText(order.getTime());
        order_begin_model_text.setText(order.getModel());
        order_begin_machine_text.setText(order.getMachine());
        order_begin_trouble_text.setText(order.getTrouble());
        order_begin_company_text.setText(order.getCompany());
        order_begin_address_text.setText(order.getAddress());
        order_begin_express_address_text.setText(order.getExpressAddress());
        order_begin_name_phone_text.setText(order.getName() + " " + order.getPhone());

        adapter = new GridImgAdapter(this, imgList);
        order_begin_fitting_horizontal_list.setAdapter(adapter);
        order_begin_fitting_horizontal_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_begin_back_img:
                finish();
                break;
            case R.id.order_begin_confirm:
                confirm();
                break;
        }
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent intent = new Intent(OrderBeginActivity.this, OrderIngActivity.class);
                intent.putExtra("ING_ORDER", order);
                startActivity(intent);
                finish();
            } else if (msg.what == 1) {
                Intent intent = new Intent(OrderBeginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 3) {
                order_begin_fitting_horizontal_list.setVisibility(View.INVISIBLE);
            } else if (msg.what == 4){
                order_begin_model_text.setText(order_id);
                order_begin_buy_time_text.setText(buy_period);
            }
        }
    };

    private void confirm() {
        String url = ServicePort.REPAIR_CONFIRM;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("rid", order.getId());
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->接单返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else if (err_no == 3000) {
                            handle.sendEmptyMessage(1);
                            LogUtils.showLogD(jsonObject.getString("err_msg"));
                        } else {
                            LogUtils.showCenterToast(OrderBeginActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
    private String buy_period;
    private void getRepairDetail() {
        String url = ServicePort.REPAIR_DETAIL;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("rid", order.getId());
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->维修详情返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            order_id = results.getString("order_id");
                            buy_period = results.getString("buy_period");
                            handle.sendEmptyMessage(4);
                            JSONObject details = results.getJSONObject("details");
                            JSONArray client_images = details.getJSONArray("client_images");
                            if (client_images.length() > 0) {
                                for (int i = 0; i < client_images.length(); i++) {
                                    JSONObject item = client_images.getJSONObject(i);
                                    imgList.add(item.getString("img"));
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                handle.sendEmptyMessage(3);
                            }

                        } else {
                            LogUtils.showCenterToast(OrderBeginActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
}
