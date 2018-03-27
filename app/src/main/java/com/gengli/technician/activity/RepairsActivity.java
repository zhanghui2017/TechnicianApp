package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Call;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.RepairsAdapter;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 维修记录
 */
public class RepairsActivity extends Activity implements View.OnClickListener {

    private PullToRefreshListView repairs_list_view;
    private ImageView repair_back_img;
    private RepairsAdapter adapter;
    private List<Order> repairList;
    private int page = 1;
    private String machine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs);
        machine = getIntent().getStringExtra("ORDER_PRODUCT_ID");

//        initData();
        getOrderData(page);
        init();
    }

    public void init() {
        repairList = new ArrayList<>();
        repair_back_img = (ImageView) findViewById(R.id.repair_back_img);
        repair_back_img.setOnClickListener(this);
        repairs_list_view = (PullToRefreshListView) findViewById(R.id.repairs_list_view);

        adapter = new RepairsAdapter(this, repairList);
        repairs_list_view.setAdapter(adapter);
        repairs_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        repairs_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getOrderData(page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getOrderData(page++);
            }
        });
        repairs_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test", "--------->点击item<---------" + repairList.get(position));
                Intent intent = new Intent(RepairsActivity.this, RepairDetailActivity.class);
                intent.putExtra("order_details", repairList.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_back_img:
                finish();
                break;
        }
    }


    private void getOrderData(final int page) {
        final List<Order> listTemp = new ArrayList<>();
        String url = ServicePort.REPAIR_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(machine)) {
            map.put("product_model", machine);
        }
        map.put("status", 4 + "");
        map.put("page", page + "");
        AjaxParams params = api.getParam(this, map);
        if (!TextUtils.isEmpty(machine)) {
            params.put("product_model", machine);
        }
        params.put("status", 4 + "");
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("-----维修记录返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray lists = results.getJSONArray("lists");

                                if (lists.length() > 0) {
                                    handle.sendEmptyMessage(2);
                                    for (int i = 0; i < lists.length(); i++) {
                                        JSONObject item = lists.getJSONObject(i);
                                        Order order = new Order();
                                        order.setId(item.getString("rid"));
                                        order.setModel(item.getString("product_id"));
                                        order.setMachine(item.getString("product_model"));
                                        order.setName(item.getString("realname"));
                                        order.setPhone(item.getString("tel"));
                                        order.setCompany(item.getString("unit"));
                                        order.setAddress(item.getString("address"));
                                        order.setChargeName(item.getString("handle_name"));
                                        order.setChargePhone(item.getString("handle_phone"));
                                        order.setTime(item.getString("create_time"));
                                        order.setType(item.getInt("status"));
                                        order.setLevel(item.getString("category_name"));
                                        order.setDesc(item.getString("des"));
                                        order.setExpressAddress(item.getString("express"));
                                        order.setStartTime(item.getString("handle_start"));
                                        order.setEndTime(item.getString("handle_end"));
                                        listTemp.add(order);
                                    }
                                    if (page == 1) {
                                        repairList.clear();
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(RepairsActivity.this, "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            repairList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(RepairsActivity.this, "没有更多数据");
                                        } else if (listTemp.size() > 0) {
                                            repairList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    handle.sendEmptyMessage(1);
                                    LogUtils.showCenterToast(RepairsActivity.this, "没有订单");
                                }


                            }
                        }
                        repairs_list_view.onRefreshComplete();
                    } catch (JSONException e) {

                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }
        });

    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            }
        }
    };

//    private void initData() {
//        repairList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Order order = new Order();
//            order.setId("APP" + i);
//            order.setAddress("洛阳市涧西区" + i);
//            order.setModel("GLZ1全液压掘进钻车" + i);
//            order.setLevel("维修工单" + i);
//            order.setTime("2018-01-12 16:1" + i);
//            order.setCompany("如凡电子商务" + i);
//            order.setName("rufan" + i);
//            order.setMachine("GLZ1全液压掘进钻车" + i);
//            order.setTrouble("液压车全车动作迟缓无力" + i);
//            order.setPhone("0000000000" + i);
//            order.setExpressAddress("洛阳市西工区" + i);
//            order.setChargeName("技术员" + i);
//            order.setChargePhone("9999999999" + i);
//            order.setStartTime("2018-01-12 16:1" + i);
//            order.setEndTime("2018-01-12 16:1" + i);
//            order.setFitting("喷头座、卡子接头" + i);
//            order.setDesc(i + "在运行过程中主控电脑板检测到的编码器脉冲数过少。ER17主控电脑板发出运行指令后，未收到变频器运行信号。）  断电后对控制柜线全面检查（注重变频器里边接线）和旋编线接头的检查，没查出松动现相，送上电后继续运行，机房观察数小时后不曾出现故障，离开。  到了晚上，离开有四个多小时，物业打电话电梯停了。就赶快去维修，查看记录还是那两个故障。就又对旋编线进行了测量，没问题，查看变频器，报F0090故障，也是这一块儿的问题，考虑M440变频器IO板容易松动，就用扎带把变频器捆了起来，接触器断电测都正常。");
//            int type = (new Random().nextInt(4)) + 1;
//            Log.d("test", "---------------------------------->" + type);
//            order.setType(type);
//            repairList.add(order);
//        }
//    }


}
