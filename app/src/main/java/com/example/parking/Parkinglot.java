package com.example.parking;

import java.sql.Date;

public class Parkinglot {

    public int id;                          // id
    public String prkplceNm;                // 주차장명
    public String lnmadr;                   // 소재지지번주소
    public double latitude;                 // 위도
    public double longitude;                // 경도


    // SET
    public void setId(int id) { this.id = id; }
    public void setPrkplceNm(String prkplceNm) { this.prkplceNm = prkplceNm; }
    public void setLnmadr(String lnmadr) { this.lnmadr = lnmadr; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // GET
    public int getId() { return this.id; }
    public String getPrkplceNm() { return this.prkplceNm; }
    public String getLnmadr() { return this.lnmadr; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }

}


