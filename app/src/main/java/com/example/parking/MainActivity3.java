package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
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
        NavigationViewHelper.enableNavigation(mContext, nav);
    }

    private void init(){
        nav = findViewById(R.id.nav);
    }
}
