package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonEndPoint {

    @GET("baking.json")
    Call<List<Recipe>> getBaking();
}
