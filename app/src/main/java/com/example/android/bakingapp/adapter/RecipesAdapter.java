package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;

import java.util.ArrayList;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecyclerHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Recipe> recipes;


    public RecipesAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipes_list_item, null);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {

        holder.recipeName.setText(recipes.get(position).getName());
        holder.recipeStepsCount.setText("" + recipes.get(position).getSteps().size());

        Glide.with(context)
                .load(recipes.get(position).getImage())
                .placeholder(R.drawable.no_image_found)
                .into(holder.recipeImage);


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
            recipeImage = (ImageView) itemView.findViewById(R.id.image);

        }
    }


}

