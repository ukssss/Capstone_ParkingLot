package com.example.parking.Database;

public class Parkinglot {

    public int id;
    public String div;
    public String name;
    public String type;
    public String addr;
    public String operDay;
    public String parkingchargeInfo;
    public String phoneNumber;
    public double latitude;                 // 위도
    public double longitude;                // 경도
    public Integer parkStat;
    public Integer favStat;

    // SET
    public void setId(int id) { this.id = id; }
    public void setDiv(String div) { this.div = div; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setAddr(String addr) { this.addr = addr; }
    public void setOperDay(String operDay) { this.operDay = operDay; }
    public void setParkingchargeInfo(String parkingchargeInfo) { this.parkingchargeInfo = parkingchargeInfo; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setParkStat(Integer parkStat) { this.parkStat = parkStat; }
    public void setFavStat(Integer favStat) { this.favStat = favStat; }

    // GET
    public int getId() { return this.id; }
    public String getDiv() { return this.div; }
    public String getName() { return this.name; }
    public String getType() { return this.type; }
    public String getAddr() { return this.addr; }
    public String getOperDay() { return this.operDay; }
    public String getParkingchargeInfo() { return this.parkingchargeInfo; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }
    public Integer getParkStat() { return this.parkStat; }
    public Integer getFavStat() { return this.favStat; }


}


