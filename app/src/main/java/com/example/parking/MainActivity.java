package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
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

    public int nRightButtonCount = 0;
    public double nowLongitude;
    public double nowLatitude;

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

    // T Map

    private void setTMapAuth() {
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey(API_Key);
    }

    private void initialize() {
        // T Map View Using Linear Layout
        LinearLayout linearLayoutTMap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        linearLayoutTMap.addView(tMapView);

        // TMapView Setting
        tMapView.setOnClickListenerCallBack(mOnClickListenerCallback);
        tMapView.setZoomLevel(15);
        tMapView.setIconVisibility(true);
        tMapView.setOnCalloutRightButtonClickListener(mOnCalloutRightButtonClickCallback);

        // Request For GPS Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

        // Add Parkinglot Marker
        addParkinglotMarker(parkinglotList);

    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        nowLongitude = location.getLongitude();
        nowLatitude = location.getLatitude();
    }

    TMapView.OnClickListenerCallback mOnClickListenerCallback = new TMapView.OnClickListenerCallback() {
        @Override
        public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
            double latitude = tMapPoint.getLatitude();
            double longitude = tMapPoint.getLongitude();

            return false;
        }

        @Override
        public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
            return false;
        }
    };

    TMapView.OnCalloutRightButtonClickCallback mOnCalloutRightButtonClickCallback = new TMapView.OnCalloutRightButtonClickCallback() {
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            TMapPoint tMapPoint = tMapMarkerItem.getTMapPoint();

            if (nRightButtonCount == 0) {
                tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude());
                Toast.makeText(mContext,"목적지로 설정하려면 빨간핀을 눌러주세요", Toast.LENGTH_SHORT).show();

                nRightButtonCount++;
            }
            else if (nRightButtonCount == 1) {
                TMapPoint tMapPointStart = new TMapPoint(nowLatitude, nowLongitude);

                FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(), tMapView);
                findCarPathTask.execute(tMapPointStart, tMapPoint);

                tMapView.setCenterPoint(nowLongitude, nowLatitude);
                tMapView.setZoomLevel(17);
                Toast.makeText(mContext,"도착하시면 빨간핀을 한번 더 눌러주세요", Toast.LENGTH_SHORT).show();
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
                tMapView.setZoomLevel(15);
                Toast.makeText(mContext,"정상적으로 도착하셨습니다", Toast.LENGTH_SHORT).show();

                nRightButtonCount = 0;
            }
        }
    };

    private List<Parkinglot> initLoadParkinglotDatabase() {
        ParkinglotDatabaseHelper databaseHelper = new ParkinglotDatabaseHelper(getApplicationContext());
        databaseHelper.OpenDatabaseFile();

        List<Parkinglot> parkinglotList = databaseHelper.getTableData();
        Log.e("test", String.valueOf(parkinglotList.size()));

        return parkinglotList;
    }

    private void addParkinglotMarker(List<Parkinglot> parkinglotList) {

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_green);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_red);

        for (int i = 0; i < parkinglotList.size(); i++) {

            String title = parkinglotList.get(i).name;
            String subTitle = parkinglotList.get(i).addr;
            double latitude = parkinglotList.get(i).latitude;
            double longitude = parkinglotList.get(i).longitude;

            TMapPoint tMapPoint = new TMapPoint(latitude, longitude);

            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            tMapMarkerItem.setIcon(bitmap1);
            tMapMarkerItem.setPosition(0.5f, 1.0f);
            tMapMarkerItem.setCalloutRightButtonImage(bitmap2);
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

