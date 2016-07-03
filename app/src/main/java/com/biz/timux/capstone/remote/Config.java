package com.biz.timux.capstone.remote;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gaojianxun on 16/6/6.
 */
public class Config {
//    public static final URL BASE_URL;
//
//    static {
//        URL url = null;
//        try {
//            //url = new URL("https://travelbriefing.org/countries.json" );
//            url = new URL("https://travelbriefing.org/Australia?format=json");
//        } catch (MalformedURLException ignored) {
//            // TODO: throw a real error
//        }
//        BASE_URL = url;
//    }

    private static String baseUrlStr = "https://travelbriefing.org/";
    private static String suffixUrlStr = "?format=json";


    private static String flagIconUrlStr = "http://www.geognos.com/api/en/countries/flag/";
    private static String flagIconSuffixStr = ".png";

//    public ConfigUrl(String country){
//        nameUrlStr = country;
//    }

    public static  URL setBaseUrl(String countryName) {
        URL url = null;
        try {
            // https://travelbriefing.org/Heard%20Island%20and%20McDonald%20Islands?format=json
            url = new URL(baseUrlStr+countryName+suffixUrlStr);
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
        }
        return url;
    }

    public static URL setFlagIconUrl(String countryCode){
        URL url = null;
        try {
            // http://www.geognos.com/api/en/countries/flag/HM.png
            url = new URL(flagIconUrlStr+countryCode+flagIconSuffixStr);
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
        }
        return url;

    }
}
