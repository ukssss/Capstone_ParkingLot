package com.example.parking;

public class MapPoint {

    public String pkNam;
    public double xCdnt;
    public double yCdnt;
    public String jibunAddr;
    public String doroAddr;
    public int currava;

    // setter
    public void setPkNam(String pkNam){ this.pkNam = pkNam; }
    public void setxCdnt(double xCdnt){ this.xCdnt = xCdnt; }
    public void setyCdnt(double yCdnt){ this.yCdnt = yCdnt; }
    public void setJibunAddr(String jibunAddr){ this.jibunAddr = jibunAddr; }
    public void setDoroAddr(String doroAddr){ this.doroAddr = doroAddr; }
    public void setCurrava(int currava){ this.currava = currava; }

    // getter
    public String getPkNam(){ return this.pkNam; }
    public double getxCdnt(){ return this.xCdnt; }
    public double getyCdnt(){ return this.yCdnt; }
    public String getJibunAddr(){ return this.jibunAddr; }
    public String getDoroAddr(){ return this.doroAddr; }
    public int getCurrava(){ return this.currava; }

}