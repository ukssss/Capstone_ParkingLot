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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;
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

    EditText edit;
    TextView text;

    String Servicekey = "iUIvfulYGP2WncxaP93CKSBBGWPWdo5JDw7Ci7aLBbYni3pvsQ1VNvuJKhXF7sQ910XZu1lT%2FSX1aBtLdZ6xTA%3D%3D";
    String data;

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
        onClickDrawer();
        NavigationViewHelper.enableNavigation(mContext, nav);

        // Parsing
        edit = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
    }

    private void init(){
        nav = findViewById(R.id.nav);
    }

    private void onClickDrawer() {
        imageView = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);

        imageView.setOnClickListener((view -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        }));
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data = getXmlData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String location = URLEncoder.encode(str);

        String queryUrl = "http://apis.data.go.kr/6260000/BusanPblcPrkngInfoService/getPblcPrkngInfo?serviceKey=" + Servicekey + "&numOfRows=300&pageNo=1";

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("Start Parsing...\n\n");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if(tag.equals("item"));
                        else if(tag.equals("pkNam")){
                            buffer.append("주차장명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("doroAddr")){
                            buffer.append("도로명주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("jibunAddr")){
                            buffer.append("지번주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("tponNum")){
                            buffer.append("전화번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) buffer.append("\n");

                        break;

                }

                eventType = xpp.next();
            }
        }
        catch (Exception e) {

        }
        buffer.append("Finished Parsing!\n");
        return buffer.toString();
    }
}

