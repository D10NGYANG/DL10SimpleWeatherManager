package com.dlong.rep.dlsimpleweathermanager;

import com.dlong.rep.dlsimpleweathermanager.model.DLPlaceInfo;
import com.dlong.rep.dlsimpleweathermanager.model.DLWeatherInfo;

/**
 * 监听天气获取接口
 *
 * @author  dlong
 * created at 2019/4/13 3:42 PM
 */
public interface OnGetWeatherListener {
    /**
     * 网络没有开
     *
     */
    void OnNetworkDisable();

    /**
     * 出错
     *
     * @param step 步骤码
     * @param code 错误码
     */
    void OnError(int step, int code);

    /**
     * 获得天气信息
     *
     * @param weatherInfo 天气信息
     */
    void OnGetWeather(DLWeatherInfo weatherInfo);

    /**
     * 获得经纬度
     *
     * @param latitude 纬度
     * @param longitude 经度
     */
    void OnGetLatAndLon(double latitude, double longitude);

    /**
     * 获得真实地址
     *
     * @param placeInfo 地址信息
     */
    void OnGetRealAddress(DLPlaceInfo placeInfo);
}
