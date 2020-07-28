package org.personal.korail_android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.personal.korail_android.R;
import org.personal.korail_android.item.FacilityReviewItem;

import java.util.ArrayList;

public class FacilityReviewAdapter extends RecyclerView.Adapter<FacilityReviewAdapter.ViewHolder> {

    ArrayList<FacilityReviewItem> facilityReviewItemArrayList;
    Activity activity;

    public FacilityReviewAdapter(ArrayList<FacilityReviewItem> facilityReviewItemArrayList, Activity activity) {
        this.facilityReviewItemArrayList = facilityReviewItemArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cultural_facilities_review, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.dateTV.setText(facilityReviewItemArrayList.get(position).getDate());
        holder.starTV.setText(facilityReviewItemArrayList.get(position).getStar());
        holder.reviewTV.setText(facilityReviewItemArrayList.get(position).getReview());

    }

    @Override
    public int getItemCount() {
        return facilityReviewItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTV;
        TextView starTV;
        TextView dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.reviewTV = itemView.findViewById(R.id.reviewTV);
            this.starTV = itemView.findViewById(R.id.reviewRB);
            this.dateTV = itemView.findViewById(R.id.dateTV);
        }
    }
}
