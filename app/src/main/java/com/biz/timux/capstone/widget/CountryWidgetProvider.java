package com.biz.timux.capstone.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.sync.CountrySyncAdapter;
import com.biz.timux.capstone.ui.CountryFragment;
import com.biz.timux.capstone.ui.MainActivity;

/**
 * Created by gaojianxun on 16/7/8.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CountryWidgetProvider extends AppWidgetProvider {

    private static final String TAG = CountryWidgetProvider.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        for (int appWidgetId : appWidgetIds){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_details);

            Intent intent = new Intent(context, MainActivity.class);
            Log.d(TAG, "set Intent! - MainActivity class");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Set up the collection (RemoteAdapter)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            }
            Log.d(TAG, "set Remote Adapter to CountryWidgetRemoteService. ");

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            //Log.d(TAG, "set Intent! - CountryFragment class");

            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            Log.d(TAG, "set Remote View with Pending Intent Template");

            views.setEmptyView(R.id.widget_list, R.id.widget_empty);
            Log.d(TAG, "set Remote Empty View");

            appWidgetManager.updateAppWidget(appWidgetId, views);


        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);

        if (CountrySyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, CountryWidgetRemoteService.class));
    }
}
