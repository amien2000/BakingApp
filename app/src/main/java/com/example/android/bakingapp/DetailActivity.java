package com.example.android.bakingapp;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.ui.DetailFragment;
import com.example.android.bakingapp.ui.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    String name;
    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Step> stepArrayList;
    String url;
    String description;
    Bundle extras;
    VideoFragment videoFragment;
    DetailFragment detailFragment;

    @Nullable @BindView(R.id.linearLayoutsw600dp) LinearLayout tablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            addTabFragment();
        }
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

    public void addTabFragment(){
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentOne, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}
