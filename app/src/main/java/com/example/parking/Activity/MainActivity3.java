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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking.Database.Parkinglot;
import com.example.parking.Database.ParkinglotDatabaseHelper;
import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.Layout.ParkinglotRecyclerAdapter;
import com.example.parking.Layout.ReservationRecyclerAdapter;
import com.example.parking.R;
import com.example.parking.TMap.FindCarPathTask;
import com.example.parking.TMap.FindElapsedTimeTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;
    private FloatingActionButton initTmap;
    private FloatingActionButton stopDirection;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ReservationRecyclerAdapter reservationRecyclerAdapter;
    private RecyclerView.Adapter adapter;


    // T Map
    private TMapView tMapView = null;
    private TMapGpsManager tMapGPS = null;
    private static String API_Key = "l7xxea74c8831aaf43e78a8bd6ca10c4128c";

    public int nRightButtonCount = 0;

    public double nowLatitude;
    public double nowLongitude;
    public double resLatitude;
    public double resLongitude;

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
        onClickFabGps();
        onClickFabStop();
        NavigationViewHelper.enableNavigation(mContext, nav);

        setTMapAuth();
        initialize();

        // 위치 관리자 객체 참조하기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    // Layout
    private void onClickDrawer() {
        imageView = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);

        imageView.setOnClickListener((view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        }));
    }

    private void onClickFabGps() {
        initTmap = findViewById(R.id.fabGPS);
        initTmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tMapView.setCenterPoint(nowLongitude, nowLatitude);
            }
        });
    }

    private void onClickFabStop() {
        stopDirection = findViewById(R.id.fabStop);
        stopDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nRightButtonCount == 2) {
                    tMapView.removeTMapPolyLine("Line");
                    tMapView.setCenterPoint(resLongitude, resLatitude);
                    tMapView.setZoomLevel(15);

                    Toast.makeText(mContext,"안내를 종료합니다", Toast.LENGTH_SHORT).show();
                    nRightButtonCount = 0;
                }
                else {
                    Toast.makeText(mContext, "목적지가 없습니다", Toast.LENGTH_SHORT).show();
                    nRightButtonCount = 0;
                }
            }
        });
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
        FrameLayout frameLayoutTMap = (FrameLayout) findViewById(R.id.frameLayoutTmap);
        frameLayoutTMap.addView(tMapView);

        // TMapView Setting
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
        addParkinglotMarker(parkinglotList);
        initializedParkinglotRecycler(parkinglotList);

    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        nowLongitude = location.getLongitude();
        nowLatitude = location.getLatitude();
    }



    TMapView.OnCalloutRightButtonClickCallback mOnCalloutRightButtonClickCallback = new TMapView.OnCalloutRightButtonClickCallback() {
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            TMapPoint tMapPoint = tMapMarkerItem.getTMapPoint();
            TMapPoint tMapPointStart = new TMapPoint(nowLatitude, nowLongitude);
            FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(), tMapView);

            if (nRightButtonCount == 0) {
                tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude());
                Toast.makeText(mContext,"목적지로 설정하려면 빨간핀을 눌러주세요", Toast.LENGTH_SHORT).show();

                nRightButtonCount++;
            }
            else if (nRightButtonCount == 1) {
                findCarPathTask.execute(tMapPointStart, tMapPoint);

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


    private void addParkinglotMarker(List<Parkinglot> parkinglotList) {

        Bitmap free = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_free);
        Bitmap littleLeft = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_littleleft);
        Bitmap full = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_full);

        Bitmap start = BitmapFactory.decodeResource(getResources(), R.drawable.info_start);

        for (int i = 0; i < parkinglotList.size(); i++) {

            if (parkinglotList.get(i).resStat == 1) {
                String title = parkinglotList.get(i).name;
                String subTitle = "주차가능대수 : " + parkinglotList.get(i).parkStat;
                resLatitude = parkinglotList.get(i).latitude;
                resLongitude = parkinglotList.get(i).longitude;

                tMapView.setCenterPoint(resLongitude, resLatitude);

                TMapPoint tMapPoint = new TMapPoint(resLatitude, resLongitude);

                TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

                if (parkinglotList.get(i).parkStat <= 5) {
                    tMapMarkerItem.setIcon(full);
                }
                else if (parkinglotList.get(i).parkStat > 5 && parkinglotList.get(i).parkStat < 20) {
                    tMapMarkerItem.setIcon(littleLeft);
                }
                else {
                    tMapMarkerItem.setIcon(free);
                }

                tMapMarkerItem.setPosition(0.5f, 1.0f);
                tMapMarkerItem.setCalloutRightButtonImage(start);
                tMapMarkerItem.setTMapPoint(tMapPoint);
                tMapMarkerItem.setName(title);

                tMapMarkerItem.setCanShowCallout(true);
                tMapMarkerItem.setCalloutTitle(title);
                tMapMarkerItem.setCalloutSubTitle(subTitle);
                tMapMarkerItem.setAutoCalloutVisible(false);

                tMapView.addMarkerItem("reservationMarker", tMapMarkerItem);

            }

        }

    }

    private void initializedParkinglotRecycler(List<Parkinglot> parkinglotList) {
        linearLayoutManager = new LinearLayoutManager(this);
        reservationRecyclerAdapter = new ReservationRecyclerAdapter();

        for (int i = 0; i < parkinglotList.size(); i++) {
            if (parkinglotList.get(i).resStat == 1) {
                reservationRecyclerAdapter.addItems(parkinglotList.get(i));
            }
        }

        adapter = reservationRecyclerAdapter;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}
