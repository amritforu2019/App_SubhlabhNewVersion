package com.vastu.shubhlabhvastu.Adapter;

import static com.vastu.shubhlabhvastu.Task.API.IMG_EXPERT;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vastu.shubhlabhvastu.Activity.ExpertProfile;
import com.vastu.shubhlabhvastu.Model.ModelAllExpert;
import com.vastu.shubhlabhvastu.R;
import com.vastu.shubhlabhvastu.Task.CommonTask;
import com.vastu.shubhlabhvastu.Task.Utility;

import java.util.ArrayList;

public class AdapterAllExpertSearch extends RecyclerView.Adapter<AdapterAllExpertSearch.ViewHolder>    {

    private Activity activity;
    private ArrayList<ModelAllExpert> modelAllExpert;
    public Utility utl;

    public AdapterAllExpertSearch(Activity activity, ArrayList<ModelAllExpert> modelAllExpert) {
        this.activity = activity;
        this.modelAllExpert = modelAllExpert;
    }

    @Override
    public AdapterAllExpertSearch.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.holder_expert_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterAllExpertSearch.ViewHolder holder, int position) {
        holder.tv_exp_name.setText(modelAllExpert.get(position).getT_name());
        holder.tvExpTyp.setText(modelAllExpert.get(position).getC_typ());
        holder.tvRating.setText(modelAllExpert.get(position).getRating());
        holder.tvExp.setText(modelAllExpert.get(position).getEdu_year()+"Yrs Exp");
        Glide.with(activity).load(IMG_EXPERT+modelAllExpert.get(position).getImage()).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(holder.ivImage);

        Bundle bundle = new Bundle();
        bundle.putString("exp_id",  modelAllExpert.get(position).getId());

        holder.itemView.setOnClickListener(v -> CommonTask.redirectActivity(activity, ExpertProfile.class, bundle));
        holder.btn_App.setOnClickListener(v->CommonTask.redirectActivity(activity, ExpertProfile.class, bundle));
    }

    @Override
    public int getItemCount() {
        return modelAllExpert.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_exp_name, tvExpTyp,tvRating,tvExp;
        private ImageView ivImage;
        private Button btn_App;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tv_exp_name = itemView.findViewById(R.id.tv_exp_name);
            tvExpTyp = itemView.findViewById(R.id.tvExpTyp);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvExp = itemView.findViewById(R.id.tvExp);
            btn_App = itemView.findViewById(R.id.btn_App);
        }
    }


}
