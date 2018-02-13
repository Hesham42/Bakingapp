package com.example.root.bakingapp.service;

import com.example.root.bakingapp.pojo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 1/17/18.
 */

public class Client {
    private static List<Recipe> recipes;
    final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static void getRecipes(final COMM listener) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Server server = retrofit.create(Server.class);
        Call<List<Recipe>> call = server.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                listener.onResponse(response);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
