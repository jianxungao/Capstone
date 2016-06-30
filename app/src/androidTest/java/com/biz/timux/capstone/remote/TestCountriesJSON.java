package com.biz.timux.capstone.remote;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.test.AndroidTestCase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gaojianxun on 16/6/6.
 */
public class TestCountriesJSON extends AndroidTestCase {

    public static final String TAG = "TestCountriesJSON";


    public void testCountriesJSON() {
        try {

            JSONArray array = RemoteEndpointUtil.fetchJsonArray();

            if (array == null) {
                throw new JSONException("Invalid parsed item array");
            }

            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                System.out.println(object.toString());
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error updating content.", e);
        }
    }




}
