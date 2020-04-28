package com.example.android.bakingapp;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ArrayList<Step> stepArrayList;
    private ArrayList<Ingredient> ingredientArrayList;
    Bundle extras;

    @Nullable @BindView(R.id.tv_description) TextView tvDescription;
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
        savedInstanceState.putString("name",name);
        savedInstanceState.putParcelableArrayList("steps",stepArrayList);
        savedInstanceState.putParcelableArrayList("ingredients",ingredientArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name=savedInstanceState.getString("name");
        stepArrayList=savedInstanceState.getParcelableArrayList("steps");
        ingredientArrayList=savedInstanceState.getParcelableArrayList("ingredients");
    }

    public void addTabFragment(){
        name = extras.getString("name");
        stepArrayList = extras.getParcelableArrayList("steps");
        ingredientArrayList = extras.getParcelableArrayList("ingredients");

        DetailFragment detailFragment = DetailFragment.newDetailFragmentInstance(name,stepArrayList,
                ingredientArrayList);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentOne, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    public void setTvDescription(String stepDescription) { //Method for detailFragment to send data
        tvDescription.setText(stepDescription);
    }
}
