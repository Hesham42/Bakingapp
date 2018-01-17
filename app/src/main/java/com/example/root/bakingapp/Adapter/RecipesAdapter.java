package com.example.root.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by root on 1/17/18.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecyclerHolder> {

    Context context;
    private ArrayList<Recipe> recipes;


    public RecipesAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_recipes, null);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {

        holder.recipeName.setText(recipes.get(position).getName());
        holder.recipeStepsCount.setText("" + recipes.get(position).getSteps().size());
        holder.recipeServe.setText(""+ recipes.get(position).getServings().toString());
        if (recipes.get(position).getImage()!=""&&!recipes.get(position).getImage().isEmpty())
        {

            Glide.with(context)
                    .load(recipes.get(position).getImage())
                    .into(holder.recipeImage);
        }

    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeStepsCount, recipeServe;
        ImageView recipeImage;

        RecyclerHolder(View itemView) {
            super(itemView);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            recipeStepsCount = (TextView) itemView.findViewById(R.id.recipe_steps_count);
            recipeServe = (TextView) itemView.findViewById(R.id.recipe_servings);
            recipeImage = (ImageView) itemView.findViewById(R.id.image);

        }
    }


}
