package com.biz.timux.capstone.remote;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gaojianxun on 16/6/6.
 */
public class Config {
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
            //url = new URL("https://travelbriefing.org/countries.json" );
            url = new URL("https://travelbriefing.org/Australia?format=json");
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
        }
        BASE_URL = url;
    }
}
