package com.gengli.technician.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Order;

public class OrderView extends LinearLayout {

    private TextView textMissionId;
    private TextView textMissionAddres;
    private TextView textMissionCompany;
    private TextView textMissionMachine;
    private TextView textMissionTrouble;
    private TextView textMissioName;
    private TextView textMissioPhone;
    private TextView text_mission_time;

    public OrderView(Context context) {
        super(context, null);
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mission_view, this);
        init();

    }

    public OrderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    private void init() {
        textMissionId = (TextView) findViewById(R.id.text_mission_id);
        textMissionAddres = (TextView) findViewById(R.id.text_mission_address);
        textMissionCompany = (TextView) findViewById(R.id.text_mission_company);
        textMissionMachine = (TextView) findViewById(R.id.text_mission_machine);
        textMissionTrouble = (TextView) findViewById(R.id.text_mission_trouble);
        textMissioName = (TextView) findViewById(R.id.text_mission_name);
        textMissioPhone = (TextView) findViewById(R.id.text_mission_phone);
        text_mission_time = (TextView)findViewById(R.id.text_mission_time);
    }

    public void setData(Order order) {
        textMissionId.setText(order.getId());
        textMissionAddres.setText(order.getAddress());
        textMissionCompany.setText(order.getCompany());
        textMissionMachine.setText(order.getMachine());
        textMissionTrouble.setText(order.getTrouble());
        textMissioName.setText(order.getName());
        textMissioPhone.setText(order.getPhone());
        text_mission_time.setText(order.getTime());
    }
}
