package com.example.android.bakingapp.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.VideoActivity;
import com.example.android.bakingapp.adapter.IngredientAdapter;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.models.Ingredient;
import com.example.android.bakingapp.models.Step;
import com.example.android.bakingapp.utils.ExoUtil;
import com.example.android.bakingapp.utils.NetworkUtil;
import com.example.android.bakingapp.utils.RecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Step> stepArrayList;

    String url;
    String description;
    String name;
    Uri uri;
    ExoUtil exoUtil;

    //@Nullable @BindView(R.id.linearLayoutsw600dp) LinearLayout tablet;
    @Nullable @BindView(R.id.textViewDescription) TextView textViewDescription;

    @BindView(R.id.tabHost) TabHost host;
    @BindView(R.id.rv_ingredients) RecyclerView ingredientRecyclerView;
    @BindView(R.id.rv_steps) RecyclerView stepRecyclerView;


    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);


        Bundle extras = getArguments();
        ingredientArrayList = extras.getParcelableArrayList("ingredients");
        stepArrayList = extras.getParcelableArrayList("steps");
        name=extras.getString("name");

        setupTab();
        setupIngredientRecyclerView();
        setupStepRecyclerView();
        //For two-pane mode onItemTouchListener will run video fragment in DetailsActivity
        //whereas for a single-pane mode, Video fragment will execute intent to run on different Activity.
        if(getActivity().findViewById(R.id.linearLayoutsw600dp)!=null){
            stepItemTouchListenerTwoPane(rootView);
        }else{
            stepItemTouchListenerOnePane();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getActivity());
    }

    private void setupTab(){
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
    }

    private void setupIngredientRecyclerView(){
        //Ingredient recyclerview
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity());
        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientRecyclerView.setHasFixedSize(true);
        ingredientAdapter = new IngredientAdapter(ingredientArrayList, getActivity());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupStepRecyclerView(){
        //Step recyclerview
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        stepRecyclerView.setLayoutManager(stepLayoutManager);
        stepRecyclerView.setHasFixedSize(true);
        stepAdapter = new StepAdapter(stepArrayList, getActivity());
        stepRecyclerView.setAdapter(stepAdapter);
    }

    private void stepItemTouchListenerOnePane(){
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(),VideoActivity.class);
                Bundle videoBundle=new Bundle();
                videoBundle.putParcelableArrayList("steps", stepArrayList);
                videoBundle.putInt("position",position);
                videoBundle.putString("name",name);
                intent.putExtra("bundle",videoBundle);
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void stepItemTouchListenerTwoPane(final View rootView){
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (exoUtil != null) {
                    exoUtil.releasePlayer();
                }

                description = stepArrayList.get(position).getDescription();
                textViewDescription.setText(description);

                url = stepArrayList.get(position).getVideoURL();
                NetworkUtil networkUtil = new NetworkUtil(getActivity(),url);
                networkUtil.networkMessage();

                uri = Uri.parse(url);
                exoUtil = new ExoUtil(getActivity(), rootView);
                exoUtil.initializePlayerActivity(uri);

            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onDestroy() {
        //Release the player when the activity is destroyed.
        super.onDestroy();
        if (exoUtil != null) {
            exoUtil.releasePlayer();
        }
    }





}
