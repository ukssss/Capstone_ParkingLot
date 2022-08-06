package com.example.parking;

public class MapPoint {
    private String pkNam;        // 주차장명
    private double xCdnt;    // 위도
    private double yCdnt;   // 경도

    public MapPoint() {
        super();
    }

    public MapPoint(String pkNam, double xCdnt, double yCdnt) {
        this.pkNam = pkNam;
        this.xCdnt = xCdnt;
        this.yCdnt = yCdnt;
    }

    // 주차장명
    public String getName() {
        return pkNam;
    }

    public void setName(String pkNam){
        this.pkNam = pkNam;
    }

    // 위도
    public double getLatitude() {
        return xCdnt;
    }

    public void setLatitude(double xCdnt) {
        this.xCdnt = xCdnt;
    }

    // 경도
    public double getLongitude() {
        return yCdnt;
    }

    public void setLongitude(double yCdnt) {
        this.yCdnt = yCdnt;
    }

}
