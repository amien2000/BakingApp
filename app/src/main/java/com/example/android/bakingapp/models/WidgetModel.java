package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WidgetModel implements Parcelable {
    public String recipeTitle;
    public ArrayList<Ingredient> ingredients;

    public WidgetModel(String recipeTitle, ArrayList<Ingredient> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }

    protected WidgetModel(Parcel in) {
        recipeTitle = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<WidgetModel> CREATOR = new Creator<WidgetModel>() {
        @Override
        public WidgetModel createFromParcel(Parcel in) {
            return new WidgetModel(in);
        }

        @Override
        public WidgetModel[] newArray(int size) {
            return new WidgetModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(recipeTitle);
        parcel.writeTypedList(ingredients);
    }
}