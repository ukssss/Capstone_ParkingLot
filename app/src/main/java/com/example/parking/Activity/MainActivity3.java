package com.example.parking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parking.Database.Parkinglot;
import com.example.parking.Database.ParkinglotDatabaseHelper;
import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.Layout.ParkinglotRecyclerAdapter;
import com.example.parking.R;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    private TextView listInfo;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ParkinglotRecyclerAdapter parkingAdapter;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 호출");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        init();
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);

        // Load Database (Parkinglot)
        List<Parkinglot> parkinglotList = initLoadParkinglotDatabase();

        listInfo = (TextView) findViewById(R.id.listInfo);
        initializedParkinglotRecyclerDefault(parkinglotList);

    }

    private void init() {
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

    private void initializedParkinglotRecyclerDefault(List<Parkinglot> parkinglotList) {
        listInfo.setText("즐겨찾기 주차장 목록");

        linearLayoutManager = new LinearLayoutManager(this);
        parkingAdapter = new ParkinglotRecyclerAdapter();

        for (int i = 0; i < parkinglotList.size(); i++) {
            if (parkinglotList.get(i).favorite.equals("TRUE")) {
                parkingAdapter.addItems(parkinglotList.get(i));
            }
        }

        adapter = parkingAdapter;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
