package com.biz.timux.capstone.data;

/**
 * Created by gaojianxun on 16/6/30.
 */
public class CountryModel {

    private final String countryName;
    //private final String countryCode;


    public CountryModel(String name) {
        countryName = name;
        //countryCode = code;
    }

    public String getCountryName() {
        return countryName;
    }

//    public String getCountryCode() {
//        return countryCode;
//    }


    public String toString(){
        return countryName;
    }
}