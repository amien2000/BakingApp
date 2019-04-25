package com.example.android.bakingapp.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.DetailActivity;
import com.example.android.bakingapp.MainActivity;
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

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private RecyclerView ingredientRecyclerView;
    private RecyclerView stepRecyclerView;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Step> stepArrayList;

    String url;
    String description;
    String name;
    Uri uri;
    ExoUtil exoUtil;
    TextView textViewDescription;
    boolean isURL;
    boolean internetConnected;
    View fragmentOne;
    TabHost host;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        fragmentOne = getActivity().findViewById(R.id.fragmentOne);

        Bundle extras = getArguments();
        ingredientArrayList = extras.getParcelableArrayList("ingredients");
        stepArrayList = extras.getParcelableArrayList("steps");
        name=extras.getString("name");

        setupTab(rootView);
        setupIngredientRecyclerView(rootView);
        setupStepRecyclerView(rootView);
        //For two-pane mode onItemTouchListener will run video fragment in DetailsActivity
        //whereas for a single-pane mode, Video fragment will execute intent to run on different Activity.
        if(getActivity().findViewById(R.id.linearLayoutsw600dp)!=null){
            stepItemTouchListenerTwoPane(rootView);
        }else{
            stepItemTouchListenerOnePane();
        }
        return rootView;
    }

    private void setupTab(View rootView){
        host = (TabHost)rootView.findViewById(R.id.tabHost);
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

    private void setupIngredientRecyclerView(View rootView){
        //Ingredient recyclerview
        ingredientRecyclerView = rootView.findViewById(R.id.rv_ingredients);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity());
        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientRecyclerView.setHasFixedSize(true);
        ingredientAdapter = new IngredientAdapter(ingredientArrayList, getActivity());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupStepRecyclerView(View rootView){
        //Step recyclerview
        stepRecyclerView = rootView.findViewById(R.id.rv_steps);
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
        textViewDescription = getActivity().findViewById(R.id.textViewDescription);
        stepRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                stepRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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

   // @Override
   // public void onConfigurationChanged(Configuration newConfig) {
        //super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        //if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //exoUtil.fullScreenMode();

        //} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //exoUtil.nonFullScreenMode();

       // }
    //}




}
