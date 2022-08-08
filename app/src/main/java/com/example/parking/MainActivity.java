package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private static final String TAG = "Main_Activity";

    private Context mContext = MainActivity.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    // T Map
    private boolean TrackingMode = true;
    private TMapView tMapView = null;
    private TMapGpsManager tMapGPS = null;
    private static String API_Key = "l7xxea74c8831aaf43e78a8bd6ca10c4128c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);
        tMap();
    }

    private void onClickDrawer() {
        imageView = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);

        imageView.setOnClickListener((view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        }));
    }

    private void init() {
        nav = findViewById(R.id.nav);
    }

    @Override
    public void onLocationChange(Location location) {
        if (TrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    private void tMap() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // T Map View
        tMapView = new TMapView(this);
        tMapView.setHttpsMode(true);

        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);
        setUpMap();

        // 현재 보는 방향으로 설정
        tMapView.setCompassMode(true);

        // 현 위치 아이콘 설정
        tMapView.setIconVisibility(true);

        //Initial Setting
        tMapView.setZoomLevel(10);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // Request For GPS Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS Using T Map
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(5);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        // tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        // tMapGPS.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

    }

    private void setUpMap() {
        ParkingLot parser = new ParkingLot();
        ArrayList<MapPoint> mapPoint = new ArrayList<MapPoint>();
        try {
            mapPoint = parser.apiParserSearch();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mapPoint.size(); i++) {
            for (MapPoint entity : mapPoint) {
                TMapPoint point = new TMapPoint(mapPoint.get(i).getxCdnt(), mapPoint.get(i).getyCdnt());
                TMapMarkerItem markerItem1 = new TMapMarkerItem();

                markerItem1.setPosition(0.5f, 1.0f);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(entity.getPkNam());
                tMapView.setCenterPoint(mapPoint.get(i).getxCdnt(), mapPoint.get(i).getyCdnt());
                tMapView.addMarkerItem("markerItem1" + i, markerItem1);
            }
        }
    }
}

