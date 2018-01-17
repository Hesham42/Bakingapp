package com.example.root.bakingapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.bakingapp.Adapter.RecipesAdapter;
import com.example.root.bakingapp.InternetConnection.ConnectivityReceiver;
import com.example.root.bakingapp.InternetConnection.MyApplication;
import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Service.COMM;
import com.example.root.bakingapp.Service.Client;
import com.example.root.bakingapp.Utilites.NetworkStateChangeReceiver;
import com.example.root.bakingapp.Utilites.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

import static com.example.root.bakingapp.Utilites.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class HomeActivity extends AppCompatActivity implements COMM, ConnectivityReceiver.ConnectivityReceiverListener {
    ArrayList<Recipe> recipes;
    @BindView(R.id.recipesList)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);
        SizeLayout_Fun();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                checkConnection();
            }
        });

        if (savedInstanceState == null) {

            recyclerView.setHasFixedSize(true);
            Client.getRecipes(HomeActivity.this);
        }
        OnClickELement();


    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {
            Client.getRecipes(this);
            message = "Good! Connected to Internet";
        } else {
            message = "Sorry! Not connected to internet";
        }

        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void SizeLayout_Fun() {
        DisplayMetrics metrics = new DisplayMetrics();
        HomeActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            gridLayoutManager = new GridLayoutManager(HomeActivity.this,
                    3);
            recyclerView.setLayoutManager(gridLayoutManager);

        } else {
            linearLayoutManager = new LinearLayoutManager(HomeActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.recipes), recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipes = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.recipes));
    }

    private void OnClickELement() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(HomeActivity.this, recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this, RecipeDetailsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onFailure(String message) {
        Toast.makeText(HomeActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        recipes = (ArrayList<Recipe>) response.body();
        recyclerView.setAdapter(new RecipesAdapter(HomeActivity.this, recipes));

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(HomeActivity.this,
                    3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager = new LinearLayoutManager(HomeActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        recyclerView.setAdapter(new RecipesAdapter(HomeActivity.this, recipes));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
