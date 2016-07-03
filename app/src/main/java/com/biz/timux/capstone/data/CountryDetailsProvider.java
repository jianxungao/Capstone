package com.biz.timux.capstone.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gaojianxun on 16/6/12.
 */
public class CountryDetailsProvider extends ContentProvider {

    private static final String TAG = CountryDetailsProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SQLiteOpenHelper mOpenHelper;


    static final int COUNTRY = 100;
    static final int COUNTRY_NAME = 101;
    static final int VACCINATION = 200;
    static final int VACCINATION_COUNTRY = 201;


    private static final String sCountryNameSelection =
            CountryContract.CountryEntry.TABLE_NAME+
                    "." + CountryContract.CountryEntry.NAME + " = ? ";

    private static final SQLiteQueryBuilder sVaccinationByCountryQueryBuilder;

    private Cursor getCountryDetailInfo(Uri uri, String[] projection, String sortOrder) {
        String countryName = CountryContract.CountryEntry.getCountryNameFromUri(uri);
        String selection;
        String[] selectionArgs;
        Cursor cursor;
        selection = sCountryNameSelection;
        selectionArgs = new String[]{countryName};

        cursor = mOpenHelper.getReadableDatabase().query(
                CountryContract.CountryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return cursor;
    }

    // Join two tables
    static {
        sVaccinationByCountryQueryBuilder = new SQLiteQueryBuilder();

        sVaccinationByCountryQueryBuilder.setTables(
                CountryContract.VaccinationEntry.TABLE_NAME + " INNER JOIN " +
                        CountryContract.CountryEntry.TABLE_NAME +
                        " ON " + CountryContract.VaccinationEntry.TABLE_NAME +
                        "." + CountryContract.VaccinationEntry.COUNTRY_KEY +
                        " = " + CountryContract.CountryEntry.TABLE_NAME +
                        "." + CountryContract.CountryEntry._ID);
    }

    private Cursor getVaccinationInfoByCountryName(Uri uri, String[] projection){
        String countryName = CountryContract.VaccinationEntry.getCountryNameFromUri(uri);
        String selection;
        String[] selectionArgs;
        selection = sCountryNameSelection;
        selectionArgs = new String[] {countryName};

        return sVaccinationByCountryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
                );
    }

    // UriMatcher
    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CountryContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CountryContract.PATH_COUNTRY, COUNTRY );
        matcher.addURI(authority, CountryContract.PATH_COUNTRY + "/*", COUNTRY_NAME);
        matcher.addURI(authority, CountryContract.PATH_VACCINATION, VACCINATION);
        matcher.addURI(authority, CountryContract.PATH_VACCINATION + "/*", VACCINATION_COUNTRY);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new CountryDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){

            case COUNTRY:{
                cursor = mOpenHelper.getReadableDatabase().query(
                        CountryContract.CountryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case COUNTRY_NAME:{
                cursor = getCountryDetailInfo(uri, projection, sortOrder);
                break;
            }

            case VACCINATION:{
                cursor = mOpenHelper.getReadableDatabase().query(
                        CountryContract.VaccinationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case VACCINATION_COUNTRY:{
                cursor = getVaccinationInfoByCountryName(uri, projection);
                break;
            }
        }

        if (null != cursor){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } else {
            Log.d(TAG, "cursor is null");
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){

            case COUNTRY:
                return CountryContract.CountryEntry.CONTENT_TYPE;
            case COUNTRY_NAME:
                return CountryContract.CountryEntry.CONTENT_ITEM_TYPE;
            case VACCINATION:
                return CountryContract.VaccinationEntry.CONTENT_TYPE;
            case VACCINATION_COUNTRY:
                return CountryContract.VaccinationEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case COUNTRY:{
                long _id = db.insert(CountryContract.CountryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CountryContract.CountryEntry.buildCountryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case VACCINATION:{
                long _id = db.insert(CountryContract.VaccinationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CountryContract.VaccinationEntry.buildVaccinationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection ) selection = "1";

        switch (match){
            case COUNTRY:
                rowsDeleted = db.delete(CountryContract.CountryEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case VACCINATION:
                rowsDeleted = db.delete(CountryContract.VaccinationEntry.TABLE_NAME, selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case COUNTRY_NAME:
                rowsUpdated = db.update(CountryContract.CountryEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            case VACCINATION_COUNTRY:
                rowsUpdated = db.update(CountryContract.VaccinationEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTRY: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CountryContract.CountryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case VACCINATION:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CountryContract.VaccinationEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
