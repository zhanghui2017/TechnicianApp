package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;

import com.gengli.technician.R;
import com.gengli.technician.util.DatasUtil;

public class GuideActivity extends Activity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guide);

        handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (DatasUtil.isLogin(GuideActivity.this)){
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            return false;
        }
        return false;
    }
}