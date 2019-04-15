package com.dlong.rep.dl10simpleweathermanager;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dlong.rep.dlocationmanager.DLocationTools;
import com.dlong.rep.dlocationmanager.DLocationUtils;
import com.dlong.rep.dlocationmanager.OnLocationChangeListener;
import com.dlong.rep.dlsimpleweathermanager.DLSimpleWeatherUtils;
import com.dlong.rep.dlsimpleweathermanager.OnGetWeatherListener;
import com.dlong.rep.dlsimpleweathermanager.model.DLCoordinateCode;
import com.dlong.rep.dlsimpleweathermanager.model.DLPlaceInfo;
import com.dlong.rep.dlsimpleweathermanager.model.DLWeatherInfo;
import com.dlong.rep.dlsimpleweathermanager.utils.DLHttpDoUtils;

import java.util.HashMap;

import static com.dlong.rep.dlocationmanager.DLocationWhat.NO_LOCATIONMANAGER;
import static com.dlong.rep.dlocationmanager.DLocationWhat.NO_PROVIDER;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "收得到";
    private Context mContext = this;

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        DLocationUtils.init(getApplicationContext());

        DLSimpleWeatherUtils.init(getApplicationContext());

        txt = findViewById(R.id.txt);
    }

    public void Click(View view){
        int status = DLocationUtils.getInstance().register(locationChangeListener);
        switch (status){
            case NO_LOCATIONMANAGER:
                txt.setText("没有定位权限");
                // TODO: 2019/4/13 请求权限
                DLocationTools.openAppSetting(mContext);

                break;
            case NO_PROVIDER:
                txt.setText("没有可用的定位提供器或尚未打开定位");
                // TODO: 2019/4/13 打开定位
                DLocationTools.openGpsSettings(mContext);
                break;
        }
    }

    /**
     * 定位监听器
     */
    private OnLocationChangeListener locationChangeListener = new OnLocationChangeListener() {
        @Override
        public void getLastKnownLocation(Location location) {
            // 获取上一次获得的定位
            Log.e(TAG, "onLocationChanged: " + location.getLatitude());
            DLSimpleWeatherUtils.checkWeather(location.getLatitude(), location.getLongitude(),
                    DLCoordinateCode.CODE_WGS84,onGetWeatherListener);
        }

        @Override
        public void onLocationChanged(Location location) {
            // 定位改变
            Log.e(TAG, "定位方式：" + location.getProvider());
            Log.e(TAG, "纬度：" + location.getLatitude());
            Log.e(TAG, "经度：" + location.getLongitude());
            Log.e(TAG, "海拔：" + location.getAltitude());
            Log.e(TAG, "时间：" + location.getTime());
            Log.e(TAG, "国家：" + DLocationTools.getCountryName(mContext, location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "获取地理位置：" + DLocationTools.getAddress(mContext, location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在地：" + DLocationTools.getLocality(mContext, location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在街道：" + DLocationTools.getStreet(mContext, location.getLatitude(), location.getLongitude()));
            DLSimpleWeatherUtils.checkWeather(location.getLatitude(), location.getLongitude(),
                    DLCoordinateCode.CODE_WGS84,onGetWeatherListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 状态改变
            // 比如GPS的开关，DLocationWhat.STATUS_ENABLE/DLocationWhat.STATUS_DISABLE

        }
    };

    /**
     * 天气获取监听器
     */
    private OnGetWeatherListener onGetWeatherListener = new OnGetWeatherListener() {
        @Override
        public void OnNetworkDisable() {
            Log.e(TAG, "没有打开网络，或没有网络权限");
        }

        @Override
        public void OnError(int step, int code) {
            Log.e(TAG, "step = " + step + "; code = " + code);
        }

        @Override
        public void OnGetWeather(DLWeatherInfo weatherInfo) {
            Log.e(TAG, weatherInfo.toString());
            txt.setText(weatherInfo.toString());
        }

        @Override
        public void OnGetLatAndLon(double latitude, double longitude) {
            Log.e(TAG, "latitude = " + latitude + "; longitude = " + longitude);
        }

        @Override
        public void OnGetRealAddress(DLPlaceInfo placeInfo) {
            Log.e(TAG, placeInfo.getInfo());
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销
        DLocationUtils.getInstance().unregister();
    }

}
