package com.biz.timux.capstone.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by gaojianxun on 16/6/28.
 */
public class TestCountryContract extends AndroidTestCase{

    private static final String TEST_COUNTRY_NAME = "/Netherlands";

    public void testBuildCountryNameUri(){

        Uri countryNameUri = CountryContract.CountryEntry.buildCountryNameUri(TEST_COUNTRY_NAME);

        assertNotNull("Error: Null Uri returned. " + countryNameUri);
        assertEquals("Error: Country name not properly appended",
                TEST_COUNTRY_NAME, countryNameUri.getLastPathSegment());
        assertEquals("Error: Country name doesn't matach",
                countryNameUri.toString(),
                "content://com.biz.timux.capstone/country/%2FNetherlands"); //[%2F="/"]
    }

}
