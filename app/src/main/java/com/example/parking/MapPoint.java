package com.example.parking;

public class MapPoint {
    private String guNam;   // 관리기관명
    private String pkNam;   // 주차장명
    private double xCdnt;    // 위도
    private double yCdnt;    // 경도

    public MapPoint() {
        super();
    }

    public MapPoint(String guNam, String pkNam, double xCdnt, double yCdnt) {
        this.guNam = guNam;
        this.pkNam = pkNam;
        this.xCdnt = xCdnt;
        this.yCdnt = yCdnt;
    }

    // 관리기관명
    public String getGuNam() {
        return guNam;
    }

    public void setGuNam(String guNam) {
        this.guNam = guNam;
    }

    // 주차장명
    public String getPkNam() {
        return pkNam;
    }

    public void setPkNam(String pkNam) {
        this.pkNam = pkNam;
    }

    // 위도
    public double getxCdnt() {
        return xCdnt;
    }

    public void setxCdnt(double xCdnt) {
        this.xCdnt = xCdnt;
    }

    // 경도
    public double getyCdnt() {
        return yCdnt;
    }

    public void setyCdnt(double yCdnt) {
        this.yCdnt = yCdnt;
    }
}
