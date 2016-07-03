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

        assertNotNull("Error(CountryEntry): Null Uri returned. " + countryNameUri);
        assertEquals("Error(CountryEntry): Country name not properly appended",
                TEST_COUNTRY_NAME, countryNameUri.getLastPathSegment());
        assertEquals("Error(CountryEntry): Country name doesn't matach",
                countryNameUri.toString(),
                "content://com.biz.timux.capstone/country/%2FNetherlands"); //[%2F="/"]
    }

    public void testBuildVaccinationUri(){

        Uri vaccinationByCountryUri = CountryContract.VaccinationEntry.buildVaccinationCountry(TEST_COUNTRY_NAME);

        assertNotNull("Error(VaccinationEntry): Null Uri returned. " + vaccinationByCountryUri);
        assertEquals("Error(VaccinationEntry): Country name not properly appended",
                TEST_COUNTRY_NAME, vaccinationByCountryUri.getLastPathSegment());
        assertEquals("Error(VaccinationEntry): Country name doesn't matach",
                vaccinationByCountryUri.toString(),
                "content://com.biz.timux.capstone/vaccination/%2FNetherlands");
    }

}
