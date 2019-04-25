package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Step;

import java.util.ArrayList;

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> stepArrayList;
    private Context context;

    public StepAdapter(ArrayList<Step> stepArrayList, Context context) {
        this.stepArrayList = stepArrayList;
        this.context = context;
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        TextView idTextView;
        TextView stepTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.tv_id);
            stepTextView = itemView.findViewById(R.id.tv_short_description);
        }

    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        StepAdapter.StepViewHolder viewHolder = new StepAdapter.StepViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepViewHolder holder, final int position) {
        Integer stepNumber;
        if(position==0){
            holder.idTextView.setText("");
        }else {
            stepNumber = stepArrayList.get(position).getId();
            holder.idTextView.setText(String.valueOf(stepNumber));
        }
        holder.stepTextView.setText(stepArrayList.get(position).getShortDescription());
    }


    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }
}
