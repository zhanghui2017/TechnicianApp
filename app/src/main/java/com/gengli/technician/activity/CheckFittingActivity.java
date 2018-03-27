package com.gengli.technician.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.CheckFittingAdapter;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看配件订单
 */
public class CheckFittingActivity extends Activity implements View.OnClickListener {

    private ImageView check_fitting_back_img;
    private ListView check_fitting_list_view;
    private TextView text_check_fitting_total_count;
    private CheckFittingAdapter adapter;
    private List<Fitting> fittingList;
    private int totalCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_fitting);
        fittingList = (List<Fitting>) getIntent().getSerializableExtra("cur_parts");
        init();
    }


    public void init() {
        for (int i = 0; i < fittingList.size(); i++) {
            LogUtils.showLogD("---------" + fittingList.get(i).getCount());
            Fitting f = fittingList.get(i);
            int tmpCount = f.getCount();
            totalCount += tmpCount;
        }
        check_fitting_back_img = (ImageView) findViewById(R.id.check_fitting_back_img);
        check_fitting_back_img.setOnClickListener(this);
        check_fitting_list_view = (ListView) findViewById(R.id.check_fitting_list_view);
        text_check_fitting_total_count = (TextView) findViewById(R.id.text_check_fitting_total_count);
        text_check_fitting_total_count.setText("" + totalCount);
        adapter = new CheckFittingAdapter(this, fittingList);
        check_fitting_list_view.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_fitting_back_img:
                finish();
                break;
        }
    }
}
