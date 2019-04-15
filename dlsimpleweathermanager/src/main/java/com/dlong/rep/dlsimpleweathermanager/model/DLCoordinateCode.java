package com.dlong.rep.dlsimpleweathermanager.model;

/**
 * 坐标代码
 *
 * @author  dlong
 * created at 2019/4/15 11:38 AM
 */
public class DLCoordinateCode {
    /**
     * WGS84 地球坐标 --> 国际地图提供商、谷歌国际地图
     */
    public static final int CODE_WGS84 = 1;

    /**
     * GCJ02 国测局坐标 --> 高德地图、谷歌地图、腾讯地图、阿里云地图
     */
    public static final int CODE_GCJ02 = 2;

    /**
     * BD09 百度坐标 --> 百度地图
     */
    public static final int CODE_BD09 = 3;
}
