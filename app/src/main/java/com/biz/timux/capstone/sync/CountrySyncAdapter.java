package com.biz.timux.capstone.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.biz.timux.capstone.data.Countries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by gaojianxun on 16/6/12.
 */
public class CountrySyncAdapter extends AbstractThreadedSyncAdapter{

    private static final String TAG = CountrySyncAdapter.class.getSimpleName();

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

        String format = "json";

        try{

            final String BASE_URL = "https://travelbriefing.org/";
            final String SUFFIX = "?format=json";

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
                    //set network status
                    return;
                }

                countryJsonStr = buffer.toString();
                getCountryDataFromJson(countryJsonStr);
                Log.d(TAG, countryJsonStr);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            //set network status
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
            //set network status
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

        final String COUNTRY_ADVICE = "advice";
        final String COUNTRY_ADVICE_UA = "UA";
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
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        //set network status
                        break;
                    default:
                        //set network status
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

            try {
                JSONArray language = countryJson.getJSONArray(COUNTRY_LANGUAGE);

                if (language.length() >= 0) {
                    JSONObject lang = language.getJSONObject(0);
                    String languageName = lang.getString(COUNTRY_LANGUAGE);
                    String official = lang.getString(COUNTRY_OFFICIAL);
                    Log.d(TAG, "Language is " + languageName + " and is official " + official);
                }
            } catch (JSONException e){
                Log.d(TAG, "no language!");
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
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


            // vaccination
            try {

                JSONArray vaccinations = countryJson.getJSONArray(COUNTRY_VACCINATIONS);

                Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(vaccinations.length());

                for (int i = 0; i < vaccinations.length(); i++) {

                    JSONObject v = vaccinations.getJSONObject(i);
                    String vName = v.getString(COUNTRY_VACCINATIONS_NAME);
                    String vMsg = v.getString(COUNTRY_VACCINATIONS_MSG);
                    Log.d(TAG, "Vaccination is " + vName + " and Message is " + vMsg);

                    //add to table vaccination
                }
            } catch (JSONException e){
                Log.d(TAG, "no vaccination");
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // currency
            JSONObject currency = countryJson.getJSONObject(COUNTRY_CURRENCY);
            String currencyName = currency.getString(COUNTRY_CURRENCY_NAME);
            String currencyCode = currency.getString(COUNTRY_CURRENCY_CODE);
            String currencySymbol = currency.getString(COUNTRY_CURRENCY_SYMBOL);
            String currencyRate = currency.getString(COUNTRY_CURRENCY_RATE);
            Log.d(TAG,"Currency - " + currencyName + " " + currencyCode + " " + currencySymbol + " " + currencyRate);

            try {
                JSONArray compare = currency.getJSONArray(COUNTRY_CURRENCY_COMPARE);

                for (int i = 0; i < compare.length(); i++) {


                    JSONObject v = compare.getJSONObject(i);
                    String name = v.getString(COUNTRY_CURRENCY_COMPARE_NAME);
                    String australianRate;
                    String canadianRate;
                    String euroRate;
                    String hgRate;
                    String mexicanRate;
                    String newzealandRate;
                    String usRate;

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
            Log.d(TAG, "weater in January " + janWeather);
            String febWeather = weather.getJSONObject(COUNTRY_WEATHER_FEB).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in February " + febWeather);
            String marWeather = weather.getJSONObject(COUNTRY_WEATHER_MAR).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in March " + marWeather);
            String aprWeather = weather.getJSONObject(COUNTRY_WEATHER_APR).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in April " + aprWeather);
            String mayWeather = weather.getJSONObject(COUNTRY_WEATHER_MAY).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in May " + mayWeather);
            String junWeather = weather.getJSONObject(COUNTRY_WEATHER_JUN).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in June " + junWeather);
            String julWeather = weather.getJSONObject(COUNTRY_WEATHER_JUL).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in July " + julWeather);
            String augWeather = weather.getJSONObject(COUNTRY_WEATHER_AUG).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in August " + augWeather);
            String sepWeather = weather.getJSONObject(COUNTRY_WEATHER_SEP).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in September " + sepWeather);
            String octWeather = weather.getJSONObject(COUNTRY_WEATHER_OCT).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in October " + octWeather);
            String novWeather = weather.getJSONObject(COUNTRY_WEATHER_NOV).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in November " + novWeather);
            String decWeather = weather.getJSONObject(COUNTRY_WEATHER_DEC).getString(COUNTRY_WEATHER_TEMP);
            Log.d(TAG, "weater in December " + decWeather);




        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
            //set network status
        }


    }
}
