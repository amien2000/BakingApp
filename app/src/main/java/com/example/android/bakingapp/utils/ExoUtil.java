package com.example.android.bakingapp.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.example.android.bakingapp.R;
import com.google.android.exoplayer2.C;
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

public class ExoUtil {

    private View mRootView;
    private Activity mActivity;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    //16/4
    private boolean playWhenReady = true;
    private int currentWindowIndex;
    private long currentPosition;

    public ExoUtil (Activity activity, View rootView){
        mActivity = activity;
        mRootView = rootView;
    }

    public void initializePlayerFragment(Uri mediaUri, boolean playWhenReady, int currentWindowIndex, long currentPosition) {
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
            if (currentPosition != C.TIME_UNSET) {
                simpleExoPlayer.seekTo(currentPosition);
            }

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
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
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }


    public void fullScreenMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else{
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void nonFullScreenMode() {
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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

    public void updateCurrentPosition() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            currentWindowIndex = simpleExoPlayer.getCurrentWindowIndex();
            currentPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    public boolean isPlayWhenReady() {
        return playWhenReady;
    }

    public int getCurrentWindowIndex() {
        return currentWindowIndex;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }



}
