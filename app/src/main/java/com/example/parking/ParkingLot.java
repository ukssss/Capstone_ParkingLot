package com.example.parking;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class ParkingLot {
    private static String ServiceKey = "iUIvfulYGP2WncxaP93CKSBBGWPWdo5JDw7Ci7aLBbYni3pvsQ1VNvuJKhXF7sQ910XZu1lT%2FSX1aBtLdZ6xTA%3D%3D";
    public ParkingLot() {
        try {
            apiParserSearch();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MapPoint> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<MapPoint> mapPoint = new ArrayList<MapPoint>();

        String pkNam = null, xCdnt = null, yCdnt = null;
        boolean bpkNam = false, bxCdnt = false, byCdnt = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if (tag.equals("pkNam")) {
                    bpkNam = true;
                }
                if (tag.equals("xCdnt")) {
                    bxCdnt = true;
                }
                if (tag.equals("yCdnt")) {
                    byCdnt = true;
                }
            }
            else if (event_type == XmlPullParser.TEXT) {
                if (bpkNam == true) {
                    pkNam = xpp.getText();
                    bpkNam = false;
                }
                else if (bxCdnt == true) {
                    xCdnt = xpp.getText();
                    bxCdnt = false;
                }
                else if (byCdnt == true) {
                    yCdnt = xpp.getText();
                    byCdnt = false;
                }
            }
            else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("row")) {
                    MapPoint entity = new MapPoint();
                    entity.setPkNam(pkNam);
                    entity.setxCdnt(Double.valueOf(xCdnt));
                    entity.setyCdnt(Double.valueOf(yCdnt));
                    mapPoint.add(entity);
                    System.out.println(mapPoint.size());
                }
            }
            event_type = xpp.next();
        }
        System.out.println(mapPoint.size());
        return  mapPoint;
    }

    private String getURLParam(String search) {
        String url = "http://apis.data.go.kr/6260000/BusanPblcPrkngInfoService/getPblcPrkngInfo?serviceKey=" + ServiceKey + "&numOfRows=300&pageNo=1";
        return url;
    }

    public static void main(String[] args) {
        new ParkingLot();
    }
}