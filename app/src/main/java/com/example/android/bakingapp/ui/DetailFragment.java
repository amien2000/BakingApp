package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.VideoActivity;
import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    ArrayList<Ingredient> ingredientArrayList;
    ArrayList<Step> stepArrayList;

    String url;
    String description;
    String name;
    Uri uri;
    Step step;

    @BindView(R.id.tabHost) TabHost host;
    @BindView(R.id.rv_ingredients) RecyclerView ingredientRecyclerView;
    @BindView(R.id.rv_steps) RecyclerView stepRecyclerView;
    @Nullable @BindView(R.id.linearLayoutsw600dp) LinearLayout tablet;

    int lastStepPosition = 0;
    int lastTabPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle extras = getArguments();
        ingredientArrayList = extras.getParcelableArrayList("ingredients");
        stepArrayList = extras.getParcelableArrayList("steps");
        name = extras.getString("name");

        setupTab();
        setupIngredientRecyclerView();
        setupStepRecyclerView();

        if (savedInstanceState==null){
            replaceVideoFragment(stepArrayList.get(lastStepPosition));
        }

        if (getActivity().findViewById(R.id.linearLayoutsw600dp) != null) {
            stepItemTouchListenerTablet();
        } else {
            stepItemTouchListenerOnePane();

        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastStepPosition", lastStepPosition);
        outState.putInt("lastTabPosition", lastTabPosition);
        outState.putParcelableArrayList("stepArrayList", stepArrayList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            lastStepPosition = savedInstanceState.getInt("lastStepPosition");
            lastTabPosition = savedInstanceState.getInt("lastTabPosition");
            Toast.makeText(getActivity(),String.valueOf(lastStepPosition),Toast.LENGTH_LONG).show();
        }
    }

    private void setupTab() {
        host.setup();
        //Tab Ingredients
        TabHost.TabSpec spec = host.newTabSpec("Ingredients");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Ingredients");
        host.addTab(spec);
        //Tab Steps
        spec = host.newTabSpec("Steps");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Steps");
        host.addTab(spec);
        host.setCurrentTab(lastTabPosition);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                lastTabPosition = host.getCurrentTab();
                //Toast.makeText(getActivity(),String.valueOf(lastTabPosition),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupIngredientRecyclerView() {
        //Ingredient recyclerview
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity());
        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientRecyclerView.setHasFixedSize(true);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientArrayList, getActivity());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupStepRecyclerView() {
        //Step recyclerview
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        stepRecyclerView.setLayoutManager(stepLayoutManager);
        stepRecyclerView.setHasFixedSize(true);
        StepAdapter stepAdapter = new StepAdapter(stepArrayList, getActivity());
        stepRecyclerView.setAdapter(stepAdapter);
    }

    private void stepItemTouchListenerOnePane() {
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                Bundle videoBundle = new Bundle();
                videoBundle.putParcelableArrayList("steps", stepArrayList);
                videoBundle.putInt("position", position);
                videoBundle.putString("name", name);
                intent.putExtra("bundle", videoBundle);
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void stepItemTouchListenerTablet() {
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                lastStepPosition = position;
                step = stepArrayList.get(position);
                replaceVideoFragment(step);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    public void replaceVideoFragment(Step step){
        //Extract only video and thumbnail url
        String vidURL = step.getVideoURL();
        String nailURL = step.getThumbnailURL();
        String desc = step.getDescription();

        //Factory method to pass bundle to fragment
        VideoFragment videoFragment = VideoFragment.newVideoFragmentInstance(vidURL,nailURL,desc);
        getFragmentManager().beginTransaction()
                    .replace(R.id.video_fragment, videoFragment)
                    .addToBackStack(null)
                    .commit();
    }
}


