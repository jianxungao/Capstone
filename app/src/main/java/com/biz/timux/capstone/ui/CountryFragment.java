package com.biz.timux.capstone.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.database.DatabaseUtils;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.CountryContract;

/**
 * Created by gaojianxun on 16/7/4.
 */
public class CountryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = CountryFragment.class.getSimpleName();
    public static final String ARG_COUNTRY_NAME = "country_name";

    private static final int COUNTRY_LOADER = 0;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private CountryDetailAdapter mCountryDetailAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final String[] COUNTRY_COLUMNS = {
            CountryContract.CountryEntry.TABLE_NAME + "." + CountryContract.CountryEntry._ID,
            CountryContract.CountryEntry.NAME,
            CountryContract.CountryEntry.FULLNAME,
            //CountryContract.VaccinationEntry.COUNTRY_KEY,
            //CountryContract.VaccinationEntry.NAME
    };

    static final int COL_COUNTRY_ID = 0;
    static final int COL_COUNTRY_NAME = 1;
    static final int COL_COUNTRY_FNAME = 2;
    static final int COL_COUNTRY_VNAME = 3;

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

        //String col01 = cursor.getString(COL_COUNTRY_ID);
        //String col02 = cursor.getString(COL_COUNTRY_NAME);
        //String col03 = cursor.getString(COL_COUNTRY_FNAME);
        //String col04 = cursor.getString(COL_COUNTRY_VNAME);
        StringBuilder sb = new StringBuilder();
        Log.d(TAG, "Cursor contains " + cursor.getColumnCount());
        //Log.d(TAG, col01 + " " + col02 + " " + col03 + " " + col04);
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

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        mCountryDetailAdapter = new CountryDetailAdapter(getActivity());

        mRecyclerView.setAdapter(mCountryDetailAdapter);
//
//        TextView tv = (TextView) rootView.findViewById(R.id.txTest);
//        //tv.
//        tv.setText(name);

        //ConfigUrl c = new ConfigUrl(name);

        //TextView tv1 = (TextView) rootView.findViewById(R.id.textView1);
        //tv1.setText(Config.setBaseUrl(name).toString());

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
}