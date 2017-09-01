package com.momu.callrock.utility;

import java.util.Calendar;

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
        if (aqi == -1) return -1;        //측정값 없음

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
        if (aqi == -1) return -1;         //측정값 없음
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
     *
     * @param grade 등급
     * @return 등급 표기
     */
    public static String getGradeStr(int grade) {
        switch (grade) {
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

    /**
     * timemillis 시간을 Date format으로 변환
     *
     * @param timeMillis 시간
     * @return 변환된 시간
     */
    public static String getTimeFormatFromMillis(long timeMillis) {
        if (timeMillis == 0) return "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return mYear + "년 " + mMonth + "월 " + mDay;
    }

    /**
     * 메인 중앙 문구 리턴
     * @param grade 등급
     * @return
     */
    public static String getWholeStr(int grade) {
        switch (grade){
            case 0:
                return "마음껏 외출하세요~!";
            case 1:
                return "그냥~그냥 쏘쏘한 날이에요";
            case 2:
                return "무리한 야외활동 금지!";
            case 3:
                return "매우 나쁜 상태! 마스크 꼭 챙겨요!";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 마스크 관련 문구 리턴
     * @param grade 등급
     * @return
     */
    public static String getMaskStr(int grade) {
        switch (grade){
            case 0:
                return "마스크가 필요없는 날이에요";
            case 1:
                return "호흡기가 민감하면, 마스크를 쓰세요";
            case 2:
                return "마스크를 껴주세요";
            case 3:
                return "마스크를 꼭! 껴주세요";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 활동관련 문구 리턴
     * @param grade 등급
     * @return
     */
    public static String getActivityStr(int grade) {
        switch (grade){
            case 0:
                return "야외활동 하기 좋은 날!";
            case 1:
                return "민감군은 장시간 또는 무리한 실외 활동을 자제하세요";
            case 2:
                return "실외활동 및 외출을 자제하세요";
            case 3:
                return "실외활동 및 외출을 자제하세요";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 생활 관련 문구 리턴
     * @param grade 등급
     * @return
     */
    public static String getLifeStr(int grade) {
        switch (grade){
            case 0:
                return "밖에서 운동 어떨까요";
            case 1:
                return "물이나 비타민 C가 많은 과일/야채를 섭취해보세요";
            case 2:
                return "혹시 외출 했다면, 깨끗이 씻어주세요";
            case 3:
                return "혹시 외출 했다면, 깨끗이 씻어주세요";
            default:
                return "알 수 없음";
        }
    }

    /**
     * 환기 관련 문구 리턴
     * @param grade 등급
     * @return
     */
    public static String getWindowStr(int grade) {
        switch (grade){
            case 0:
                return "창문을 활짝 열어 환기해요";
            case 1:
                return "환기를 되도록 자제해주세요";
            case 2:
                return "환기는 최대 1분 내외로 해주세요";
            case 3:
                return "창문을 꽁꽁 닫아주세요";
            default:
                return "알 수 없음";
        }
    }
}
