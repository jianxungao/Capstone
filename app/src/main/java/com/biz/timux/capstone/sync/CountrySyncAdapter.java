package com.biz.timux.capstone.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.Countries;
import com.biz.timux.capstone.data.CountryContract;
import com.biz.timux.capstone.utils.CustomFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by gaojianxun on 16/6/12.
 */
public class CountrySyncAdapter extends AbstractThreadedSyncAdapter{

    private static final String TAG = CountrySyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED = "com.biz.timux.capstone.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 600 = 10 hours
    public static final int SYNC_INTERVAL = 60 * 600;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({COUNTRY_STATUS_OK, COUNTRY_STATUS_SERVER_DOWN,  COUNTRY_STATUS_SERVER_INVALID,
            COUNTRY_STATUS_UNKNOWN, COUNTRY_STATUS_INVALID})
    public @interface CountryInfoStatus {}

    public static final int COUNTRY_STATUS_OK = 0;
    public static final int COUNTRY_STATUS_SERVER_DOWN = 1;
    public static final int COUNTRY_STATUS_SERVER_INVALID = 2;
    public static final int COUNTRY_STATUS_UNKNOWN = 3;
    public static final int COUNTRY_STATUS_INVALID = 4;



    public CountrySyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
    }


    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null. Invocations
     * of this method are guaranteed to be serialized.
     *
     * @param account    the account that should be synced
     * @param extras     SyncAdapter-specific parameters
     * @param authority  the authority of this sync request
     * @param provider   a ContentProviderClient that points to the ContentProvider for this
     *                   authority
     * @param syncResult SyncAdapter-specific parameters
     */
    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        Log.d(TAG, "Starting sync data! ");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String countryJsonStr = null;
        //String format = "json";

        try{

            final String BASE_URL = "https://travelbriefing.org/";
            final String SUFFIX = "?format=json";
            //test
            //String country = "france";
            for (String country : Countries.COUNTRIES) {
                System.out.println(country);
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(country)
                        .appendPath(SUFFIX)
                        .build();
                URL url = new URL(builtUri.toString());

                System.out.println(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    setCountryInfoStatus(getContext(), COUNTRY_STATUS_SERVER_DOWN);
                    return;
                }

                countryJsonStr = buffer.toString();
                getCountryDataFromJson(countryJsonStr);
                //Log.d(TAG, countryJsonStr);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            //set network status
            setCountryInfoStatus(getContext(), COUNTRY_STATUS_SERVER_DOWN);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
            //set network status
            setCountryInfoStatus(getContext(), COUNTRY_STATUS_SERVER_INVALID);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e){
                    Log.e(TAG, "Error closing stream!", e);
                }
            }
        }
        return;
    }

    static private void setCountryInfoStatus(Context context, int countryStatus) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.pref_country_info_status_key), countryStatus);
        editor.commit();
    }

    private void getCountryDataFromJson(String countryJsonStr) throws JSONException {
        final String MESSAGE_CODE = "cod";

        final String COUNTRY_NAMES = "names";
        final String COUNTRY_NAME = "name";
        final String COUNTRY_FULL = "full";
        final String COUNTRY_ISO2 = "iso2";
        final String COUNTRY_CONTINENT = "continent";

        final String COUNTRY_MAPS = "maps";
        final String COUNTRY_MAPS_LAT = "lat";
        final String COUNTRY_MAPS_LONG = "long";

        final String COUNTRY_TIMEZONE = "timezone";
        final String COUNTRY_TIMEZONE_NAME = "name";

        final String COUNTRY_LANGUAGE = "language";
        final String COUNTRY_OFFICIAL = "official";

        final String COUNTRY_ELECTRICITY = "electricity";
        final String COUNTRY_VOLTAGE = "voltage";
        final String COUNTRY_FREQUENCY = "frequency";

        final String COUNTRY_TELEPHONE = "telephone";
        final String COUNTRY_CALLING_CODE = "calling_code";
        final String COUNTRY_TEL_POLICE = "police";
        final String COUNTRY_TEL_AMBULANCE = "ambulance";
        final String COUNTRY_TEL_FIRE = "fire";

        final String COUNTRY_WATER = "water";
        final String COUNTRY_WATER_SHORT = "short";

        final String COUNTRY_VACCINATIONS = "vaccinations";
        final String COUNTRY_VACCINATIONS_NAME = "name";
        final String COUNTRY_VACCINATIONS_MSG = "message";

        final String COUNTRY_ADVICE = "advise";
        final String COUNTRY_ADVICE_UA = "UA";
        final String COUNTRY_ADVICE_CA = "CA";
        final String COUNTRY_ADVICE_URL = "url";

        final String COUNTRY_WEATHER = "weather";
        final String COUNTRY_WEATHER_TEMP = "tAvg";
        final String COUNTRY_WEATHER_JAN = "January";
        final String COUNTRY_WEATHER_FEB = "February";
        final String COUNTRY_WEATHER_MAR = "March";
        final String COUNTRY_WEATHER_APR = "April";
        final String COUNTRY_WEATHER_MAY = "May";
        final String COUNTRY_WEATHER_JUN = "June";
        final String COUNTRY_WEATHER_JUL = "July";
        final String COUNTRY_WEATHER_AUG = "August";
        final String COUNTRY_WEATHER_SEP = "September";
        final String COUNTRY_WEATHER_OCT = "October";
        final String COUNTRY_WEATHER_NOV = "November";
        final String COUNTRY_WEATHER_DEC = "December";

        final String COUNTRY_CURRENCY = "currency";
        final String COUNTRY_CURRENCY_NAME = "name";
        final String COUNTRY_CURRENCY_CODE = "code";
        final String COUNTRY_CURRENCY_SYMBOL = "symbol";
        final String COUNTRY_CURRENCY_RATE = "rate";
        final String COUNTRY_CURRENCY_COMPARE = "compare";
        final String COUNTRY_CURRENCY_COMPARE_NAME = "name";
        final String COUNTRY_CURRENCY_AUSTRALIAN = "Australian Dollar";
        final String COUNTRY_CURRENCY_CANADIAN = "Canadian Dollar";
        final String COUNTRY_CURRENCY_EURO = "Euro";
        final String COUNTRY_CURRENCY_HONGKONG = "Hong Kong Dollar";
        final String COUNTRY_CURRENCY_MEXICAN = "Mexican Peso";
        final String COUNTRY_CURRENCY_NEWZEALAND = "New Zealand Dollar";
        final String COUNTRY_CURRENCY_US = "US Dollar";


        try {

            JSONObject countryJson = new JSONObject(countryJsonStr);
            if (countryJson.has(MESSAGE_CODE)){
                int errorCode = countryJson.getInt(MESSAGE_CODE);

                switch (errorCode){
                    case HttpURLConnection.HTTP_OK:
                        Log.d(TAG, "http connection is ok. ");
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        //set network status
                        setCountryInfoStatus(getContext(), COUNTRY_STATUS_INVALID);
                        Log.d(TAG, "http connection is broken, server not found. ");
                        break;
                    default:
                        //set network status
                        setCountryInfoStatus(getContext(), COUNTRY_STATUS_SERVER_DOWN);
                        Log.d(TAG, "http connection is broken, server down. ");
                        return;
                }
            }

            JSONObject countryNames = countryJson.getJSONObject(COUNTRY_NAMES);
            String countryName = countryNames.getString(COUNTRY_NAME);
            String fullName = countryNames.getString(COUNTRY_FULL);
            String iso2 = countryNames.getString(COUNTRY_ISO2);
            String continent = countryNames.getString(COUNTRY_CONTINENT);
            Log.d(TAG, "Country name-> " + countryName + " - " + fullName +  " - " + iso2 + " - " + continent);

            JSONObject countryMaps = countryJson.getJSONObject(COUNTRY_MAPS);
            //double mapLat = Double.parseDouble(countryMaps.getString(COUNTRY_MAPS_LAT));
            double mapLat = countryMaps.getDouble(COUNTRY_MAPS_LAT);
            double mapLong = Double.parseDouble(countryMaps.getString(COUNTRY_MAPS_LONG));
            Log.d(TAG, "Map-> " + mapLat + " - " + mapLong);

            JSONObject timezone = countryJson.getJSONObject(COUNTRY_TIMEZONE);
            String timezoneName = timezone.getString(COUNTRY_TIMEZONE_NAME);
            Log.d(TAG, "Timezone is " + timezoneName);

            String languageName = null;
            String official = null;
            try {
                JSONArray language = countryJson.getJSONArray(COUNTRY_LANGUAGE);

                if (language.length() >= 0) {
                    JSONObject lang = language.getJSONObject(0);
                    languageName = lang.getString(COUNTRY_LANGUAGE);
                    official = lang.getString(COUNTRY_OFFICIAL);
                    Log.d(TAG, "Language is " + languageName + " and is official " + official);
                }
            } catch (JSONException e){
                e.printStackTrace();
                Log.d(TAG, "no language!");
            }

            JSONObject electricity = countryJson.getJSONObject(COUNTRY_ELECTRICITY);
            String voltage = electricity.getString(COUNTRY_VOLTAGE);
            String frequency = electricity.getString(COUNTRY_FREQUENCY);
            Log.d(TAG, "voltage " + voltage + ", frequency " + frequency);

            JSONObject telephone = countryJson.getJSONObject(COUNTRY_TELEPHONE);
            String calling_code = telephone.getString(COUNTRY_CALLING_CODE);
            String police = telephone.getString(COUNTRY_TEL_POLICE);
            String ambulance = telephone.getString(COUNTRY_TEL_AMBULANCE);
            String fire = telephone.getString(COUNTRY_TEL_FIRE);
            Log.d(TAG, "telephone info " + calling_code + " - " + police + " - " + ambulance + " - " + fire);

            JSONObject water = countryJson.getJSONObject(COUNTRY_WATER);
            String shortDesc = water.getString(COUNTRY_WATER_SHORT);
            Log.d(TAG, "water is safe? " + shortDesc);

            String advise = null;
            String advise_url = null;
            try{
                JSONObject advises = countryJson.getJSONObject(COUNTRY_ADVICE);

                if (null != advises) {
                    try {
                        advise = advises.getJSONObject(COUNTRY_ADVICE_UA).getString(COUNTRY_ADVICE);
                        advise_url = advises.getJSONObject(COUNTRY_ADVICE_UA).getString(COUNTRY_ADVICE_URL);
                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.d(TAG, "no UA try CA");
                    }

                    if (advise == null) {
                        advise = advises.getJSONObject(COUNTRY_ADVICE_CA).getString(COUNTRY_ADVICE);
                        advise_url = advises.getJSONObject(COUNTRY_ADVICE_CA).getString(COUNTRY_ADVICE_URL);
                    }
                }

                Log.d(TAG, "advice: " + advise + " url " + advise_url);
            } catch (JSONException e){
                e.printStackTrace();
                Log.d(TAG, "no advise");
            }


            // currency
            JSONObject currency = countryJson.getJSONObject(COUNTRY_CURRENCY);
            String currencyName = currency.getString(COUNTRY_CURRENCY_NAME);
            String currencyCode = currency.getString(COUNTRY_CURRENCY_CODE);
            String currencySymbol = currency.getString(COUNTRY_CURRENCY_SYMBOL);
            String currencyRate = currency.getString(COUNTRY_CURRENCY_RATE);
            Log.d(TAG,"Currency - " + currencyName + " " + currencyCode + " " + currencySymbol + " " + currencyRate);

            String australianRate = null;
            String canadianRate = null;
            String euroRate = null;
            String hgRate = null;
            String mexicanRate = null;
            String newzealandRate = null;
            String usRate = null;
            try {
                JSONArray compare = currency.getJSONArray(COUNTRY_CURRENCY_COMPARE);

                for (int i = 0; i < compare.length(); i++) {


                    JSONObject v = compare.getJSONObject(i);
                    String name = v.getString(COUNTRY_CURRENCY_COMPARE_NAME);

                    if (name.equals(COUNTRY_CURRENCY_AUSTRALIAN)){
                        australianRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + australianRate);
                    } else if (name.equals(COUNTRY_CURRENCY_CANADIAN)){
                        canadianRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + canadianRate);
                    } else if (name.equals(COUNTRY_CURRENCY_EURO)){
                        euroRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + euroRate);
                    } else if (name.equals(COUNTRY_CURRENCY_HONGKONG)){
                        hgRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + hgRate);
                    } else if (name.equals(COUNTRY_CURRENCY_MEXICAN)){
                        mexicanRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + mexicanRate);
                    } else if (name.equals(COUNTRY_CURRENCY_NEWZEALAND)){
                        newzealandRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + newzealandRate);
                    } else if (name.equals(COUNTRY_CURRENCY_US)){
                        usRate = v.getString(COUNTRY_CURRENCY_RATE);
                        Log.d(TAG, "Rate - " + name + " and is " + usRate);
                    }


                }
            } catch (JSONException e){
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // weather
            JSONObject weather = countryJson.getJSONObject(COUNTRY_WEATHER);
            String janWeather = weather.getJSONObject(COUNTRY_WEATHER_JAN).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in January " + janWeather);
            String febWeather = weather.getJSONObject(COUNTRY_WEATHER_FEB).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in February " + febWeather);
            String marWeather = weather.getJSONObject(COUNTRY_WEATHER_MAR).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in March " + marWeather);
            String aprWeather = weather.getJSONObject(COUNTRY_WEATHER_APR).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in April " + aprWeather);
            String mayWeather = weather.getJSONObject(COUNTRY_WEATHER_MAY).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in May " + mayWeather);
            String junWeather = weather.getJSONObject(COUNTRY_WEATHER_JUN).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in June " + junWeather);
            String julWeather = weather.getJSONObject(COUNTRY_WEATHER_JUL).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in July " + julWeather);
            String augWeather = weather.getJSONObject(COUNTRY_WEATHER_AUG).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in August " + augWeather);
            String sepWeather = weather.getJSONObject(COUNTRY_WEATHER_SEP).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in September " + sepWeather);
            String octWeather = weather.getJSONObject(COUNTRY_WEATHER_OCT).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in October " + octWeather);
            String novWeather = weather.getJSONObject(COUNTRY_WEATHER_NOV).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in November " + novWeather);
            String decWeather = weather.getJSONObject(COUNTRY_WEATHER_DEC).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weather in December " + decWeather);

            // country table
            long countryId;
            Cursor countryCursor = getContext().getContentResolver().query(
                    CountryContract.CountryEntry.CONTENT_URI,
                    new String[]{CountryContract.CountryEntry._ID},
                    CountryContract.CountryEntry.NAME + " =? ",
                    new String[]{countryName},
                    null
            );

            if (countryCursor.moveToFirst()){
                int countryIdIndex = countryCursor.getColumnIndex(CountryContract.CountryEntry._ID);
                countryId = countryCursor.getLong(countryIdIndex);
            } else {
                ContentValues countryValues = new ContentValues();

                countryValues.put(CountryContract.CountryEntry.NAME, countryName);
                countryValues.put(CountryContract.CountryEntry.FULLNAME, fullName);
                countryValues.put(CountryContract.CountryEntry.ISO2, iso2);
                countryValues.put(CountryContract.CountryEntry.CONTINENT, continent);
                countryValues.put(CountryContract.CountryEntry.MAPS_LAT, mapLat);
                countryValues.put(CountryContract.CountryEntry.MAPS_LONG, mapLong);
                countryValues.put(CountryContract.CountryEntry.TIMEZONE, timezoneName);
                countryValues.put(CountryContract.CountryEntry.LANGUAGE, languageName);
                countryValues.put(CountryContract.CountryEntry.OFFICIAL, official);
                countryValues.put(CountryContract.CountryEntry.VOLTAGE, voltage);
                countryValues.put(CountryContract.CountryEntry.FREQUENCY, frequency);
                countryValues.put(CountryContract.CountryEntry.TEL_CODE, calling_code);
                countryValues.put(CountryContract.CountryEntry.TEL_POLICE, police);
                countryValues.put(CountryContract.CountryEntry.TEL_AMB, ambulance);
                countryValues.put(CountryContract.CountryEntry.TEL_FIRE, fire);
                countryValues.put(CountryContract.CountryEntry.WATER, shortDesc);
                //countryValues.put(CountryContract.CountryEntry.VACCINATION, "");
                countryValues.put(CountryContract.CountryEntry.ADVISE, advise);
                countryValues.put(CountryContract.CountryEntry.URL, advise_url);

                countryValues.put(CountryContract.CountryEntry.JAN_AVG, janWeather);
                countryValues.put(CountryContract.CountryEntry.FEB_AVG, febWeather);
                countryValues.put(CountryContract.CountryEntry.MAR_AVG, marWeather);
                countryValues.put(CountryContract.CountryEntry.APR_AVG, aprWeather);
                countryValues.put(CountryContract.CountryEntry.MAY_AVG, mayWeather);
                countryValues.put(CountryContract.CountryEntry.JUN_AVG, junWeather);
                countryValues.put(CountryContract.CountryEntry.JUL_AVG, julWeather);
                countryValues.put(CountryContract.CountryEntry.AUG_AVG, augWeather);
                countryValues.put(CountryContract.CountryEntry.SEP_AVG, sepWeather);
                countryValues.put(CountryContract.CountryEntry.OCT_AVG, octWeather);
                countryValues.put(CountryContract.CountryEntry.NOV_AVG, novWeather);
                countryValues.put(CountryContract.CountryEntry.DEC_AVG, decWeather);

                countryValues.put(CountryContract.CountryEntry.CUR_NAME, currencyName);
                countryValues.put(CountryContract.CountryEntry.CODE, currencyCode);
                countryValues.put(CountryContract.CountryEntry.SYMBOL, currencySymbol);
                countryValues.put(CountryContract.CountryEntry.RATE, currencyRate);
                countryValues.put(CountryContract.CountryEntry.AUSTRALIAN_RAT, australianRate);
                countryValues.put(CountryContract.CountryEntry.CANADIAN_RAT, canadianRate);
                countryValues.put(CountryContract.CountryEntry.EURO_RAT, euroRate);
                countryValues.put(CountryContract.CountryEntry.HONG_KONG_RAT, hgRate);
                countryValues.put(CountryContract.CountryEntry.MEXICAN_RAT, mexicanRate);
                countryValues.put(CountryContract.CountryEntry.NEW_ZEALAND_RAT, newzealandRate);
                countryValues.put(CountryContract.CountryEntry.US_RAT, usRate);

                Uri insertUri = getContext().getContentResolver().insert(
                        CountryContract.CountryEntry.CONTENT_URI,
                        countryValues);

                countryId = ContentUris.parseId(insertUri);

                countryCursor.close();
            }

            // vaccination table
            try {

                JSONArray vaccinations = countryJson.getJSONArray(COUNTRY_VACCINATIONS);

                Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(vaccinations.length());

                for (int i = 0; i < vaccinations.length(); i++) {

                    JSONObject v = vaccinations.getJSONObject(i);
                    String vName = v.getString(COUNTRY_VACCINATIONS_NAME);
                    String vMsg = v.getString(COUNTRY_VACCINATIONS_MSG);
                    Log.d(TAG, "Vaccination is " + vName + " and Message is " + vMsg);

                    //add to table vaccination
                    ContentValues vacciValues = new ContentValues();
                    vacciValues.put(CountryContract.VaccinationEntry.COUNTRY_KEY, countryId);
                    vacciValues.put(CountryContract.VaccinationEntry.NAME, vName);
                    vacciValues.put(CountryContract.VaccinationEntry.DESC,vMsg);

                    contentValuesVector.add(vacciValues);
                }

                if (contentValuesVector.size()>0){
                    ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
                    contentValuesVector.toArray(cvArray);

                    getContext().getContentResolver().bulkInsert(CountryContract.VaccinationEntry.CONTENT_URI, cvArray);

                }

                Log.d(TAG, "Sync Complete for country. " + countryName + "vaccination " + contentValuesVector.size());

            } catch (JSONException e){
                Log.d(TAG, "no vaccination");
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // both tables should be inserted
            Log.d(TAG, "Sync Complete for country. " + countryName);
            setCountryInfoStatus(getContext(), COUNTRY_STATUS_OK);
            updateWidgets();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
            //set network status
            setCountryInfoStatus(getContext(), COUNTRY_STATUS_SERVER_INVALID);
        }


    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        CountrySyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    private void updateWidgets(){

        Context context = getContext();
        Intent intent = new Intent(ACTION_DATA_UPDATED).setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

}
