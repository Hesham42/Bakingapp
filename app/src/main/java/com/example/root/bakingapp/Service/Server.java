package com.example.root.bakingapp.Service;

import com.example.root.bakingapp.Pojo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by root on 1/17/18.
 */
public interface Server {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
