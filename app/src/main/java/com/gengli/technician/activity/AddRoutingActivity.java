package com.gengli.technician.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Routing;
import com.gengli.technician.fragment.AddRoutingFrament;
import com.gengli.technician.fragment.NullFrament;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SharePreferencesUtil;
import com.gengli.technician.util.Util;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class AddRoutingActivity extends FragmentActivity implements OnClickListener {
    public static final String ROUTING_PEIXUN = "routing_peixun";
    public static final String ROUTING_XUNJIAN = "routing_xunjian";
    public static final String ROUTING_WEIXIU = "routing_weixiu";
    public static final String ROUTING_TIAOSHI = "routing_tiaoshi";

    public static final String[] types = {"培训", "巡检", "维修", "调试"};
    private ImageView img_add_routing_peixun;
    private ImageView img_add_routing_xunjian;
    private ImageView img_add_routing_weixiu;
    private ImageView img_add_routing_tiaoshi;
    private TextView text_add_routing_commit_bt;
    private boolean isOneSel = false;
    private boolean isTwoSel = false;
    private boolean isThreeSel = false;
    private boolean isFourSel = false;
    private FragmentTabHost mTabHost;
    private TabHost.TabSpec tabSpecNull;
    private List<String> tablist;
    public boolean isEditShow = false;
    private String chooseDay;
    private Routing currentRouting;

    private JSONObject curObj;
    private JSONObject pxObj;
    private JSONObject xjObj;
    private JSONObject wxObj;
    private JSONObject tsObj;

//    public Routing getRouting() {
//        return routing;
//    }
//
//    public void setRouting(Routing routing) {
//        this.routing = routing;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routing);
        Intent intent = getIntent();
        chooseDay = intent.getStringExtra("chooseday");
        tablist = new ArrayList<String>();
        text_add_routing_commit_bt = (TextView) findViewById(R.id.text_add_routing_commit_bt);
        img_add_routing_peixun = (ImageView) findViewById(R.id.img_add_routing_peixun);
        img_add_routing_xunjian = (ImageView) findViewById(R.id.img_add_routing_xunjian);
        img_add_routing_weixiu = (ImageView) findViewById(R.id.img_add_routing_weixiu);
        img_add_routing_tiaoshi = (ImageView) findViewById(R.id.img_add_routing_tiaoshi);
        img_add_routing_peixun.setOnClickListener(this);
        img_add_routing_xunjian.setOnClickListener(this);
        img_add_routing_weixiu.setOnClickListener(this);
        img_add_routing_tiaoshi.setOnClickListener(this);
        text_add_routing_commit_bt.setOnClickListener(this);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.getTabWidget().setBackgroundResource(android.R.color.white);
        tabSpecNull = mTabHost.newTabSpec("").setIndicator(getTextView(""));
        mTabHost.addTab(tabSpecNull, NullFrament.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mTabHost.setCurrentTabByTag(tabId);
                updateTab(mTabHost, true);
                Log.d("test", "-----onTabChange-----" + tabId);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isEditShow) {
                showNormalDialog();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
     * @setTitle 设置对话框标题
     *
     * @setMessage 设置对话框消息提示 setXXX方法返回Dialog对象，因此可以链式设置属性
     */
    private void showNormalDialog() {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        normalDialog.setTitle("提示：");
        normalDialog.setMessage("输入内容未保存，请谨慎退出！");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        normalDialog.show();
    }

    private View getTextView(String type) {
        View view = getLayoutInflater().inflate(R.layout.item_routing_tab_view, null);
        TextView textView = (TextView) view.findViewById(R.id.text_tab_name);
        textView.setText(type);
        return view;
    }

    private View getTextView(int index) {
        View view = getLayoutInflater().inflate(R.layout.item_routing_tab_view, null);
        TextView textView = (TextView) view.findViewById(R.id.text_tab_name);
        textView.setText(tablist.get(index));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_routing_peixun:
                if (isOneSel) {
                    removeTab(types[0]);
                    img_add_routing_peixun.setSelected(false);
                    isOneSel = false;
                } else {
                    tablist.add(types[0]);
                    img_add_routing_peixun.setSelected(true);
                    isOneSel = true;
                    if (tabSpecNull != null) {
                        mTabHost.setCurrentTab(0);
                        mTabHost.clearAllTabs();
                        tabSpecNull = null;
                    }
                    TabHost.TabSpec tabOne = mTabHost.newTabSpec(types[0]).setIndicator(getTextView(types[0]));
                    Bundle bundle = new Bundle();
                    bundle.putString("test_day", chooseDay);
                    mTabHost.addTab(tabOne, AddRoutingFrament.class, bundle);
                    mTabHost.setCurrentTabByTag(types[0]);
                    updateTab(mTabHost, true);
                }
                break;
            case R.id.img_add_routing_xunjian:
                if (isTwoSel) {
                    removeTab(types[1]);
                    img_add_routing_xunjian.setSelected(false);
                    isTwoSel = false;

                } else {
                    tablist.add(types[1]);
                    img_add_routing_xunjian.setSelected(true);
                    isTwoSel = true;
                    if (tabSpecNull != null) {
                        mTabHost.setCurrentTab(0);
                        mTabHost.clearAllTabs();
                        tabSpecNull = null;
                    }
                    TabHost.TabSpec tabTwo = mTabHost.newTabSpec(types[1]).setIndicator(getTextView(types[1]));
                    Bundle bundle = new Bundle();
                    bundle.putString("test_day", chooseDay);
                    mTabHost.addTab(tabTwo, AddRoutingFrament.class, bundle);
                    mTabHost.setCurrentTabByTag(types[1]);
                    updateTab(mTabHost, true);
                }
                break;
            case R.id.img_add_routing_weixiu:
                if (isThreeSel) {
                    removeTab(types[2]);
                    img_add_routing_weixiu.setSelected(false);
                    isThreeSel = false;
                } else {
                    tablist.add(types[2]);
                    img_add_routing_weixiu.setSelected(true);
                    isThreeSel = true;
                    if (tabSpecNull != null) {
                        mTabHost.setCurrentTab(0);
                        mTabHost.clearAllTabs();
                        tabSpecNull = null;
                    }
                    TabHost.TabSpec tabThree = mTabHost.newTabSpec(types[2]).setIndicator(getTextView(types[2]));
                    Bundle bundle = new Bundle();
                    bundle.putString("test_day", chooseDay);
                    mTabHost.addTab(tabThree, AddRoutingFrament.class, bundle);
                    mTabHost.setCurrentTabByTag(types[2]);
                    updateTab(mTabHost, true);
                }
                break;
            case R.id.img_add_routing_tiaoshi:
                if (isFourSel) {
                    removeTab(types[3]);
                    img_add_routing_tiaoshi.setSelected(false);
                    isFourSel = false;
                } else {
                    tablist.add(types[3]);
                    img_add_routing_tiaoshi.setSelected(true);
                    isFourSel = true;
                    if (tabSpecNull != null) {
                        mTabHost.setCurrentTab(0);
                        mTabHost.clearAllTabs();
                        tabSpecNull = null;
                    }
                    TabHost.TabSpec tabFour = mTabHost.newTabSpec(types[3]).setIndicator(getTextView(types[3]));
                    Bundle bundle = new Bundle();
                    bundle.putString("test_day", chooseDay);
                    mTabHost.addTab(tabFour, AddRoutingFrament.class, bundle);
                    mTabHost.setCurrentTabByTag(types[3]);
                    updateTab(mTabHost, true);
                }
                break;
            case R.id.text_add_routing_commit_bt:
                if (tablist.size() == 1) {
                    AddRoutingFrament fg = (AddRoutingFrament) getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
                    currentRouting = fg.getData();
                    try {
                        curObj = new JSONObject();
                        curObj.put("day", chooseDay);
                        curObj.put("location", currentRouting.getAddress());
                        curObj.put("content", currentRouting.getJobContent());
                        curObj.put("product_id", currentRouting.getModel());
                        curObj.put("product_name", currentRouting.getMachine());
                        curObj.put("customer", currentRouting.getPhone());
                        curObj.put("owner", currentRouting.getChargeName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Log.d("test", "-----routing-----" + currentRouting.toString());
                    addRouting();
                } else if (tablist.size() > 1) {
                    AddRoutingFrament fg = (AddRoutingFrament) getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
                    currentRouting = fg.getData();
                    try {
                        curObj = new JSONObject();
                        curObj.put("day", chooseDay);
                        curObj.put("location", currentRouting.getAddress());
                        curObj.put("content", currentRouting.getJobContent());
                        curObj.put("product_id", currentRouting.getModel());
                        curObj.put("product_name", currentRouting.getMachine());
                        curObj.put("customer", currentRouting.getPhone());
                        curObj.put("owner", currentRouting.getChargeName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Log.d("test", "-----多个界面当前显示的   -----" + currentRouting.toString());
                    if (tablist.contains(types[0])) {
                        if (!currentRouting.getType().equals(types[0])) {
                            SharedPreferences preferences = getSharedPreferences(AddRoutingActivity.ROUTING_PEIXUN, Activity.MODE_PRIVATE);
                            String address = preferences.getString(AddRoutingFrament.ADDRESS, "");
                            String jobContent = preferences.getString(AddRoutingFrament.JOBCONTENT, "");
                            String machine = preferences.getString(AddRoutingFrament.MACHINE, "");
                            String model = preferences.getString(AddRoutingFrament.MODEL, "");
                            String namePhone = preferences.getString(AddRoutingFrament.NAMEPHONE, "");
                            String chargeName = preferences.getString(AddRoutingFrament.CHARGENAME, "");
                            String type = preferences.getString(AddRoutingFrament.TYPE, "");

//                            Routing routing = new Routing();
//                            routing.setAddress(address);
//                            routing.setJobContent(jobContent);
//                            routing.setMachine(machine);
//                            routing.setModel(model);
//                            routing.setPhone(namePhone);
//                            routing.setChargeName(chargeName);
//                            routing.setType(type);

                            try {
                                pxObj = new JSONObject();
                                pxObj.put("day", chooseDay);
                                pxObj.put("location", address);
                                pxObj.put("content", jobContent);
                                pxObj.put("product_id", model);
                                pxObj.put("product_name", machine);
                                pxObj.put("customer", namePhone);
                                pxObj.put("owner", chargeName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("test", "-----routing peixun-----" + routing.toString());
                        }
                    }
                    if (tablist.contains(types[1])) {
                        if (!currentRouting.getType().equals(types[1])) {
                            SharedPreferences preferences1 = getSharedPreferences(AddRoutingActivity.ROUTING_XUNJIAN, Activity.MODE_PRIVATE);
                            String address1 = preferences1.getString(AddRoutingFrament.ADDRESS, "");
                            String jobContent1 = preferences1.getString(AddRoutingFrament.JOBCONTENT, "");
                            String machine1 = preferences1.getString(AddRoutingFrament.MACHINE, "");
                            String model1 = preferences1.getString(AddRoutingFrament.MODEL, "");
                            String namePhone1 = preferences1.getString(AddRoutingFrament.NAMEPHONE, "");
                            String chargeName1 = preferences1.getString(AddRoutingFrament.CHARGENAME, "");
                            String type = preferences1.getString(AddRoutingFrament.TYPE, "");
//                            Routing routing1 = new Routing();
//                            routing1.setAddress(address1);
//                            routing1.setJobContent(jobContent1);
//                            routing1.setMachine(machine1);
//                            routing1.setModel(model1);
//                            routing1.setPhone(namePhone1);
//                            routing1.setChargeName(chargeName1);
//                            routing1.setType(type);

                            try {
                                xjObj = new JSONObject();
                                xjObj.put("day", chooseDay);
                                xjObj.put("location", address1);
                                xjObj.put("content", jobContent1);
                                xjObj.put("product_id", model1);
                                xjObj.put("product_name", machine1);
                                xjObj.put("customer", namePhone1);
                                xjObj.put("owner", chargeName1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("test", "-----routing xunjian-----" + routing1.toString());
                        }
                    }
                    if (tablist.contains(types[2])) {
                        if (!currentRouting.getType().equals(types[2])) {
                            SharedPreferences preferences2 = getSharedPreferences(AddRoutingActivity.ROUTING_WEIXIU, Activity.MODE_PRIVATE);
                            String address2 = preferences2.getString(AddRoutingFrament.ADDRESS, "");
                            String jobContent2 = preferences2.getString(AddRoutingFrament.JOBCONTENT, "");
                            String machine2 = preferences2.getString(AddRoutingFrament.MACHINE, "");
                            String model2 = preferences2.getString(AddRoutingFrament.MODEL, "");
                            String namePhone2 = preferences2.getString(AddRoutingFrament.NAMEPHONE, "");
                            String chargeName2 = preferences2.getString(AddRoutingFrament.CHARGENAME, "");
                            String type = preferences2.getString(AddRoutingFrament.TYPE, "");
//                            Routing routing2 = new Routing();
//                            routing2.setAddress(address2);
//                            routing2.setJobContent(jobContent2);
//                            routing2.setMachine(machine2);
//                            routing2.setModel(model2);
//                            routing2.setPhone(namePhone2);
//                            routing2.setChargeName(chargeName2);
//                            routing2.setType(type);

                            try {
                                wxObj = new JSONObject();
                                wxObj.put("day", chooseDay);
                                wxObj.put("location", address2);
                                wxObj.put("content", jobContent2);
                                wxObj.put("product_id", model2);
                                wxObj.put("product_name", machine2);
                                wxObj.put("customer", namePhone2);
                                wxObj.put("owner", chargeName2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("test", "-----routing weixiu-----" + routing2.toString());
                        }
                    }
                    if (tablist.contains(types[3])) {
                        if (!currentRouting.getType().equals(types[3])) {
                            SharedPreferences preferences3 = getSharedPreferences(AddRoutingActivity.ROUTING_TIAOSHI, Activity.MODE_PRIVATE);
                            String address3 = preferences3.getString(AddRoutingFrament.ADDRESS, "");
                            String jobContent3 = preferences3.getString(AddRoutingFrament.JOBCONTENT, "");
                            String machine3 = preferences3.getString(AddRoutingFrament.MACHINE, "");
                            String model3 = preferences3.getString(AddRoutingFrament.MODEL, "");
                            String namePhone3 = preferences3.getString(AddRoutingFrament.NAMEPHONE, "");
                            String chargeName3 = preferences3.getString(AddRoutingFrament.CHARGENAME, "");
                            String type = preferences3.getString(AddRoutingFrament.TYPE, "");
//                            Routing routing3 = new Routing();
//                            routing3.setAddress(address3);
//                            routing3.setJobContent(jobContent3);
//                            routing3.setMachine(machine3);
//                            routing3.setModel(model3);
//                            routing3.setPhone(namePhone3);
//                            routing3.setChargeName(chargeName3);
//                            routing3.setType(type);
                            try {
                                tsObj = new JSONObject();
                                tsObj.put("day", chooseDay);
                                tsObj.put("location", address3);
                                tsObj.put("content", jobContent3);
                                tsObj.put("product_id", model3);
                                tsObj.put("product_name", machine3);
                                tsObj.put("customer", namePhone3);
                                tsObj.put("owner", chargeName3);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("test", "-----routing tiaoshi-----" + routing3.toString());
                        }
                    }
                    addRouting();
                } else if (tablist.size() == 0) {
//                    Log.d("test", "------------>nothing edit<-----------");
                }

                break;
            default:
                break;
        }

    }

    private void removeTab(String type) {
        if (tablist.size() > 1) {
            mTabHost.setCurrentTab(0);
            mTabHost.clearAllTabs();
            tablist.remove(type);
            for (int j = 0; j < tablist.size(); j++) {
                TabHost.TabSpec tmpTabSpec = mTabHost.newTabSpec(tablist.get(j)).setIndicator(getTextView(j));
                mTabHost.addTab(tmpTabSpec, AddRoutingFrament.class, null);
            }
            mTabHost.setCurrentTab(0);
            updateTab(mTabHost, true);
        } else if (tablist.size() == 1) {
            mTabHost.setCurrentTab(0);
            mTabHost.clearAllTabs();
            tabSpecNull = mTabHost.newTabSpec("").setIndicator(getTextView(""));
            mTabHost.addTab(tabSpecNull, NullFrament.class, null);
            tablist.remove(type);
            updateTab(mTabHost, false);
        }
    }

    /**
     * 更新Tab标签的颜色，和字体的颜色
     *
     * @param tabHost
     */
    private void updateTab(TabHost tabHost, boolean isShowLine) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.text_tab_name);
            View view_under_line = tabHost.getTabWidget().getChildAt(i).findViewById(R.id.view_under_line);
            if (!isShowLine) {
                view_under_line.setBackgroundResource(android.R.color.transparent);
            } else {
                if (tabHost.getCurrentTab() == i) {// 选中
                    tv.setTextColor(Util.setColor(this, R.color.color_order_bg_4373f1));
                    view_under_line.setBackgroundResource(R.color.color_order_bg_4373f1);
                } else {// 不选中
                    tv.setTextColor(Util.setColor(this, R.color.tmp_color_1));
                    view_under_line.setBackgroundResource(android.R.color.transparent);
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.ADDRESS);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.JOBCONTENT);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.MACHINE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.MODEL);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.NAMEPHONE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_PEIXUN).removeItem(AddRoutingFrament.CHARGENAME);

        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.ADDRESS);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.JOBCONTENT);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.MACHINE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.MODEL);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.NAMEPHONE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_XUNJIAN).removeItem(AddRoutingFrament.CHARGENAME);


        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.ADDRESS);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.JOBCONTENT);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.MACHINE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.MODEL);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.NAMEPHONE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_TIAOSHI).removeItem(AddRoutingFrament.CHARGENAME);

        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.ADDRESS);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.JOBCONTENT);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.MACHINE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.MODEL);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.NAMEPHONE);
        SharePreferencesUtil.getInstance(this, AddRoutingActivity.ROUTING_WEIXIU).removeItem(AddRoutingFrament.CHARGENAME);

    }


    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent mIntent = new Intent();
                mIntent.putExtra("chooseday", chooseDay);
                mIntent.putExtra("tmp_type", 1);
                setResult(0, mIntent);
                finish();
            } else if (msg.what == 1) {
                LogUtils.showCenterToast(AddRoutingActivity.this, "上传行程失败，请稍后再试");
            }
        }
    };

    public void addRouting() {
        LogUtils.showLogD("--->day---> " + chooseDay);
        String url = ServicePort.TRIP_ADD;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        if (pxObj != null && pxObj.length() > 0) {
            map.put("px", pxObj.toString());
        }
        if (xjObj != null && xjObj.length() > 0) {
            map.put("xj", xjObj.toString());
        }
        if (wxObj != null && wxObj.length() > 0) {
            map.put("wx", wxObj.toString());
        }
        if (tsObj != null && tsObj.length() > 0) {
            map.put("ts", tsObj.toString());
        }
        if (currentRouting.getType().equals(types[0])) {
//            LogUtils.showLogD("...................0......................" + curObj.toString());
            map.put("px", curObj.toString());
        } else if (currentRouting.getType().equals(types[1])) {
//            LogUtils.showLogD("...................1......................" + curObj.toString());
            map.put("xj", curObj.toString());
        } else if (currentRouting.getType().equals(types[2])) {
//            LogUtils.showLogD("...................2......................" + curObj.toString());
            map.put("wx", curObj.toString());
        } else if (currentRouting.getType().equals(types[3])) {
//            LogUtils.showLogD("...................3......................" + curObj.toString());
            map.put("ts", curObj.toString());
        }
        AjaxParams params = api.getParam(this, map);
        if (pxObj != null && pxObj.length() > 0) {
            params.put("px", pxObj.toString());
        }
        if (xjObj != null && xjObj.length() > 0) {
            params.put("xj", xjObj.toString());
        }
        if (wxObj != null && wxObj.length() > 0) {
            params.put("wx", wxObj.toString());
        }
        if (tsObj != null && tsObj.length() > 0) {
            params.put("ts", tsObj.toString());
        }
        if (currentRouting.getType().equals(types[0])) {
            params.put("px", curObj.toString());
        } else if (currentRouting.getType().equals(types[1])) {
            params.put("xj", curObj.toString());
        } else if (currentRouting.getType().equals(types[2])) {
            params.put("wx", curObj.toString());
        } else if (currentRouting.getType().equals(types[3])) {
            params.put("ts", curObj.toString());
        }

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("--->添加行程返回数据：" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            handle.sendEmptyMessage(1);
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

}
