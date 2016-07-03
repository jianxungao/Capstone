package com.biz.timux.capstone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.biz.timux.capstone.data.CountryContract.*;

/**
 * Manages a local database for country data.
 *
 * Created by gaojianxun on 16/6/6.
 */
public class CountryDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "countries.db";

    public CountryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        final String CREATE_COUNTRIES_TABLE = "CREATE TABLE " + CountriesEntry.TABLE_NAME + " (" +
//                CountriesEntry._ID + " INTEGER PRIMARY KEY," +
//                CountriesEntry.NAME + " TEXT UNIQUE NOT NULL, " +
//                CountriesEntry.URL + " TEXT NOT NULL, " +
//                " );";

//        final String CREATE_COUNTRY_CURRENCY_TABLE = "CREATE TABLE " + CountryCurrency.TABLE_NAME + " (" +
//                CountryCurrency._ID + " INTEGER PRIMARY KEY," +
//                CountryCurrency.COUNTRY_KEY + " INTEGER NOT NULL," +
//                CountryCurrency.COMPARE_NAME + " TEXT UNIQUE NOT NULL, " +
//                CountryCurrency.COMPARE_RATE + " REAL NOT NULL, " +
//                " FOREIGN KEY (" + CountryCurrency.COUNTRY_KEY + ") REFERENCES " +
//                CountryEntry.TABLE_NAME + " (" + CountryEntry._ID + "), " +
//                " );";
//
        final String CREATE_VACCINATION_TABLE = "CREATE TABLE " + VaccinationEntry.TABLE_NAME + " (" +
                VaccinationEntry._ID + " INTEGER PRIMARY KEY," +
                VaccinationEntry.COUNTRY_KEY + " INTEGER NOT NULL, " +
                VaccinationEntry.NAME + " TEXT, " +
                VaccinationEntry.DESC + " TEXT, " +
                " FOREIGN KEY (" + VaccinationEntry.COUNTRY_KEY + ") REFERENCES " +
                CountryEntry.TABLE_NAME + " (" + CountryEntry._ID + ") " +
                " );";

        final String CREATE_COUNTRY_TABLE = "CREATE TABLE " + CountryEntry.TABLE_NAME + " (" +

                CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CountryEntry.NAME + " TEXT NOT NULL, " +
                CountryEntry.FULLNAME + " TEXT NOT NULL, " +
                CountryEntry.ISO2 + " TEXT NOT NULL, " +
                CountryEntry.CONTINENT + " TEXT NOT NULL, " +
                CountryEntry.MAPS_LAT + " TEXT NOT NULL, " +
                CountryEntry.MAPS_LONG + " TEXT NOT NULL, " +
                CountryEntry.TIMEZONE + " TEXT NOT NULL, " +
                CountryEntry.LANGUAGE + " TEXT NOT NULL, " +
                CountryEntry.OFFICIAL + " TEXT NOT NULL, " +
                CountryEntry.VOLTAGE + " TEXT NOT NULL, " +
                CountryEntry.FREQUENCY + " TEXT NOT NULL, " +
                CountryEntry.TEL_CODE + " TEXT, " +
                CountryEntry.TEL_POLICE + " TEXT, " +
                CountryEntry.TEL_AMB + " TEXT, " +
                CountryEntry.TEL_FIRE + " TEXT, " +
                CountryEntry.WATER + " TEXT, " +
                //CountryEntry.VACCINATION + " TEXT, " +
                CountryEntry.ADVISE + " TEXT, " +
                CountryEntry.URL + " TEXT, " +
                CountryEntry.JAN_AVG + " TEXT NOT NULL, " +
                CountryEntry.FEB_AVG + " TEXT NOT NULL, " +
                CountryEntry.MAR_AVG + " TEXT NOT NULL, " +
                CountryEntry.APR_AVG + " TEXT NOT NULL, " +
                CountryEntry.MAY_AVG + " TEXT NOT NULL, " +
                CountryEntry.JUN_AVG + " TEXT NOT NULL, " +
                CountryEntry.JUL_AVG + " TEXT NOT NULL, " +
                CountryEntry.AUG_AVG + " TEXT NOT NULL, " +
                CountryEntry.SEP_AVG + " TEXT NOT NULL, " +
                CountryEntry.OCT_AVG + " TEXT NOT NULL, " +
                CountryEntry.NOV_AVG + " TEXT NOT NULL, " +
                CountryEntry.DEC_AVG + " TEXT NOT NULL, " +
                CountryEntry.CUR_NAME + " TEXT, " +
                CountryEntry.CODE + " TEXT, " +
                CountryEntry.SYMBOL + " TEXT, " +
                CountryEntry.RATE + " TEXT, " +
                CountryEntry.AUSTRALIAN_RAT + " TEXT, " +
                CountryEntry.CANADIAN_RAT + " TEXT, " +
                CountryEntry.EURO_RAT + " TEXT, " +
                CountryEntry.HONG_KONG_RAT + " TEXT, " +
                CountryEntry.MEXICAN_RAT + " TEXT, " +
                CountryEntry.NEW_ZEALAND_RAT + " TEXT, " +
                CountryEntry.US_RAT + " TEXT " +
                " );";

        sqLiteDatabase.execSQL(CREATE_VACCINATION_TABLE);
        sqLiteDatabase.execSQL(CREATE_COUNTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VaccinationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryEntry.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryCurrency.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CountryWeather.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
