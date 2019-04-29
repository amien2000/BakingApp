package com.example.android.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.ExoUtil;
import com.example.android.bakingapp.utils.NetworkUtil;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private ArrayList<Step> stepArrayList;
    private Integer position;
    ExoUtil exoUtil;
    String url;
    String description;
    Uri uri;
    View rootView;

    //@Nullable @BindView(R.id.linearLayoutsw600dp) LinearLayout tablet;
    @BindView(R.id.playerView) PlayerView playerView;
    @BindView(R.id.horizontalHalf) Guideline horizontalHalf;
    @BindView(R.id.textViewDescription) TextView textViewDescription;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);

        Bundle videoExtras = getActivity().getIntent().getBundleExtra("bundle");
        position = videoExtras.getInt("position");
        stepArrayList = videoExtras.getParcelableArrayList("steps");
        description = stepArrayList.get(position).getDescription();
        textViewDescription.setText(description);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
        if(getActivity().findViewById(R.id.linearLayoutsw600dp) == null) {

            url = stepArrayList.get(position).getVideoURL();

            NetworkUtil networkUtil = new NetworkUtil(getActivity(), url);
            networkUtil.networkMessage();

            uri = Uri.parse(url);
            exoUtil = new ExoUtil(getActivity(), rootView);
            exoUtil.initializePlayer(uri);
        }
        renderFragmentView(getActivity().getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renderFragmentView(newConfig);
    }

    @Override
    public void onDestroy() {
        //Release the player when the activity is destroyed.
        super.onDestroy();
        if (exoUtil != null) {
            exoUtil.releasePlayer();
        }
    }

    public void renderFragmentView(Configuration newConfig){
        if(getActivity().findViewById(R.id.linearLayoutsw600dp) == null) {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                horizontalHalf.setGuidelinePercent(1);
                exoUtil.fullScreenMode();
                textViewDescription.setVisibility(View.INVISIBLE);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                horizontalHalf.setGuidelinePercent(0.5f);
                exoUtil.nonFullScreenMode();
                textViewDescription.setVisibility(View.VISIBLE);
            }
        }
    }
}
