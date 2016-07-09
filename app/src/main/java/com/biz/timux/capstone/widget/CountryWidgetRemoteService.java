package com.biz.timux.capstone.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.CountryContract;
import com.biz.timux.capstone.utils.Utility;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class CountryWidgetRemoteService extends RemoteViewsService{

    private static final String TAG = CountryWidgetRemoteService.class.getSimpleName();

    private static final String[] COUNTRY_COLUMNS = {
            CountryContract.CountryEntry.TABLE_NAME + "." + CountryContract.CountryEntry._ID,
            CountryContract.CountryEntry.NAME,
            CountryContract.CountryEntry.FULLNAME,
            CountryContract.CountryEntry.ISO2,
            CountryContract.CountryEntry.CONTINENT
    };

    static final int COL_COUNTRY_FNAME = 2;
    static final int COL_COUNTRY_ISO2 = 3;
    static final int COL_COUNTRY_CONTINENT = 4;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            private String[] widgetData = new String[1];
            String countryName;
            @Override
            public void onCreate() {
                // nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (null != data) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                countryName = Utility.getFavoriteCountry(CountryWidgetRemoteService.this);
                Log.d(TAG, "get favorite country is " + countryName);
                Uri countryUri = CountryContract.CountryEntry.buildCountryNameUri(countryName);
                Log.d(TAG, "uri is " + countryUri);
                data = getContentResolver().query(countryUri,
                        COUNTRY_COLUMNS,
                        null,
                        null,
                        null);

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (null != data) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                views.setTextViewText(R.id.widget_country_name, countryName);
                views.setTextViewText(R.id.widget_full_name, data.getString(COL_COUNTRY_FNAME));
                Log.d(TAG, " set text widget country full name " + data.getString(COL_COUNTRY_FNAME));
                views.setTextViewText(R.id.widget_iso2, data.getString(COL_COUNTRY_ISO2));
                Log.d(TAG, " set text widget country iso2 " + data.getString(COL_COUNTRY_ISO2));
                views.setTextViewText(R.id.widget_continent, data.getString(COL_COUNTRY_CONTINENT));
                Log.d(TAG, " set text widget country continent " + data.getString(COL_COUNTRY_CONTINENT));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
