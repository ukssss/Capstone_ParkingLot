package com.example.parking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "Main_Activity2";

    private Context mContext = MainActivity2.this;
    private ImageView imageView;
    private DrawerLayout drawerLayout;
    private NavigationView nav;

    TextView text;
    TextView tv_spinner;
    Spinner spinner;
    String data;

    XmlPullParser xpp;
    String key = "iUIvfulYGP2WncxaP93CKSBBGWPWdo5JDw7Ci7aLBbYni3pvsQ1VNvuJKhXF7sQ910XZu1lT%2FSX1aBtLdZ6xTA%3D%3D";
    String area;
    String areaNum;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 호출");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spinner = findViewById(R.id.spinner);
        text = (TextView) findViewById(R.id.text);

        init();
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);
        setSpinner();
    }

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

    // Spinner
    public void setSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected (AdapterView < ? > adapterView, View view, int i, long l) {
                area = spinner.getSelectedItem().toString();
                if (area.equals("중구")) {areaNum = "3250000";}
                else if (area.equals("서구")) {areaNum = "3260000";}
                else if (area.equals("동구")) {areaNum = "3270000";}
                else if (area.equals("영도구")) {areaNum = "3280000";}
                else if (area.equals("부산진구")) {areaNum = "3290000";}
                else if (area.equals("동래구")) {areaNum = "3300000";}
                else if (area.equals("남구")) {areaNum = "3310000";}
                else if (area.equals("북구")) {areaNum = "3320000";}
                else if (area.equals("해운대구")) {areaNum = "3330000";}
                else if (area.equals("사하구")) {areaNum = "3340000";}
                else if (area.equals("금정구")) {areaNum = "3350000";}
                else if (area.equals("강서구")) {areaNum = "3360000";}
                else if (area.equals("연제구")) {areaNum = "3370000";}
                else if (area.equals("수영구")) {areaNum = "3380000";}
                else if (area.equals("사상구")) {areaNum = "3390000";}
                else if (area.equals("기장군")) {areaNum = "3400000";}
            }

            @Override
            public void onNothingSelected (AdapterView < ? > adapterView){}
    });
}


    //Button을 클릭했을 때 자동으로 호출되는 callback method....
    public void btnClick(View v){
        switch( v.getId() ){
            case R.id.button:
                Toast.makeText(getApplicationContext(), "부산광역시 "+ area , Toast.LENGTH_SHORT).show();
                //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        data = getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                        //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                        //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data); //TextView에 문자열  data 출력
                            }
                        });
                    }
                }).start();
                break;
        }
    }//btnClick method..

    //XmlPullParser를 이용하여 OpenAPI XML 파일 파싱하기(parsing)
    String getXmlData(){

        StringBuffer buffer = new StringBuffer();

        String queryUrl="http://api.data.go.kr/openapi/tn_pubr_prkplce_info_api"
                + "?serviceKey="+ key
                + "&pageNo=1&numOfRows=300&type=xml"
                + "&prkplceSe=공영"
                + "&insttCode=" + areaNum;

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
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("prkplceNm")){
                            buffer.append("주차장명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("prkplceSe")){
                            buffer.append("주차장구분 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("rdnmadr")){
                            buffer.append("소재지도로명주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("lnmadr")){
                            buffer.append("소재지지번주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("phoneNumber")){
                            buffer.append("전화번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....

}

