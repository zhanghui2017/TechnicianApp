package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.ListImgAdapter;
import com.gengli.technician.adapter.ListVideoAdapter;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.RepairListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维修详情
 */
public class OrderOKActivity extends Activity implements View.OnClickListener {

    private TextView ordr_ok_level_text;
    private TextView ordr_ok_start_time_text;
    private TextView ordr_ok_end_time_text;
    private TextView ordr_ok_machine_text;
    private TextView ordr_ok_trouble_text;
    private TextView ordr_ok_fitting_text;
    private TextView ordr_ok_name_phone_text;
    private TextView ordr_ok_desc_text;
    private TextView order_ok_company_text;
    private TextView order_ok_address_text;
    private TextView order_ok_express_address_text;
    private TextView order_ok_name_phone_text;
    private TextView order_ok_model_text;
    private TextView order_ok_buy_time_text;
    private TextView ordr_ok_report_text;


    private ImageView order_ok_back_img;
    private ListImgAdapter beginAdapter;
    private ListImgAdapter afterAdapter;
    private ListVideoAdapter begin2Adapter;
    private ListVideoAdapter after2Adapter;
    private RepairListView ordr_ok_begin_img_list;
    private RepairListView ordr_ok_after_img_list;
    private RepairListView ordr_ok_begin_video_list;
    private RepairListView ordr_ok_after_video_list;
    private List<String> beginImgs;
    private List<String> afterImgs;
    private List<String> beginVideos;
    private List<String> afterVideos;
    private Order order;
    private String content;
    private String end_time;
    private String buy_period;
    private String order_id;
    private StringBuilder fits = new StringBuilder();
    private StringBuilder des = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ok);
        beginImgs = new ArrayList<>();
        afterImgs = new ArrayList<>();
        beginVideos = new ArrayList<>();
        afterVideos = new ArrayList<>();
        order = (Order) getIntent().getSerializableExtra("OK_ORDER");
        getRepairDetail();
//        initData();
        init();
    }

    public void init() {
        order_ok_back_img = (ImageView) findViewById(R.id.order_ok_back_img);
        order_ok_back_img.setOnClickListener(this);
        ordr_ok_level_text = (TextView) findViewById(R.id.ordr_ok_level_text);
        ordr_ok_start_time_text = (TextView) findViewById(R.id.ordr_ok_start_time_text);
        ordr_ok_end_time_text = (TextView) findViewById(R.id.ordr_ok_end_time_text);
        ordr_ok_machine_text = (TextView) findViewById(R.id.ordr_ok_machine_text);
        ordr_ok_trouble_text = (TextView) findViewById(R.id.ordr_ok_trouble_text);
        ordr_ok_fitting_text = (TextView) findViewById(R.id.ordr_ok_fitting_text);
        ordr_ok_name_phone_text = (TextView) findViewById(R.id.ordr_ok_name_phone_text);
        ordr_ok_desc_text = (TextView) findViewById(R.id.ordr_ok_desc_text);
        order_ok_company_text = (TextView) findViewById(R.id.order_ok_company_text);
        order_ok_address_text = (TextView) findViewById(R.id.order_ok_address_text);
        order_ok_express_address_text = (TextView) findViewById(R.id.order_ok_express_address_text);
        order_ok_name_phone_text = (TextView) findViewById(R.id.order_ok_name_phone_text);
        order_ok_model_text = (TextView) findViewById(R.id.order_ok_model_text);
        order_ok_buy_time_text = (TextView) findViewById(R.id.order_ok_buy_time_text);
        ordr_ok_report_text = (TextView) findViewById(R.id.ordr_ok_report_text);

        ordr_ok_begin_img_list = (RepairListView) findViewById(R.id.ordr_ok_begin_img_list);
        ordr_ok_after_img_list = (RepairListView) findViewById(R.id.ordr_ok_after_img_list);
        ordr_ok_begin_video_list = (RepairListView) findViewById(R.id.ordr_ok_begin_video_list);
        ordr_ok_after_video_list = (RepairListView) findViewById(R.id.ordr_ok_after_video_list);

        order_ok_name_phone_text.setText(order.getName() + " " + order.getPhone());
        order_ok_express_address_text.setText(order.getExpressAddress());
        order_ok_company_text.setText(order.getCompany());
        order_ok_address_text.setText(order.getAddress());
        ordr_ok_level_text.setText(order.getLevel());
        ordr_ok_start_time_text.setText(order.getTime());
        ordr_ok_machine_text.setText(order.getMachine());
        ordr_ok_trouble_text.setText(order.getTrouble());
        ordr_ok_name_phone_text.setText(order.getChargeName() + " " + order.getChargePhone());


        beginAdapter = new ListImgAdapter(this, beginImgs);
        ordr_ok_begin_img_list.setAdapter(beginAdapter);
        afterAdapter = new ListImgAdapter(this, afterImgs);
        ordr_ok_after_img_list.setAdapter(afterAdapter);
        begin2Adapter = new ListVideoAdapter(this, beginVideos);
        ordr_ok_begin_video_list.setAdapter(begin2Adapter);
        after2Adapter = new ListVideoAdapter(this, afterVideos);
        ordr_ok_after_video_list.setAdapter(after2Adapter);

        ordr_ok_begin_img_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderOKActivity.this, PictureActivity.class);
                intent.putExtra("img_url", beginImgs.get(position));
                startActivity(intent);
            }
        });

        ordr_ok_after_img_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderOKActivity.this, PictureActivity.class);
                intent.putExtra("img_url", afterImgs.get(position));
                startActivity(intent);
            }
        });

        ordr_ok_begin_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderOKActivity.this, VideoActivity.class);
                intent.putExtra("video_url", beginVideos.get(position));
                startActivity(intent);
            }
        });
        ordr_ok_after_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderOKActivity.this, VideoActivity.class);
                intent.putExtra("video_url", beginVideos.get(position));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_ok_back_img:
                finish();
                break;
        }
    }


    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ordr_ok_desc_text.setText(des);
            } else if (msg.what == 2) {
                ordr_ok_end_time_text.setText(end_time);
                order_ok_model_text.setText(order_id);
                order_ok_buy_time_text.setText(buy_period);
                ordr_ok_report_text.setText(content);
            } else if (msg.what == 3) {
                ordr_ok_fitting_text.setText(fits);
            } else if (msg.what == 4) {
                ordr_ok_fitting_text.setText("无需配件");
            }
        }
    };


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
                            end_time = results.getString("handle_end");
                            order_id = results.getString("order_id");
                            buy_period = results.getString("buy_period");
                            content = results.getString("content");
                            handle.sendEmptyMessage(2);
                            JSONArray parts = results.getJSONArray("parts");
                            if (parts.length() > 0) {
                                for (int i = 0; i < parts.length(); i++) {
                                    JSONObject item = parts.getJSONObject(i);
                                    fits.append(" " + item.getString("name"));
                                }
                                handle.sendEmptyMessage(3);
                            } else {
                                handle.sendEmptyMessage(4);
                            }

                            JSONObject problems = results.getJSONObject("problems");

                            if (!TextUtils.isEmpty(problems.toString())) {
                                if (problems.has("suningji")) {
                                    String suningji = problems.getString("suningji");
                                    des.append("速凝剂添加系统：" + suningji + "\n");
                                }
                                if (problems.has("dipan")) {
                                    String dipan = problems.getString("dipan");
                                    des.append("底盘系统：" + dipan + "\n");
                                }
                                if (problems.has("runhua")) {
                                    String runhua = problems.getString("runhua");
                                    des.append("润滑系统：" + runhua + "\n");
                                }
                                if (problems.has("qita")) {
                                    String qita = problems.getString("qita");
                                    des.append("其他系统：" + qita + "\n");
                                }
                                if (problems.has("yeya")) {
                                    String yeya = problems.getString("yeya");
                                    des.append("液压系统：" + yeya + "\n");
                                }
                                if (problems.has("dianqi")) {
                                    String dianqi = problems.getString("dianqi");
                                    des.append("电气：" + dianqi + "\n");
                                }
                                if (problems.has("jixie")) {
                                    String jixie = problems.getString("jixie");
                                    des.append("机械：" + jixie + "\n");
                                }
                                if (problems.has("bengsong")) {
                                    String bengsong = problems.getString("bengsong");
                                    des.append("泵送：" + bengsong + "\n");
                                }
                                handle.sendEmptyMessage(0);
                            }

                            JSONObject details = results.getJSONObject("details");
                            JSONArray worker_images = details.getJSONArray("worker_images");
                            JSONArray worker_videos = details.getJSONArray("worker_videos");


                            for (int i = 0; i < worker_images.length(); i++) {
                                JSONObject item = worker_images.getJSONObject(i);
                                beginImgs.add(item.getString("img"));
                                afterImgs.add(item.getString("img_after"));
                            }
                            for (int j = 0; j < worker_videos.length(); j++) {
                                JSONObject item = worker_videos.getJSONObject(j);
                                beginVideos.add(item.getString("video"));
                                afterVideos.add(item.getString("video_after"));
                            }
                            beginAdapter.notifyDataSetChanged();
                            afterAdapter.notifyDataSetChanged();
                            begin2Adapter.notifyDataSetChanged();
                            after2Adapter.notifyDataSetChanged();
                        } else {
                            LogUtils.showCenterToast(OrderOKActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
