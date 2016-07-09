package com.biz.timux.capstone.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.biz.timux.capstone.data.CountryContract;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class CountryWidgetRemoteService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            private String[] widgetData = new String[1];
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

                Uri countryUri = CountryContract.CountryEntry.buildCountryNameUri("China");
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                return null;
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
