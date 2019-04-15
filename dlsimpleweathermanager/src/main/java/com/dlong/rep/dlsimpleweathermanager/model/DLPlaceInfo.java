package com.dlong.rep.dlsimpleweathermanager.model;

import java.io.Serializable;

/**
 * 定位地址模型
 * @author  dlong
 * created at 2019/4/13 2:56 PM
 */
public class DLPlaceInfo implements Serializable {
    /** 国家 */
    private String country;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 区 */
    private String district;
    /** 详细 */
    private String info;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
