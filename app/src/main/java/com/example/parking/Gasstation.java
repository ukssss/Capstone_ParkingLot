package com.example.parking;

public class Gasstation {

    public int id;
    public String name;
    public String div;
    public String addr;
    public double latitude;
    public double longitude;

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
