package com.gengli.technician.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.MessageAdapter;
import com.gengli.technician.bean.Message;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
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

public class MessageActivity extends Activity implements View.OnClickListener {
    private ImageView message_back_img;
    private PullToRefreshListView message_list_view;
    private MessageAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageList = new ArrayList<>();
        getMmessage();
//        initData();
        initView();
    }

    private void initView() {
        message_back_img = (ImageView) findViewById(R.id.message_back_img);
        message_back_img.setOnClickListener(this);
        message_list_view = (PullToRefreshListView) findViewById(R.id.message_list_view);
        adapter = new MessageAdapter(this, messageList);
        message_list_view.setAdapter(adapter);
        message_list_view.setOnItemClickListener(new MessageItemClick());
    }

    public class MessageItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Message message = messageList.get(position);
            if (message.getType() == 1) {

            } else if (message.getType() == 2) {

            } else if (message.getType() == 3) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_back_img:
                finish();
                break;
        }
    }


    private void getMmessage() {
        String url = ServicePort.MSG_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        api.Post(url, null, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->消息列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            JSONArray list = results.getJSONArray("list");
                            for (int i = 0;i<list.length();i++){
                                JSONObject item = list.getJSONObject(i);
                                Message message = new Message();
                                message.setImgUrl(item.getString("logo"));
                                message.setContent(item.getString("des"));
                                message.setTitle(item.getString("name"));
                            }

                        } else {
                            LogUtils.showCenterToast(MessageActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
//
//    public void initData() {
//        messageList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Message m = new Message();
//            m.setImgUrl("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
//            m.setTitle("标题" + i);
//            m.setContent("你维修的南阳市矿山厂设备“GHP24D-Ⅰ 车载\n" +
//                    "混凝土湿喷机”评价未解决问题，" + i);
//            int type = (new Random().nextInt(4)) + 1;
//            m.setType(type);
//            messageList.add(m);
//        }
//    }
}
