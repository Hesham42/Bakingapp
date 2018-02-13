package com.example.root.bakingapp.service;

import com.example.root.bakingapp.pojo.Recipe;

import java.util.List;

import retrofit2.Response;

/**
 * Created by root on 1/17/18.
 */

public interface COMM {
    void onFailure(String message);

    void onResponse(Response<List<Recipe>> response);
}
