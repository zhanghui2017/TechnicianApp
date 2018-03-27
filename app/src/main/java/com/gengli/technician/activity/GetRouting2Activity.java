package com.gengli.technician.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.fragment.ExcelOneFragment;
import com.gengli.technician.fragment.ExcelTwoFragment;
import com.gengli.technician.util.LogUtils;

public class GetRouting2Activity extends FragmentActivity implements View.OnClickListener, ExcelOneFragment.FragmentInteraction {

    private ImageView get_routing2_back_bt;
    private RelativeLayout get_routing2_tab_one_bt, get_routing2_tab_two_bt;
    private TextView get_routing2_tab_one_text, get_routing2_tab_two_text, get_routing2_add_excel_bt;
    private View get_routing2_tab_one_line, get_routing2_tab_two_line;

    private ExcelOneFragment oneFragment;
    private ExcelTwoFragment twoFragment;
    private FragmentManager fragmentManager;

    private int type = 0;
    private String chooseDay = "";

    @Override
    public void process(String str) {
        chooseDay = str;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_routing2);
        fragmentManager = getSupportFragmentManager();
        initView();
        changeFrame(0);
    }

    private void initView() {
        get_routing2_back_bt = (ImageView) findViewById(R.id.get_routing2_back_bt);
        get_routing2_tab_one_bt = (RelativeLayout) findViewById(R.id.get_routing2_tab_one_bt);
        get_routing2_tab_two_bt = (RelativeLayout) findViewById(R.id.get_routing2_tab_two_bt);
        get_routing2_tab_one_text = (TextView) findViewById(R.id.get_routing2_tab_one_text);
        get_routing2_tab_two_text = (TextView) findViewById(R.id.get_routing2_tab_two_text);
        get_routing2_add_excel_bt = (TextView) findViewById(R.id.get_routing2_add_excel_bt);
        get_routing2_tab_one_line = findViewById(R.id.get_routing2_tab_one_line);
        get_routing2_tab_two_line = findViewById(R.id.get_routing2_tab_two_line);

        get_routing2_back_bt.setOnClickListener(this);
        get_routing2_tab_one_bt.setOnClickListener(this);
        get_routing2_tab_two_bt.setOnClickListener(this);
        get_routing2_add_excel_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_routing2_back_bt:
                finish();
                break;
            case R.id.get_routing2_tab_one_bt:
                changeFrame(0);
                break;
            case R.id.get_routing2_tab_two_bt:
                changeFrame(1);
                break;
            case R.id.get_routing2_add_excel_bt:
                if (type == 0) {
                    startActivity(new Intent(this, CarCountActivity.class).putExtra("choose_day", chooseDay));
                } else {
                    startActivity(new Intent(this, AssessActivity.class).putExtra("choose_day", chooseDay));
                }
                break;
        }
    }


    public void changeFrame(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFrame(transaction);
        switch (index) {
            case 0:
                type = 0;
                if (oneFragment == null) {
                    oneFragment = new ExcelOneFragment();
                    transaction.add(R.id.get_routing2_content, oneFragment);
                } else {
                    transaction.show(oneFragment);
                }
                get_routing2_tab_one_text.setTextColor(0xff4373f1);
                get_routing2_tab_two_text.setTextColor(0xff333333);
                get_routing2_tab_one_line.setVisibility(View.VISIBLE);
                get_routing2_tab_two_line.setVisibility(View.INVISIBLE);
                break;
            case 1:
                type = 1;
                if (twoFragment == null) {
                    twoFragment = new ExcelTwoFragment();
                    transaction.add(R.id.get_routing2_content, twoFragment);
                } else {
                    transaction.show(twoFragment);
                }
                get_routing2_tab_one_text.setTextColor(0xff333333);
                get_routing2_tab_two_text.setTextColor(0xff4373f1);
                get_routing2_tab_one_line.setVisibility(View.INVISIBLE);
                get_routing2_tab_two_line.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFrame(FragmentTransaction transaction) {
        if (oneFragment != null) {
            transaction.hide(oneFragment);
        }
        if (twoFragment != null) {
            transaction.hide(twoFragment);
        }
    }
}
