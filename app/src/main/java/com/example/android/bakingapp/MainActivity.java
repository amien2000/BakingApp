package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.test.SimpleIdlingResource;
import com.example.android.bakingapp.utils.RecyclerViewItemClickListener;
import com.example.android.bakingapp.utils.RetrofitUtil;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.view.View.GONE;

import android.support.test.espresso.IdlingResource;

public class MainActivity extends AppCompatActivity implements RetrofitUtil.OnRequestFinishedListener {

    Context context;
    ArrayList<Recipe> recipes = new ArrayList<>();
    RecyclerView recycler;
    ProgressBar bar;
    Boolean mTablet;
    int position;
    private IdlingResource mIdlingResource;
    //private CountingIdlingResource mIdlingResource= new CountingIdlingResource("Loading_Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerView);
        bar = findViewById(R.id.progressBar);

        if(savedInstanceState == null)
        {
           // mIdlingResource.increment();
            RetrofitUtil.getRecipes(this);
        }

        if(findViewById(R.id.activity_homesw600dp)!=null)
        {
            mTablet = true;
        } else{
            mTablet = false;
        }

        recycler.addOnItemTouchListener(new RecyclerViewItemClickListener(MainActivity.this, recycler, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent details=new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("steps",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getSteps());
                bundle.putParcelableArrayList("ingredients",
                        (ArrayList<? extends Parcelable>) recipes.get(position).getIngredients());
                bundle.putString("name",recipes.get(position).getName());
                details.putExtra("bundle",bundle);
                startActivity(details);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onFailure(String message) {
        bar.setVisibility(GONE);
        Toast.makeText(MainActivity.this, "No internet connection !", Toast.LENGTH_SHORT).show();
        //reload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        recipes = (ArrayList<Recipe>) response.body();
        bar.setVisibility(GONE);
        renderRecyclerView();
        //reload.setVisibility(GONE);
        //mIdlingResource.decrement();
    }

    public void renderRecyclerView()
    {
        int spanCount;
        if(mTablet)
        {

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                spanCount = 3;
            }else{
                spanCount = 2;
            }
        }
        else
        {
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                spanCount = 2;
            }else{
                spanCount = 1;
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount);
        recycler.setLayoutManager(gridLayoutManager);

        recycler.setAdapter(new RecipesAdapter(MainActivity.this,recipes));
        recycler.getLayoutManager().scrollToPosition(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("recipes",recipes);
        savedInstanceState.putInt("position",((GridLayoutManager)recycler.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipes=savedInstanceState.getParcelableArrayList("recipes");
        position=savedInstanceState.getInt("position");
        renderRecyclerView();
        bar.setVisibility(GONE);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }



    //public void reload(View view)
    //{
    //    bar.setVisibility(View.VISIBLE);
        //reload.setVisibility(GONE);
    //    RetrofitUtil.getRecipes(this);
    //}

    /**
     * Only called from test, creates and returns a new {@link HomeIdlingResource}.
     */
    //@VisibleForTesting
    //@NonNull
   // public CountingIdlingResource getIdlingResource() {
    //    return mIdlingResource;
    //}

    //@Override
    //public void onConfigurationChanged(Configuration newConfig) {
    //    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    //    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

    //   } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

    //    }
    // }

}
