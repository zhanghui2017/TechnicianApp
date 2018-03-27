package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.ListImgAdapter;
import com.gengli.technician.adapter.ListVideoAdapter;
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
public class RepairDetailActivity extends Activity implements View.OnClickListener {

    private TextView repair_detail_level_text;
    private TextView repair_details_start_time_text;
    private TextView repair_details_end_time_text;
    private TextView repair_details_machine_text;
    private TextView repair_details_trouble_text;
    private TextView repair_details_fitting_text;
    private TextView repair_details_name_phone_text;
    private TextView repair_details_desc_text;
    private ImageView repair_details_back_img;
    private TextView repair_details_model_text;
    private ListImgAdapter beginAdapter;
    private ListImgAdapter afterAdapter;
    private ListVideoAdapter begin2Adapter;
    private ListVideoAdapter after2Adapter;
    private RepairListView repair_begin_img_list;
    private RepairListView repair_after_img_list;
    private RepairListView repair_begin_video_list;
    private RepairListView repair_after_video_list;
    private List<String> beginImgs;
    private List<String> afterImgs;
    private List<String> beginVideos;
    private List<String> afterVideos;
    private Order order;

    private String content;
    private String order_id;
    private StringBuilder fits = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        beginImgs = new ArrayList<>();
        afterImgs = new ArrayList<>();
        beginVideos = new ArrayList<>();
        afterVideos = new ArrayList<>();
        order = (Order) getIntent().getSerializableExtra("order_details");
        getRepairDetail();
        init();
    }

    public void init() {
        repair_details_back_img = (ImageView) findViewById(R.id.repair_details_back_img);
        repair_details_back_img.setOnClickListener(this);
        repair_detail_level_text = (TextView) findViewById(R.id.repair_detail_level_text);
        repair_details_start_time_text = (TextView) findViewById(R.id.repair_details_start_time_text);
        repair_details_end_time_text = (TextView) findViewById(R.id.repair_details_end_time_text);
        repair_details_machine_text = (TextView) findViewById(R.id.repair_details_machine_text);
        repair_details_trouble_text = (TextView) findViewById(R.id.repair_details_trouble_text);
        repair_details_fitting_text = (TextView) findViewById(R.id.repair_details_fitting_text);
        repair_details_name_phone_text = (TextView) findViewById(R.id.repair_details_name_phone_text);
        repair_details_desc_text = (TextView) findViewById(R.id.repair_details_desc_text);
        repair_details_model_text = (TextView) findViewById(R.id.repair_details_model_text);

        repair_detail_level_text.setText(order.getLevel());
        repair_details_start_time_text.setText(order.getStartTime());
        repair_details_end_time_text.setText(order.getEndTime());
        repair_details_machine_text.setText(order.getMachine());
        repair_details_trouble_text.setText(order.getDesc());
        repair_details_fitting_text.setText(order.getFitting());
        repair_details_name_phone_text.setText(order.getChargeName() + " " + order.getChargePhone());


        repair_begin_img_list = (RepairListView) findViewById(R.id.repair_begin_img_list);
        repair_after_img_list = (RepairListView) findViewById(R.id.repair_after_img_list);
        repair_begin_video_list = (RepairListView) findViewById(R.id.repair_begin_video_list);
        repair_after_video_list = (RepairListView) findViewById(R.id.repair_after_video_list);

        beginAdapter = new ListImgAdapter(this, beginImgs);
        repair_begin_img_list.setAdapter(beginAdapter);
        afterAdapter = new ListImgAdapter(this, afterImgs);
        repair_after_img_list.setAdapter(afterAdapter);
        begin2Adapter = new ListVideoAdapter(this, beginVideos);
        repair_begin_video_list.setAdapter(begin2Adapter);
        after2Adapter = new ListVideoAdapter(this, afterVideos);
        repair_after_video_list.setAdapter(after2Adapter);

        repair_begin_img_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RepairDetailActivity.this, PictureActivity.class);
                intent.putExtra("img_url", beginImgs.get(position));
                startActivity(intent);
            }
        });

        repair_after_img_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RepairDetailActivity.this, PictureActivity.class);
                intent.putExtra("img_url", afterImgs.get(position));
                startActivity(intent);


            }
        });

        repair_begin_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RepairDetailActivity.this, VideoActivity.class);
                intent.putExtra("video_url", beginVideos.get(position));
                startActivity(intent);
            }
        });
        repair_after_video_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RepairDetailActivity.this, VideoActivity.class);
                intent.putExtra("video_url", afterVideos.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_details_back_img:
                finish();
                break;
        }
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                repair_details_desc_text.setText(des);
            }
            if (msg.what == 2) {
                repair_details_model_text.setText(order_id);
            } else if (msg.what == 3) {
                repair_details_fitting_text.setText(fits);
            } else if (msg.what == 4) {
                repair_details_fitting_text.setText("无需配件");
            }
        }
    };
    private StringBuilder des = new StringBuilder();

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
                            content = results.getString("content");
                            handle.sendEmptyMessage(2);
                            JSONArray parts = results.getJSONArray("parts");
                            if (parts.length() > 0) {
                                for (int i = 0; i < parts.length(); i++) {
                                    JSONObject item = parts.getJSONObject(i);
                                    LogUtils.showLogD(" ---------------------- " + item.getString("name"));
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

//                            Message message = new Message();
//                            message.arg1 = 0;
//                            message.obj = problems;
//                            handle.sendMessage(message);

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
                            LogUtils.showCenterToast(RepairDetailActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

    public void initData() {
        beginImgs = new ArrayList<>();
        beginImgs.add("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
        beginImgs.add("http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg");
        beginImgs.add("http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg");


        afterImgs = new ArrayList<>();
        afterImgs.add("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
        afterImgs.add("http://pic2.zhimg.com/551fac8833ec0f9e0a142aa2031b9b09.jpg");
        afterImgs.add("http://pic2.zhimg.com/be6f444c9c8bc03baa8d79cecae40961.jpg");

        beginVideos = new ArrayList<>();
        beginVideos.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        beginVideos.add("http://qiubai-video.qiushibaike.com/9VN8C90AK3735I4Y.mp4");
        beginVideos.add("http://qiubai-video.qiushibaike.com/14J428HITWYWLOLM.mp4");


        afterVideos = new ArrayList<>();
        afterVideos.add("http://qiubai-video.qiushibaike.com/9VN8C90AK3735I4Y_3g.mp4");
        afterVideos.add("http://qiubai-video.qiushibaike.com/PCYQ58X3ZNW1LKJE.mp4");
        afterVideos.add("http://qiubai-video.qiushibaike.com/PCYQ58X3ZNW1LKJE_3g.mp4");
    }


}
