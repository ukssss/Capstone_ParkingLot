package com.example.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking.Database.ParkinglotDatabaseHelper;
import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.Database.Parkinglot;
import com.example.parking.R;
import com.example.parking.Layout.ParkinglotRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "Main_Activity2";

    private Context mContext = MainActivity2.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    private Spinner divSp;
    private TextView listInfo;
    private String div;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ParkinglotRecyclerAdapter parkingAdapter;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter divAdapter;
    private RecyclerView.Adapter favoriteAdapter;

    public double lat1;
    public double lon1;
    public double lat2;
    public double lon2;

    public boolean isCheck[] = new boolean[50];
    private ArrayList<Parkinglot> favoriteData = new ArrayList<>();
    public CheckBox favCheck;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 호출");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initNavigationView();
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);


        // Load Database (Parkinglot)
        List<Parkinglot> parkinglotList = initLoadParkinglotDatabase();

        listInfo = (TextView) findViewById(R.id.listInfo);

        initializedParkinglotRecycler(parkinglotList);

        setLocation(parkinglotList);
        setSpinner(parkinglotList);
        setFavorite(parkinglotList);


    }

    private void initNavigationView() {
        nav = findViewById(R.id.nav);
    }

    private void onClickDrawer() {
        imageView = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);

        imageView.setOnClickListener((view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        }));
    }

    private List<Parkinglot> initLoadParkinglotDatabase() {
        ParkinglotDatabaseHelper parkinglotDatabaseHelper = new ParkinglotDatabaseHelper(getApplicationContext());
        parkinglotDatabaseHelper.OpenDatabaseFile();

        List<Parkinglot> parkinglotList = parkinglotDatabaseHelper.getTableData();

        return parkinglotList;
    }

    // 거리 계산 함수
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public void setLocation(List<Parkinglot> parkinglotList) {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                lon1 = location.getLongitude();
                lat1 = location.getLatitude();

                listInfo.setText(
                        "위도 : " + lon1 + "\n" + "경도 : " + lat1 + "\n"
                );

            }
        }
        initializedParkinglotRecycler(parkinglotList);
    }

    private void initializedParkinglotRecycler(List<Parkinglot> parkinglotList) {
        linearLayoutManager = new LinearLayoutManager(this);
        parkingAdapter = new ParkinglotRecyclerAdapter();

        for (int i = 0; i < parkinglotList.size(); i++) {
            lon2 = parkinglotList.get(i).longitude;
            lat2 = parkinglotList.get(i).latitude;
            double nowMeter = distance(lat1, lon1, lat2, lon2, "meter");
            if (nowMeter < 2000) {
                parkingAdapter.addItems(parkinglotList.get(i));
            }
        }

        adapter = parkingAdapter;

        listInfo.setText("현 위치 2Km 이내의 주차장 검색 목록입니다");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void btnSearchClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                listInfo.setText("부산광역시 " + div + " 주차장 검색 결과입니다");

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(divAdapter);
        }
    }

    public void setSpinner(List<Parkinglot> parkinglotList) {
        divSp = findViewById(R.id.div_sp);
        divSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                div = divSp.getSelectedItem().toString();
                initializedParkinglotRecyclerDiv(parkinglotList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initializedParkinglotRecyclerDiv(List<Parkinglot> parkinglotList) {
        linearLayoutManager = new LinearLayoutManager(this);
        parkingAdapter = new ParkinglotRecyclerAdapter();

        for (int i = 0; i < parkinglotList.size(); i++) {
            if (parkinglotList.get(i).div.equals(div)) {
                parkingAdapter.addItems(parkinglotList.get(i));
            }
        }

        divAdapter = parkingAdapter;
    }

    public void btnInfoClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_btn:
                listInfo.setText("주차장 즐겨찾기 목록 결과입니다");

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(favoriteAdapter);
        }
    }

    public void setFavorite(List<Parkinglot> parkinglotList) {
        initializedParkinglotRecyclerFavorite(parkinglotList);
    }



    private void initializedParkinglotRecyclerFavorite(List<Parkinglot> parkinglotList) {
        linearLayoutManager = new LinearLayoutManager(this);
        parkingAdapter = new ParkinglotRecyclerAdapter();


        for (int i = 0; i < parkinglotList.size(); i++) {
            if (parkinglotList.get(i).favStat.equals(1)) {
                favoriteData.add(parkinglotList.get(i));
            }
        }

        for (int j = 0; j < favoriteData.size(); j++) {
            parkingAdapter.addItems(favoriteData.get(j));
        }

        favoriteAdapter = parkingAdapter;
    }

}



