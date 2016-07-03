package com.biz.timux.capstone.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by gaojianxun on 16/6/28.
 */
public class TestProvider extends AndroidTestCase {
    public static final String TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(CountryContract.CountryEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(CountryContract.VaccinationEntry.CONTENT_URI, null, null);

        Cursor c = mContext.getContentResolver().query(
                CountryContract.CountryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error(CountryTable): Records not deleted ", 0, c.getCount());
        c.close();

        c = mContext.getContentResolver().query(
                CountryContract.VaccinationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error(VaccinationTable): Records not deleted ", 0, c.getCount());
        c.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName cn = new ComponentName(mContext.getPackageName(),
                CountryDetailsProvider.class.getName());

        try {
            ProviderInfo pi = pm.getProviderInfo(cn, 0);

            assertEquals("Error: CountryDetailProvider registered with authority: " + pi.authority +
                            " instead of authority: " + CountryContract.CONTENT_AUTHORITY,
                    pi.authority, CountryContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: CountryDetailProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(CountryContract.CountryEntry.CONTENT_URI);

        assertEquals("Error: the CountryEntry CONTENT_URI should return CountryEntry.CONTENT_TYPE",
                CountryContract.CountryEntry.CONTENT_TYPE, type);

        String testCountryName = "Netherlands";

        type = mContext.getContentResolver().getType(
                CountryContract.CountryEntry.buildCountryNameUri(testCountryName));
        assertEquals("Error: the CountryEntry CONTENT_URI with Country name should return CountryEntry.CONTENT_ITEM_TYPE",
                CountryContract.CountryEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(
                CountryContract.VaccinationEntry.buildVaccinationCountry(testCountryName)
        );

        assertEquals("Error: ", CountryContract.VaccinationEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(CountryContract.VaccinationEntry.CONTENT_URI);
        assertEquals("Error: ", CountryContract.VaccinationEntry.CONTENT_TYPE, type);

    }

    public void testBasicCountryQuery() {
        CountryDBHelper dbHelper = new CountryDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testVal = TestUtilities.createCountryValues();
        long rowId = TestUtilities.insertNetherLandsValues(mContext);
        assertTrue("Unable to Insert country data to db ", rowId != -1);
        db.close();

        Cursor c = mContext.getContentResolver().query(
                CountryContract.CountryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCurrentRecord("testBasicCountryQuery", c, testVal);
        c.close();
    }


    public void testBasicVaccinationQuery() {
        CountryDBHelper dbHelper = new CountryDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testVal = TestUtilities.createCountryValues();
        long countryRowId = TestUtilities.insertNetherLandsValues(mContext);

        ContentValues vacciVal = TestUtilities.createVaccinationValues(countryRowId);
        long vacciRowId = db.insert(CountryContract.VaccinationEntry.TABLE_NAME, null, vacciVal);
        assertTrue("Unable to Insert(Vaccination)", vacciRowId != -1);
        db.close();


        Cursor c = mContext.getContentResolver().query(
                CountryContract.VaccinationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCurrentRecord("testBasicVaccinationQuery", c, vacciVal);
        c.close();
    }


    public void testInsertReadProvider() {
        ContentValues testVal = TestUtilities.createCountryValues();

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CountryContract.CountryEntry.CONTENT_URI, true, tco);
        Uri countryUri = mContext.getContentResolver().insert(CountryContract.CountryEntry.CONTENT_URI, testVal);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long rowId = ContentUris.parseId(countryUri);

        assertTrue(rowId != -1);

        Cursor c = mContext.getContentResolver().query(
                CountryContract.CountryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating CountryEntry. ",
                c, testVal);

        ContentValues vacciVal = TestUtilities.createVaccinationValues(rowId);

        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(
                CountryContract.VaccinationEntry.CONTENT_URI,
                true,
                tco);

        //
        Uri vacciInsertUri = mContext.getContentResolver().insert(CountryContract.VaccinationEntry.CONTENT_URI, vacciVal);

        assertTrue(vacciInsertUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        Cursor vacciCursor = mContext.getContentResolver().query(
                CountryContract.VaccinationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating VaccinationEntry. ",
                vacciCursor, vacciVal);

        vacciVal.putAll(testVal);

        vacciCursor = mContext.getContentResolver().query(
                CountryContract.VaccinationEntry.buildVaccinationCountry(TestUtilities.TEST_COUNTRY_NAME),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating JOIN TABLE",
                vacciCursor, vacciVal);

    }


    public void testDeleteRecords() {

        testInsertReadProvider();

        TestUtilities.TestContentObserver countryObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CountryContract.CountryEntry.CONTENT_URI, true, countryObserver);


        TestUtilities.TestContentObserver vacciObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CountryContract.VaccinationEntry.CONTENT_URI, true, vacciObserver);

        deleteAllRecordsFromProvider();


        countryObserver.waitForNotificationOrFail();
        vacciObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(countryObserver);
        mContext.getContentResolver().unregisterContentObserver(vacciObserver);

    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 9;

    static ContentValues[] createBulkInsertVaccinationVal(long countryRowId) {
        ContentValues[] returnCV = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues vacci = new ContentValues();
            vacci.put(CountryContract.VaccinationEntry.COUNTRY_KEY, countryRowId);
            vacci.put(CountryContract.VaccinationEntry.NAME, "vaccination " + i);
            vacci.put(CountryContract.VaccinationEntry.DESC, "this is desc");
            returnCV[i]= vacci;
        }
        return returnCV;
    }



    public void testBulkInsert() {

        ContentValues testVal = TestUtilities.createCountryValues();
        Uri countryUri = mContext.getContentResolver().insert(CountryContract.CountryEntry.CONTENT_URI, testVal);
        long countryRowId = ContentUris.parseId(countryUri);

        ContentValues[] bulkInsertCV = createBulkInsertVaccinationVal(countryRowId);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CountryContract.CountryEntry.CONTENT_URI, true, tco);

        int insertCount = mContext.getContentResolver().bulkInsert(CountryContract.VaccinationEntry.CONTENT_URI, bulkInsertCV);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor c = mContext.getContentResolver().query(
                CountryContract.VaccinationEntry.CONTENT_URI,
                null,
                null,
                null,
                CountryContract.VaccinationEntry.NAME + " ASC"
        );

        assertEquals(c.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        c.moveToFirst();

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, c.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating data " + i,
                    c, bulkInsertCV[i]);
        }

        c.close();

    }

    //    static ContentValues[] createBulkInsertCountryVal() {
//
//        String TEST_COUNTRY_NAME = "Country0";
//        ContentValues[] returnCV = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
//
//        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
//            ContentValues countryValues = new ContentValues();
////            countryValues.put(CountryContract.CountryEntry.NAME, new String(TEST_COUNTRY_NAME+i));
//            countryValues.put(CountryContract.CountryEntry.NAME, TEST_COUNTRY_NAME);
//            countryValues.put(CountryContract.CountryEntry.FULLNAME, "Kingdom of the Netherlands");
//            countryValues.put(CountryContract.CountryEntry.ISO2, "NL");
//            countryValues.put(CountryContract.CountryEntry.CONTINENT, "EU");
//            countryValues.put(CountryContract.CountryEntry.MAPS_LAT, 52.21299919);
//            countryValues.put(CountryContract.CountryEntry.MAPS_LONG, 5.2793703);
//            countryValues.put(CountryContract.CountryEntry.TIMEZONE, "Europe/Amsterdam");
//            countryValues.put(CountryContract.CountryEntry.LANGUAGE, "Dutch");
//            countryValues.put(CountryContract.CountryEntry.OFFICIAL, "Yes");
//            countryValues.put(CountryContract.CountryEntry.VOLTAGE, 230);
//            countryValues.put(CountryContract.CountryEntry.FREQUENCY, 50);
//            countryValues.put(CountryContract.CountryEntry.TEL_CODE, "31");
//            countryValues.put(CountryContract.CountryEntry.TEL_POLICE, 112);
//            countryValues.put(CountryContract.CountryEntry.TEL_AMB, 112);
//            countryValues.put(CountryContract.CountryEntry.TEL_FIRE, 112);
//            countryValues.put(CountryContract.CountryEntry.WATER, "safe");
//            //countryValues.put(CountryContract.CountryEntry.VACCINATION, "");
//            countryValues.put(CountryContract.CountryEntry.ADVISE, "Exercise normal safety precautions");
//            countryValues.put(CountryContract.CountryEntry.URL, "http://www.smartraveller.gov.au/zw-cgi/view/Advice/Netherlands");
//
//            countryValues.put(CountryContract.CountryEntry.JAN_AVG, "2.2");
//            countryValues.put(CountryContract.CountryEntry.FEB_AVG, "2.4");
//            countryValues.put(CountryContract.CountryEntry.MAR_AVG, "3.6");
//            countryValues.put(CountryContract.CountryEntry.APR_AVG, "6.13");
//            countryValues.put(CountryContract.CountryEntry.MAY_AVG, "9.73");
//            countryValues.put(CountryContract.CountryEntry.JUN_AVG, "13.46");
//            countryValues.put(CountryContract.CountryEntry.JUL_AVG, "15.86");
//            countryValues.put(CountryContract.CountryEntry.AUG_AVG, "15.3");
//            countryValues.put(CountryContract.CountryEntry.SEP_AVG, "12.86");
//            countryValues.put(CountryContract.CountryEntry.OCT_AVG, "9.4");
//            countryValues.put(CountryContract.CountryEntry.NOV_AVG, "6");
//            countryValues.put(CountryContract.CountryEntry.DEC_AVG, "3.6");
//
//            countryValues.put(CountryContract.CountryEntry.CUR_NAME, "Euro");
//            countryValues.put(CountryContract.CountryEntry.CODE, "EUR");
//            countryValues.put(CountryContract.CountryEntry.SYMBOL, "€");
//            countryValues.put(CountryContract.CountryEntry.RATE, 0.097153);
//            countryValues.put(CountryContract.CountryEntry.AUSTRALIAN_RAT, 1.35);
//            countryValues.put(CountryContract.CountryEntry.CANADIAN_RAT, 1.30);
//            countryValues.put(CountryContract.CountryEntry.EURO_RAT, 0.907);
//            countryValues.put(CountryContract.CountryEntry.HONG_KONG_RAT, 7.75);
//            countryValues.put(CountryContract.CountryEntry.MEXICAN_RAT, 19.8);
//            countryValues.put(CountryContract.CountryEntry.NEW_ZEALAND_RAT, 1.42);
//            countryValues.put(CountryContract.CountryEntry.US_RAT, 1);
//            returnCV[i] = countryValues;
//        }
//        return returnCV;
//    }

}
