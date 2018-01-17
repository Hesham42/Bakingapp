package com.example.root.bakingapp.Fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.root.bakingapp.Pojo.Ingredient;
import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Service.COMM;
import com.example.root.bakingapp.Utilites.OnVersionNameSelectionChangeListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by root on 1/17/18.
 */

public class VersionsFragment extends ListFragment implements COMM {
     ArrayList<Recipe> recipes= new ArrayList<>();
     ArrayList<Ingredient> ingredients= new ArrayList<>();
    // Mandatory empty constructor for the fragment manager to instantiate the fragment

    public VersionsFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] versionName = getResources().getStringArray(R.array.version_names);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, versionName);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        OnVersionNameSelectionChangeListener listener = (OnVersionNameSelectionChangeListener) getActivity();
        listener.OnSelectionChanged(position);
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        recipes = (ArrayList<Recipe>) response.body();


    }
}
