package com.example.android.bakingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class ExoUtil {

    private View mRootView;
    private Activity mActivity;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    public ExoUtil (Activity activity, View rootView){
        mActivity = activity;
        mRootView = rootView;
    }

    public void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            //Default buffering policy
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mActivity);
            // Create an instance of the ExoPlayer.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

            playerView = mRootView.findViewById(R.id.playerView);
            playerView.setPlayer(simpleExoPlayer);


            // Prepare the MediaSource.
            // String userAgent = Util.getUserAgent(this, "BakingApp");

            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(mActivity, Util.getUserAgent(mActivity, String.valueOf(R.string.app_name)));

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public void initializePlayerActivity(Uri mediaUri) {
        if (simpleExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            //Default buffering policy
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mActivity);
            // Create an instance of the ExoPlayer.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);


            playerView = mActivity.findViewById(R.id.playerView);
            playerView.setPlayer(simpleExoPlayer);


            // Prepare the MediaSource.
            // String userAgent = Util.getUserAgent(this, "BakingApp");

            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(mActivity, Util.getUserAgent(mActivity, String.valueOf(R.string.app_name)));

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }


    public void fullScreenMode() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void nonFullScreenMode() {
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }


    /**
     * Release ExoPlayer.
     */
    public void releasePlayer() {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
    }
}
