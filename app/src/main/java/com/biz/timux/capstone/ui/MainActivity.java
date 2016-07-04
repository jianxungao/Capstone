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
import com.biz.timux.capstone.sync.CountrySyncAdapter;

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
        Uri contentUri = getIntent() != null ? getIntent().getData() : null;

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

        if (savedInstanceState == null){
           // CountryFragment fragment = new CountryFragment();
            if (contentUri != null){
                Log.d(TAG, "here");
            }
        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, countryFragment, FRAGMENT_TAG)
//                .commit();

        CountrySyncAdapter.initializeSyncAdapter(this);

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





}
