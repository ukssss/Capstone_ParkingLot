package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.opengl.EGLExt;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "Main_Activity2";

    private Context mContext = MainActivity2.this;
    private NavigationView nav;
    private ListView listView;
    private ArrayAdapter arrayAdapter;

    List<String> items = Arrays.asList(
            "장성주차장", "엄궁초등학교지하주차장", "학장동행정복지센터", "사상구청주차장", "부경대학교주차장"
    );

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 호출");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();
        NavigationViewHelper.enableNavigation(mContext, nav);

        listView();

    }

    private void init(){
        nav = findViewById(R.id.nav);
    }

    private void listView() {
        listView = findViewById(R.id.list_view);

        arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_expandable_list_item_1, items);
        listView.setAdapter(arrayAdapter);
    }
}
