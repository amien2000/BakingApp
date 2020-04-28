package com.example.android.bakingapp.ui;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.android.bakingapp.DetailActivity;
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

    Step step;
    String name, description;
    ArrayList<Step> stepArrayList;
    ArrayList<Ingredient> ingredientArrayList;

    private static final String ARG_REC_NAME = "name";
    private static final String ARG_STEPS = "steps";
    private static final String ARG_INGREDIENTS = "ingredients";

    private static final String KEY_LAST_TAB = "lastTabPosition";
    private static final String KEY_LAST_STEP = "lastStepPosition";

    @BindView(R.id.tabHost) TabHost host;
    @BindView(R.id.rv_ingredients) RecyclerView ingredientRecyclerView;
    @BindView(R.id.rv_steps) RecyclerView stepRecyclerView;
    @Nullable @BindView(R.id.linearLayoutsw600dp) LinearLayout tablet;

    int lastStepPosition = 0;
    int lastTabPosition = 0;

    public static DetailFragment newDetailFragmentInstance(String name, ArrayList<Step> steps,
                                                           ArrayList<Ingredient> ingredients){
        DetailFragment detailFragment = new DetailFragment();
        Bundle detailBundle = new Bundle();
        detailBundle.putString(ARG_REC_NAME,name);
        detailBundle.putParcelableArrayList(ARG_STEPS,steps);
        detailBundle.putParcelableArrayList(ARG_INGREDIENTS,ingredients);
        detailFragment.setArguments(detailBundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        name = getArguments().getString(ARG_REC_NAME);
        stepArrayList = getArguments().getParcelableArrayList(ARG_STEPS);
        ingredientArrayList = getArguments().getParcelableArrayList(ARG_INGREDIENTS);

        setupTab();
        setupIngredientRecyclerView();

        if (getActivity().findViewById(R.id.linearLayoutsw600dp) != null) {
            //Tablet initial setup with video auto-play and auto-select step introduction
            if (savedInstanceState==null){
                replaceVideoFragment(stepArrayList.get(lastStepPosition));
            }
            setupStepRecyclerView();
            stepItemTouchListenerTablet();
            description = stepArrayList.get(lastStepPosition).getDescription();
            ((DetailActivity) getActivity()).setTvDescription(description);
        } else {
            //Phone initial setup with no initial step selection
            lastStepPosition = -1;
            setupStepRecyclerView();
            stepItemTouchListenerPhone();
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAST_STEP, lastStepPosition);
        outState.putInt(KEY_LAST_TAB, lastTabPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
            setupStepRecyclerView();// To ensure scroll to position work after orientation change
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            lastStepPosition = savedInstanceState.getInt(KEY_LAST_STEP);
            lastTabPosition = savedInstanceState.getInt(KEY_LAST_TAB);
        }
    }

    private void setupTab() {
        host.setup();
        //Tab Ingredients
        TabHost.TabSpec spec = host.newTabSpec(getString(R.string.tab_1_ingredients));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.tab_1_ingredients));
        host.addTab(spec);
        //Tab Steps
        spec = host.newTabSpec(getString(R.string.tab_2_steps));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.tab_2_steps));
        host.addTab(spec);
        host.setCurrentTab(lastTabPosition);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                lastTabPosition = host.getCurrentTab();
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
        //To highlight the position
        StepAdapter stepAdapter = new StepAdapter(stepArrayList, getActivity(),lastStepPosition);
        stepRecyclerView.setAdapter(stepAdapter);
        stepRecyclerView.getLayoutManager().scrollToPosition(lastStepPosition);
    }

    private void stepItemTouchListenerPhone() {
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                Bundle videoBundle = new Bundle();
                videoBundle.putParcelableArrayList("steps", stepArrayList);
                videoBundle.putInt("position", position);
                videoBundle.putString("name",name);
                intent.putExtra("bundle", videoBundle);
                startActivity(intent);
            }
            //@Override
            //public void onLongItemClick(View view, int position) {
            //}
        }));
    }

    private void stepItemTouchListenerTablet() {
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                lastStepPosition = position;
                setupStepRecyclerView(); //For item selection color and scroll to position
                step = stepArrayList.get(position);
                replaceVideoFragment(step);
                description = stepArrayList.get(lastStepPosition).getDescription();
                ((DetailActivity) getActivity()).setTvDescription(description);
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


