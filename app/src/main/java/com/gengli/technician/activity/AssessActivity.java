package com.gengli.technician.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.fragment.StepOneFragment;
import com.gengli.technician.fragment.StepThreeFragment;
import com.gengli.technician.fragment.StepTwoFragment;

public class AssessActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout tab_one_bt, tab_two_bt, tab_three_bt;
    private TextView tab_one_text, tab_two_text, tab_three_text;
    private View tab_one_line, tab_two_line, tab_three_line;

    private StepOneFragment oneFragment;
    private StepTwoFragment twoFragment;
    private StepThreeFragment threeFragment;
    private FragmentManager fragmentManager;
    private ImageView assess_back_bt;
    private String chooseDay;
    private String month;
    private String day;
    private TextView assess_month_text, assess_day_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess);
        chooseDay = getIntent().getStringExtra("choose_day");

        fragmentManager = getSupportFragmentManager();
        initView();
        changeFrame(0);

        if (!TextUtils.isEmpty(chooseDay)) {
            String[] str = chooseDay.split("-");
            month = str[1];
            day = str[2];
            assess_month_text.setText(month);
            assess_day_text.setText(day);
        }
    }

    public String getDay() {
        return chooseDay;
    }

    private void initView() {
        assess_month_text = (TextView) findViewById(R.id.assess_month_text);
        assess_day_text = (TextView) findViewById(R.id.assess_day_text);
        tab_one_bt = (RelativeLayout) findViewById(R.id.tab_one_bt);
        tab_two_bt = (RelativeLayout) findViewById(R.id.tab_two_bt);
        tab_three_bt = (RelativeLayout) findViewById(R.id.tab_three_bt);
        assess_back_bt = (ImageView) findViewById(R.id.assess_back_bt);
        assess_back_bt.setOnClickListener(this);
        tab_one_bt.setOnClickListener(this);
        tab_two_bt.setOnClickListener(this);
        tab_three_bt.setOnClickListener(this);
        tab_one_text = (TextView) findViewById(R.id.tab_one_text);
        tab_two_text = (TextView) findViewById(R.id.tab_two_text);
        tab_three_text = (TextView) findViewById(R.id.tab_three_text);
        tab_one_line = findViewById(R.id.tab_one_line);
        tab_two_line = findViewById(R.id.tab_two_line);
        tab_three_line = findViewById(R.id.tab_three_line);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_one_bt:
                twoFragment.setData();
                changeFrame(0);
                break;
            case R.id.tab_two_bt:
                oneFragment.setData();
                changeFrame(1);
                break;
            case R.id.tab_three_bt:
                oneFragment.setData();
                twoFragment.setData();
                changeFrame(2);
                break;
            case R.id.assess_back_bt:
                finish();
                break;
        }
    }


    public void changeFrame(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFrame(transaction);
        switch (index) {
            case 0:
                if (oneFragment == null) {
                    oneFragment = new StepOneFragment();
                    transaction.add(R.id.assess_content, oneFragment);
                } else {
                    transaction.show(oneFragment);
                }
                tab_one_text.setTextColor(0xff4373f1);
                tab_two_text.setTextColor(0xff333333);
                tab_three_text.setTextColor(0xff333333);
                tab_one_line.setVisibility(View.VISIBLE);
                tab_two_line.setVisibility(View.INVISIBLE);
                tab_three_line.setVisibility(View.INVISIBLE);
                break;
            case 1:
                if (twoFragment == null) {
                    twoFragment = new StepTwoFragment();
                    transaction.add(R.id.assess_content, twoFragment);
                } else {
                    transaction.show(twoFragment);
                }
                tab_one_text.setTextColor(0xff333333);
                tab_two_text.setTextColor(0xff4373f1);
                tab_three_text.setTextColor(0xff333333);
                tab_one_line.setVisibility(View.INVISIBLE);
                tab_two_line.setVisibility(View.VISIBLE);
                tab_three_line.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if (threeFragment == null) {
                    threeFragment = new StepThreeFragment();
                    transaction.add(R.id.assess_content, threeFragment);
                } else {
                    transaction.show(threeFragment);
                }
                tab_one_text.setTextColor(0xff333333);
                tab_two_text.setTextColor(0xff333333);
                tab_three_text.setTextColor(0xff4373f1);
                tab_one_line.setVisibility(View.INVISIBLE);
                tab_two_line.setVisibility(View.INVISIBLE);
                tab_three_line.setVisibility(View.VISIBLE);
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
        if (threeFragment != null) {
            transaction.hide(threeFragment);
        }
    }
}
