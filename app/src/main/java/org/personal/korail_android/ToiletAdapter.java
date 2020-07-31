package org.personal.korail_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.personal.korail_android.adapter.FacilitiesAdapter;

import java.util.ArrayList;

public class ToiletAdapter extends RecyclerView.Adapter<ToiletAdapter.ViewHolder> {

    ArrayList<ToiletItem> list;
    Activity activity;

    String TAG = "화장실 어댑터";
    ToiletAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ToiletAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ToiletAdapter(ArrayList<ToiletItem> list, Activity activity){
        this.list = list;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTV;
        private TextView insideOutTV;
        private TextView wcTV;


        public ViewHolder(View view) {
            super(view);
            locationTV = view.findViewById(R.id.toiletLocationTV);
            insideOutTV = view.findViewById(R.id.insideAndOutTV);
            wcTV = view.findViewById(R.id.manWomenTV);

        }
    }

    @NonNull
    @Override
    public ToiletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_toilet, viewGroup, false);

        ToiletAdapter.ViewHolder viewHolder = new ToiletAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToiletAdapter.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null){
                    onItemClickListener.OnItemClick(view,position);
                }
            }
        });
        holder.locationTV.setText(list.get(position).getLocation());
        holder.insideOutTV.setText(list.get(position).getInsideOrOut());
        holder.wcTV.setText(list.get(position).getManWomen());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public interface OnItemClickListener {
        void OnItemClick(View view,int position);
    }
}
