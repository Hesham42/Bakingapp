package com.example.root.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.bakingapp.pojo.Ingredient;
import com.example.root.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by root on 1/18/18.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecyclerHolder> {

    Context context;
    private ArrayList<Ingredient> ingredients;


    public IngredientsAdapter(Context context , ArrayList<Ingredient> ingredients) {
        this.context = context;
        this.ingredients=ingredients;
    }


    @Override
    public IngredientsAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_ingredient,null);
        return new IngredientsAdapter.RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        holder.name.setText(ingredients.get(position).getIngredient());
        holder.quantity.setText(""+ingredients.get(position).getQuantity());
        holder.measure.setText(ingredients.get(position).getMeasure());

    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView name,quantity,measure;

        RecyclerHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
            quantity=(TextView) itemView.findViewById(R.id.quantity);
            measure=(TextView) itemView.findViewById(R.id. measure);
        }
    }

}
