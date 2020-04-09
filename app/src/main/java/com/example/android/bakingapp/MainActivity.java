package com.example.android.bakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.test.SimpleIdlingResource;
import com.example.android.bakingapp.utils.RecyclerViewItemClickListener;
import com.example.android.bakingapp.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements RetrofitUtil.OnRequestFinishedListener {

    Context context;
    ArrayList<Recipe> recipes = new ArrayList<>();
    @BindView(R.id.recyclerView) RecyclerView recycler;
    @BindView(R.id.progressBar) ProgressBar bar;
    @Nullable @BindView(R.id.activity_homesw600dp) RelativeLayout tablet;
    Boolean mTablet;
    Boolean mDataDownLoaded;
    int mItemPosition;
    private IdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(tablet!=null)
        {
            mTablet = true;
        }else{
            mTablet = false;
        }
        if(savedInstanceState == null)
        {
            getRecipes();
        }
    }

    @Override
    public void onFailure(String message) {
        bar.setVisibility(GONE);
        mDataDownLoaded = false;
        //If internet connection is not available, PREVENT user from entering the widget until connection is available.
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.alert_dialog_title);
        alertDialog.setMessage(getString(R.string.alert_dialog_message));
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,getString(R.string.retry),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int i){
                getRecipes();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,getString(R.string.exit),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int i){
                finish();
                System.exit(0);
            }
        });
        alertDialog.setCancelable(false);//Prevent use from swiping the screen and dismiss the dialog box
        alertDialog.show();
    }

    @Override
    public void onResponse(Response<List<Recipe>> response) {
        recipes = (ArrayList<Recipe>) response.body();
        bar.setVisibility(GONE);
        mDataDownLoaded = true;
        renderRecyclerView();
    }

    public void renderRecyclerView()
    {
        int spanCount;
        if(mTablet)
        {
            if(this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE){
                spanCount = 3;
            }else{
                spanCount = 2;
            }
        }
        else
        {
            if(this.getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE){
                spanCount = 2;
            }else{
                spanCount = 1;
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager
                (MainActivity.this, spanCount);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(new RecipesAdapter(MainActivity.this,recipes));
        if(recycler.getLayoutManager()!=null) {
            recycler.getLayoutManager().scrollToPosition(mItemPosition);
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
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("recipes", recipes);
        savedInstanceState.putBoolean("dataDownloaded",mDataDownLoaded);
        if (recycler.getLayoutManager()!=null) {
            savedInstanceState.putInt("itemPosition", ((GridLayoutManager) recycler.getLayoutManager())
                    .findFirstVisibleItemPosition());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipes = savedInstanceState.getParcelableArrayList("recipes");
        mDataDownLoaded = savedInstanceState.getBoolean("dataDownloaded");
        mItemPosition = savedInstanceState.getInt("itemPosition");
        if (mDataDownLoaded) {
            renderRecyclerView();
            bar.setVisibility(GONE);
        }else{
            getRecipes();
        }
    }

    protected  void getRecipes(){
        bar.setVisibility(View.VISIBLE);
        RetrofitUtil.getRecipes(this);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
