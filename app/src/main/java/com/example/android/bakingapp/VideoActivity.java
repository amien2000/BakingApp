package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.ui.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String KEY_BUNDLE = "bundle";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_POSITION = "position";
    private static final String KEY_NAME = "name";
    private static final Float guideLineFullScreen = 1f;
    private static final Float guideLineNonFullScreen = 0.5f;

    ArrayList<Step> stepArrayList;
    Bundle extras;
    int position;
    String name;

    @BindView(R.id.tv_description) TextView tvStepDescription;
    @BindView(R.id.btn_previous_step1) Button btnPreviousStep;
    @BindView(R.id.btn_next_step1) Button btnNextStep;
    @BindView(R.id.grp_non_fullscreen) Group grpNonFullScreen;
    @BindView(R.id.video_fragment) FrameLayout flVideoFragment;
    @BindView(R.id.gl_bottom) Guideline glBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        extras = getIntent().getBundleExtra(KEY_BUNDLE);
        stepArrayList = extras.getParcelableArrayList(KEY_STEPS);
        position = extras.getInt(KEY_POSITION);
        name = extras.getString(KEY_NAME);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);

        if (savedInstanceState == null) {
        replaceVideoFragment(stepArrayList.get(position));
        }
        renderView(getResources().getConfiguration());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION,position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position=savedInstanceState.getInt(KEY_POSITION);
        renderView(getResources().getConfiguration());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_previous_step1:
                if(position==0) {
                    Toast toast = Toast.makeText(this,R.string.next_step, LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else{
                    position--;
                    updateUIText();
                    replaceVideoFragment(stepArrayList.get(position));
                }
                break;
            case R.id.btn_next_step1:
                if(position==stepArrayList.size()-1){
                    Toast toast = Toast.makeText(this,R.string.previous_step,LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {
                    position++;
                    updateUIText();
                    replaceVideoFragment(stepArrayList.get(position));
                    break;
                }
        }
    }

    public void renderView(Configuration newConfig){
        // Checks the orientation of the screen and implement full screen if landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateUIText();
            glBottom.setGuidelinePercent(guideLineFullScreen);
            grpNonFullScreen.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            glBottom.setGuidelinePercent(guideLineNonFullScreen);
            grpNonFullScreen.setVisibility(View.VISIBLE);
            btnPreviousStep.setOnClickListener(this);
            btnNextStep.setOnClickListener(this);
            updateUIText();
        }
    }

    public void updateUIText(){
        //String shortDescription = stepArrayList.get(position).getShortDescription();
        String description = stepArrayList.get(position).getDescription();
        //getSupportActionBar().setTitle(shortDescription);
        tvStepDescription.setText(description);
    }

    public void replaceVideoFragment(Step step){
        //Extract only video and thumbnail url
        String vidURL = step.getVideoURL();
        String nailURL = step.getThumbnailURL();
        String desc = step.getDescription();

        //Factory method to pass bundle to fragment
        VideoFragment videoFragment = VideoFragment.newVideoFragmentInstance(vidURL,nailURL,desc);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.video_fragment, videoFragment)
                .addToBackStack(null)
                .commit();
    }
}
