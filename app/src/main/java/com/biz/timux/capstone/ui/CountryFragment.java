package com.biz.timux.capstone.ui;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.CountryContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gaojianxun on 16/7/4.
 */
public class CountryFragment extends Fragment implements
        OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = CountryFragment.class.getSimpleName();
    public static final String ARG_COUNTRY_NAME = "country_name";

    private static final int COUNTRY_LOADER = 0;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private CountryDetailAdapter mCountryDetailAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private ScrollView mScrollView;

    public double mLan;
    public double mLng;

    private GoogleMap m_map;
    private boolean mapReady=false;

    private static final String[] COUNTRY_COLUMNS = {
            CountryContract.CountryEntry.TABLE_NAME + "." + CountryContract.CountryEntry._ID,
            CountryContract.CountryEntry.NAME,
            CountryContract.CountryEntry.FULLNAME,
            CountryContract.CountryEntry.ISO2,
            CountryContract.CountryEntry.CONTINENT,
            CountryContract.CountryEntry.MAPS_LAT,
            CountryContract.CountryEntry.MAPS_LONG,
            CountryContract.CountryEntry.TIMEZONE,
            CountryContract.CountryEntry.LANGUAGE,
            CountryContract.CountryEntry.OFFICIAL,
            CountryContract.CountryEntry.VOLTAGE,
            CountryContract.CountryEntry.FREQUENCY,
            CountryContract.CountryEntry.TEL_CODE,
            CountryContract.CountryEntry.TEL_POLICE,
            CountryContract.CountryEntry.TEL_AMB,
            CountryContract.CountryEntry.TEL_FIRE,
            CountryContract.CountryEntry.WATER,
            CountryContract.CountryEntry.ADVISE,
            CountryContract.CountryEntry.URL,
            CountryContract.CountryEntry.JAN_AVG,
            CountryContract.CountryEntry.FEB_AVG,
            CountryContract.CountryEntry.MAR_AVG,
            CountryContract.CountryEntry.APR_AVG,
            CountryContract.CountryEntry.MAY_AVG,
            CountryContract.CountryEntry.JUN_AVG,
            CountryContract.CountryEntry.JUL_AVG,
            CountryContract.CountryEntry.AUG_AVG,
            CountryContract.CountryEntry.SEP_AVG,
            CountryContract.CountryEntry.OCT_AVG,
            CountryContract.CountryEntry.NOV_AVG,
            CountryContract.CountryEntry.DEC_AVG,
            CountryContract.CountryEntry.CUR_NAME,
            CountryContract.CountryEntry.CODE,
            CountryContract.CountryEntry.SYMBOL,
            CountryContract.CountryEntry.RATE,
            CountryContract.CountryEntry.AUSTRALIAN_RAT,
            CountryContract.CountryEntry.CANADIAN_RAT,
            CountryContract.CountryEntry.EURO_RAT,
            CountryContract.CountryEntry.HONG_KONG_RAT,
            CountryContract.CountryEntry.MEXICAN_RAT,
            CountryContract.CountryEntry.NEW_ZEALAND_RAT,
            CountryContract.CountryEntry.US_RAT,
            //CountryContract.VaccinationEntry.COUNTRY_KEY,
            //CountryContract.VaccinationEntry.NAME
    };

    static final int COL_COUNTRY_ID = 0;
    static final int COL_COUNTRY_NAME = 1;
    static final int COL_COUNTRY_FNAME = 2;
    static final int COL_COUNTRY_ISO2 = 3;
    static final int COL_COUNTRY_CONTINENT = 4;
    static final int COL_COUNTRY_MAPS_LAT = 5;
    static final int COL_COUNTRY_MAPS_LONG = 6;
    static final int COL_COUNTRY_TIMEZONE = 7;
    static final int COL_COUNTRY_LANGUAGE = 8;
    static final int COL_COUNTRY_OFFICAL = 9;
    static final int COL_COUNTRY_VOLTAGE = 10;
    static final int COL_COUNTRY_FREQUENCY = 11;
    static final int COL_COUNTRY_TEL_CODE = 12;
    static final int COL_COUNTRY_TEL_POLICE = 13;
    static final int COL_COUNTRY_TEL_AMB = 14;
    static final int COL_COUNTRY_TEL_FIRE = 15;
    static final int COL_COUNTRY_WATER = 16;
    static final int COL_COUNTRY_ADVISE = 17;
    static final int COL_COUNTRY_URL = 18;
    static final int COL_COUNTRY_JAN = 19;
    static final int COL_COUNTRY_FEB = 20;
    static final int COL_COUNTRY_MAR = 21;
    static final int COL_COUNTRY_APR = 22;
    static final int COL_COUNTRY_MAY = 23;
    static final int COL_COUNTRY_JUN = 24;
    static final int COL_COUNTRY_JUL = 25;
    static final int COL_COUNTRY_AUG = 26;
    static final int COL_COUNTRY_SEP = 27;
    static final int COL_COUNTRY_OCT = 28;
    static final int COL_COUNTRY_NOV = 29;
    static final int COL_COUNTRY_DEC = 30;
    static final int COL_COUNTRY_CUR_NAME = 31;
    static final int COL_COUNTRY_CODE = 32;
    static final int COL_COUNTRY_SYMBOL = 33;
    static final int COL_COUNTRY_RATE = 34;
    static final int COL_COUNTRY_AU_RATE = 35;
    static final int COL_COUNTRY_CA_RATE = 36;
    static final int COL_COUNTRY_EU_RATE = 37;
    static final int COL_COUNTRY_HK_RATE = 38;
    static final int COL_COUNTRY_MX_RATE = 39;
    static final int COL_COUNTRY_NZ_RATE = 40;
    static final int COL_COUNTRY_US_RATE = 41;



    public CountryFragment() {
        // Empty constructor required for fragment subclasses
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(COUNTRY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String countryName = getArguments().getString(ARG_COUNTRY_NAME);
        Log.d(TAG,"onCreateLoader - countryName is " + countryName);
        Uri countryInfoUri = CountryContract.CountryEntry.buildCountryNameUri(countryName);
        Log.d(TAG, "onCreateLoader - Uri is " + countryInfoUri);

        Cursor cursor = getActivity().getContentResolver().query(
                countryInfoUri,
                COUNTRY_COLUMNS,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                mLan = Double.parseDouble(cursor.getString(COL_COUNTRY_MAPS_LAT));
                mLng = Double.parseDouble(cursor.getString(COL_COUNTRY_MAPS_LONG));
                Log.d(TAG, " latitude is " + mLan + ". " + " longitude is " + mLng);
                StringBuilder sb = new StringBuilder();
                int columnsQty = cursor.getColumnCount();
                for (int idx = 0; idx < columnsQty; ++idx) {
                    sb.append(cursor.getString(idx));
                    if (idx < columnsQty - 1)
                        sb.append("; ");
                }
                Log.v(TAG, String.format("Row: %d, Values: %s", cursor.getPosition(), sb.toString()));
            } while (cursor.moveToNext()) ;
        }

        StringBuilder sb = new StringBuilder();
        Log.d(TAG, "Cursor contains " + cursor.getColumnCount() + " columns");
        Log.d(TAG, "Cursor - " + cursor);
        cursor.close();

        return new CursorLoader(
                getActivity(),
                countryInfoUri,
                COUNTRY_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCountryDetailAdapter.swapCursor(data);
        if (mPosition != RecyclerView.NO_POSITION){
            mRecyclerView.smoothScrollToPosition(mPosition);
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCountryDetailAdapter.swapCursor(null);

    }

    public static Fragment newInstance(String cname) {
        Fragment fragment = new CountryFragment();
        Bundle args = new Bundle();
        args.putString(CountryFragment.ARG_COUNTRY_NAME, cname);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String name = getArguments().getString(ARG_COUNTRY_NAME);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_country_detials);

        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        // disable RecyclerView scrolling to enable scrollview inside it
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.setNestedScrollingEnabled(true);

        mCountryDetailAdapter = new CountryDetailAdapter(getActivity());

        mRecyclerView.setAdapter(mCountryDetailAdapter);

        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll_view);
        if (null != mScrollView) {
//            mScrollView.setCallbacks(new ObservableScrollView.Callbacks() {
//                @Override
//                public void onScrollChanged() {
//                    mScrollY = mScrollView.getScrollY();
//                    getActivityCast().onUpButtonFloorChanged(mItemId, ArticleDetailFragment.this);
//                    mPhotoContainerView.setTranslationY((int) (mScrollY - mScrollY / PARALLAX_FACTOR));
//                    updateStatusBar();
//                }
//            });
            mScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            });
        } else {
            Log.d(TAG, " Scroll View is null");
        }


        if(mapReady)
            m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });
        getActivity().setTitle(name);
        Log.d(TAG, "Title set!");
        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapReady=true;
        m_map = googleMap;
        LatLng place = setLatLng(mLan,-mLng);
        CameraPosition target = CameraPosition.builder().target(place).zoom(3).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));

    }

    public LatLng setLatLng (double lat, double lng){

        return new LatLng(lat, lng);
    }
}