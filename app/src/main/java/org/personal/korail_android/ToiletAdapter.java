package org.personal.korail_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class ToiletAdapter extends RecyclerView.Adapter<ToiletAdapter.CustomViewHolder> {

    ArrayList<ToiletItem> list;
    private Context context;
    String TAG = "화장실 어댑터";
    ToiletItem toiletItem;

    public ToiletAdapter(Context context, ArrayList<ToiletItem> list){
        this.context = context;
        this.list = list;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTV;
        private TextView insideOutTV;
        private TextView wcTV;


        public CustomViewHolder(View view) {
            super(view);
            locationTV = view.findViewById(R.id.locationTV);
            insideOutTV = view.findViewById(R.id.insideAndOutTV);
            wcTV = view.findViewById(R.id.manWomenTV);

        }
    }

    @NonNull
    @Override
    public ToiletAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_toilet, viewGroup, false);

        ToiletAdapter.CustomViewHolder viewHolder = new ToiletAdapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToiletAdapter.CustomViewHolder holder, int position) {

        holder.locationTV.setText(list.get(position).getLocation());
        holder.insideOutTV.setText(list.get(position).getInsideOrOut());
        holder.wcTV.setText(list.get(position).getInsideOrOut());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}
