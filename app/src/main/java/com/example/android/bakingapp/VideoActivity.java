package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.ExoUtil;
import com.example.android.bakingapp.utils.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.widget.Toast;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Step> stepArrayList;
    Bundle extras;
    String name;
    int position;
    String url;

    ExoUtil exoUtil;
    int mVideoNumber;
    String description;
    Uri uri;

    @BindView(R.id.tv_step_title) TextView tvStepTitle;
    @BindView(R.id.tv_step_description) TextView tvStepDescription;
    @BindView(R.id.btn_previous_step1) Button btnPreviousStep;
    @BindView(R.id.btn_next_step1) Button btnNextStep;
    @BindView(R.id.grp_non_fullscreen) Group grpNonFullScreen;
    @BindView(R.id.guidelineUpper) Guideline gdLineUpper;
    @BindView(R.id.guidelineLower) Guideline gdLineLower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        extras = getIntent().getBundleExtra("bundle");
        name = extras.getString("name");
        position = extras.getInt("position");
        stepArrayList = extras.getParcelableArrayList("steps");
        url = stepArrayList.get(position).getVideoURL();

        mVideoNumber = extras.getInt("position");
        description = stepArrayList.get(mVideoNumber).getDescription();
        tvStepDescription.setText(description);

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeExoPlayer(mVideoNumber);
        renderView(getResources().getConfiguration());
    }

    @Override
    public void onDestroy() {
        //Release the player when the activity is destroyed.
        super.onDestroy();
        if (exoUtil != null) {
            exoUtil.releasePlayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renderView(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_previous_step1:
                if(mVideoNumber==0) {
                    Toast toast = Toast.makeText(this,R.string.next_step,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    mVideoNumber--;
                    exoUtil.releasePlayer();
                    description = stepArrayList.get(mVideoNumber).getDescription();
                    tvStepDescription.setText(description);
                    initializeExoPlayer(mVideoNumber);
                }
                break;
            case R.id.btn_next_step1:
                if(mVideoNumber==stepArrayList.size()-1){
                    Toast toast = Toast.makeText(this,R.string.previous_step,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {
                    mVideoNumber++;
                    exoUtil.releasePlayer();
                    description = stepArrayList.get(mVideoNumber).getDescription();
                    tvStepDescription.setText(description);
                    initializeExoPlayer(mVideoNumber);
                    break;
                }
        }
    }

    private void initializeExoPlayer(int videoNumber) {
        url = stepArrayList.get(videoNumber).getVideoURL();
        //Make sure internet and video are available to run the video link
        NetworkUtil networkUtil = new NetworkUtil(this, url);
        networkUtil.networkMessage();
        if(url!="") {
            exoUtil = new ExoUtil(this,null );
            uri = Uri.parse(url);


            exoUtil.initializePlayerActivity(uri);
        }
    }

    public void renderView(Configuration newConfig){
        if(findViewById(R.id.linearLayoutsw600dp) == null) {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gdLineUpper.setGuidelinePercent(0);
                gdLineLower.setGuidelinePercent(1);
                grpNonFullScreen.setVisibility(View.GONE);
                exoUtil.fullScreenMode();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                gdLineUpper.setGuidelinePercent(0.15f);
                gdLineLower.setGuidelinePercent(0.85f);
                grpNonFullScreen.setVisibility(View.VISIBLE);
                btnPreviousStep.setOnClickListener(this);
                btnNextStep.setOnClickListener(this);
                exoUtil.nonFullScreenMode();
            }
        }
    }
}
