package com.gengli.technician.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gengli.technician.R;
import com.gengli.technician.bean.CarInfo;
import com.gengli.technician.util.LogUtils;

public class CarInfoActivity extends Activity implements View.OnClickListener {

    private TextView car_info_start_time_text;
    private TextView car_info_end_time_text;
    private EditText car_info_pjfl_edit;
    private EditText car_info_yxqk_edit;
    private EditText car_info_qita_edit;
    private TimePickerDialog timePickerDialog;
    private ImageView car_info_back_bt;
    private TextView car_info_commit_bt;

    private int type = 0;

    private CarInfo carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        carInfo = (CarInfo) getIntent().getSerializableExtra("start_car_info");
        initView();
    }

    private void initView() {
        car_info_start_time_text = (TextView) findViewById(R.id.car_info_start_time_text);
        car_info_end_time_text = (TextView) findViewById(R.id.car_info_end_time_text);
        car_info_pjfl_edit = (EditText) findViewById(R.id.car_info_pjfl_edit);
        car_info_pjfl_edit.setHintTextColor(0xffd4d4d4);
        car_info_yxqk_edit = (EditText) findViewById(R.id.car_info_yxqk_edit);
        car_info_yxqk_edit.setHintTextColor(0xffd4d4d4);
        car_info_qita_edit = (EditText) findViewById(R.id.car_info_qita_edit);
        car_info_qita_edit.setHintTextColor(0xffd4d4d4);
        car_info_back_bt = (ImageView) findViewById(R.id.car_info_back_bt);
        car_info_commit_bt = (TextView) findViewById(R.id.car_info_commit_bt);
        car_info_commit_bt.setOnClickListener(this);
        car_info_back_bt.setOnClickListener(this);
        car_info_start_time_text.setOnClickListener(this);
        car_info_end_time_text.setOnClickListener(this);

        if (carInfo != null) {
            car_info_start_time_text.setText(carInfo.getStartTime());
            car_info_end_time_text.setText(carInfo.getEndTime());
            car_info_pjfl_edit.setText(carInfo.getWeight());
            car_info_yxqk_edit.setText(carInfo.getDesc());
            car_info_qita_edit.setText(carInfo.getOther());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_info_start_time_text:
                type = 0;
                showTimePickerDialog();
                break;
            case R.id.car_info_end_time_text:
                type = 1;
                showTimePickerDialog();
                break;
            case R.id.car_info_commit_bt:
//                String startTime = car_info_start_time_text.getText().toString();
//                String endTime = car_info_end_time_text.getText().toString();
                String pjfl = car_info_pjfl_edit.getText().toString();
                String yxqk = car_info_yxqk_edit.getText().toString();
                String qita = car_info_qita_edit.getText().toString();
                if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(pjfl) || TextUtils.isEmpty(yxqk) || TextUtils.isEmpty(qita)) {
                    LogUtils.showCenterToast(this, "请输入完整信息");
                    return;
                }
                Intent intent = new Intent();
                if (carInfo == null) {
                    CarInfo carInfonew = new CarInfo();
                    carInfonew.setStartTime(startTime);
                    carInfonew.setEndTime(endTime);
                    carInfonew.setWeight(pjfl);
                    carInfonew.setDesc(yxqk);
                    carInfonew.setOther(qita);
                    intent.putExtra("car_info", carInfonew);
                } else {
                    carInfo.setStartTime(startTime);
                    carInfo.setEndTime(endTime);
                    carInfo.setWeight(pjfl);
                    carInfo.setDesc(yxqk);
                    carInfo.setOther(qita);
                    intent.putExtra("car_info", carInfo);
                }
                setResult(0, intent);
                finish();
                break;
            case R.id.car_info_back_bt:
                finish();
                break;
        }

    }

    private String startTime;
    private String endTime;
    public void showTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type == 0) {
                    startTime = add0(hourOfDay) + "点" + add0(minute) + "分";
                    car_info_start_time_text.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
                } else if (type == 1) {
                    endTime = add0(hourOfDay) + "点" + add0(minute) + "分";
                    car_info_end_time_text.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
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

}
