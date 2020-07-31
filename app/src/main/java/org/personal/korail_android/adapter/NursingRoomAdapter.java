package org.personal.korail_android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.personal.korail_android.R;
import org.personal.korail_android.item.NursingRoomItem;

import java.util.ArrayList;

public class NursingRoomAdapter extends RecyclerView.Adapter<NursingRoomAdapter.ViewHolder> {

    ArrayList<NursingRoomItem> nursingRoomItemArrayList;
    Activity activity;

    public NursingRoomAdapter(ArrayList<NursingRoomItem> nursingRoomItemArrayList, Activity activity) {
        this.nursingRoomItemArrayList = nursingRoomItemArrayList;
        this.activity = activity;
    }

    NursingRoomAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nursing_room,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       holder.phoneNumberTV.setText("전화번호 : "+nursingRoomItemArrayList.get(position).getPhoneNumber());
       holder.stationNameTV.setText(nursingRoomItemArrayList.get(position).getStationName());
       holder.locationTV.setText("위치 : "+nursingRoomItemArrayList.get(position).getLocation());

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
        return nursingRoomItemArrayList.size();
    }

    public interface OnItemClickListener{
        void OnItemClick(View view,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView locationTV;
        TextView stationNameTV;
        TextView phoneNumberTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.locationTV=itemView.findViewById(R.id.locationTV);
            this.stationNameTV=itemView.findViewById(R.id.stationNameTV);
            this.phoneNumberTV=itemView.findViewById(R.id.phoneNumberTV);
        }
    }

    public void filterList(ArrayList<NursingRoomItem> filteredList){
        nursingRoomItemArrayList=filteredList;
        notifyDataSetChanged();
    }
}
