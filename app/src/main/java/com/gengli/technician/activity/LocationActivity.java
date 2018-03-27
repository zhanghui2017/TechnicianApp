package com.gengli.technician.activity;

import android.Manifest;


import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gengli.technician.R;
import com.gengli.technician.util.LocationUtils;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean flag;
    private Button gsp_btn;
    private Button best_btn;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//                    Log.d("test", "location ：" + aMapLocation.getAddress());
//                    Log.d("test", "city ：" + aMapLocation.getCity());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("test", "location "
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            } else {
                Log.e("test","aMapLocation is null");
            }
        }
    };

    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Log.d("test", "----->oncreat<-----");
//        initView();
//        initListener();
//        //初始化定位
//        mLocationClient = new AMapLocationClient(this);
//        mLocationClient.setApiKey("690bd4df16cf5b81f297a518d696e05e");
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setOnceLocation(true);
//        mLocationOption.setOnceLocationLatest(true);
//        mLocationOption.setHttpTimeOut(20000);
//        mLocationOption.setLocationCacheEnable(false);

    }


    private void initView() {
        gsp_btn = (Button) findViewById(R.id.gps);
        best_btn = (Button) findViewById(R.id.best);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();

    }


    private void initListener() {
        gsp_btn.setOnClickListener(this);
        best_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();//针对6.0以上版本做权限适配
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
    }

    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            flag = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps:
//                if (flag) {
//                    getGPSLocation();
//                } else {
//                    Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
//                }
                if (null != mLocationClient) {
                    mLocationClient.setLocationOption(mLocationOption);
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    mLocationClient.stopLocation();
                    mLocationClient.startLocation();
                } else {
                    mLocationClient.setLocationOption(mLocationOption);
                    mLocationClient.startLocation();
                }
                break;
            case R.id.best:
                if (flag) {
                    getBestLocation();
                } else {
                    Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 通过GPS获取定位信息
     */
    public void getGPSLocation() {
//        Location gps = LocationUtils.getGPSLocation(this);
//        if (gps == null) {
//            //设置定位监听，因为GPS定位，第一次进来可能获取不到，通过设置监听，可以在有效的时间范围内获取定位信息
//            LocationUtils.addLocationListener(this, LocationManager.GPS_PROVIDER, new LocationUtils.ILocationListener() {
//                @Override
//                public void onSuccessLocation(Location location) {
//                    if (location != null) {
//
//                        Toast.makeText(LocationActivity.this, "gps onSuccessLocation location:  lat==" + location.getLatitude() + "     lng==" + location.getLongitude(), Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(LocationActivity.this, "gps location is null", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(this, "gps location: lat==" + gps.getLatitude() + "  lng==" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
//            Log.d("test", "lat === " + gps.getLatitude());
//            Log.d("test", "lng === " + gps.getLongitude());
//        }
    }


    /**
     * 采用最好的方式获取定位信息
     */
    private void getBestLocation() {
//        Criteria c = new Criteria();//Criteria类是设置定位的标准信息（系统会根据你的要求，匹配最适合你的定位供应商），一个定位的辅助信息的类
//        c.setPowerRequirement(Criteria.POWER_LOW);//设置低耗电
//        c.setAltitudeRequired(true);//设置需要海拔
//        c.setBearingAccuracy(Criteria.ACCURACY_COARSE);//设置COARSE精度标准
//        c.setAccuracy(Criteria.ACCURACY_LOW);//设置低精度
//        //... Criteria 还有其他属性，就不一一介绍了
//        Location best = LocationUtils.getBestLocation(this, c);
//        if (best == null) {
//            Toast.makeText(this, " best location is null", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "best location: lat==" + best.getLatitude() + " lng==" + best.getLongitude(), Toast.LENGTH_SHORT).show();
//        }
    }

}
