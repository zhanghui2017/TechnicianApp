package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SystemMsgUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePhoneActivity extends Activity implements View.OnClickListener {

    private ImageView img_change_phone_back;
    private EditText edit_change_phone_phone;
    //    private EditText edit_change_phone_verify;
//    private ImageView img_change_phone_get_verify;
    private EditText edit_change_phone_pwd;
    private TextView text_change_phone_commit;
    private EditText edit_change_phone_captcha;
    private ImageView img_change_phone_get_captcha;
    private static final int BIND_SUCCESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        getCaptcha();
        initView();
    }

    private void initView() {
        img_change_phone_back = (ImageView) findViewById(R.id.img_change_phone_back);
        img_change_phone_back.setOnClickListener(this);
        edit_change_phone_phone = (EditText) findViewById(R.id.edit_change_phone_phone);
//        edit_change_phone_verify = (EditText) findViewById(R.id.edit_change_phone_verify);
        edit_change_phone_pwd = (EditText) findViewById(R.id.edit_change_phone_pwd);
//        img_change_phone_get_verify = (ImageView) findViewById(R.id.img_change_phone_get_verify);
//        img_change_phone_get_verify.setOnClickListener(this);
        text_change_phone_commit = (TextView) findViewById(R.id.text_change_phone_commit);
        text_change_phone_commit.setOnClickListener(this);
        edit_change_phone_captcha = (EditText) findViewById(R.id.edit_change_phone_captcha);
        img_change_phone_get_captcha = (ImageView) findViewById(R.id.img_change_phone_get_captcha);
        img_change_phone_get_captcha.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_change_phone_back:
                finish();
                break;
//            case R.id.img_change_phone_get_verify:
//                String mobile = edit_change_phone_phone.getText().toString();
//                String captcha = edit_change_phone_captcha.getText().toString();
//                if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(captcha)) {
//                    LogUtils.showCenterToast(this, "请输入手机号码和图形验证码");
//                    return;
//                } else {
//                    getVerify(mobile, captcha);
//                }
//                break;
            case R.id.text_change_phone_commit:
                String phone = edit_change_phone_phone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    LogUtils.showCenterToast(this, "请输入手机号码");
                    return;
                } else {
                    bind(phone, "471003");
                }
                break;
            case R.id.img_change_phone_get_captcha:
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                getCaptcha();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ImageLoader.getInstance().displayImage(ServicePort.DATA_CAPTCHA, img_change_phone_get_captcha);
            } else if (msg.what == BIND_SUCCESS) {
                String phone = edit_change_phone_phone.getText().toString();
                DatasUtil.changeUserString(ChangePhoneActivity.this, "mobile", phone);
                finish();
            }
        }
    };


    private void getCaptcha() {
        String url = ServicePort.DATA_CAPTCHA;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        String deviceid = SystemMsgUtil.getSingleKey(this);
        map.put("deviceid", deviceid);
        AjaxParams params = api.getParam(this, map);
        params.put("deviceid", deviceid);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
//                Message message = new Message();
//                message.arg1 = 0;
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }
        });

    }

//    private void getVerify(String phone, String captcha) {
//        String deviceid = SystemMsgUtil.getSingleKey(this);
//        String url = ServicePort.DATA_VERIFY;
//        ApiClientHttp api = new ApiClientHttp();
//        Map<String, String> map = new HashMap<>();
//        map.put("mobile", phone);
//        map.put("deviceid", deviceid);
//        map.put("captcha", captcha);
//        AjaxParams params = api.getParam(this, map);
//        params.put("mobile", phone);
//        params.put("deviceid", deviceid);
//        params.put("captcha", captcha);
//
//        api.Post(url, params, new AjaxCallBack<Object>() {
//            @Override
//            public void onSuccess(Object o) {
//                LogUtils.showLogD("-------------getVerify------------------" + o.toString());
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//
//            }
//        });
//    }


    private void bind(String phone, String verify) {
        String url = ServicePort.ACCOUNT_BIND;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("verify", verify);
        AjaxParams params = api.getParam(this, map);
        params.put("mobile", phone);
        params.put("verify", verify);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->绑定号码返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handler.sendEmptyMessage(BIND_SUCCESS);
                        } else {
                            LogUtils.showCenterToast(ChangePhoneActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
}
