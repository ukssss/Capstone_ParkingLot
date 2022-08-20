package com.example.parking.Layout;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.parking.Activity.MainActivity;
import com.example.parking.Activity.MainActivity2;
import com.example.parking.Activity.MainActivity3;
import com.example.parking.R;
import com.google.android.material.navigation.NavigationView;

public class NavigationViewHelper {
    public static void enableNavigation(final Context context, NavigationView view) {
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.first_tab) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
                else if(item.getItemId() == R.id.second_tab) {
                    Intent intent = new Intent(context, MainActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
                else if(item.getItemId() == R.id.third_tab) {
                    Intent intent = new Intent(context, MainActivity3.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                }
                return true;
            }
        });
    }
}
