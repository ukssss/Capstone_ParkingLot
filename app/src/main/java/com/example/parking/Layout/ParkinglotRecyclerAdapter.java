package com.example.parking.Layout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking.Database.Parkinglot;
import com.example.parking.R;

import org.w3c.dom.Text;

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
        private TextView parkStat;
        private CheckBox favCheck;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            addr = itemView.findViewById(R.id.addr);
            div = itemView.findViewById(R.id.div);
            parkStat = itemView.findViewById(R.id.parkStat);
            favCheck = itemView.findViewById(R.id.fav_check);

        }

        public void onBind(Parkinglot parkinglot, int position) {
            String s = "" + (position + 1);
            id.setText(s);
            div.setText(parkinglot.getDiv());

            name.setText(parkinglot.getName());
            addr.setText("주소 : " + parkinglot.getAddr());
            parkStat.setText("주차가능대수 : " + parkinglot.getParkStat());

            if (parkinglot.getFavStat() == 1) {
                favCheck.setChecked(true);
            }
            else {
                favCheck.setChecked(false);
            }

            favCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (parkinglot.getFavStat() == 1) {
                        parkinglot.setFavStat(0);
                    }
                    else
                        parkinglot.setFavStat(1);
                }
            });

        }

    }
}
