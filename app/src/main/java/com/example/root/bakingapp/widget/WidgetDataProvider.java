package com.example.root.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.root.bakingapp.pojo.Ingredient;
import com.example.root.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by root on 1/18/18.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    ArrayList<Ingredient> ingredients;
    private Context ctxt=null;
    private int appWidgetId;

    public WidgetDataProvider(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(ingredients.size());
    }



    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.row_widget);
        row.setTextViewText(R.id.widget_recipe_name, ingredients.get(position).getIngredient());
        row.setTextViewText(R.id.widget_recipe_measure, ingredients.get(position).getQuantity()
                + " " + ingredients.get(position).getMeasure());
        return(row);
    }

    @Override
    public void onDataSetChanged() {
        ingredients= new ArrayList<>();
        WidgetModel model= (WidgetModel) WidgetProvider.DB.getObject(String.valueOf(appWidgetId),WidgetModel.class);
        ingredients=model.getIngredients();

    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }
}