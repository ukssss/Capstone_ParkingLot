package com.example.parking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.example.parking.ConvertGeoPoint.GeoPoint;
import com.example.parking.ConvertGeoPoint.GeoTrans;
import com.example.parking.Layout.NavigationViewHelper;
import com.example.parking.R;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "Main_Activity3";

    private Context mContext = MainActivity3.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    private TextView textView;
    private static String API_Key = "F220822311";

    private TextView test;
    private TextView aroundAll;

    public double nowLatitude;
    public double nowLongitude;

    String pregasolinePrice;
    String gasolinePrice;
    String dieselPrice;
    String pregasolinePriceDiff;
    String gasolinePriceDiff;
    String dieselPriceDiff;

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

        avgBusanPrice();

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Button locationBtn = (Button) findViewById(R.id.locationBtn);
        test = (TextView) findViewById(R.id.test);
        aroundAll = (TextView) findViewById(R.id.aroundAll);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION )
                            != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( MainActivity3.this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
                    }
                else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        String provider = location.getProvider();
                        nowLatitude = location.getLatitude();
                        nowLongitude = location.getLongitude();

                        GeoPoint in_pt = new GeoPoint(nowLongitude, nowLatitude);
                        GeoPoint geo_trans = GeoTrans.convert(GeoTrans.GEO, GeoTrans.KATEC, in_pt);
                        aroundAll.setText(geo_trans.getX() + ", " + geo_trans.getY() );

                        test.setText(
                                "위치정보 : " + provider + "\n" +
                                "위도 : " + nowLatitude + "\n" +
                                "경도 : " + nowLongitude + "\n"
                        );

                    }

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

                }


            }
        });
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            nowLatitude = location.getLatitude();
            nowLongitude = location.getLongitude();

            GeoPoint in_pt = new GeoPoint(nowLongitude, nowLatitude);
            GeoPoint geo_trans = GeoTrans.convert(GeoTrans.GEO, GeoTrans.KATEC, in_pt);
            aroundAll.setText(geo_trans.getX() + ", " + geo_trans.getY() );

            test.setText(
                    "위치정보 : " + provider + "\n" +
                    "위도 : " + nowLatitude + "\n" +
                    "경도 : " + nowLongitude + "\n"
            );


        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };

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

    private void avgBusanPrice() {

        TextView pregasoline = (TextView) findViewById(R.id.pre_gasoline);
        TextView gasoline = (TextView) findViewById(R.id.gasoline);
        TextView diesel = (TextView) findViewById(R.id.diesel);

        TextView pregasolineDiff = (TextView) findViewById(R.id.pregasoline_diff);
        TextView gasolineDiff = (TextView) findViewById(R.id.gasoline_diff);
        TextView dieselDiff = (TextView) findViewById(R.id.diesel_diff);


        new Thread(new Runnable() {

            @Override
            public void run() {
                pregasolinePrice = getPregasolinePrice();
                gasolinePrice = getGasolinePrice();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                dieselPrice = getDieselPrice();

                pregasolinePriceDiff = getPregasolineDiff();
                gasolinePriceDiff = getGasolineDiff();
                dieselPriceDiff = getDieselDiff();

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pregasoline.setText(" 고급휘발유 : " + pregasolinePrice);
                        gasoline.setText(" 휘발유 : " + gasolinePrice); //TextView에 문자열  data 출력
                        diesel.setText(" 경유 : " + dieselPrice);

                        pregasolineDiff.setText(pregasolinePriceDiff);
                        gasolineDiff.setText(gasolinePriceDiff);
                        dieselDiff.setText(dieselPriceDiff);

                    }
                });
            }
        }).start();
    }

    String getPregasolinePrice(){

        StringBuffer pregasolineBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=B034";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("PRICE")){
                            xpp.next();
                            pregasolineBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            pregasolineBuffer.append("원"); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) pregasolineBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return pregasolineBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getPregasolineDiff(){

        StringBuffer pregasolineBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=B034";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("DIFF")){
                            xpp.next();
                            pregasolineBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) pregasolineBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return pregasolineBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getGasolinePrice(){

        StringBuffer gasolineBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=B027";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("PRICE")){
                            xpp.next();
                            gasolineBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            gasolineBuffer.append("원"); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) gasolineBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return gasolineBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getGasolineDiff(){

        StringBuffer gasolineBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=B027";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("DIFF")){
                            xpp.next();
                            gasolineBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) gasolineBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return gasolineBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getDieselPrice(){

        StringBuffer dieselBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=D047";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("PRICE")){
                            xpp.next();
                            dieselBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            dieselBuffer.append("원"); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) dieselBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return dieselBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

    String getDieselDiff(){

        StringBuffer dieselBuffer = new StringBuffer();

        String queryUrl="http://www.opinet.co.kr/api/avgSidoPrice.do?out=xml"
                + "&code=" + API_Key
                + "&sido=10"
                + "&prodcd=D047";

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("OIL")) ;// 첫번째 검색결과
                        else if(tag.equals("DIFF")){
                            xpp.next();
                            dieselBuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("OIL")) dieselBuffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return dieselBuffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

}

