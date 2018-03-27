package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gengli.technician.R;
import com.gengli.technician.adapter.FittingInfoAdapter;
import com.gengli.technician.adapter.RelatedFittingAdapter;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.StringUtil;
import com.gengli.technician.view.HorizontalListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderIngActivity extends Activity implements View.OnClickListener {

    private TextView order_ing_id_text;
    private TextView order_ing_level_text;
    private TextView order_ing_time_text;
    private TextView order_ing_machine_text;
    private TextView order_ing_model_text;
    private TextView order_ing_buy_time_text;
    private TextView order_ing_trouble_text;
    private TextView order_ing_charge_text;
    private TextView order_ing_name_phone_text;
    private TextView order_ing_company_text;
    private TextView order_ing_address_text;
    private TextView order_ing_express_address_text;
    private TextView order_sign_address_text;
    private HorizontalListView order_ing_fitting_horizontal_list;
    private ListView order_ing_fitting_list;
    private TextView order_ing_total_price_text;

    private ImageView order_ing_shenqing_bt;
    private ImageView order_ing_ckdd_bt;
    private ImageView order_ing_sign_bt;
    private ImageView order_ing_djck_bt;
    private ImageView order_back_img;
    private Order order;

    private List<Fitting> list;
    private List<Fitting> fittingInfos;
    private RelatedFittingAdapter adapter;
    private FittingInfoAdapter fittingInfoAdapter;
    private String signAddress;
    private TextView order_fitting_more_bt;
    private TextView bt_order_ing_fitting_commit;
    private ScrollView tmp_scrollview;
    private RelativeLayout order_ing_fitting_info;
    private LinearLayout order_ing_fitting_more;


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    //public AMapLocationListener mLocationListener;
    boolean isHavePart = false;
    private int totalPrice = 0;
    private String order_id;
    private SharedPreferences preferences;

    private boolean isSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ing);
        list = new ArrayList<>();
        fittingInfos = new ArrayList<>();
        order = (Order) getIntent().getSerializableExtra("ING_ORDER");
        getRepairDetail();
        init();
        preferences = getSharedPreferences("order_sign", Activity.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String address = preferences.getString(order.getId(), "");
        if (!TextUtils.isEmpty(address)) {
            isSign = true;
            order_sign_address_text.setVisibility(View.VISIBLE);
            order_sign_address_text.setText(address);
            order_ing_sign_bt.setImageResource(R.drawable.order_sign_ok_img);
        }
    }

    public void init() {
        order_fitting_more_bt = (TextView) findViewById(R.id.order_fitting_more_bt);
        order_fitting_more_bt.setOnClickListener(this);
        order_ing_id_text = (TextView) findViewById(R.id.order_ing_id_text);
        order_ing_level_text = (TextView) findViewById(R.id.order_ing_level_text);
        order_ing_time_text = (TextView) findViewById(R.id.order_ing_time_text);
        order_ing_machine_text = (TextView) findViewById(R.id.order_ing_machine_text);
        order_ing_model_text = (TextView) findViewById(R.id.order_ing_model_text);
        order_ing_buy_time_text = (TextView) findViewById(R.id.order_ing_buy_time_text);
        order_ing_trouble_text = (TextView) findViewById(R.id.order_ing_trouble_text);
        order_ing_company_text = (TextView) findViewById(R.id.order_ing_company_text);
        order_ing_address_text = (TextView) findViewById(R.id.order_ing_address_text);
        order_ing_express_address_text = (TextView) findViewById(R.id.order_ing_express_address_text);
        order_ing_name_phone_text = (TextView) findViewById(R.id.order_ing_name_phone_text);
        order_ing_charge_text = (TextView) findViewById(R.id.order_ing_charge_text);
        order_sign_address_text = (TextView) findViewById(R.id.order_sign_address_text);
        order_ing_shenqing_bt = (ImageView) findViewById(R.id.order_ing_shenqing_bt);
        order_ing_ckdd_bt = (ImageView) findViewById(R.id.order_ing_ckdd_bt);
        order_ing_sign_bt = (ImageView) findViewById(R.id.order_ing_sign_bt);
        order_ing_djck_bt = (ImageView) findViewById(R.id.order_ing_ckjl_bt);
        order_back_img = (ImageView) findViewById(R.id.order_back_img);
        bt_order_ing_fitting_commit = (TextView) findViewById(R.id.bt_order_ing_fitting_commit);
        order_ing_fitting_list = (ListView) findViewById(R.id.order_ing_fitting_list);
        order_ing_total_price_text = (TextView) findViewById(R.id.order_ing_total_price_text);
        tmp_scrollview = (ScrollView) findViewById(R.id.tmp_scrollview);
        tmp_scrollview.setFocusable(true);
        tmp_scrollview.setFocusableInTouchMode(true);
        tmp_scrollview.requestFocus();
        order_ing_fitting_info = (RelativeLayout) findViewById(R.id.order_ing_fitting_info);
        order_ing_fitting_more = (LinearLayout) findViewById(R.id.order_ing_fitting_more);

        order_back_img.setOnClickListener(this);
        bt_order_ing_fitting_commit.setOnClickListener(this);
        order_ing_djck_bt.setOnClickListener(this);
        order_ing_sign_bt.setOnClickListener(this);
        order_ing_ckdd_bt.setOnClickListener(this);
        order_ing_shenqing_bt.setOnClickListener(this);

        order_ing_fitting_horizontal_list = (HorizontalListView) findViewById(R.id.order_ing_fitting_horizontal_list);
        adapter = new RelatedFittingAdapter(this, list);
        order_ing_fitting_horizontal_list.setAdapter(adapter);
        order_ing_fitting_horizontal_list.setOnItemClickListener(new FittingHorizontalListItemClick());

        fittingInfoAdapter = new FittingInfoAdapter(this, list);
        order_ing_fitting_list.setAdapter(fittingInfoAdapter);
        order_ing_fitting_list.setOnItemClickListener(new FittingListItemClick());
        order_ing_fitting_list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    tmp_scrollview.requestDisallowInterceptTouchEvent(false);
                } else {
                    tmp_scrollview.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        order_ing_id_text.setText(order.getId());
        order_ing_level_text.setText(order.getLevel());
        order_ing_time_text.setText(order.getTime());
        order_ing_machine_text.setText(order.getMachine());
        order_ing_model_text.setText(order.getModel());
        order_ing_trouble_text.setText(order.getTrouble());
        order_ing_company_text.setText(order.getCompany());
        order_ing_address_text.setText(order.getAddress());
        order_ing_express_address_text.setText(order.getExpressAddress());
        order_ing_name_phone_text.setText(order.getName() + " " + order.getPhone());
        order_ing_charge_text.setText(order.getChargeName() + " " + order.getChargePhone());

//        String model = order.getModel();
//        String date = "20" + StringUtil.subString(model, 1) + StringUtil.subString(model, 3) + "-" + StringUtil.subString(model, 2) + StringUtil.subString(model, 2);
//        order_ing_buy_time_text.setText(date);
    }


    public class FittingListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    public class FittingHorizontalListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_ing_ckjl_bt:
//                startActivity(new Intent(this, RepairsActivity.class));
                Intent intent2 = new Intent(this, RepairsActivity.class);
                intent2.putExtra("ORDER_PRODUCT_ID", order.getMachine());
                startActivity(intent2);
                break;

            case R.id.order_ing_sign_bt:
                if (isSign) {
                    return;
                }
                initLocation();
                break;
            case R.id.order_ing_ckdd_bt:
                if (isHavePart) {
                    Intent intent = new Intent(this, CheckFittingActivity.class);
                    intent.putExtra("cur_parts", (Serializable) list);
                    startActivity(intent);
                } else {
                    LogUtils.showCenterToast(this, "该订单没有申请配件");
                }
                break;
            case R.id.order_ing_shenqing_bt:
                Intent intent1 = new Intent(this, GetFittingActivity.class);
                intent1.putExtra("order_rid", order.getId());
                startActivity(intent1);
                break;
            case R.id.order_back_img:
                finish();
                break;
            case R.id.order_fitting_more_bt:
                Intent intent3 = new Intent(this, MoreFittingActivity.class);
                intent3.putExtra("order_rid", order.getId());
                startActivity(intent3);
                break;
            case R.id.bt_order_ing_fitting_commit:
                Intent intent = new Intent(this, CommitActivity.class);
                intent.putExtra("OK_ORDER", order);
                intent.putExtra("order_buy_time", buy_period);
                intent.putExtra("order_id", order_id);
                intent.putExtra("order_isHavePart", isHavePart);
                if (isHavePart) {
                    StringBuilder fits = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        fits.append(" " + list.get(i).getName());
                    }
                    intent.putExtra("order_fit_list", fits.toString());
                }

                startActivity(intent);
                finish();
                break;
        }
    }


    /**
     * 定位
     */
    public void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setApiKey("690bd4df16cf5b81f297a518d696e05e");
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//                    Log.d("test", "location ：" + aMapLocation.getAddress());
//                    Log.d("test", "city ：" + aMapLocation.getCity());
                        signAddress = aMapLocation.getAddress();
                        sign(signAddress);
//                        order_sign_address_text.setVisibility(View.VISIBLE);
//                        order_sign_address_text.setText(signAddress);
//                        order_ing_sign_bt.setImageResource(R.drawable.order_sign_ok_img);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("test", "location "
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                } else {
                    Log.e("test", "aMapLocation is null");
                }
            }
        });
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        } else {
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    private void sign(String loc) {
        String url = ServicePort.REPAIR_SIGN;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        LogUtils.showLogD("----------------" + order.getId());
        map.put("rid", order.getId());
        map.put("address", loc);
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        params.put("address", loc);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->签到返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handler.sendEmptyMessage(10);
                            LogUtils.showCenterToast(OrderIngActivity.this, "签到成功");
                        } else {
                            LogUtils.showCenterToast(OrderIngActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private String buy_period;

    private void getRepairDetail() {
        String url = ServicePort.REPAIR_DETAIL;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("rid", order.getId());
        AjaxParams params = api.getParam(this, map);
        params.put("rid", order.getId());
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->维修详情返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            buy_period = results.getString("buy_period");
                            order_id = results.getString("order_id");
                            totalPrice = results.getInt("parts_total");
                            handler.sendEmptyMessage(2);
                            JSONArray parts = results.getJSONArray("parts");
                            if (parts.length() > 0) {
                                for (int i = 0; i < parts.length(); i++) {
                                    JSONObject item = parts.getJSONObject(i);
                                    Fitting f = new Fitting();
                                    f.setId(String.valueOf(item.getInt("id")));
                                    f.setName(item.getString("name"));
                                    f.setCount(item.getInt("amount"));
                                    f.setPrice(item.getString("price"));
                                    f.setImgUrl(item.getString("thumb"));
                                    list.add(f);
                                }
                                LogUtils.showLogD(".................." + totalPrice);
                                isHavePart = true;
                                handler.sendEmptyMessage(1);
                                fittingInfoAdapter.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();
                            } else {
                                isHavePart = false;
                                handler.sendEmptyMessage(0);
                            }

                        } else {
                            LogUtils.showCenterToast(OrderIngActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                order_ing_fitting_info.setVisibility(View.GONE);
                order_ing_fitting_more.setVisibility(View.GONE);
            } else if (msg.what == 10) {
                isSign = true;
                order_sign_address_text.setVisibility(View.VISIBLE);
                order_sign_address_text.setText(signAddress);
                order_ing_sign_bt.setImageResource(R.drawable.order_sign_ok_img);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(order.getId(), signAddress);
                editor.commit();

            } else if (msg.what == 1) {

            } else if (msg.what == 2) {
                order_ing_buy_time_text.setText(buy_period);
                order_ing_model_text.setText(order_id);
                order_ing_total_price_text.setText(totalPrice + "");
            }
        }
    };

}