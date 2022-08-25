package com.example.parking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking.Database.Gasstation;
import com.example.parking.Database.GasstationDatabaseHelper;
import com.example.parking.Database.ParkinglotDatabaseHelper;
import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.Database.Parkinglot;
import com.example.parking.R;
import com.example.parking.TMap.FindCarPathTask;
import com.example.parking.TMap.FindElapsedTimeTask;
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
    private TextView textView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    // T Map
    private TMapView tMapView = null;
    private TMapGpsManager tMapGPS = null;
    private static String API_Key = "l7xxea74c8831aaf43e78a8bd6ca10c4128c";

    public int nParkinglotButtonCount = 0;
    public int nGasstationButtonCount = 0;
    public int nRightButtonCount = 0;

    public double nowLatitude;
    public double nowLongitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);

        setTMapAuth();
        initialize();

    }

    // Layout

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

    private void setTMapAuth() {
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(API_Key);
    }

    private void initialize() {
        // T Map View Using Linear Layout
        LinearLayout linearLayoutTMap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTMap.addView(tMapView);

        // TMapView Setting
        tMapView.setCenterPoint(129.0600331, 35.1578157);
        tMapView.setZoomLevel(15);
        tMapView.setIconVisibility(true);
        tMapView.setOnCalloutRightButtonClickListener(mOnCalloutRightButtonClickCallback);

        // Request For GPS Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS using T Map
        tMapGPS = new TMapGpsManager(this);
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        //tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);
        tMapGPS.OpenGps();

        // Load Database (Parkinglot)
        List<Parkinglot> parkinglotList = initLoadParkinglotDatabase();

        // Load Database (Gasstation)
        List<Gasstation> gasstationList = initLoadGasstationDatabase();

        Button parkinglotBtn = (Button) findViewById(R.id.parkinglotBtn);
        Button gasstationBtn = (Button) findViewById(R.id.gasstationBtn);

        parkinglotBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (nParkinglotButtonCount == 0) {
                    addParkinglotMarker(parkinglotList);
                    nParkinglotButtonCount++;
                }
                else if (nParkinglotButtonCount == 1) {
                    removeParkinglotMarker(parkinglotList);
                    nParkinglotButtonCount = 0;
                }

            }
        });

        gasstationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (nGasstationButtonCount == 0){
                    addGasstationMarker(gasstationList);
                    nGasstationButtonCount++;
                }
                else if (nGasstationButtonCount == 1) {
                    removeGasstationMarker(gasstationList);
                    nGasstationButtonCount = 0;
                }

            }
        });
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        nowLongitude = location.getLongitude();
        nowLatitude = location.getLatitude();
    }

    TMapView.OnCalloutRightButtonClickCallback mOnCalloutRightButtonClickCallback = new TMapView.OnCalloutRightButtonClickCallback() {
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            Bitmap start = BitmapFactory.decodeResource(getResources(), R.drawable.start);
            Bitmap arrive = BitmapFactory.decodeResource(getResources(), R.drawable.arrive);
            Bitmap destination = BitmapFactory.decodeResource(getResources(), R.drawable.destination);

            TMapPoint tMapPoint = tMapMarkerItem.getTMapPoint();
            TMapPoint tMapPointStart = new TMapPoint(nowLatitude, nowLongitude);
            FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(), tMapView);

            if (nRightButtonCount == 0) {
                tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude());
                tMapMarkerItem.setCalloutRightButtonImage(start);
                Toast.makeText(mContext,"목적지로 설정하려면 빨간핀을 눌러주세요", Toast.LENGTH_SHORT).show();

                nRightButtonCount++;
            }
            else if (nRightButtonCount == 1) {
                findCarPathTask.execute(tMapPointStart, tMapPoint);

                tMapMarkerItem.setCalloutRightButtonImage(arrive);
                tMapView.setCenterPoint(nowLongitude, nowLatitude);
                tMapView.setZoomLevel(17);
                nRightButtonCount++;

                try {
                    FindElapsedTimeTask findElapsedTimeTask = new FindElapsedTimeTask(getApplicationContext());
                    findElapsedTimeTask.execute("1", API_Key,
                            String.valueOf(tMapPointStart.getLongitude()),
                            String.valueOf(tMapPointStart.getLatitude()),
                            String.valueOf(tMapPoint.getLongitude()),
                            String.valueOf(tMapPoint.getLatitude()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            else if (nRightButtonCount == 2) {
                tMapView.removeTMapPolyLine("Line");
                tMapMarkerItem.setCalloutRightButtonImage(destination);

                tMapView.setZoomLevel(15);
                Toast.makeText(mContext,"안내를 종료합니다", Toast.LENGTH_SHORT).show();

                nRightButtonCount = 0;
            }
        }
    };

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

    private void addParkinglotMarker(List<Parkinglot> parkinglotList) {

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_parkinglot);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.destination);

        for (int i = 0; i < parkinglotList.size(); i++) {

            String title = parkinglotList.get(i).name;
            String subTitle = parkinglotList.get(i).addr;
            double latitude = parkinglotList.get(i).latitude;
            double longitude = parkinglotList.get(i).longitude;

            TMapPoint tMapPoint = new TMapPoint(latitude, longitude);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap1);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setCalloutRightButtonImage(bitmap3);
            tMapMarkerItem.setTMapPoint(tMapPoint);
            tMapMarkerItem.setName(title);

            tMapMarkerItem.setCanShowCallout(true);
            tMapMarkerItem.setCalloutTitle(title);
            tMapMarkerItem.setCalloutSubTitle(subTitle);
            tMapMarkerItem.setAutoCalloutVisible(false);

            tMapView.addMarkerItem("parkinglotMarker" + i, tMapMarkerItem);

        }

    }

    private void removeParkinglotMarker(List<Parkinglot> parkinglotList) {
        for (int i = 0; i < parkinglotList.size(); i++) {
            tMapView.removeMarkerItem("parkinglotMarker" + i);
        }
    }

    private void addGasstationMarker(List<Gasstation> gasstationList) {
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_gasstation);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.destination);

        for (int i = 0; i < gasstationList.size(); i++) {

            String title = gasstationList.get(i).name;
            String subTitle = gasstationList.get(i).addr;
            double latitude = gasstationList.get(i).latitude;
            double longitude = gasstationList.get(i).longitude;

            TMapPoint tMapPoint = new TMapPoint(latitude, longitude);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap2);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setCalloutRightButtonImage(bitmap3);
            tMapMarkerItem.setTMapPoint(tMapPoint);
            tMapMarkerItem.setName(title);

            tMapMarkerItem.setCanShowCallout(true);
            tMapMarkerItem.setCalloutTitle(title);
            tMapMarkerItem.setCalloutSubTitle(subTitle);
            tMapMarkerItem.setAutoCalloutVisible(false);

            tMapView.addMarkerItem("gasstationMarker" + i, tMapMarkerItem);

        }

    }

    private void removeGasstationMarker(List<Gasstation> gasstationList) {
        for (int i = 0; i < gasstationList.size(); i++) {
            tMapView.removeMarkerItem("gasstationMarker" + i);
        }
    }


}

