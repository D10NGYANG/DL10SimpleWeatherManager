package com.dlong.rep.dlsimpleweathermanager.model;

import java.io.Serializable;

/**
 * 天气信息模型
 * @author  dlong
 * created at 2019/4/13 2:51 PM
 */
public class DLWeatherInfo implements Serializable {

    /** 天气状态代码 */
    private String statusCode;
    /** 天气状况文字 */
    private String statusText;
    /** 湿度 */
    private String humidity;
    /** 当前温度 */
    private String currentTemperature;
    /** 空气质量 32 */
    private String airPMQuality;
    /** 空气评级 优 */
    private String airPMLevel;
    /** 数据更新时间 */
    private String updateTime;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(String currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public String getAirPMQuality() {
        return airPMQuality;
    }

    public void setAirPMQuality(String airPMQuality) {
        this.airPMQuality = airPMQuality;
    }

    public String getAirPMLevel() {
        return airPMLevel;
    }

    public void setAirPMLevel(String airPMLevel) {
        this.airPMLevel = airPMLevel;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("更新时间：").append(this.updateTime).append("\r\n")
                .append("天气代码：").append(this.statusCode).append("\r\n")
                .append("天气描述：").append(this.statusText).append("\r\n")
                .append("天气温度：").append(this.currentTemperature).append("\r\n")
                .append("天气湿度：").append(this.humidity).append("\r\n")
                .append("空气质量：").append(this.airPMQuality).append("\r\n")
                .append("空气评级：").append(this.airPMLevel).append("\r\n");
        return builder.toString();
    }
}
