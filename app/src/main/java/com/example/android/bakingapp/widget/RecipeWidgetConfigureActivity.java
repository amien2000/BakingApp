package com.example.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.example.android.bakingapp.utils.RetrofitUtil;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.WidgetModel;
import com.example.android.bakingapp.database.Database;

import butterknife.ButterKnife;
import retrofit2.Response;

import butterknife.BindView;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends AppCompatActivity implements RetrofitUtil.OnRequestFinishedListener, View.OnClickListener {

    @BindView(R.id.spinner) Spinner mSpinner;
    @BindView(R.id.add_button) Button mButton;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    String[] mRecipeTitle;
    ArrayList<Recipe> mRecipes;
    ArrayAdapter<String> mSpinnerArrayAdapter;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    int mPositionSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_widget_configure);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.VISIBLE);

        setResult(RESULT_CANCELED);

        // Find the widget from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        if (savedInstanceState == null) {
            getRecipes();
            mButton.setOnClickListener(this);
        }
        }

    @Override
    public void onFailure(String message) {
        mProgressBar.setVisibility(View.INVISIBLE);
        //If internet connection is not available, PREVENT user from entering the widget until connection is available.
        AlertDialog alertDialog = new AlertDialog.Builder(RecipeWidgetConfigureActivity.this).create();
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
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipes = (ArrayList<Recipe>) response.body();
        mRecipeTitle = new  String [mRecipes.size()];
        for(int i = 0; i < mRecipes.size(); i++)
        {
            mRecipeTitle[i]= mRecipes.get(i).getName();
        }
        renderView();
    }

    @Override
    public void onClick(View view) {
        final Context context = RecipeWidgetConfigureActivity.this;
        int position= mSpinner.getSelectedItemPosition();
        WidgetModel model = new WidgetModel(mRecipes.get(position).getName(),
                (ArrayList<Ingredient>) mRecipes.get(position).getIngredients());
        Database db=new Database(RecipeWidgetConfigureActivity.this);
        db.insertItem(model,mAppWidgetId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("recipeKey", mRecipeTitle);
        outState.putParcelableArrayList("ingredientsKey", mRecipes);
        outState.putInt("positionKey", mSpinner.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipeTitle = savedInstanceState.getStringArray("recipeKey");
        mRecipes = savedInstanceState.getParcelableArrayList("ingredientsKey");
        mPositionSpinner = savedInstanceState.getInt("positionKey");
        renderView();
    }

    protected void renderView(){
                mSpinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mRecipeTitle);
                mSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(mSpinnerArrayAdapter);
                mSpinner.setSelection(mPositionSpinner);
    }

    protected  void getRecipes(){
        RetrofitUtil.getRecipes(this);
    }
}

