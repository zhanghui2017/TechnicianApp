package com.gengli.technician.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gengli.technician.R;
import com.gengli.technician.fragment.HelpFragment;
import com.gengli.technician.fragment.MainFragment;
import com.gengli.technician.fragment.MineFragment;
import com.gengli.technician.fragment.OrderFragment;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.FileUtils;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SystemMsgUtil;
import com.gengli.technician.util.Util;
import com.gengli.technician.view.UpdatePopu;


import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout main_frame;
    private LinearLayout bottom_main_bt, bottom_order_bt, bottom_help_bt, bottom_mine_bt;
    private TextView first_page, second_page, third_page, four_page;
    private ImageView first_img, seconde_img, third_img, fourth_img;
    private MainFragment mainFragment;
    private OrderFragment orderFragment;
    private HelpFragment helpFragment;
    private MineFragment mineFragment;
    private FragmentManager fragmentManager;
    private List<TextView> textList;
    private List<ImageView> vList;
    private Long time = (long) 0;

    private int[] bottom_img_one = {R.drawable.img_main_1, R.drawable.img_main_2, R.drawable.img_main_3,
            R.drawable.img_main_4};
    private int[] bottom_img_two = {R.drawable.img_main_sel_1, R.drawable.img_main_sel_2, R.drawable.img_main_sel_3,
            R.drawable.img_main_sel_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        initView();
        checkUpdate();
    }

    public void initView() {
        main_frame = (FrameLayout) findViewById(R.id.main_frame);
        bottom_main_bt = (LinearLayout) findViewById(R.id.bottom_main_bt);
        bottom_order_bt = (LinearLayout) findViewById(R.id.bottom_order_bt);
        bottom_help_bt = (LinearLayout) findViewById(R.id.bottom_help_bt);
        bottom_mine_bt = (LinearLayout) findViewById(R.id.bottom_mine_bt);

        first_img = (ImageView) findViewById(R.id.first_img);
        seconde_img = (ImageView) findViewById(R.id.seconde_img);
        third_img = (ImageView) findViewById(R.id.third_img);
        fourth_img = (ImageView) findViewById(R.id.fourth_img);

        textList = new ArrayList<TextView>();
        vList = new ArrayList<ImageView>();
        vList.add(first_img);
        vList.add(seconde_img);
        vList.add(third_img);
        vList.add(fourth_img);

        first_page = (TextView) findViewById(R.id.first_page);
        second_page = (TextView) findViewById(R.id.second_page);
        third_page = (TextView) findViewById(R.id.third_page);
        four_page = (TextView) findViewById(R.id.fourth_page);

        if (textList != null) {
            textList.clear();
        }
        textList.add(first_page);
        textList.add(second_page);
        textList.add(third_page);
        textList.add(four_page);

        bottom_main_bt.setOnClickListener(this);
        bottom_order_bt.setOnClickListener(this);
        bottom_help_bt.setOnClickListener(this);
        bottom_mine_bt.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        //设置默认
        changeFrame(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_main_bt:
                changeFrame(0);
                break;
            case R.id.bottom_order_bt:
                if (DatasUtil.isTechLogin(this)) {
                    changeFrame(1);
                } else {
                    LogUtils.showCenterToast(this, "功能未开放");
                }
                break;
            case R.id.bottom_help_bt:
                changeFrame(2);
                break;
            case R.id.bottom_mine_bt:
                changeFrame(3);
                break;
            default:
                break;
        }
    }

    public void changeFrame(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFrame(transaction);
        changeMenuAttr(index);
        switch (index) {
            case 0:
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.main_frame, mainFragment);
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case 1:
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    transaction.add(R.id.main_frame, orderFragment);
                } else {
                    transaction.show(orderFragment);
                }
                break;
            case 2:
                if (helpFragment == null) {
                    helpFragment = new HelpFragment();
                    transaction.add(R.id.main_frame, helpFragment);
                } else {
                    transaction.show(helpFragment);
                }
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.main_frame, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFrame(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (helpFragment != null) {
            transaction.hide(helpFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    /**
     * 修改菜单属性
     */
    @SuppressWarnings("deprecation")
    private void changeMenuAttr(int index) {
        for (int i = 0; i < textList.size(); i++) {
            if (index == i) {
                textList.get(i).setTextColor(getResources().getColor(R.color.color_main_menu_text_sel));
                vList.get(i).setImageResource(bottom_img_two[i]);
            } else {
                textList.get(i).setTextColor(getResources().getColor(R.color.color_main_menu_text));
                vList.get(i).setImageResource(bottom_img_one[i]);
            }
        }
    }


    /**
     * 双击退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (time == 0) {
                time = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            } else {
                if ((System.currentTimeMillis() - time) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    time = System.currentTimeMillis();
                } else {
//                    Logout();
                    this.finish();
                }
            }
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private String new_version;
    private String cur_version;
    private String filePATH;
    private UpdatePopu popUpdate;

    private void checkUpdate() {
        cur_version = String.valueOf(SystemMsgUtil.getVersionName(this));
        String url = ServicePort.CLIENT_UPDATE;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        AjaxParams params = api.getParam(this, map);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                LogUtils.showLogD("--------版本更新返回数据:" + o.toString());
                String responce = o.toString();
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
//                            needUpdate = results.getBoolean("update");
                            new_version = results.getString("new_version");
//                            old_version = results.getString("old_version");
                            filePATH = results.getString("file");

//                            needUpdate = Util.aVb(Util.chartStr(new_version), Util.chartStr(cur_version));
                            LogUtils.showLogD("...cur_version === " + cur_version);
                            LogUtils.showLogD("...new_version === " + new_version);
                            if (TextUtils.isEmpty(new_version)) {
                                return;
                            }
                            int i = Util.compareVersion(cur_version, new_version);
                            if (i == 0) {
                                LogUtils.showLogD("...0...");
                            } else if (i == 1) {
                                LogUtils.showLogD("...1...");
                            } else if (i == -1) {
                                LogUtils.showLogD("...-1...");
                                handler.sendEmptyMessage(0);
                            }
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


    private void clientUpdate() {
        String url = filePATH;
        if (TextUtils.isEmpty(filePATH)) {
            return;
        }
        ApiClientHttp api = new ApiClientHttp();
        String apkPath = FileUtils.getRootPath();
        api.download(url, apkPath, new AjaxCallBack<File>() {

            @Override
            public void onSuccess(File file) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long count, long current) {

            }
        });

    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                popUpdate = new UpdatePopu(MainActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.pop_update_bt1:
                                popUpdate.dismiss();
                                break;
                            case R.id.pop_update_bt2:
                                clientUpdate();
                                LogUtils.showCenterToast(MainActivity.this, "正在下载请稍候");
                                break;
                        }

                    }
                });
                popUpdate.showPopupWindow(main_frame);
            }
        }
    };

}
