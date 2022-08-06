package com.example.parking;

public class ParkingLot {

    // 현재 Parsing 하는 속성은 'O' 표시

    String guNm;            // 관리기관명 O
    String pkNam;           // 주차장명 O
    String mgntNum;         // 주차장관리번호 O
    String doroAddr;        // 소재지도로명주소 O
    String jibunAddr;       // 소재지지번주소 O
    String tponNum;         // 전화번호 O
    String pkFm;            // 주차장유형
    String pkCnt;           // 주차구획수
    String svcSrtTe;        // 평일운영시작시각
    String svcEndTe;        // 평일운영종료시각
    String satSrtTe;        // 토요일운영시작시각
    String satEndTe;        // 토요일운영종료시각
    String hldSrtTe;        // 공휴일운영시작시각
    String hldEndTe;        // 공휴일운영종료시각
    String ldRtg;           // 급지구분
    String tenMin;          // 주차기본요금
    String ftDay;           // 1일주차권요금
    String ftMon;           // 월정기권요금
    String xCdnt;           // 위도 O
    String yCdnt;           // 경도 O
    String fnlDt;           // 데이터기준일자
    String pkGubun;         // 주차장구분
    String bujeGubun;       // 부제시행구분
    String oprDay;          // 운영요일
    String feeInfo;         // 요금정보
    String pkBascTime;      // 주차기본시간
    String pkAddTime;       // 추가단위시간
    String feeAdd;          // 추가단위요금
    String ftDayApplytime;  // 1일주차권요금적용시간
    String payMtd;          // 결제방법
    String spclNote;        // 특기사항
    String currava;         // 실시간주차면수
    String oprt_fm;         // 운영형태

    public String getGuNm() {
        return guNm;
    }

    public void setGuNm(String guNm) {
        this.guNm = guNm;
    }

    public String getPkNam() {
        return pkNam;
    }

    public void setPkNam(String pkNam) {
        this.pkNam = pkNam;
    }

    public String getMgntNum() {
        return mgntNum;
    }

    public void setMgntNum(String mgntNum) {
        this.mgntNum = mgntNum;
    }

    public String getDoroAddr() {
        return doroAddr;
    }

    public void setDoroAddr() {
        this.doroAddr = doroAddr;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr() {
        this.jibunAddr = jibunAddr;
    }

    public String getTponNum() {
        return tponNum;
    }

    public void  setTponNum() {
        this.tponNum = tponNum;
    }

    public String getxCdnt() {
        return xCdnt;
    }

    public void setxCdnt() {
        this.xCdnt = xCdnt;
    }

    public String getyCdnt() {
        return yCdnt;
    }

    public void setyCdnt() {
        this.yCdnt = yCdnt;
    }

}