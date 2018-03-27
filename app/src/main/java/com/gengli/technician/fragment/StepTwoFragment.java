package com.gengli.technician.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.AssessActivity;
import com.gengli.technician.util.LogUtils;

public class StepTwoFragment extends Fragment {
    private EditText fra_two_text_1, fra_two_text_2, fra_two_text_3, fra_two_text_4, fra_two_text_5, fra_two_text_6, fra_two_text_7, fra_two_text_8, fra_two_text_9, fra_two_text_10, fra_two_text_11, fra_two_text_12;
    private TextView fra_two_next_bt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_two, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        fra_two_next_bt = (TextView) view.findViewById(R.id.fra_two_next_bt);
        fra_two_text_1 = (EditText) view.findViewById(R.id.fra_two_text_1);
        fra_two_text_2 = (EditText) view.findViewById(R.id.fra_two_text_2);
        fra_two_text_3 = (EditText) view.findViewById(R.id.fra_two_text_3);
        fra_two_text_4 = (EditText) view.findViewById(R.id.fra_two_text_4);
        fra_two_text_5 = (EditText) view.findViewById(R.id.fra_two_text_5);
        fra_two_text_6 = (EditText) view.findViewById(R.id.fra_two_text_6);
        fra_two_text_7 = (EditText) view.findViewById(R.id.fra_two_text_7);
        fra_two_text_8 = (EditText) view.findViewById(R.id.fra_two_text_8);
        fra_two_text_9 = (EditText) view.findViewById(R.id.fra_two_text_9);
        fra_two_text_10 = (EditText) view.findViewById(R.id.fra_two_text_10);
        fra_two_text_11 = (EditText) view.findViewById(R.id.fra_two_text_11);
        fra_two_text_12 = (EditText) view.findViewById(R.id.fra_two_text_12);


        fra_two_next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                ((AssessActivity) getActivity()).changeFrame(2);
            }
        });
    }

    public void setData() {
        String data_kcgy = fra_two_text_1.getText().toString();
        String data_ycjb = fra_two_text_2.getText().toString();
        String data_gddy = fra_two_text_3.getText().toString();
        String data_gfkyj = fra_two_text_4.getText().toString();
        String data_jindong = fra_two_text_5.getText().toString();
        String data_shuini = fra_two_text_6.getText().toString();
        String data_sha = fra_two_text_7.getText().toString();
        String data_leibie = fra_two_text_8.getText().toString();
        String data_shizi = fra_two_text_9.getText().toString();
        String data_shui = fra_two_text_10.getText().toString();
        String data_suningji = fra_two_text_11.getText().toString();
        String data_jianshuini = fra_two_text_12.getText().toString();

        SharedPreferences preferences = getActivity().getSharedPreferences("edit_assess", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data_kcgy", data_kcgy);
        editor.putString("data_ycjb", data_ycjb);
        editor.putString("data_gddy", data_gddy);
        editor.putString("data_gfkyj", data_gfkyj);
        editor.putString("data_jindong", data_jindong);
        editor.putString("data_shuini", data_shuini);
        editor.putString("data_sha", data_sha);
        editor.putString("data_leibie", data_leibie);
        editor.putString("data_shizi", data_shizi);
        editor.putString("data_shui", data_shui);
        editor.putString("data_suningji", data_suningji);
        editor.putString("data_jianshuini", data_jianshuini);
        editor.commit();
    }
}
