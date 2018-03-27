package com.gengli.technician.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gengli.technician.R;
import com.gengli.technician.activity.AssessActivity;
import com.gengli.technician.util.LogUtils;

public class StepOneFragment extends Fragment {

    private EditText fra_one_text_1, fra_one_text_2, fra_one_text_3, fra_one_text_4, fra_one_text_5, fra_one_text_6, fra_one_text_7, fra_one_text_8;

    private TextView fra_one_next_bt, fra_one_text_9, fra_one_text_10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        fra_one_text_1 = (EditText) view.findViewById(R.id.fra_one_text_1);
        fra_one_text_2 = (EditText) view.findViewById(R.id.fra_one_text_2);
        fra_one_text_3 = (EditText) view.findViewById(R.id.fra_one_text_3);
        fra_one_text_4 = (EditText) view.findViewById(R.id.fra_one_text_4);
        fra_one_text_5 = (EditText) view.findViewById(R.id.fra_one_text_5);
        fra_one_text_6 = (EditText) view.findViewById(R.id.fra_one_text_6);
        fra_one_text_7 = (EditText) view.findViewById(R.id.fra_one_text_7);
        fra_one_text_8 = (EditText) view.findViewById(R.id.fra_one_text_8);
        fra_one_text_9 = (TextView) view.findViewById(R.id.fra_one_text_9);
        fra_one_text_10 = (TextView) view.findViewById(R.id.fra_one_text_10);
        fra_one_next_bt = (TextView) view.findViewById(R.id.fra_one_next_bt);
        fra_one_next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                ((AssessActivity) getActivity()).changeFrame(1);

            }
        });
        fra_one_text_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                witchType = 0;
                showTimePickerDialog();
            }
        });
        fra_one_text_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                witchType = 1;
                showTimePickerDialog();
            }
        });
    }

    private TimePickerDialog timePickerDialog;
    private int witchType = 0;

    public void showTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (witchType == 0) {
                    fra_one_text_9.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
                } else if (witchType == 1) {
                    fra_one_text_10.setText(add0(hourOfDay) + "点" + add0(minute) + "分");
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

    public void setData() {
        String name = fra_one_text_1.getText().toString();
        String address = fra_one_text_2.getText().toString();
        String sale_name = fra_one_text_3.getText().toString();
        String sale_phone = fra_one_text_4.getText().toString();
        String lead_name = fra_one_text_5.getText().toString();
        String lead_phone = fra_one_text_6.getText().toString();
        String product_model = fra_one_text_7.getText().toString();
        String order_id = fra_one_text_8.getText().toString();
        String time_arrive = fra_one_text_9.getText().toString();
        String time_service = fra_one_text_10.getText().toString();

        SharedPreferences preferences = getActivity().getSharedPreferences("edit_assess", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("address", address);
        editor.putString("sale_name", sale_name);
        editor.putString("sale_phone", sale_phone);
        editor.putString("lead_name", lead_name);
        editor.putString("lead_phone", lead_phone);
        editor.putString("product_model", product_model);
        editor.putString("order_id", order_id);
        editor.putString("time_arrive", time_arrive);
        editor.putString("time_service", time_service);
        editor.commit();
    }
}
