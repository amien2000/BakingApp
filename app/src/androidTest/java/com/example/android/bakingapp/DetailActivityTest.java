package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    final public ActivityTestRule<DetailActivity> mDetailActivityTestRule =
            new ActivityTestRule<>(DetailActivity.class, true, false);

    @Test
    public void launchDetailActivity() {
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        Intent details=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("steps",steps);
        bundle.putParcelableArrayList("ingredients",ingredients);
        bundle.putString("name","NAME");
        details.putExtra("bundle",bundle);
        mDetailActivityTestRule.launchActivity(details);
    }
}
