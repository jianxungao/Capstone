package com.biz.timux.capstone.data;

import android.content.ContentResolver;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;



/**
 *
 * Defines table and column names for the weather database.
 *
 * Created by gaojianxun on 16/6/6.
 */

public class CountryContract {

    // The "Content authority" is a name for the entire content provider,
    public static final String CONTENT_AUTHORITY = "com.biz.timux.capstone";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)

    public static final String PATH_COUNTRIES = "countries";
    public static final String PATH_COUNTRY = "country";
//    public static final String PATH_COUNTRY_WEATHER = "country_weather";
//    public static final String PATH_COUNTRY_CURRENCY = "country_currency";


    /* Inner class that defines the table contents of the location table */
    public static final class CountriesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRIES;

        // Table name
        public static final String TABLE_NAME = "countries";

        // Those countries are available at travelbriefing.org.
        public static final String NAME = "name";
        public static final String URL = "url";

    }

    /* Inner class that defines the table contents of the country table */
    public static final class CountryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;

        public static final String TABLE_NAME = "country";

        public static final String NAME = "name";
        public static final String FULLNAME = "full_name";
        public static final String ISO2 = "iso2";
        public static final String CONTINENT = "continent";
        public static final String MAPS_LAT = "maps_lat";
        public static final String MAPS_LONG = "maps_long";
        public static final String TIMEZONE = "timezone";
        public static final String LANGUAGE = "language";
        public static final String OFFICIAL = "official";
        public static final String VOLTAGE = "voltage";
        public static final String FREQUENCY = "frequency";
        public static final String TEL_CODE = "tel_code";
        public static final String TEL_POLICE = "tel_police";
        public static final String TEL_AMB = "tel_amb";
        public static final String TEL_FIRE = "tel_fire";
        public static final String WATER = "water";
        public static final String VACCINATION = "vaccination";
        public static final String ADVISE = "advise";
        public static final String URL = "url";

        public static final String JAN_AVG = "jan_avg";
        public static final String FEB_AVG = "feb_avg";
        public static final String MAR_AVG = "mar_avg";
        public static final String APR_AVG = "apr_avg";
        public static final String MAY_AVG = "may_avg";
        public static final String JUN_AVG = "jun_avg";
        public static final String JUL_AVG = "jul_avg";
        public static final String AUG_AVG = "aug_avg";
        public static final String SEP_AVG = "sep_avg";
        public static final String OCT_AVG = "oct_avg";
        public static final String NOV_AVG = "nov_avg";
        public static final String DEC_AVG = "dec_avg";


        public static final String CUR_NAME = "cur_name";
        public static final String CODE = "code";
        public static final String SYMBOL = "symbol";
        public static final String RATE = "rate";
        public static final String AUSTRALIAN_RAT = "australian_dollar";
        public static final String CANADIAN_RAT = "canadian_dollar";
        public static final String EURO_RAT = "euro";
        public static final String HONG_KONG_RAT = "hong_kong_dollar";
        public static final String MEXICAN_RAT ="mexican_peso";
        public static final String NEW_ZEALAND_RAT = "new_zealand_dollar";
        public static final String US_RAT = "us_dollar";


        public static Uri buildCountryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCountryNameUri(String countryName){
            return CONTENT_URI.buildUpon().appendPath(countryName).build();
        }

        public static String getCountryNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

//    public static final class CountryWeather implements BaseColumns {
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRY_WEATHER).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY_WEATHER;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY_WEATHER;
//
//        // Table name
//        public static final String TABLE_NAME = "country_weather";
//
//        // Column with the foreign key into the country table.
//        public static final String COUNTRY_KEY = "country_id";

//        public static final String JAN_AVG = "jan_avg";
//        public static final String FEB_AVG = "feb_avg";
//        public static final String MAR_AVG = "mar_avg";
//        public static final String APR_AVG = "apr_avg";
//        public static final String MAY_AVG = "may_avg";
//        public static final String JUN_AVG = "jun_avg";
//        public static final String JUL_AVG = "jul_avg";
//        public static final String AUG_AVG = "aug_avg";
//        public static final String SEP_AVG = "sep_avg";
//        public static final String OCT_AVG = "oct_avg";
//        public static final String NOV_AVG = "nov_avg";
//        public static final String DEC_AVG = "dec_avg";

//    }
//
//    public static final class CountryCurrency implements BaseColumns {
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRY_CURRENCY).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY_CURRENCY;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY_CURRENCY;
//
//        // Table name
//        public static final String TABLE_NAME = "country_currency";
//
//        // Column with the foreign key into the country table.
//        public static final String COUNTRY_KEY = "country_id";
//
//        public static final String CUR_NAME = "cur_name";
//        public static final String CODE = "code";
//        public static final String SYMBOL = "symbol";
//        public static final String RATE = "rate";
//        public static final String AUSTRALIAN_RAT = "australian_dollar";
//        public static final String CANADIAN_RAT = "canadian_dollar";
//        public static final String EURO_RAT = "euro";
//        public static final String HONG_KONG_RAT = "hong_kong_dollar";
//        public static final String MEXICAN_RAT ="mexican_peso";
//        public static final String NEW_ZEALAND_RAT = "new_zealand_dollar";
//        public static final String US_RAT = "us_dollar";
//
//    }

}
