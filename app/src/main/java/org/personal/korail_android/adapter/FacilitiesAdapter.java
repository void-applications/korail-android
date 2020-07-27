package org.personal.korail_android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.personal.korail_android.R;
import org.personal.korail_android.item.FacilitiesItem;

import java.util.ArrayList;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.ViewHolder> {

    ArrayList<FacilitiesItem> facilitiesItemArrayList;
    Activity activity;

    public FacilitiesAdapter(ArrayList<FacilitiesItem> facilitiesItemArrayList, Activity activity) {
        this.facilitiesItemArrayList = facilitiesItemArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cultural_facilities,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.equipmentTV.setText(facilitiesItemArrayList.get(position).getEquipment());
        holder.locationTV.setText(facilitiesItemArrayList.get(position).getFloor()+"  "+facilitiesItemArrayList.get(position).getLocation());
        holder.stationNameTV.setText(facilitiesItemArrayList.get(position).getLineName()+"  "+facilitiesItemArrayList.get(position).getStationName());
    }

    @Override
    public int getItemCount() {
        return facilitiesItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView facilityIV;
        TextView locationTV;
        TextView stationNameTV;
        TextView equipmentTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.facilityIV=itemView.findViewById(R.id.facilityIV);
            this.locationTV=itemView.findViewById(R.id.locationTV);
            this.stationNameTV=itemView.findViewById(R.id.stationNameTV);
            this.equipmentTV=itemView.findViewById(R.id.equipmentTV);

        }
    }
}
