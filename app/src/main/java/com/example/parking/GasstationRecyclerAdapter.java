package com.example.parking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GasstationRecyclerAdapter extends RecyclerView.Adapter<GasstationRecyclerAdapter.MyViewHolder>{
    private ArrayList<Gasstation> gasstationData = new ArrayList<>();

    @NonNull
    @Override
    public GasstationRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gasstation_listview, parent, false);
        return new GasstationRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GasstationRecyclerAdapter.MyViewHolder holder, int position) {
        holder.onBind(gasstationData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return gasstationData.size();
    }

    public void addItems(Gasstation gasstation) {
        gasstationData.add(gasstation);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView div;
        private TextView name;
        private TextView addr;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            div = itemView.findViewById(R.id.div);
            name = itemView.findViewById(R.id.name);
            addr = itemView.findViewById(R.id.addr);

        }

        public void onBind(Gasstation gasstation, int position) {
            String s = "" + (position + 1);
            id.setText(s);
            div.setText(gasstation.getDiv());
            name.setText(gasstation.getName());
            addr.setText("주소 : " + gasstation.getAddr());


        }
    }
}