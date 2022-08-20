package com.example.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    private TextView textView;
    String data;

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

}

