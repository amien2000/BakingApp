package com.example.android.bakingapp.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment{

    View rootView;
    String mVideoURL, mVideoThumbnail, mDescription;
    SimpleExoPlayer mSimpleExoPlayer;
    long mPlayerPosition = 0L;
    boolean mShouldPlayWhenReady = true;
    int mWindowIndex;
    Uri mVideoUri;

    private static final String ARG_VID_URL = "mVideoURL";
    private static final String ARG_NAIL_URL = "mVideoThumbnail";
    private static final String ARG_DESC = "mDescription";

    public static final String KEY_READY =  "mShouldPlayWhenReady";
    public static final String KEY_POSITION =  "mPlayerPosition";
    public static final String KEY_INDEX =  "mWindowIndex";
    public static final String KEY_URL =  "mVideoURL";

    @BindView(R.id.iv_video_placeholder) ImageView mImageViewPlaceholder;
    @BindView(R.id.playerView) PlayerView mPlayerView;
    @BindView(R.id.tv_descriptionsw600) TextView tv_description;

    public static VideoFragment newVideoFragmentInstance(String vidURL, String nailURL, String desc){
        VideoFragment videoFragment = new VideoFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putString(ARG_VID_URL,vidURL);
        stepsBundle.putString(ARG_NAIL_URL,nailURL);
        stepsBundle.putString(ARG_DESC,desc);
        videoFragment.setArguments(stepsBundle);
        return videoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);

        if(getArguments() != null) {

            mImageViewPlaceholder.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);

            mVideoURL = getArguments().getString(ARG_VID_URL);
            mVideoThumbnail = getArguments().getString(ARG_NAIL_URL);
            mDescription = getArguments().getString(ARG_DESC);

            if(!mVideoURL.endsWith(".mp4")){
                if(mVideoThumbnail.equals("")){
                    mImageViewPlaceholder.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.INVISIBLE);
                    mPlayerView.setUseController(false);
                }
                else{
                    //if the thumbnail format is .mp4 or others
                    mImageViewPlaceholder.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.INVISIBLE);
                    Glide.with(this)
                            .load(mVideoThumbnail)
                            .placeholder(R.drawable.thumbnail_error)
                            .into(mImageViewPlaceholder);
                }
            }
            else{
                mVideoUri = Uri.parse(mVideoURL);
            }
        }
        if(savedInstanceState != null){
            mShouldPlayWhenReady = savedInstanceState.getBoolean(KEY_READY);
            mPlayerPosition = savedInstanceState.getLong(KEY_POSITION);
            mWindowIndex = savedInstanceState.getInt(KEY_INDEX);
            mVideoURL = savedInstanceState.getString(KEY_URL);
            mVideoUri = Uri.parse(mVideoURL);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putBoolean(KEY_READY, mShouldPlayWhenReady);
            outState.putLong(KEY_POSITION, mPlayerPosition);
            outState.putInt(KEY_INDEX,mWindowIndex);
            outState.putString(KEY_URL, mVideoURL);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeVideoPlayer(mVideoUri);
        tv_description.setText(mDescription);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeVideoPlayer(mVideoUri);
        tv_description.setText(mDescription);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    // Initialize exoplayer
    public void initializeVideoPlayer(Uri videoUri){
        if(mSimpleExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();

            //Default buffering policy
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

            // Create an instance of the ExoPlayer.
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

            // Bind the player to the view.
            mPlayerView = getView().findViewById(R.id.playerView);
            mPlayerView.setPlayer(mSimpleExoPlayer);

            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getActivity(), String.valueOf(R.string.app_name)));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            // Prepare the player with the source.
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.seekTo(mWindowIndex,mPlayerPosition);
            mSimpleExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mShouldPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mWindowIndex = mSimpleExoPlayer.getCurrentWindowIndex();
            mPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }
}
