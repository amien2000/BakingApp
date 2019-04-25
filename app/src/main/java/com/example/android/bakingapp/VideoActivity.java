package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.ExoUtil;
import com.example.android.bakingapp.utils.NetworkUtil;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private ArrayList<Step> stepArrayList;
    Bundle extras;
    String name;
    int position;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("name");

        position = extras.getInt("position");
        stepArrayList = extras.getParcelableArrayList("steps");
        url = stepArrayList.get(position).getVideoURL();

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
