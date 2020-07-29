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

    FacilitiesAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //장비가 없을 때
        if(facilitiesItemArrayList.get(position).getEquipment().equals("null")){
            holder.equipmentTV.setText("");
        }
        //장비가 있을 때
        else{
            holder.equipmentTV.setText("장비 "+facilitiesItemArrayList.get(position).getEquipment());
        }

        //상세 위치가 없을 때
        if(facilitiesItemArrayList.get(position).getLocation().equals("null")){

            holder.locationTV.setText(facilitiesItemArrayList.get(position).getFloor());
        }
        //상세 위치가 있을 때
        else{
            holder.locationTV.setText(facilitiesItemArrayList.get(position).getFloor()+"  "+facilitiesItemArrayList.get(position).getLocation());
        }

        holder.stationNameTV.setText(facilitiesItemArrayList.get(position).getLineName()+"  "+facilitiesItemArrayList.get(position).getStationName()+"역");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.OnItemClick(view,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return facilitiesItemArrayList.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(View view,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView locationTV;
        TextView stationNameTV;
        TextView equipmentTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.locationTV=itemView.findViewById(R.id.locationTV);
            this.stationNameTV=itemView.findViewById(R.id.stationNameTV);
            this.equipmentTV=itemView.findViewById(R.id.equipmentTV);

        }
    }
}
