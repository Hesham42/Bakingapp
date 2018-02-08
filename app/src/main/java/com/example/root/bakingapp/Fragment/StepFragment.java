package com.example.root.bakingapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.bakingapp.Adapter.IngredientsAdapter;
import com.example.root.bakingapp.Adapter.StepsAdapter;
import com.example.root.bakingapp.Pojo.Ingredient;
import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.Pojo.Step;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Utilites.OnVersionNameSelectionChangeListener;
import com.example.root.bakingapp.Utilites.RecyclerTouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 1/17/18.
 */
public class StepFragment extends Fragment {
    int index;
    ArrayList<Step> steps = new ArrayList<>();
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    @BindView(R.id.stepsList)
    RecyclerView recycler;
    @BindView(R.id.ingredientList)
    RecyclerView ingredientList;
    LinearLayoutManager ingredientsManager, stepsManager;
    int p1, p2;
    public StepFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.steps_ingredient_fragment, container, false);

        ButterKnife.bind(this, root);


        if (savedInstanceState == null) {
            Bundle extra = getArguments();
            try {

            ingredients = extra.getParcelableArrayList(getResources().getString(R.string.ingredients));
            steps = extra.getParcelableArrayList(getResources().getString(R.string.steps));
            }catch (NullPointerException e){
                Log.e("guinness","there is crach now in it ");
            }
        }else{
            ingredients = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.ingredients));
            steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));
            p1 =savedInstanceState.getInt(getResources().getString(R.string.p1));
            p2 =savedInstanceState.getInt(getResources().getString(R.string.p2));
        }
        ingredientsManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        stepsManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        ingredientList.setLayoutManager(ingredientsManager);
        ingredientList.setAdapter(new IngredientsAdapter(getActivity(), ingredients));
          if(p1 !=0)
        {
            ingredientList.getLayoutManager().scrollToPosition(p1);
        }
        recycler.setLayoutManager(stepsManager);
        recycler.setAdapter(new StepsAdapter(getActivity(), steps));
        if(p2 !=0)
        {
            recycler.getLayoutManager().scrollToPosition(p2);
        }
        recycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recycler, new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        OnVersionNameSelectionChangeListener listener = (OnVersionNameSelectionChangeListener) getActivity();
                        listener.OnSelectionChanged(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extra = getArguments();
        if (extra!=null){
            try {

                ingredients = extra.getParcelableArrayList(getResources().getString(R.string.ingredients));
                steps = extra.getParcelableArrayList(getResources().getString(R.string.steps));
            }catch (NullPointerException e){
                Log.e("guinness","there is crach now in it  Step"+e);
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.ingredients),ingredients);
        outState.putParcelableArrayList(getResources().getString(R.string.steps),steps);
        outState.putInt(getResources().getString(R.string.p1),((LinearLayoutManager)ingredientList.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putInt(getResources().getString(R.string.p2),((LinearLayoutManager)recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
     }

    public void setRecipes(int versionNameIndex, ArrayList<Step> steps, ArrayList<Ingredient> ingredients) {
    this.steps=steps;
    this.ingredients=ingredients;
    this.index=versionNameIndex;
    OnVersionNameSelectionChangeListener listener = (OnVersionNameSelectionChangeListener) getActivity();
    listener.OnSelectionChanged(versionNameIndex);
    }
}