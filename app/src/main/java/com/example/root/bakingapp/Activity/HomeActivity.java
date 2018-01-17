package com.example.root.bakingapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.root.bakingapp.Adapter.RecipesAdapter;
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

public class HomeActivity extends AppCompatActivity implements COMM{
    ArrayList<Recipe> recipes;
    @BindView(R.id.recipesList)
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);
        Client.getRecipes(HomeActivity.this);

        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
         linearLayoutManager = new LinearLayoutManager(HomeActivity.this,
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(gridLayoutManager);

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                        String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                        if (networkStatus == "connected") {

                            Client.getRecipes(HomeActivity.this);
                        } else if (networkStatus == "disconnected") {
                            Toast.makeText(getApplicationContext(), "ther is no internet Connection pleas open the internet", Toast.LENGTH_LONG).show();

                        }
                    }
                }, intentFilter);


            OnClickELement();

    }

    private void OnClickELement() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(HomeActivity.this, recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
             Intent intent = new Intent(HomeActivity.this,RecipeDetailsActivity.class);
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
           gridLayoutManager = new GridLayoutManager( HomeActivity.this,
                    3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false);
            recyclerView.setLayoutManager(linearLayoutManager);

        }
            recyclerView.setAdapter(new RecipesAdapter(HomeActivity.this, recipes));

    }
}
