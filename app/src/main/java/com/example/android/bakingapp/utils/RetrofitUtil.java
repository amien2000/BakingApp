package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public interface OnRequestFinishedListener {
        void onFailure(String message);
        void onResponse(Response<List<Recipe>> response);
    }


    public static void getRecipes(final OnRequestFinishedListener listener) {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    JsonEndPoint jsonEndPoint = retrofit.create(JsonEndPoint.class);

    Call<List<Recipe>> call = jsonEndPoint.getBaking();

        call.enqueue(new Callback<List<Recipe>>() {

        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            listener.onResponse(response);
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            listener.onFailure(t.getMessage());
        }

    });
}
}
