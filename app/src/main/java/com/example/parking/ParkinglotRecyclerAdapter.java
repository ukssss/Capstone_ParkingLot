package com.example.parking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkinglotRecyclerAdapter extends RecyclerView.Adapter<ParkinglotRecyclerAdapter.MyViewHolder> {

    private ArrayList<Parkinglot> parkinglotData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parkinglot_listview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(parkinglotData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return parkinglotData.size();
    }

    public void addItems(Parkinglot parkinglot) {
        parkinglotData.add(parkinglot);
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
            name = itemView.findViewById(R.id.name);
            addr = itemView.findViewById(R.id.addr);
            div = itemView.findViewById(R.id.div);
        }

        public void onBind(Parkinglot parkinglot, int position) {
            String s = "" + (position + 1);
            id.setText(s);
            div.setText(parkinglot.getDiv());

            name.setText(parkinglot.getName());
            addr.setText("주소 : " + parkinglot.getAddr());

        }
    }
}
