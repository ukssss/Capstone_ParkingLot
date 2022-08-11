package com.example.parking;

import java.sql.Date;

public class BusanParkingInfo {

    public int id;                          // id
    public String district;                 // 행정구역
    public String prkplceNo;                // 주차장관리번호
    public String prkplceNm;                // 주차장명
    public String prkplceSe;                // 주차장구분
    public String prkplceType;              // 주차장유형
    public String rdnmadr;                  // 소재지도로명주소
    public String lnmadr;                   // 소재지지번주소
    public String operDay;                  // 운영요일
    public String parkingchargeInfo;        // 요금정보
    public String institutionNm;            // 관리기관명
    public String phoneNumber;              // 전화번호

    public double latitude;                 // 위도
    public double longitude;                // 경도

    public String insttCode;                // 제공기관코드
    public Date referenceDate;              // 데이터기준일자

    // SET
    public void setId(int id) { this.id = id; }

    public void setDistrict(String district) { this.district = district; }
    public void setPrkplceNo(String prkplceNo) { this.prkplceNo = prkplceNo; }
    public void setPrkplceNm(String prkplceNm) { this.prkplceNm = prkplceNm; }
    public void setPrkplceSe(String prkplceSe) { this.prkplceSe = prkplceSe; }
    public void setPrkplceType(String prkplceType) { this.prkplceType = prkplceType; }
    public void setRdnmadr(String rdnmadr) { this.rdnmadr = rdnmadr; }
    public void setLnmadr(String lnmadr) { this.lnmadr = lnmadr; }
    public void setOperDay(String operDay) { this.operDay = operDay; }
    public void setParkingchargeInfo(String parkingchargeInfo) { this.parkingchargeInfo = parkingchargeInfo; }
    public void setInstitutionNm(String institutionNm) { this.institutionNm = institutionNm; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setInsttCode(String insttCode) { this.insttCode = insttCode; }
    public void setReferenceDate(Date referenceDate) { this.referenceDate = referenceDate; }

    // GET
    public int getId() { return this.id; }

    public String getDistrict() { return this.district; }
    public String getPrkplceNo() { return this.prkplceNo; }
    public String getPrkplceNm() { return this.prkplceNm; }
    public String getPrkplceSe() { return this.prkplceSe; }
    public String getPrkplceType() { return this.prkplceType; }
    public String getRdnmadr() { return this.rdnmadr; }
    public String getLnmadr() { return this.lnmadr; }
    public String getOperDay() { return this.operDay; }
    public String getParkingchargeInfo() { return this.parkingchargeInfo; }
    public String getInstitutionNm() { return this.institutionNm; }
    public String getPhoneNumber() { return this.phoneNumber; }

    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }

    public String getInsttCode() { return this.insttCode; }
    public Date getReferenceDate() { return this.referenceDate; }

}


