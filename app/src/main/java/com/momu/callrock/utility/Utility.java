package com.momu.callrock.utility;

/**
 * 유틸리티
 * Created by knulps on 2017. 7. 1..
 */

public class Utility {
    /**
     * pm10 미세먼지 등급 계산 0 : 좋음, 1: 보통, 2: 나쁨, 3: 매우나쁨
     *
     * @param aqi        미세먼지 농도
     * @param isWhoGrade WHO 기준 여부
     * @return pm10 미세먼지 등급
     */
    public static int pm10Grade(int aqi, boolean isWhoGrade) {
        if(aqi == -1) return -1;        //측정값 없음

        if (isWhoGrade) {
            if (aqi < 31) {
                return 0;
            } else if (aqi < 81) {
                return 1;
            } else if (aqi < 151) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (aqi < 31) {
                return 0;
            } else if (aqi < 51) {
                return 1;
            } else if (aqi < 101) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    /**
     * pm25 미세먼지 등급 계산 0 : 좋음, 1: 보통, 2: 나쁨, 3: 매우나쁨
     *
     * @param aqi        미세먼지 농도
     * @param isWhoGrade WHO 기준 여부
     * @return pm25 미세먼지 등급
     */
    public static int pm25Grade(int aqi, boolean isWhoGrade) {
        if(aqi == -1) return -1;         //측정값 없음
        if (isWhoGrade) {
            if (aqi < 16) {
                return 0;
            } else if (aqi < 26) {
                return 1;
            } else if (aqi < 51) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (aqi < 16) {
                return 0;
            } else if (aqi < 51) {
                return 1;
            } else if (aqi < 101) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    /**
     * 등급 별 해당 표기 문자 리턴
     * @param grade 등급
     * @return 등급 표기
     */
    public static String getGradeStr(int grade) {
        switch(grade) {
            case 0:
                return "좋음";
            case 1:
                return "보통";
            case 2:
                return "나쁨";
            case 3:
                return "매우 나쁨";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 일반 좌표계를 TM 좌표계로 변환한다.
     *
     * @param x x 좌표
     * @param y y 좌표
     */
    public static GeoPoint convertToTM(Double x, Double y) {
        return GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, new GeoPoint(x, y));
//        txtResult.setText("변환 결과 : " + (int)resultPoint.getX() + ", " + (int)resultPoint.getY());
    }
}
