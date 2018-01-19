package com.example.root.bakingapp.Widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.root.bakingapp.Pojo.Ingredient;
import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Service.COMM;
import com.example.root.bakingapp.Service.Client;
import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by root on 1/18/18.
 */

public class WidgetActivity extends Activity implements COMM {
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ProgressDialog dialog;
    private ArrayList<Recipe> recipes;
    private Spinner spinner;
    View.OnClickListener mOnClickListener =
            new View.OnClickListener() {
                public void onClick(View v) {
                    final Context context = WidgetActivity.this;

                    int position=spinner.getSelectedItemPosition();

                    WidgetModel model=new WidgetModel(recipes.get(position).getName(),
                            (ArrayList<Ingredient>) recipes.get(position).getIngredients());
                    WidgetProvider.DB.putObject(String.valueOf(mAppWidgetId),model);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    WidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();
                }
            };

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.recipe_widget_configure);
        spinner= (Spinner) findViewById(R.id.spinner);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.show();
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
         Client.getRecipes(this);

        }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, "There is a problem try again later ! or make sure you are connected to internet"
                , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        dialog.dismiss();
        recipes = (ArrayList<Recipe>) response.body();

        String[]values= new  String [recipes.size()];
        for(int i=0; i < recipes.size();i++)
        {
            values[i]=recipes.get(i).getName();
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    }


