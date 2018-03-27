package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.User;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button login_bt;
    private TextView regist_bt;
    private EditText accountEdit;
    private EditText passwordEdit;
    private static final int SERVER_SUCCESS = 1;

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == SERVER_SUCCESS) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("from_login", 0);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        login_bt = (Button) findViewById(R.id.login_bt);
        login_bt.setOnClickListener(this);
        regist_bt = (TextView) findViewById(R.id.login_to_register_bt);
        regist_bt.setOnClickListener(this);
        accountEdit = (EditText) findViewById(R.id.login_account_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password_edit);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:
                login();
                break;
            case R.id.login_to_register_bt:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }


    private void login() {
        String url = ServicePort.ACCOUNT_LOGIN_WORKER;
        ApiClientHttp api = new ApiClientHttp();
        final String account = accountEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("pass", password);
        AjaxParams params = api.getParam(this, map);
        params.put("account", account);
        params.put("pass", password);
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            LogUtils.showCenterToast(this, "帐号密码请输入完整");
        } else {
            api.Post(url, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    String responce = o.toString();
                    LogUtils.showLogD("----->登录返回数据----->" + responce);
                    if (!TextUtils.isEmpty(responce)) {
                        try {
                            JSONObject jsonObject = new JSONObject(responce);
                            int err_no = jsonObject.getInt("err_no");
                            if (err_no == 0) {
                                JSONObject results = jsonObject.getJSONObject("results");
                                if (!TextUtils.isEmpty(results.toString())) {
                                    LogUtils.showLogD("----->login data----->" + results.toString());
                                    new Thread() {
                                        public void run() {
                                            android.os.Message msg = new Message();
                                            msg.arg1 = SERVER_SUCCESS;
                                            handle.sendMessage(msg);
                                        }
                                    }.start();

                                    SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("sessionid", results.getString("sessionid"));
                                    editor.putString("mobile", results.getString("mobile"));
                                    editor.putString("username", results.getString("username"));
                                    editor.putString("realname", results.getString("realname"));
                                    editor.putInt("gender", results.getInt("gender"));
                                    editor.putString("prov", results.getString("prov"));
                                    editor.putString("prov_name", results.getString("prov_name"));
                                    editor.putString("city", results.getString("city"));
                                    editor.putString("city_name", results.getString("city_name"));
                                    editor.putString("birthday", results.getString("birthday"));
                                    editor.putString("avatar", results.getString("avatar"));

                                    editor.putBoolean("LoginState", true);//登录状态
                                    editor.putString("company_id", accountEdit.getText().toString());
                                    editor.putString("password", password);
                                    JPushInterface.setAlias(LoginActivity.this, 1,accountEdit.getText().toString());
                                    editor.commit();
                                }
                            } else {
                                LogUtils.showCenterToast(LoginActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
    }
}
