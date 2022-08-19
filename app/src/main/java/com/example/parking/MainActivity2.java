package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "Main_Activity2";

    private Context mContext = MainActivity2.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    String key = "iUIvfulYGP2WncxaP93CKSBBGWPWdo5JDw7Ci7aLBbYni3pvsQ1VNvuJKhXF7sQ910XZu1lT%2FSX1aBtLdZ6xTA%3D%3D";

    private Spinner typeSp;
    private Spinner divSp;
    private String type;
    private String div;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ParkinglotRecyclerAdapter parkingAdapter;
    private GasstationRecyclerAdapter gasstationAdapter;

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

        // Load Database (Gasstation)
        List<Gasstation> gasstationList = initLoadGasstationDatabase();

        // Set Option (Database, Division Select)
        // setSpinner(parkinglotList, gasstationList);

        initializedParkinglotRecycler(parkinglotList);

        // initializedGasstationRecycler(gasstationList);

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

    private List<Gasstation> initLoadGasstationDatabase() {
        GasstationDatabaseHelper gasstationDatabaseHelper = new GasstationDatabaseHelper(getApplicationContext());
        gasstationDatabaseHelper.OpenDatabaseFile();

        List<Gasstation> gasstationList = gasstationDatabaseHelper.getTableData();

        return gasstationList;
    }

//    public void btnClick(View v, List<Parkinglot> parkinglotList, List<Gasstation> gasstationList) {
//        switch (v.getId()) {
//            case R.id.button:
//                setSpinner(parkinglotList, gasstationList);
//        }
//    }
//
//    private void setSpinner(List<Parkinglot> parkinglotList, List<Gasstation> gasstationList) {
//        typeSp = findViewById(R.id.type_sp);
//        divSp = findViewById(R.id.div_sp);
//
//        typeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        divSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    private void initializedParkinglotRecycler(List<Parkinglot> parkinglotList) {
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        parkingAdapter = new ParkinglotRecyclerAdapter();

        for (int i = 0; i < parkinglotList.size(); i++) {
            parkingAdapter.addItems(parkinglotList.get(i));
        }

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(parkingAdapter);
    }

    private void initializedGasstationRecycler(List<Gasstation> gasstationList) {
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        gasstationAdapter = new GasstationRecyclerAdapter();

        for (int i = 0; i < gasstationList.size(); i++) {
            gasstationAdapter.addItems(gasstationList.get(i));
        }

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(gasstationAdapter);
    }

}

