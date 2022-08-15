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

    // Initial Location
    double initialLatitude = 35.1348103;
    double initialLongitude = 129.1030291;

    int nRightButtonCount = 0;

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
        tMapView.setCenterPoint(initialLongitude, initialLatitude);
        tMapView.setZoomLevel(17);
        tMapView.setOnCalloutRightButtonClickListener(mOnCalloutRightButtonClickCallback);

        // Load Database (Parkinglot)
        List<Parkinglot> parkinglotList = initLoadParkinglotDatabase();

        // Add Parkinglot Marker
        addParkinglotMarker(parkinglotList);

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

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    private List<Parkinglot> initLoadParkinglotDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.OpenDatabaseFile();

        List<Parkinglot> parkinglotList = databaseHelper.getTableData();
        Log.e("test", String.valueOf(parkinglotList.size()));

        return parkinglotList;
    }

    private void addParkinglotMarker(List<Parkinglot> parkinglotList) {

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_green);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.markerline_red);

        for (int i = 0; i < parkinglotList.size(); i++) {

            String title = parkinglotList.get(i).prkplceNm;
            String subTitle = parkinglotList.get(i).lnmadr;
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

    TMapView.OnCalloutRightButtonClickCallback mOnCalloutRightButtonClickCallback = new TMapView.OnCalloutRightButtonClickCallback() {
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            TMapPoint tMapPoint = tMapMarkerItem.getTMapPoint();

            if (nRightButtonCount == 0) {
                tMapView.setCenterPoint(tMapPoint.getLongitude(), tMapPoint.getLatitude());
                tMapView.setZoom(17);

                nRightButtonCount++;
            }
            else if (nRightButtonCount == 1) {
                TMapPoint tMapPointStart = new TMapPoint(35.1348103, 129.1030291);

                FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(), tMapView);
                findCarPathTask.execute(tMapPointStart, tMapPoint);
                nRightButtonCount = 0;

                tMapView.setCenterPoint(129.1030291, 35.1348103);
            }
        }
    };
}

