package com.dld.coupon.db;

import java.util.ArrayList;

public class CityBean {
    public static final String CITY_CODE = "city_code";
    public static final String CITY_ID = "cityid";
    public static final String CITY_NAME = "cityname";
    public static final String CITY_TOPID = "topid";
    public static final String CITY_TYPE = "type";
    public static final String TABLE_CREATE = "create table notes (_id integer primary key autoincrement, title text not null, body text not null);";
    public ArrayList<CityBean> chirdrenCityList = null;
    public String cityCode;
    public String cityId;
    public String cityName;
    public Integer id;
    public Integer topId;
    public Integer type;
}
