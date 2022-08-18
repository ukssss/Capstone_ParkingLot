package com.example.parking;

import java.sql.Date;

public class Parkinglot {

    public int id;                          // id
    public String name;                // 주차장명
    public String div;                   // 소재지지번주소
    public String addr;
    public double latitude;                 // 위도
    public double longitude;                // 경도

    // SET
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDiv(String div) { this.div = div; }
    public void setAddr(String addr) { this.addr = addr; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // GET
    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public String getDiv() { return this.div; }
    public String getAddr() { return this.addr; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }

}


