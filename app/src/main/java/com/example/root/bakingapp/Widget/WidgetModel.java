package com.example.root.bakingapp.Widget;

import com.example.root.bakingapp.Pojo.Ingredient;

import java.util.ArrayList;

/**
 * Created by root on 1/19/18.
 */
public class WidgetModel {
    public String recipeTitle;

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Ingredient> ingredients;

    public WidgetModel(String recipeTitle, ArrayList<Ingredient> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }

}

