package com.biz.timux.capstone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.biz.timux.capstone.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by gaojianxun on 16/6/28.
 */
public class TestUtilities extends AndroidTestCase {

    static final String TEST_COUNTRY_NAME = "Netherlands";

    // for provider
    static void validateCursor(String err, Cursor valueCursor, ContentValues expectedValues){
        assertTrue("Empty cursor returned. " + err, valueCursor.moveToFirst());
        validateCurrentRecord(err, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String err, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        valueCursor.moveToFirst();
        for (Map.Entry<String, Object> entry : valueSet){
            String col = entry.getKey();
            int idx = valueCursor.getColumnIndex(col);
            assertFalse("column '" + col + "' not found. " + err, idx == -1);

            String exp = entry.getValue().toString();
            assertEquals("value '" + entry.getValue().toString() +
                        "' did not match the expected value '" +
                        exp + "'. " + err, exp, valueCursor.getString(idx));
        }

    }

    // prepare inserted data
    static ContentValues createCountryValues(){
        ContentValues countryValues = new ContentValues();

        countryValues.put(CountryContract.CountryEntry.NAME, TEST_COUNTRY_NAME);
        countryValues.put(CountryContract.CountryEntry.FULLNAME, "Kingdom of the Netherlands");
        countryValues.put(CountryContract.CountryEntry.ISO2, "NL");
        countryValues.put(CountryContract.CountryEntry.CONTINENT, "EU");
        countryValues.put(CountryContract.CountryEntry.MAPS_LAT, 52.21299919);
        countryValues.put(CountryContract.CountryEntry.MAPS_LONG, 5.2793703);
        countryValues.put(CountryContract.CountryEntry.TIMEZONE, "Europe/Amsterdam");
        countryValues.put(CountryContract.CountryEntry.LANGUAGE, "Dutch");
        countryValues.put(CountryContract.CountryEntry.OFFICIAL, "Yes");
        countryValues.put(CountryContract.CountryEntry.VOLTAGE, 230);
        countryValues.put(CountryContract.CountryEntry.FREQUENCY, 50);
        countryValues.put(CountryContract.CountryEntry.TEL_CODE, "31");
        countryValues.put(CountryContract.CountryEntry.TEL_POLICE, 112);
        countryValues.put(CountryContract.CountryEntry.TEL_AMB, 112);
        countryValues.put(CountryContract.CountryEntry.TEL_FIRE, 112);
        countryValues.put(CountryContract.CountryEntry.WATER, "safe");
        countryValues.put(CountryContract.CountryEntry.VACCINATION, "");
        countryValues.put(CountryContract.CountryEntry.ADVISE, "Exercise normal safety precautions");
        countryValues.put(CountryContract.CountryEntry.URL, "http://www.smartraveller.gov.au/zw-cgi/view/Advice/Netherlands");

        countryValues.put(CountryContract.CountryEntry.JAN_AVG,"2.2");
        countryValues.put(CountryContract.CountryEntry.FEB_AVG,"2.4");
        countryValues.put(CountryContract.CountryEntry.MAR_AVG,"3.6");
        countryValues.put(CountryContract.CountryEntry.APR_AVG,"6.13");
        countryValues.put(CountryContract.CountryEntry.MAY_AVG,"9.73");
        countryValues.put(CountryContract.CountryEntry.JUN_AVG,"13.46");
        countryValues.put(CountryContract.CountryEntry.JUL_AVG,"15.86");
        countryValues.put(CountryContract.CountryEntry.AUG_AVG,"15.3");
        countryValues.put(CountryContract.CountryEntry.SEP_AVG,"12.86");
        countryValues.put(CountryContract.CountryEntry.OCT_AVG,"9.4");
        countryValues.put(CountryContract.CountryEntry.NOV_AVG,"6");
        countryValues.put(CountryContract.CountryEntry.DEC_AVG,"3.6");

        countryValues.put(CountryContract.CountryEntry.CUR_NAME, "Euro");
        countryValues.put(CountryContract.CountryEntry.CODE, "EUR");
        countryValues.put(CountryContract.CountryEntry.SYMBOL, "€");
        countryValues.put(CountryContract.CountryEntry.RATE, 0.097153);
        countryValues.put(CountryContract.CountryEntry.AUSTRALIAN_RAT, 1.35);
        countryValues.put(CountryContract.CountryEntry.CANADIAN_RAT,1.30);
        countryValues.put(CountryContract.CountryEntry.EURO_RAT,0.907);
        countryValues.put(CountryContract.CountryEntry.HONG_KONG_RAT, 7.75);
        countryValues.put(CountryContract.CountryEntry.MEXICAN_RAT,19.8);
        countryValues.put(CountryContract.CountryEntry.NEW_ZEALAND_RAT, 1.42);
        countryValues.put(CountryContract.CountryEntry.US_RAT, 1);

        return countryValues;
    }

    // for provider
    static long insertNetherLandsValues(Context context){

        CountryDBHelper dbHelper = new CountryDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCountryValues();

        long rowId;
        rowId = db.insert(CountryContract.CountryEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert Netherlands values", rowId != -1);

        return rowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
