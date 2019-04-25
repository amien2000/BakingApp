package com.example.android.bakingapp.models;

import java.util.ArrayList;

public class WidgetModel {
    public String recipeTitle;
    public ArrayList<Ingredient> ingredients;

    public WidgetModel(String recipeTitle, ArrayList<Ingredient> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }
}