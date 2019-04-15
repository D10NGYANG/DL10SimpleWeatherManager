package com.dlong.rep.dlsimpleweathermanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.dlong.rep.dlsimpleweathermanager.exception.DLSimpleWeatherException;
import com.dlong.rep.dlsimpleweathermanager.model.DLCoordinateCode;
import com.dlong.rep.dlsimpleweathermanager.model.DLErrorCode;
import com.dlong.rep.dlsimpleweathermanager.model.DLPlaceInfo;
import com.dlong.rep.dlsimpleweathermanager.model.DLStepCode;
import com.dlong.rep.dlsimpleweathermanager.model.DLWeatherInfo;
import com.dlong.rep.dlsimpleweathermanager.utils.CoordinateTransformUtil;
import com.dlong.rep.dlsimpleweathermanager.utils.DLHttpDoUtils;
import com.dlong.rep.dlsimpleweathermanager.utils.DLNetworkUtils;
import com.dlong.rep.dlsimpleweathermanager.utils.DLReadJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 简单天气信息工具
 * ----------------------------
 * 1、拿到经纬度
 * 2、根据经纬度拿到真实地址
 * 3、根据真实地址查询天气
 * ----------------------------
 * @author  dlong
 * created at 2019/4/13 2:48 PM
 */
public class DLSimpleWeatherUtils {

    private static final String TAG = "DLSimpleWeatherUtils";

    /** 查询经纬度URL */
    private static final String URL_LATITUDE_AND_LONGITUDE = "http://ip-api.com/json/";

    /** 查询地址URL */
    private static final String URL_REAL_ADDRESS = "https://apis.map.qq.com/jsapi?qt=rgeoc&lnglat=**1**%2C**2**&key=FBOBZ-VODWU-C7SVF-B2BDI-UK3JE-YBFUS&output=jsonp&pf=jsapi&ref=jsapi&cb=qq.maps._svcb2.geocoder0";

    /** 查询地址URL */
    private static final String URL_WEATHER = "http://weather.123.duba.net/static/weather_info/**.html";

    private static Context mContext;

    /** 监听器 */
    private static OnGetWeatherListener mGetWeatherListener;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DLStepCode
                        .STEP_GET_LATITUDE_AND_LONGITUDE:
                    if (msg.arg1 == DLErrorCode.HTTP_OK){
                        handlerGetLatAndLongMsg((String)msg.obj);
                    } else {
                        mGetWeatherListener.OnError(msg.what, msg.arg1);
                    }
                    break;
                case DLStepCode
                        .STEP_GET_REAL_ADDRESS:
                    if (msg.arg1 == DLErrorCode.HTTP_OK){
                        handlerGetRealAddressMsg((String)msg.obj);
                    } else {
                        mGetWeatherListener.OnError(msg.what, msg.arg1);
                    }
                    break;
                case DLStepCode
                        .STEP_GET_WEATHER:
                    if (msg.arg1 == DLErrorCode.HTTP_OK){
                        // TODO: 2019/4/13 请求成功进行解析
                        handlerGetWeatherMsg((String)msg.obj);
                    } else {
                        mGetWeatherListener.OnError(msg.what, msg.arg1);
                    }
                    break;
            }
        }
    };

    /**
     * 初始化
     *
     * @param context 需要getApplicationContext()
     */
    public static void init(Context context){
        mContext = context;
    }

    /**
     * 不带经纬度查天气
     *
     * @param getWeatherListener 监听器
     */
    public static void checkWeather(OnGetWeatherListener getWeatherListener){
        checkContext();
        mGetWeatherListener = getWeatherListener;
        if (!DLNetworkUtils.isNetworkAvailable(mContext)){
            mGetWeatherListener.OnNetworkDisable();
            return;
        }
        getLatitudeAndLongitude();
    }

    /**
     * 带经纬度查天气
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @param coordinateCode 坐标代码，代表输入的经纬度的标准来源
     * @param getWeatherListener 监听器
     */
    public static void checkWeather(double latitude, double longitude, int coordinateCode,
                                    OnGetWeatherListener getWeatherListener){
        checkContext();
        mGetWeatherListener = getWeatherListener;
        if (!DLNetworkUtils.isNetworkAvailable(mContext)){
            mGetWeatherListener.OnNetworkDisable();
            return;
        }
        // 如果不是CODE_GCJ02，需要转换成CODE_GCJ02
        switch (coordinateCode){
            case DLCoordinateCode.CODE_WGS84:
                double[] location1 = CoordinateTransformUtil.wgs84togcj02(longitude, latitude);
                latitude = location1[1];
                longitude = location1[0];
                break;
            case DLCoordinateCode.CODE_GCJ02:
                break;
            case DLCoordinateCode.CODE_BD09:
                double[] location3 = CoordinateTransformUtil.bd09togcj02(longitude, latitude);
                latitude = location3[1];
                longitude = location3[0];
                break;
        }
        getRealAddress(latitude, longitude);
    }

    /**
     * 检查是否初始化
     */
    private static void checkContext(){
        if (null == mContext) {
            throw new DLSimpleWeatherException("DLSimpleWeatherUtils needs to initialize，Use " +
                    "\"DLSimpleWeatherUtils.init(getApplicationContext());\"");
        }
    }

    /**
     * 查询经纬度
     */
    private static void getLatitudeAndLongitude(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DLHttpDoUtils.requestGet(mHandler, DLStepCode.STEP_GET_LATITUDE_AND_LONGITUDE,
                        URL_LATITUDE_AND_LONGITUDE, new HashMap<String, String>(), "UTF-8");
            }
        }).start();
    }

    /**
     * 查询真实地址
     *
     * @param latitude 纬度
     * @param longitude 经度
     */
    private static void getRealAddress(double latitude, double longitude){
        final String url = URL_REAL_ADDRESS.replace("**1**", longitude + "")
                .replace("**2**", latitude +"");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DLHttpDoUtils.requestGet(mHandler, DLStepCode.STEP_GET_REAL_ADDRESS,
                        url, new HashMap<String, String>(), "GBK");
            }
        }).start();
    }

    /**
     * 查询天气信息
     *
     * @param placeInfo 地址信息
     */
    private static void getWeather(DLPlaceInfo placeInfo){
        // 拿到全部信息
        JSONObject mWeatherCityCodeJson = DLReadJsonUtils.getCityCodeJSON(mContext);
        if (null == mWeatherCityCodeJson) {
            // 没有正确读取到全部信息
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER, DLErrorCode.READ_DATA_FAIL);
            return;
        }
        // 拿到省
        JSONObject provinceObj = mWeatherCityCodeJson.optJSONObject(placeInfo.getProvince());
        if (null == provinceObj) {
            // 没有拿到省报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER, DLErrorCode.READ_PROVINCE_DATA_FAIL);
            return;
        }
        // 拿到市
        JSONObject cityObj = provinceObj.optJSONObject(placeInfo.getCity());
        if (null == cityObj) {
            // 没有拿到市报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER, DLErrorCode.READ_CITY_DATA_FAIL);
            return;
        }
        // 根据区拿代码
        String code = cityObj.optString(placeInfo.getDistrict());
        if (null == code || code.equals("")){
            // 没拿到，那就根据市拿代码
            code = cityObj.optString(placeInfo.getCity());
        }
        // 还是没有代码
        if (null == code || code.equals("")) {
            // 没有拿到代码报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER, DLErrorCode.READ_CODE_DATA_FAIL);
            return;
        }
        final String url = URL_WEATHER.replace("**", code);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DLHttpDoUtils.requestGet(mHandler, DLStepCode.STEP_GET_WEATHER,
                        url, new HashMap<String, String>(), "UTF-8");
            }
        }).start();
    }

    /**
     * 请求经纬度回复解析
     *
     * @param obj 字符串
     */
    private static void handlerGetLatAndLongMsg(String obj) {
        try {
            JSONObject object = new JSONObject(obj);
            String lat = object.optString("lat");
            String lon = object.optString("lon");
            if (null == lat || null == lon || lat.equals("") || lon.equals("")){
                // 报错
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_LATITUDE_AND_LONGITUDE,
                        DLErrorCode.HTTP_DATA_CONVERSION_ERROR);
                return;
            }
            double latitude = Double.valueOf(lat);
            double longitude = Double.valueOf(lon);

            mGetWeatherListener.OnGetLatAndLon(latitude, longitude);
            getRealAddress(latitude, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
            // 报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_LATITUDE_AND_LONGITUDE,
                    DLErrorCode.HTTP_DATA_CONVERSION_ERROR);
        } catch (NumberFormatException e) {
            // 报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_LATITUDE_AND_LONGITUDE,
                    DLErrorCode.HTTP_DATA_CONVERSION_ERROR);
        }
    }

    /**
     * 请求真实地址回复解析
     *
     * @param obj 字符串
     */
    private static void handlerGetRealAddressMsg(String obj) {
        String n = "";  // 国家
        String p = "";  // 省份
        String c = "";  // 城市
        String d = "";  // 区域
        String info = ""; // 详细地址
        try {
            obj = obj.replace("qq.maps._svcb2.geocoder0&&qq.maps._svcb2.geocoder0(","");
            obj = obj.substring(0, obj.lastIndexOf(")"));
            JSONObject object = new JSONObject(obj);
            JSONObject infoObj = object.optJSONObject("info");
            if (0 != infoObj.optInt("error")) {
                // 请求失败
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_REAL_ADDRESS,
                        DLErrorCode.HTTP_REQ_REAL_ADDRESS_ERROR);
                return;
            }
            // 拿到细节信息
            JSONObject detailObj = object.optJSONObject("detail");
            if (null == detailObj){
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_REAL_ADDRESS,
                        DLErrorCode.HTTP_REQ_REAL_ADDRESS_ERROR);
                return;
            }
            // 拿到结果数组
            JSONArray results = detailObj.optJSONArray("results");
            if (results.length() < 1) {
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_REAL_ADDRESS,
                        DLErrorCode.HTTP_REQ_REAL_ADDRESS_ERROR);
                return;
            }
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.optJSONObject(i);
                n = result.optString("n");
                p = result.optString("p");
                c = result.optString("c");
                d = result.optString("d");
            }
            // 去读详细地址
            int poiCount = detailObj.optInt("poi_count");
            if (poiCount > 0) {
                JSONArray poilist = detailObj.optJSONArray("poilist");
                for (int i = 0; i < poilist.length(); i++) {
                    JSONObject poi = poilist.optJSONObject(i);
                    JSONObject addrInfo = poi.optJSONObject("addr_info");
                    if (addrInfo.optString("c").equals("") ||
                            addrInfo.optString("p").equals("") ||
                            addrInfo.optString("d").equals(""))
                        continue;
                    String addr = poi.optString("addr");
                    if (p.equals("")) p = addrInfo.optString("p");
                    if (c.equals("")) c = addrInfo.optString("c");
                    if (d.equals("")) d = addrInfo.optString("d");
                    info = addr;
                    break;
                }
            }
            if (p.equals("") || c.equals("") || d.equals("")){
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_REAL_ADDRESS,
                        DLErrorCode.HTTP_REQ_REAL_ADDRESS_ERROR);
                return;
            }
            DLPlaceInfo placeInfo = new DLPlaceInfo();
            placeInfo.setCountry(n);
            placeInfo.setProvince(p);
            placeInfo.setCity(c);
            placeInfo.setDistrict(d);
            placeInfo.setInfo(info);
            mGetWeatherListener.OnGetRealAddress(placeInfo);
            getWeather(placeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            // 报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_REAL_ADDRESS,
                    DLErrorCode.HTTP_DATA_CONVERSION_ERROR);
        }
    }

    /**
     * 请求天气信息回复解析
     *
     * @param obj 字符串
     */
    private static void handlerGetWeatherMsg(String obj) {
        DLWeatherInfo weatherInfo = new DLWeatherInfo();
        try {
            obj = obj.replace("weather_callback(","");
            obj = obj.substring(0, obj.lastIndexOf(")"));
            JSONObject object = new JSONObject(obj);
            weatherInfo.setUpdateTime(object.optString("update_time"));
            JSONObject weather = object.getJSONObject("weatherinfo");
            if (null == weather){
                // 没有天气信息
                mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER,
                        DLErrorCode.HTTP_REQ_WEATHER_INFO_ERROR);
                return;
            }
            weatherInfo.setStatusCode(weather.optString("img_single"));
            weatherInfo.setStatusText(weather.optString("img_title_single"));
            weatherInfo.setCurrentTemperature(weather.optString("temp"));
            weatherInfo.setHumidity(weather.optString("sd"));
            weatherInfo.setAirPMQuality(weather.optString("pm"));
            weatherInfo.setAirPMLevel(weather.optString("pm-level"));
            mGetWeatherListener.OnGetWeather(weatherInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            // 报错
            mGetWeatherListener.OnError(DLStepCode.STEP_GET_WEATHER,
                    DLErrorCode.HTTP_DATA_CONVERSION_ERROR);
        }
    }

}
