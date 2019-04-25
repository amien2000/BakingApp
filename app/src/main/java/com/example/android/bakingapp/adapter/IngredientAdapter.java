package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredients;
    private Context context;

    public IngredientAdapter(ArrayList<Ingredient> ingredientArrayList, Context context) {
        this.ingredients = ingredientArrayList;
        this.context = context;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientTextView;
        TextView quantityTextView;
        TextView measureTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTextView = itemView.findViewById(R.id.tv_ingredients);
            quantityTextView = itemView.findViewById(R.id.tv_quantity);
            measureTextView = itemView.findViewById(R.id.tv_measure);
        }

    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        IngredientViewHolder viewHolder = new IngredientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, final int position) {
        holder.ingredientTextView.setText(ingredients.get(position).getIngredient());
        holder.quantityTextView.setText(String.valueOf(ingredients.get(position).getQuantity()));
        holder.measureTextView.setText(ingredients.get(position).getMeasure());
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
