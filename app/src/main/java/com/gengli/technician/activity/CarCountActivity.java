package com.gengli.technician.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gengli.technician.R;
import com.gengli.technician.adapter.CarCountAdapter;
import com.gengli.technician.bean.CarInfo;
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

public class CarCountActivity extends Activity implements View.OnClickListener {

    private EditText car_count_address_edit;
    private EditText car_count_machine_edit;
    private EditText car_count_model_id_edit;
    private TextView car_count_in_time_edit;
    private TextView car_count_ready_time_edit;
    private TimePickerDialog timePickerDialog;
    private TextView car_count_add_bt;
    private RepairListView car_count_listView;
    private CarCountAdapter adapter;
    private TextView car_count_kupingding_bt;
    private ImageView car_count_back_bt;
    private TextView car_count_commit_bt;
    private TextView car_count_month_text;
    private TextView car_count_day_text;

    private List<CarInfo> carInfoList;
    private int position;
    private String chooseDay;
    private String month;
    private String day;
    private int witchType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_count);
        chooseDay = getIntent().getStringExtra("choose_day");
//        LogUtils.showLogD("........." + chooseDay);

        initView();
        if (!TextUtils.isEmpty(chooseDay)) {
            String[] str = chooseDay.split("-");
            month = str[1];
            day = str[2];
            car_count_month_text.setText(month);
            car_count_day_text.setText(day);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        carInfoList = new ArrayList<>();
        car_count_listView = (RepairListView) findViewById(R.id.car_count_listView);
        adapter = new CarCountAdapter(this, carInfoList);
        car_count_listView.setAdapter(adapter);
        adapter.setListener(new CarCountAdapter.CarCountListener() {
            @Override
            public void delCount(View v) {
                position = (Integer) v.getTag();
                carInfoList.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void modifyCount(View v) {
                position = (Integer) v.getTag();
                Intent intent = new Intent(CarCountActivity.this, CarInfoActivity.class);
                intent.putExtra("start_car_info", carInfoList.get((Integer) v.getTag()));
                startActivityForResult(intent, 3);
            }
        });
        car_count_back_bt = (ImageView) findViewById(R.id.car_count_back_bt);
        car_count_kupingding_bt = (TextView) findViewById(R.id.car_count_kupingding_bt);
        car_count_address_edit = (EditText) findViewById(R.id.car_count_address_edit);
        car_count_address_edit.setHintTextColor(0xffd4d4d4);
        car_count_machine_edit = (EditText) findViewById(R.id.car_count_machine_edit);
        car_count_machine_edit.setHintTextColor(0xffd4d4d4);
        car_count_model_id_edit = (EditText) findViewById(R.id.car_count_model_id_edit);
        car_count_model_id_edit.setHintTextColor(0xffd4d4d4);
        car_count_in_time_edit = (TextView) findViewById(R.id.car_count_in_time_edit);
        car_count_ready_time_edit = (TextView) findViewById(R.id.car_count_ready_time_edit);
        car_count_add_bt = (TextView) findViewById(R.id.car_count_add_bt);
        car_count_commit_bt = (TextView) findViewById(R.id.car_count_commit_bt);
        car_count_month_text = (TextView) findViewById(R.id.car_count_month_text);
        car_count_day_text = (TextView) findViewById(R.id.car_count_day_text);
        car_count_commit_bt.setOnClickListener(this);
        car_count_back_bt.setOnClickListener(this);
        car_count_add_bt.setOnClickListener(this);
        car_count_in_time_edit.setOnClickListener(this);
        car_count_ready_time_edit.setOnClickListener(this);
        car_count_kupingding_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_count_in_time_edit:
                witchType = 0;
                showTimePickerDialog();
                break;
            case R.id.car_count_ready_time_edit:
                witchType = 1;
                showTimePickerDialog();
                break;
            case R.id.car_count_add_bt:
                Intent intent = new Intent(this, CarInfoActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.car_count_kupingding_bt:
                startActivity(new Intent(this, AssessActivity.class));
                break;
            case R.id.car_count_back_bt:
                finish();
                break;
            case R.id.car_count_commit_bt:
                commitOperator();
                break;
        }
    }


    private String enterTime;
    private String readyTime;

    public void showTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (witchType == 0) {
                    enterTime = add0(hourOfDay) + "点" + add0(minute) + "分";
                    car_count_in_time_edit.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
                } else if (witchType == 1) {
                    readyTime = add0(hourOfDay) + "点" + add0(minute) + "分";
                    car_count_ready_time_edit.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
                }
            }
        }, 0, 0, true);
        timePickerDialog.setTitle("请选择时间");
        timePickerDialog.show();
    }


    public String add0(int a) {
        if (a < 10) {
            return "0" + a;
        }
        return a + "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (data == null) {
                return;
            }
            if (requestCode == 1) {
                CarInfo carInfo = (CarInfo) data.getSerializableExtra("car_info");
                if (carInfo == null) {
                    return;
                }
                carInfoList.add(carInfo);
            } else if (requestCode == 3) {
                CarInfo carInfo = (CarInfo) data.getSerializableExtra("car_info");
                if (carInfo == null) {
                    return;
                }
                carInfoList.get(position).setStartTime(carInfo.getStartTime());
                carInfoList.get(position).setEndTime(carInfo.getEndTime());
                carInfoList.get(position).setWeight(carInfo.getWeight());
                carInfoList.get(position).setDesc(carInfo.getDesc());
                carInfoList.get(position).setOther(carInfo.getOther());
            } else {

            }
            adapter.notifyDataSetChanged();
        }

    }

    private void commitOperator() {
        String address = car_count_address_edit.getText().toString();
        String model = car_count_machine_edit.getText().toString();
        String orderID = car_count_model_id_edit.getText().toString();
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(model)
                || TextUtils.isEmpty(orderID) || TextUtils.isEmpty(enterTime)
                || TextUtils.isEmpty(readyTime)) {
            LogUtils.showCenterToast(this, "请输入完整信息");
            return;
        }
        JSONArray jsonArray = new JSONArray();
        if (carInfoList != null && carInfoList.size() > 0) {
            for (int i = 0; i < carInfoList.size(); i++) {
                JSONObject object = new JSONObject();
                try {
                    object.put("start", carInfoList.get(i).getStartTime().toString());
                    object.put("end", carInfoList.get(i).getEndTime().toString());
                    object.put("pjfl", carInfoList.get(i).getWeight().toString());
                    object.put("yxqk", carInfoList.get(i).getDesc().toString());
                    object.put("qtys", carInfoList.get(i).getOther().toString());
                    jsonArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LogUtils.showLogD("...." + jsonArray.toString());
        } else {
            LogUtils.showCenterToast(this, "请至少输入一个车辆信息");
            return;
        }

        String url = ServicePort.OPERATOR_ADD;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("day", chooseDay);
        map.put("address", address);
        map.put("product_model", model);
        map.put("order_id", orderID);
        map.put("enter", enterTime);
        map.put("prepare", readyTime);
        map.put("products", jsonArray.toString());

        AjaxParams params = api.getParam(this, map);
        params.put("day", chooseDay);
        params.put("address", address);
        params.put("product_model", model);
        params.put("order_id", orderID);
        params.put("enter", enterTime);
        params.put("prepare", readyTime);
        params.put("products", jsonArray.toString());

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("--->添加记录表返回数据：" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            handle.sendEmptyMessage(1);
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
            } else if (msg.what == 1) {
                LogUtils.showCenterToast(CarCountActivity.this, "上传失败，请稍后再试");
            }
        }
    };
}
