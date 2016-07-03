package com.biz.timux.capstone.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by gaojianxun on 16/6/28.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String COUNTRY_QUERY = "Netherlands";

    private static final Uri TEST_COUNTRY_DIR = CountryContract.CountryEntry.CONTENT_URI;
    private static final Uri TEST_COUNTRY_WITH_COUNTRY_NAME =
            CountryContract.CountryEntry.buildCountryNameUri(COUNTRY_QUERY);
    private static final Uri TEST_VACCINATION_WITH_COUNTRY_DIR =
            CountryContract.VaccinationEntry.buildVaccinationCountry(COUNTRY_QUERY);
    private static final Uri TEST_VACCINATION_DIR = CountryContract.VaccinationEntry.CONTENT_URI;

    public void testUriMatcher(){

        UriMatcher matcher = CountryDetailsProvider.buildUriMatcher();

        assertEquals("Error: ", matcher.match(TEST_COUNTRY_DIR), CountryDetailsProvider.COUNTRY);

        assertEquals("Error: ", matcher.match(TEST_COUNTRY_WITH_COUNTRY_NAME), CountryDetailsProvider.COUNTRY_NAME);

        assertEquals("Error: ", matcher.match(TEST_VACCINATION_WITH_COUNTRY_DIR), CountryDetailsProvider.VACCINATION_COUNTRY);

        assertEquals("Error: ", matcher.match(TEST_VACCINATION_DIR), CountryDetailsProvider.VACCINATION);
    }


}
