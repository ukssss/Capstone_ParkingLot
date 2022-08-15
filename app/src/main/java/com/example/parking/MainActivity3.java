package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

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
    }

    private void init(){
        nav = findViewById(R.id.nav);
    }

    private void onClickDrawer() {
        imageView = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);

        imageView.setOnClickListener((view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        }));
    }

    public List<Parkinglot> initLoadParkinglotDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.OpenDatabaseFile();

        List<Parkinglot> parkinglotList = databaseHelper.getTableData();
        Log.e("test", String.valueOf(parkinglotList.size()));

        return parkinglotList;
    }

    public void searchParkinglot(List<Parkinglot> parkinglotList) {

        for (int i = 0; i < parkinglotList.size(); i++) {
            String title = parkinglotList.get(i).prkplceNm;
            String subTitle = parkinglotList.get(i).lnmadr;
            double latitude = parkinglotList.get(i).latitude;
            double longitude = parkinglotList.get(i).longitude;

        }
    }
}

