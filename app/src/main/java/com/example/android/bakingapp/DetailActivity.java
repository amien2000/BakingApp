package com.example.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.ui.DetailFragment;
import com.example.android.bakingapp.utils.ExoUtil;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

import static android.view.View.GONE;

public class DetailActivity extends AppCompatActivity {

    String name;
    //String recipe_name;
    private ArrayList<Ingredient> ingredientArrayList;
    private boolean mTwoPane;
    private ArrayList<Step> stepArrayList;
    String url;
    String description;
    TextView textViewDescription;
    Uri uri;
    ExoUtil exoUtil;
    Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        renderView();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("steps",stepArrayList);
        savedInstanceState.putParcelableArrayList("ingredients",ingredientArrayList);
        savedInstanceState.putString("name",name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepArrayList=savedInstanceState.getParcelableArrayList("steps");
        ingredientArrayList=savedInstanceState.getParcelableArrayList("ingredients");
        name=savedInstanceState.getString("name");
    }


    public void renderView(){

        if(findViewById(R.id.linearLayoutsw600dp) != null){
            //In two-pane mode we need the introduction video to be played for each recipe.
            //The position of the video in array list is 0.
            int stepPosition = 0;

            textViewDescription = findViewById(R.id.textViewDescription);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(extras);
            FragmentManager fragmentManager = getSupportFragmentManager();

            //To prevent fragment overlapping during configuration change, only add when there isn't fragment available.
            if(fragmentManager.findFragmentById(R.id.fragmentOne)==null) {
                fragmentManager.beginTransaction().add(R.id.fragmentOne, detailFragment).commit();
            }

            stepArrayList = extras.getParcelableArrayList("steps");
            description = stepArrayList.get(stepPosition).getShortDescription();
            url = stepArrayList.get(stepPosition).getVideoURL();

            textViewDescription.setText(description);

        } else {

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(extras);
            FragmentManager fragmentManager = getSupportFragmentManager();

            //To prevent fragment overlapping during configuration change, only add when there isn't fragment available.
            if(fragmentManager.findFragmentById(R.id.fragmentOne)==null) {
                fragmentManager.beginTransaction().add(R.id.fragmentOne, detailFragment).commit();
            }
        }
    }
}
