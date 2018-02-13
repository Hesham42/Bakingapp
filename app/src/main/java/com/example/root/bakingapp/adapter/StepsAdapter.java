package com.example.root.bakingapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.root.bakingapp.pojo.Step;
import com.example.root.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by root on 1/18/18.
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.RecyclerHolder> {

    Context context;
    private ArrayList<Step> steps;



    public StepsAdapter(Context context, ArrayList<Step> steps) {
        this.context = context;
        this.steps = steps;
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_steps, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        holder.title.setText(steps.get(position).getShortDescription());
        holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        holder.title.setTextColor(ContextCompat.getColor(context, R.color.white));

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView title;
        RelativeLayout root;

        RecyclerHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
        }
    }

}

