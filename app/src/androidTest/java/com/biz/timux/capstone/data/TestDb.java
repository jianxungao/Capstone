package com.biz.timux.capstone.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.Test;

import java.util.HashSet;

/**
 * Created by gaojianxun on 16/6/28.
 */
public class TestDb extends AndroidTestCase {

    public static final String TAG = TestDb.class.getSimpleName();

    void deleteDb() {
        mContext.deleteDatabase(CountryDBHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteDb();
    }

    public void testCreateDb() throws Throwable{

        final HashSet<String> tableNameHashSet = new HashSet<>();

        tableNameHashSet.add(CountryContract.CountryEntry.TABLE_NAME);
        tableNameHashSet.add(CountryContract.VaccinationEntry.TABLE_NAME);

        mContext.deleteDatabase(CountryDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new CountryDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = null;
        // verify the tables were created
        c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do{
            tableNameHashSet.remove(c.getString(0));
        } while ( c.moveToNext() );
        assertTrue("Error: Database was created without tables", tableNameHashSet.isEmpty());

        // verify the tables contains the correct columns
        c = db.rawQuery("PRAGMA table_info(" + CountryContract.CountryEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that unable to query db for table info. ", c.moveToFirst());

        // build a HashSet of all columns
        final HashSet<String> countryColumnHashSet = new HashSet<String>();
        countryColumnHashSet.add(CountryContract.CountryEntry._ID);
        countryColumnHashSet.add(CountryContract.CountryEntry.NAME);
        countryColumnHashSet.add(CountryContract.CountryEntry.FULLNAME);
        countryColumnHashSet.add(CountryContract.CountryEntry.ISO2);
        countryColumnHashSet.add(CountryContract.CountryEntry.CONTINENT);
        countryColumnHashSet.add(CountryContract.CountryEntry.MAPS_LAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.MAPS_LONG);
        countryColumnHashSet.add(CountryContract.CountryEntry.TIMEZONE);
        countryColumnHashSet.add(CountryContract.CountryEntry.LANGUAGE);
        countryColumnHashSet.add(CountryContract.CountryEntry.OFFICIAL);
        countryColumnHashSet.add(CountryContract.CountryEntry.VOLTAGE);
        countryColumnHashSet.add(CountryContract.CountryEntry.FREQUENCY);
        countryColumnHashSet.add(CountryContract.CountryEntry.TEL_CODE);
        countryColumnHashSet.add(CountryContract.CountryEntry.TEL_POLICE);
        countryColumnHashSet.add(CountryContract.CountryEntry.TEL_AMB);
        countryColumnHashSet.add(CountryContract.CountryEntry.TEL_FIRE);
        countryColumnHashSet.add(CountryContract.CountryEntry.WATER);
        countryColumnHashSet.add(CountryContract.CountryEntry.ADVISE);
        countryColumnHashSet.add(CountryContract.CountryEntry.URL);

        countryColumnHashSet.add(CountryContract.CountryEntry.JAN_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.FEB_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.MAR_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.APR_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.MAY_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.JUN_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.JUL_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.AUG_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.SEP_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.OCT_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.NOV_AVG);
        countryColumnHashSet.add(CountryContract.CountryEntry.DEC_AVG);

        countryColumnHashSet.add(CountryContract.CountryEntry.CUR_NAME);
        countryColumnHashSet.add(CountryContract.CountryEntry.CODE);
        countryColumnHashSet.add(CountryContract.CountryEntry.SYMBOL);
        countryColumnHashSet.add(CountryContract.CountryEntry.RATE);
        countryColumnHashSet.add(CountryContract.CountryEntry.AUSTRALIAN_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.CANADIAN_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.EURO_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.HONG_KONG_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.MEXICAN_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.NEW_ZEALAND_RAT);
        countryColumnHashSet.add(CountryContract.CountryEntry.US_RAT);

        int colInx = c.getColumnIndex("name");
        do {
            String col = c.getString(colInx);
            Log.d(TAG, "column index >> " + col);
            countryColumnHashSet.remove(col);
        } while ( c.moveToNext() );

        assertTrue("Error: The DB does not contain all of the columns", countryColumnHashSet.isEmpty());

        db.close();
    }


    public void testCountryTable(){

        insertCountry();

    }

    public void testVaccinationTable(){

        long rowId = insertCountry();

        assertFalse("Error: Country Not Inserted Correctly", rowId == -1L);

        CountryDBHelper dbHelper = new CountryDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues vacciValue = TestUtilities.createVaccinationValues(rowId);

        long vacciRowId = db.insert(CountryContract.VaccinationEntry.TABLE_NAME, null, vacciValue);
        assertTrue(vacciRowId != -1);

        Cursor vacciCursor = db.query(
                CountryContract.VaccinationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No Records return from query", vacciCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("testInsertDb vaccination failed to validate", vacciCursor, vacciValue);

        assertFalse("Error: More than one record", vacciCursor.moveToNext());

        vacciCursor.close();
        dbHelper.close();

    }

    public long insertCountry(){

        CountryDBHelper dbHelper = new CountryDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testVal = TestUtilities.createCountryValues();

        long rowId;
        rowId = db.insert(CountryContract.CountryEntry.TABLE_NAME,null,testVal);

        assertTrue(rowId != -1);

        Cursor c = db.query(
                CountryContract.CountryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No Records !", c.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Country Query Validation Failed",
                c, testVal);

        assertFalse("Error: More than one record returned",
                c.moveToNext());

        c.close();
        db.close();
        return rowId;
    }
}

