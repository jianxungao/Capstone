package com.biz.timux.capstone.ui;

import android.app.ActionBar;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.Countries;
import com.biz.timux.capstone.data.CountryAdapter;
import com.biz.timux.capstone.data.CountryContract;
import com.biz.timux.capstone.data.CountryModel;
import com.biz.timux.capstone.remote.Config;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements
        //NavigationView.OnNavigationItemSelectedListener,
        CountryAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "COUNTRY_DETAILS";
    private FloatingActionButton mFab;
    private DrawerLayout mDrawer;
    //private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCountryName;

    private ActionBar mActionBar;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private ArrayList<CountryModel> mModels;

    private MenuItem mMenuItem;
    private SearchView mSearchView;

    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        //drawer.setDrawerListener(toggle);
        //drawer.setOnCreateContextMenuListener();
        mDrawerToggle.syncState();

        //mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        //mNavigationView.setNavigationItemSelectedListener();
        //mNavigationView.setOnContextClickListener();



        // RecycleView Imp
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mModels = new ArrayList<>();

        for (String country : Countries.COUNTRIES) {
            mModels.add(new CountryModel(country));
        }

        mAdapter = new CountryAdapter(this, mModels, this);
        mRecyclerView.setAdapter(mAdapter);

        CountryFragment countryFragment = (CountryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mToolbar.setTitle(mTitle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
        mMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mMenuItem);
        //mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateDrawer();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                Log.d(TAG, "onQueryTextChange!");
                return false;
            }
        });
        updateDrawer();
        return true;
    }

    public void updateDrawer() {
        mAdapter.notifyDataSetChanged();
        // OR
        //mListView.setAdapter(new AdapterShowingTheRightTitles());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        boolean drawerOpen = mDrawer.isDrawerOpen(GravityCompat.START);
        if (drawerOpen) {

            updateDrawer();
        }

        return super.onPrepareOptionsMenu(menu);
    }


    /* The click listener for RecyclerView in the navigation drawer */
    public void onClick(View view, String name) {
        selectItem(name);
        mSearchView.onActionViewCollapsed();
        Log.d(TAG, "== onClick ==");
    }

    private void selectItem(String cname) {
        // update the main content by replacing fragments
        Fragment fragment = CountryFragment.newInstance(cname);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, FRAGMENT_TAG).commit();

        // update selected item title, then close the drawer
        //setTitle((String)mModels.get(position).toString());
        //setTitle("abc");
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            Log.d(TAG, "Drawer is Open");
        } else {
            super.onBackPressed();
            Log.d(TAG, "Drawer is closed");
        }
    }


    public static class CountryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        public static final String ARG_COUNTRY_NAME = "country_name";

        private RecyclerView mRecyclerView;
        private CountryDetailAdapter mCountryDetailAdapter;
        private int mPosition = RecyclerView.NO_POSITION;

        private static final String[] COUNTRY_COLUMNS = {
                CountryContract.CountryEntry.TABLE_NAME + "." + CountryContract.CountryEntry._ID,
                CountryContract.CountryEntry.NAME,
                CountryContract.CountryEntry.FULLNAME,
                CountryContract.VaccinationEntry.COUNTRY_KEY,
                CountryContract.VaccinationEntry.NAME
        };

        static final int COL_COUNTRY_ID = 0;
        static final int COL_COUNTRY_NAME = 1;

        public CountryFragment() {
            // Empty constructor required for fragment subclasses
        }

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String countryName = getArguments().getString(ARG_COUNTRY_NAME);
            Uri countryInfoUri = CountryContract.CountryEntry.buildCountryNameUri(countryName);

            return new CursorLoader(
                    getActivity(),
                    countryInfoUri,
                    COUNTRY_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        /**
         * Called when a previously created loader has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p/>
         * <p>This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         * <p/>
         * <ul>
         * <li> <p>The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a {@link Cursor}
         * and you place it in a {@link CursorAdapter}, use
         * the {@link CursorAdapter#CursorAdapter(Context,
         * Cursor, int)} constructor <em>without</em> passing
         * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
         * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
         * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
         * from doing its own observing of the Cursor, which is not needed since
         * when a change happens you will get a new Cursor throw another call
         * here.
         * <li> The Loader will release the data once it knows the application
         * is no longer using it.  For example, if the data is
         * a {@link Cursor} from a {@link CursorLoader},
         * you should not call close() on it yourself.  If the Cursor is being placed in a
         * {@link CursorAdapter}, you should use the
         * {@link CursorAdapter#swapCursor(Cursor)}
         * method so that the old Cursor is not closed.
         * </ul>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            mCountryDetailAdapter.swapCursor(data);
            if (mPosition != RecyclerView.NO_POSITION){
                mRecyclerView.smoothScrollToPosition(mPosition);
            }

        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
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

            TextView tv = (TextView) rootView.findViewById(R.id.txTest);
            //tv.
            tv.setText(name);

            //ConfigUrl c = new ConfigUrl(name);

            //TextView tv1 = (TextView) rootView.findViewById(R.id.textView1);
            //tv1.setText(Config.setBaseUrl(name).toString());


            getActivity().setTitle(name);
            return rootView;
        }
    }


}
