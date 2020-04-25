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

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> stepArrayList;
    private Context context;
    private int rowNo;


    public StepAdapter(ArrayList<Step> stepArrayList, Context context, int position) {
        this.stepArrayList = stepArrayList;
        this.context = context;
        this.rowNo = position;
    }

    class StepViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_id) TextView idTextView;
        @BindView(R.id.tv_short_description) TextView stepTextView;


        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_item, viewGroup, false);
        return new StepAdapter.StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepAdapter.StepViewHolder holder, final int position) {
        Integer stepNumber;
        if(position==0){
            holder.idTextView.setText(R.string.space);
        }else {
            stepNumber = stepArrayList.get(position).getId();
            holder.idTextView.setText(String.valueOf(stepNumber));
        }
        holder.stepTextView.setText(stepArrayList.get(position).getShortDescription());
        if (rowNo==position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorSelected));
        }
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }
}
