package com.example.parking.Layout;

import static java.lang.Integer.parseInt;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking.Database.Parkinglot;
import com.example.parking.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<ReservationRecyclerAdapter.MyViewHolder> {

    private ArrayList<Parkinglot> parkinglotData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_listview, parent, false);
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
        private TextView name;
        private TextView addr;
        private TextView parkStat;
        private TextView phoneNumber;
        private TextView operDay;
        private TextView parkingchargeInfo;
        private TextView div;
        private TextView type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            addr = itemView.findViewById(R.id.addr);
            parkStat = itemView.findViewById(R.id.parkStat);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            operDay = itemView.findViewById(R.id.operDay);
            parkingchargeInfo = itemView.findViewById(R.id.pay);
            div = itemView.findViewById(R.id.div);
            type = itemView.findViewById(R.id.type);

        }

        public void onBind(Parkinglot parkinglot, int position) {

            name.setText(parkinglot.getName());
            addr.setText("주소 : " + parkinglot.getAddr());

            parkStat.setText("주차가능대수 : " + parkinglot.getParkStat());
            if (parkinglot.getParkStat() <= 5) {
                parkStat.setTextColor(Color.RED);
            }
            else if (parkinglot.getParkStat() > 5 && parkinglot.getParkStat() < 20) {
                parkStat.setTextColor(Color.MAGENTA);
            }
            else {
                parkStat.setTextColor(Color.GREEN);
            }
            phoneNumber.setText("전화번호 : " + parkinglot.getPhoneNumber());
            type.setText("주차장유형 : " + parkinglot.getType());
            operDay.setText("운영요일 : " + parkinglot.getOperDay());
            parkingchargeInfo.setText("결제 : " + parkinglot.getParkingchargeInfo());
            div.setText("행정구역 : " + parkinglot.getDiv());


        }

    }
}
