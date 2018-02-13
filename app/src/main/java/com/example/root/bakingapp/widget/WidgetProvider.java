package com.example.root.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.root.bakingapp.activity.HomeActivity;
import com.example.root.bakingapp.R;
import com.mukesh.tinydb.TinyDB;

/**
 * Created by root on 1/18/18.
 */

public class WidgetProvider extends AppWidgetProvider {
    public  static TinyDB DB;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context ctxt, AppWidgetManager appWidgetManager, int appWidgetIds) {

    //   https://inducesmile.com/android/android-how-to-add-search-widget-and-searchview-implementation-in-android-ui/
        DB= new TinyDB(ctxt);
        RemoteViews views= new RemoteViews(ctxt.getPackageName(),R.layout.widget);
        Intent intent= new Intent(ctxt,WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.listViewWidget, intent);
        Intent appIntent = new Intent(ctxt, HomeActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(ctxt
                ,0
                ,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listViewWidget,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, views);


    }

}