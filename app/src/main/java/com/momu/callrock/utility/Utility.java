package com.momu.callrock.utility;

/**
 * 유틸리티
 * Created by knulps on 2017. 7. 1..
 */

public class Utility {
    /**
     * 미세먼지 등급 계산 0 : 좋음, 1: 보통, 2: 나쁨, 3: 매우나쁨
     *
     * @return 미세먼지 등급
     */
    public static int airQualityGrade(int aqi) {
        if (aqi < 51) {
            return 0;
        } else if (aqi < 101) {
            return 1;
        } else if (aqi < 201) {
            return 2;
        } else {
            return 3;
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
