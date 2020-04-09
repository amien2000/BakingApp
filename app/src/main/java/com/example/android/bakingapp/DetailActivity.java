package com.example.android.bakingapp;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.ui.DetailFragment;

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

    @Nullable @BindView(R.id.textViewDescription) TextView textViewDescription;
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

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(extras);
        FragmentManager fragmentManager = getSupportFragmentManager();

        //To prevent fragment overlapping during configuration change, only add when there isn't fragment available.
        if(fragmentManager.findFragmentById(R.id.fragmentOne)==null) {
            fragmentManager.beginTransaction().add(R.id.fragmentOne, detailFragment).commit(); }

        if(tablet != null){
                textViewDescription.setText(R.string.tablet_instruction);
        }
    }
}
