package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePwdActivity extends Activity implements View.OnClickListener {

    private ImageView img_change_pwd_back;
    private EditText edit_change_pwd_phone;
    private EditText edit_change_pwd_old;
    private EditText edit_change_pwd_new;
    private TextView text_change_pwd_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        img_change_pwd_back = (ImageView) findViewById(R.id.img_change_pwd_back);
        img_change_pwd_back.setOnClickListener(this);
        edit_change_pwd_phone = (EditText) findViewById(R.id.edit_change_pwd_phone);
        edit_change_pwd_old = (EditText) findViewById(R.id.edit_change_pwd_old);
        edit_change_pwd_new = (EditText) findViewById(R.id.edit_change_pwd_new);
        text_change_pwd_commit = (TextView) findViewById(R.id.text_change_pwd_commit);
        text_change_pwd_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_change_pwd_back:
                finish();
                break;
            case R.id.text_change_pwd_commit:
                changePwd();
                break;
        }
    }

    private void changePwd() {

        String oldString = edit_change_pwd_old.getText().toString();
        String newString = edit_change_pwd_new.getText().toString();
        if (TextUtils.isEmpty(oldString)) {
            LogUtils.showCenterToast(this, "请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(newString)) {
            LogUtils.showCenterToast(this, "请输入新密码");
            return;
        }
        String url = ServicePort.ACCOUNT_REPASS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("oldpass", oldString);
        map.put("pass", newString);
        AjaxParams params = api.getParam(this, map);
        params.put("oldpass", oldString);
        params.put("pass", newString);
        api.Post(url, params, new AjaxCallBack<Object>() {

            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("--------修改密码返回数据:" + o.toString());
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handler.sendEmptyMessage(0);
                        } else {
                            LogUtils.showCenterToast(ChangePwdActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                startActivity(new Intent(ChangePwdActivity.this, LoginActivity.class));
            }
        }
    };


}
