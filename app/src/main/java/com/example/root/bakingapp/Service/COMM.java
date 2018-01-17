package com.example.root.bakingapp.Service;

import com.example.root.bakingapp.Pojo.Recipe;

import java.util.List;

import retrofit2.Response;

/**
 * Created by root on 1/17/18.
 */

public interface COMM {
    void onFailure(String message);

    void onResponse(Response<List<Recipe>> response);
}
