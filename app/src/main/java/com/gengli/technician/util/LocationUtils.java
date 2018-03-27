

package com.gengli.technician.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationUtils {
    //声明AMapLocationClient类对象
    private String city;
    private String address;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationListener mLocationListener;

    public LocationUtils(Context context){
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setApiKey("690bd4df16cf5b81f297a518d696e05e");
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        } else {
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }

        mLocationListener = new AMapLocationListener(){
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//                    Log.d("test", "location ：" + aMapLocation.getAddress());
//                    Log.d("test", "city ：" + aMapLocation.getCity());
                        city = aMapLocation.getCity();
                        address = aMapLocation.getAddress();
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
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

}