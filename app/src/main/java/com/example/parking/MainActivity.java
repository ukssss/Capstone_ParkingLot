package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private static final String TAG = "Main_Activity";

    private Context mContext = MainActivity.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    // T Map
    private TMapView tMapView = null;
    private TMapGpsManager tMapGPS = null;
    private static String API_Key = "l7xxea74c8831aaf43e78a8bd6ca10c4128c";

    // Initial Location
    double initialLatitude = 35.13339;
    double initialLongitude = 129.10498;

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

    private void tMap() {
        // T Map View
        tMapView = new TMapView(this);
        initTMapView();

        // T Map GPS
        initTMapGPS();

        // Load Database (Parkinglot)
        List<Parkinglot> parkinglotList = initLoadParkinglotDatabase();

        // Add Parkinglot Marker
        addParkinglotMarker(parkinglotList);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTMap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTMap.addView(tMapView);
    }

    public void initTMapView() {
        // API Key
        tMapView.setSKTMapApiKey(API_Key);

        // Initial Setting
        tMapView.setZoomLevel(16);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // Initial Location Setting
        tMapView.setLocationPoint(initialLongitude, initialLatitude);
        tMapView.setCenterPoint(initialLongitude, initialLatitude);

    }

    public void initTMapGPS() {
        // Request For GPS Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // T Map GPS
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        // tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        // Using GPS
        tMapGPS.OpenGps();
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    public List<Parkinglot> initLoadParkinglotDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.OpenDatabaseFile();

        List<Parkinglot> parkinglotList = databaseHelper.getTableData();
        Log.e("test", String.valueOf(parkinglotList.size()));

        return parkinglotList;
    }

    public void addParkinglotMarker(List<Parkinglot> parkinglotList) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_green);

        for (int i = 0; i < parkinglotList.size(); i++) {

            String title = parkinglotList.get(i).prkplceNm;
            String subTitle = parkinglotList.get(i).lnmadr;
            double latitude = parkinglotList.get(i).latitude;
            double longitude = parkinglotList.get(i).longitude;

            TMapPoint tMapPoint = new TMapPoint(latitude, longitude);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setTMapPoint(tMapPoint);
            tMapMarkerItem.setName(title);

            tMapMarkerItem.setCanShowCallout(true);
            tMapMarkerItem.setCalloutTitle(title);
            tMapMarkerItem.setCalloutSubTitle(subTitle);
            tMapMarkerItem.setAutoCalloutVisible(false);

            tMapView.addMarkerItem("marker" + i, tMapMarkerItem);

        }
    }
}

