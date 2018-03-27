package com.gengli.technician.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.AddImgListAdapter;
import com.gengli.technician.adapter.AddVideoListAdapter;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.CommandPhotoUtil;
import com.gengli.technician.util.CommandVideoUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.PhotoSystemOrShoot;
import com.gengli.technician.util.SharePreferencesUtil;
import com.gengli.technician.util.StringUtil;
import com.gengli.technician.util.VideoSystem;
import com.gengli.technician.view.RepairListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维修详情
 */
public class CommitActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private TextView order_commit_id_text;
    private TextView order_commit_level_text;
    private TextView order_commit_start_time_text;
    private TextView order_commit_machine_text;
    private TextView order_commit_trouble_text;
    private TextView order_commit_fitting_text;
    private TextView order_commit_name_phone_text;
    private TextView order_commit_model_text;
    private TextView order_commit_buy_time_text;
    private TextView order_commit_company_text;
    private TextView order_commit_address_text;
    private TextView order_commit_express_address_text;
    private TextView order_commit_charge_name_phone_text;

    private TextView text_desc_bt;
    //    private EditText edit_desc_view;
    private TextView text_report_bt;
    private EditText edit_report_view;
    private TextView text_order_commit_bt;
    private ImageView order_commit_back_img;
    private AddImgListAdapter beginAdapter;
    private AddImgListAdapter afterAdapter;
    private AddVideoListAdapter beginVideoAdapter;
    private AddVideoListAdapter afterVideoAdapter;
    private RepairListView order_commit_begin_img_list;
    private RepairListView order_commit_after_img_list;
    private RepairListView order_commit_begin_video_list;
    private RepairListView order_commit_after_video_list;
    private List<String> beginImgs;
    private List<String> afterImgs;
    private List<String> beginVideos;
    private List<String> afterVideos;
    private Order order;
    private String buy_period;
    private String order_id;
    private boolean isHavePart;
    private String fits;
    private PhotoSystemOrShoot selectPhoto;
    private PhotoSystemOrShoot selectPhoto2;
    private VideoSystem selectVideo;
    private VideoSystem selectVideo2;
    private CommandPhotoUtil commandPhoto;
    private CommandPhotoUtil commandPhoto2;
    private CommandVideoUtil commandVideo;
    private CommandVideoUtil commandVideo2;
    private LinearLayout click1;
    private LinearLayout click2;
    private LinearLayout click3;
    private LinearLayout click4;
    private int type = 0;
    //    private boolean isDescEditShow = false;
    private boolean isReportEditShow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        order = (Order) getIntent().getSerializableExtra("OK_ORDER");
        buy_period = getIntent().getStringExtra("order_buy_time");
        order_id = getIntent().getStringExtra("order_id");
        isHavePart = getIntent().getBooleanExtra("order_isHavePart", false);
        fits = getIntent().getStringExtra("order_fit_list");
        init();
        addPlus();
        addPlus2();
        addVideoPlus();
        addVideoPlus2();
    }

    public void init() {
        order_commit_back_img = (ImageView) findViewById(R.id.order_commit_back_img);
        order_commit_back_img.setOnClickListener(this);
        click1 = (LinearLayout) findViewById(R.id.click_1);
        click2 = (LinearLayout) findViewById(R.id.click_2);
        click3 = (LinearLayout) findViewById(R.id.click_3);
        click4 = (LinearLayout) findViewById(R.id.click_4);

        order_commit_id_text = (TextView) findViewById(R.id.order_commit_id_text);
        order_commit_level_text = (TextView) findViewById(R.id.order_commit_level_text);
        order_commit_start_time_text = (TextView) findViewById(R.id.order_commit_start_time_text);
        order_commit_machine_text = (TextView) findViewById(R.id.order_commit_machine_text);
        order_commit_trouble_text = (TextView) findViewById(R.id.order_commit_trouble_text);
        order_commit_fitting_text = (TextView) findViewById(R.id.order_commit_fitting_text);
        order_commit_name_phone_text = (TextView) findViewById(R.id.order_commit_name_phone_text);
        order_commit_company_text = (TextView) findViewById(R.id.order_commit_company_text);
        order_commit_address_text = (TextView) findViewById(R.id.order_commit_address_text);
        order_commit_express_address_text = (TextView) findViewById(R.id.order_commit_express_address_text);
        order_commit_charge_name_phone_text = (TextView) findViewById(R.id.order_commit_charge_name_phone_text);
        order_commit_model_text = (TextView) findViewById(R.id.order_commit_model_text);

        text_desc_bt = (TextView) findViewById(R.id.text_desc_bt);
        text_report_bt = (TextView) findViewById(R.id.text_report_bt);
//        edit_desc_view = (EditText) findViewById(R.id.edit_desc_view);
        edit_report_view = (EditText) findViewById(R.id.edit_report_view);
        text_order_commit_bt = (TextView) findViewById(R.id.text_order_commit_bt);
        order_commit_buy_time_text = (TextView) findViewById(R.id.order_commit_buy_time_text);

        text_desc_bt.setOnClickListener(this);
        text_report_bt.setOnClickListener(this);
        text_order_commit_bt.setOnClickListener(this);

        order_commit_id_text.setText(order.getId());
        order_commit_level_text.setText(order.getLevel());
        order_commit_start_time_text.setText(order.getTime());
        order_commit_machine_text.setText(order.getMachine());
        order_commit_trouble_text.setText(order.getTrouble());
        if (isHavePart) {
            order_commit_fitting_text.setText(fits);
        } else {
            order_commit_fitting_text.setText("无需配件");
        }
        order_commit_name_phone_text.setText(order.getName() + " " + order.getPhone());
        order_commit_model_text.setText(order_id);
        order_commit_buy_time_text.setText(buy_period);
        order_commit_address_text.setText(order.getAddress());
        order_commit_express_address_text.setText(order.getExpressAddress());
        order_commit_charge_name_phone_text.setText(order.getChargeName() + " " + order.getChargePhone());
        order_commit_company_text.setText(order.getCompany());


        order_commit_begin_img_list = (RepairListView) findViewById(R.id.order_commit_begin_img_list);
        order_commit_after_img_list = (RepairListView) findViewById(R.id.order_commit_after_img_list);
        order_commit_begin_video_list = (RepairListView) findViewById(R.id.order_commit_begin_video_list);
        order_commit_after_video_list = (RepairListView) findViewById(R.id.order_commit_after_video_list);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (isTouchPointInView(click1, x, y)) {
            type = 1;
            return super.dispatchTouchEvent(ev);
        }
        if (isTouchPointInView(click2, x, y)) {
            type = 2;
        }
        if (isTouchPointInView(click3, x, y)) {
            type = 3;
        }
        if (isTouchPointInView(click4, x, y)) {
            type = 4;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_commit_back_img:
                finish();
                break;

            case R.id.text_desc_bt:
                Intent intent = new Intent(this, OrderDescActivity.class);
                intent.putExtra("commit_order", order);
                startActivity(intent);
                break;

            case R.id.text_report_bt:
                text_report_bt.setVisibility(View.GONE);
                edit_report_view.setVisibility(View.VISIBLE);
                isReportEditShow = true;
                break;
            case R.id.text_order_commit_bt:
                commitOrder();
                break;
        }
    }

    private ProgressDialog dialog;

    public void commitOrder() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在上传...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        String url = ServicePort.REPAIR_FINISH;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("rid", order.getId());
        map.put("content", edit_report_view.getText().toString());
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        params.put("content", edit_report_view.getText().toString());
        try {
            if (beginAdapter.imageItemData.size() > 0) {
                for (int i = 0; i < beginAdapter.imageItemData.size(); i++) {
                    params.put("image_" + i + "_before", new File(beginAdapter.imageItemData.get(i)));
                }
            }
            if (afterAdapter.imageItemData.size() > 0) {
                for (int i = 0; i < afterAdapter.imageItemData.size(); i++) {
                    params.put("image_" + i + "_after", new File(afterAdapter.imageItemData.get(i)));
                }
            }
            if (beginVideoAdapter.videoItemData.size() > 0) {
                params.put("video_before", new File(beginVideoAdapter.videoItemData.get(0)));
            }
            if (afterVideoAdapter.videoItemData.size() > 0) {
                params.put("video_after", new File(afterVideoAdapter.videoItemData.get(0)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        api.Post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onLoading(long count, long current) {
//                super.onLoading(count, current);
//                int rate = (int) ((current * 1.0 / count) * 100);
//                LogUtils.showLogD("--------提交订单  onLoading:" + current);
//                LogUtils.showLogD("--------提交订单  onLoading:" + count);
//                dialog.setProgress(rate);

            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String responce = o.toString();
                LogUtils.showLogD("--------提交订单返回数据:" + o.toString());
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            LogUtils.showCenterToast(CommitActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("yeya");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("dianqi");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("jixie");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("bengsong");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("suningji");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("dipan");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("runhua");
                SharePreferencesUtil.getInstance(CommitActivity.this, "order_desc").removeItem("qita");
                dialog.cancel();
                finish();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isReportEditShow) {
                text_report_bt.setVisibility(View.VISIBLE);
                edit_report_view.setVisibility(View.GONE);
                isReportEditShow = false;
                return true;
            }
            return super.onKeyDown(keyCode, event);
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void addVideoPlus2() {
        afterVideoAdapter = new AddVideoListAdapter(this, 1);
        order_commit_after_video_list.setAdapter(afterVideoAdapter);

        // 选择图片获取途径
        selectVideo2 = new VideoSystem(this) {
            @Override
            public void onStartActivityForResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        };
        commandVideo2 = new CommandVideoUtil(this, order_commit_after_video_list, afterVideoAdapter, selectVideo2);
    }


    private void addVideoPlus() {
        beginVideoAdapter = new AddVideoListAdapter(this, 1);
        order_commit_begin_video_list.setAdapter(beginVideoAdapter);

        // 选择图片获取途径
        selectVideo = new VideoSystem(this) {
            @Override
            public void onStartActivityForResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        };
        commandVideo = new CommandVideoUtil(this, order_commit_begin_video_list, beginVideoAdapter, selectVideo);
    }

    /**
     * 实例化组件
     */
    private void addPlus() {
        beginAdapter = new AddImgListAdapter(this, 3);
        order_commit_begin_img_list.setAdapter(beginAdapter);

        // 选择图片获取途径
        selectPhoto = new PhotoSystemOrShoot(this) {
            @Override
            public void onStartActivityForResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        };
        commandPhoto = new CommandPhotoUtil(this, order_commit_begin_img_list, beginAdapter, selectPhoto);
    }


    private void addPlus2() {

        afterAdapter = new AddImgListAdapter(this, 3);
        order_commit_after_img_list.setAdapter(afterAdapter);

        // 选择图片获取途径
        selectPhoto2 = new PhotoSystemOrShoot(this) {
            @Override
            public void onStartActivityForResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        };
        commandPhoto2 = new CommandPhotoUtil(this, order_commit_after_img_list, afterAdapter, selectPhoto2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (type == 1) {
            if (selectPhoto != null) {
                String photoPath = selectPhoto.getPhotoResultPath(requestCode, resultCode, data);
                LogUtils.showLogD("--->photo save path 1== " + photoPath);
                if (!TextUtils.isEmpty(photoPath)) {
                    commandPhoto.showGridPhoto(photoPath);
                }
            }
            if (beginAdapter.getCount() == 0) {
                beginAdapter.setClearImgShow(false);
            }
        } else if (type == 2) {
            if (selectPhoto2 != null) {
                String photoPath = selectPhoto2.getPhotoResultPath(requestCode, resultCode, data);
                LogUtils.showLogD("--->photo save path 2== " + photoPath);
                if (!TextUtils.isEmpty(photoPath)) {
                    commandPhoto2.showGridPhoto(photoPath);
                }
            }
            if (beginAdapter.getCount() == 0) {
                beginAdapter.setClearImgShow(false);
            }
        } else if (type == 3) {
            if (selectVideo != null) {
                String photoPath = selectVideo.getPhotoResultPath(requestCode, resultCode, data);
                if (!TextUtils.isEmpty(photoPath)) {
                    commandVideo.showGridPhoto(photoPath);
                }
            }
            if (beginVideoAdapter.getCount() == 0) {
                beginVideoAdapter.setClearImgShow(false);
            }
        } else if (type == 4) {
            if (selectVideo2 != null) {
                String photoPath = selectVideo2.getPhotoResultPath(requestCode, resultCode, data);
                if (!TextUtils.isEmpty(photoPath)) {
                    commandVideo2.showGridPhoto(photoPath);
                }
            }
            if (afterVideoAdapter.getCount() == 0) {
                afterVideoAdapter.setClearImgShow(false);
            }
        }


    }
}
