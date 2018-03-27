package com.gengli.technician.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.DateBean;
import com.gengli.technician.bean.Routing;
import com.gengli.technician.fragment.GetRoutingFrament;
import com.gengli.technician.fragment.NullFrament;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.listener.OnPagerChangeListener;
import com.gengli.technician.listener.OnSingleChooseListener;
import com.gengli.technician.util.CalendarUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.Util;
import com.gengli.technician.view.CalendarView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"NewApi"})
public class GetRoutingActivity extends FragmentActivity implements View.OnClickListener {
    ArrayList<Routing> routingList = null;
    private CalendarView calendarView;
    private int[] cDate = CalendarUtil.getCurrentDate();
    private ImageView get_routing_back_img;
    private TextView get_routing_add_bt_img;
    private String chooseDay;
    private TabHost.TabSpec tabSpecNull;
    private FragmentTabHost mTabHost;
    private RelativeLayout no_routing_view;
    private RelativeLayout routing_view;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_routing);
        routingList = new ArrayList<>();
        initView();
        initCalendar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRouting();
    }

    public void initView() {
//        routingList = new ArrayList<Routing>();
        no_routing_view = (RelativeLayout) findViewById(R.id.no_routing_view);
        routing_view = (RelativeLayout) findViewById(R.id.rounting_view);

        get_routing_back_img = (ImageView) findViewById(R.id.get_routing_back_img);
        get_routing_add_bt_img = (TextView) findViewById(R.id.get_routing_add_bt_img);
        get_routing_back_img.setOnClickListener(this);
        get_routing_add_bt_img.setOnClickListener(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_routing_back_img:
                finish();
                break;
            case R.id.get_routing_add_bt_img:
                Intent intent = new Intent(this, AddRoutingActivity.class);
                intent.putExtra("chooseday", chooseDay);
                int requestCode = 0;
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            type = data.getIntExtra("tmp_type", 0);
            getDateRouting(chooseDay);
        }
    }

    /**
     * 日历初始化
     */
    public void initCalendar() {
        final TextView title = (TextView) findViewById(R.id.title);
        calendarView = (CalendarView) findViewById(R.id.calendar);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("2017.10.30", "qaz");
        map.put("2017.10.1", "wsx");
        map.put("2017.11.12", "yhn");
        map.put("2017.9.15", "edc");
        map.put("2017.11.6", "rfv");
        map.put("2017.11.11", "tgb");
        calendarView.setStartEndDate("2016.1", "2028.12").setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1]).setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

        title.setText(cDate[0] + "." + cDate[1]);
        chooseDay = cDate[0] + "-" + cDate[1] + "-" + cDate[2];
        getDateRouting(chooseDay);
        LogUtils.showLogD("当前选中的日期：" + chooseDay);
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                title.setText(date[0] + "." + date[1]);
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                title.setText(date.getSolar()[0] + "." + date.getSolar()[1]);
                if (date.getType() == 1) {
                    title.setText(date.getSolar()[0] + "." + date.getSolar()[1]);
                    chooseDay = date.getSolar()[0] + "-" + date.getSolar()[1] + "-" + date.getSolar()[2];
                    getDateRouting(chooseDay);
                    LogUtils.showLogD("当前选中的日期：" + chooseDay);
                }
            }
        });
    }


    /**
     * 行程初始化
     */
    public void initRouting() {
        if (routingList.size() <= 0 || routingList == null) {
            mTabHost.clearAllTabs();
            tabSpecNull = mTabHost.newTabSpec("").setIndicator(getTextView(""));
            mTabHost.addTab(tabSpecNull, NullFrament.class, null);
        } else {
            mTabHost.clearAllTabs();
            tabSpecNull = null;

            for (int i = 0; i < routingList.size(); i++) {
                // Tab按钮添加文字和图片
                TabSpec tabSpec = mTabHost.newTabSpec(routingList.get(i).getType()).setIndicator(getTextView(i));
                // 添加Fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("test_bundle", routingList);
                mTabHost.addTab(tabSpec, GetRoutingFrament.class, bundle);
                // 设置Tab按钮的背景
                mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(android.R.color.white);

            }
            mTabHost.setCurrentTabByTag(routingList.get(0).getType()); // 设置第一次打开时默认显示的标签，
            updateTab(mTabHost);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    mTabHost.setCurrentTabByTag(tabId);
                    updateTab(mTabHost);
                }
            });
        }

    }

    public void lastMonth(View view) {
        calendarView.lastMonth();
    }

    public void nextMonth(View view) {
        calendarView.nextMonth();
    }

    /**
     * 获取选项卡显示文字
     *
     * @param index
     * @return
     */
    private View getTextView(int index) {
        View view = getLayoutInflater().inflate(R.layout.item_routing_tab_view, null);
        TextView textView = (TextView) view.findViewById(R.id.text_tab_name);
        textView.setText(routingList.get(index).getType());
        return view;
    }


    private View getTextView(String type) {
        View view = getLayoutInflater().inflate(R.layout.item_routing_tab_view, null);
        TextView textView = (TextView) view.findViewById(R.id.text_tab_name);
        textView.setText(type);
        return view;
    }

    /**
     * 更新Tab标签的颜色，和字体的颜色
     *
     * @param tabHost
     */
    private void updateTab(TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.text_tab_name);
            View view_under_line = tabHost.getTabWidget().getChildAt(i).findViewById(R.id.view_under_line);
            if (tabHost.getCurrentTab() == i) {// 选中
                tv.setTextColor(Util.setColor(this, R.color.color_order_bg_4373f1));
                view_under_line.setBackgroundResource(R.color.color_order_bg_4373f1);
            } else {// 不选中
                tv.setTextColor(Util.setColor(this, R.color.tmp_color_1));
                view_under_line.setBackgroundResource(android.R.color.transparent);
            }
        }

    }


    public void getDateRouting(String day) {
        if (routingList != null)
            routingList.clear();
        else
            routingList = new ArrayList<>();

        if (type == 0) {
            mTabHost.clearAllTabs();
            tabSpecNull = mTabHost.newTabSpec("").setIndicator(getTextView(""));
            mTabHost.addTab(tabSpecNull, NullFrament.class, null);
        }

        type = 0;
        String url = ServicePort.TRIP_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("day", day);
        AjaxParams params = api.getParam(this, map);
        params.put("day", day);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                LogUtils.showLogD("-------行程列表返回数据:" + o.toString());
                String responce = o.toString();
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            JSONObject lists = results.getJSONObject("lists");

                            if (lists != null && lists.length() > 0) {
                                handle.sendEmptyMessage(0);
                                if (lists.has("PX")) {
                                    JSONArray pxArray = lists.getJSONArray("PX");
                                    if (pxArray != null && pxArray.length() > 0) {
                                        Routing pxRouting = new Routing();
                                        pxRouting.setType("培训");
                                        pxRouting.setAddress(pxArray.getJSONObject(0).getString("location").toString());
                                        pxRouting.setJobContent(pxArray.getJSONObject(0).getString("content").toString());
                                        pxRouting.setMachine(pxArray.getJSONObject(0).getString("product_name").toString());
                                        pxRouting.setModel(pxArray.getJSONObject(0).getString("product_id").toString());
                                        pxRouting.setPhone(pxArray.getJSONObject(0).getString("customer").toString());
                                        pxRouting.setChargeName(pxArray.getJSONObject(0).getString("owner").toString());
                                        pxRouting.setTime(chooseDay);
                                        routingList.add(pxRouting);
                                        LogUtils.showLogD("pxArray   " + pxRouting.toString());
                                    }
                                }
                                if (lists.has("XJ")) {
                                    JSONArray xjArray = lists.getJSONArray("XJ");
                                    if (xjArray != null && xjArray.length() > 0) {
                                        Routing xjRouting = new Routing();
                                        xjRouting.setType("巡检");
                                        xjRouting.setAddress(xjArray.getJSONObject(0).getString("location").toString());
                                        xjRouting.setJobContent(xjArray.getJSONObject(0).getString("content").toString());
                                        xjRouting.setMachine(xjArray.getJSONObject(0).getString("product_name").toString());
                                        xjRouting.setModel(xjArray.getJSONObject(0).getString("product_id").toString());
                                        xjRouting.setPhone(xjArray.getJSONObject(0).getString("customer").toString());
                                        xjRouting.setChargeName(xjArray.getJSONObject(0).getString("owner").toString());
                                        xjRouting.setTime(chooseDay);
                                        routingList.add(xjRouting);
                                        LogUtils.showLogD("xjArray   " + xjRouting.toString());
                                    }
                                }
                                if (lists.has("WX")) {
                                    JSONArray wxArray = lists.getJSONArray("WX");
                                    if (wxArray != null && wxArray.length() > 0) {
                                        Routing wxRouting = new Routing();
                                        wxRouting.setType("维修");
                                        wxRouting.setAddress(wxArray.getJSONObject(0).getString("location").toString());
                                        wxRouting.setJobContent(wxArray.getJSONObject(0).getString("content").toString());
                                        wxRouting.setMachine(wxArray.getJSONObject(0).getString("product_name").toString());
                                        wxRouting.setModel(wxArray.getJSONObject(0).getString("product_id").toString());
                                        wxRouting.setPhone(wxArray.getJSONObject(0).getString("customer").toString());
                                        wxRouting.setChargeName(wxArray.getJSONObject(0).getString("owner").toString());
                                        wxRouting.setTime(chooseDay);
                                        routingList.add(wxRouting);
                                        LogUtils.showLogD("wxArray   " + wxRouting.toString());
                                    }
                                }
                                if (lists.has("TS")) {
                                    JSONArray tsArray = lists.getJSONArray("TS");
                                    if (tsArray != null && tsArray.length() > 0) {
                                        Routing tsRouting = new Routing();
                                        tsRouting.setType("调试");
                                        tsRouting.setAddress(tsArray.getJSONObject(0).getString("location").toString());
                                        tsRouting.setJobContent(tsArray.getJSONObject(0).getString("content").toString());
                                        tsRouting.setMachine(tsArray.getJSONObject(0).getString("product_name").toString());
                                        tsRouting.setModel(tsArray.getJSONObject(0).getString("product_id").toString());
                                        tsRouting.setPhone(tsArray.getJSONObject(0).getString("customer").toString());
                                        tsRouting.setChargeName(tsArray.getJSONObject(0).getString("owner").toString());
                                        tsRouting.setTime(chooseDay);
                                        routingList.add(tsRouting);
                                        LogUtils.showLogD("tsArray   " + tsRouting.toString());
                                    }
                                }
                            } else {
                                handle.sendEmptyMessage(2);
                                LogUtils.showCenterToast(GetRoutingActivity.this, "今日无行程");
                            }
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(1);
                        } else {
                            LogUtils.showCenterToast(GetRoutingActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                    } catch (JSONException e) {

                    }
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                initRouting();
                routing_view.setVisibility(View.VISIBLE);
                no_routing_view.setVisibility(View.INVISIBLE);
            } else if (msg.what == 1) {
                startActivity(new Intent(GetRoutingActivity.this, LoginActivity.class));
                finish();
            } else if (msg.what == 2) {
                routing_view.setVisibility(View.INVISIBLE);
                no_routing_view.setVisibility(View.VISIBLE);
            }
        }
    };
}
