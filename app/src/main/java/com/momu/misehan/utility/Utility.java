package com.momu.misehan.utility;

import android.content.Context;
import android.location.LocationManager;

import com.momu.misehan.preference.AppPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 유틸리티
 * Created by knulps on 2017. 7. 1..
 */

public class Utility {
    private static final String TAG = "Utility";

    /**
     * pm10 미세먼지 등급 계산 0 : 좋음, 1: 보통, 2: 나쁨, 3: 매우나쁨
     *
     * @param strAqi 미세먼지 농도
     * @return pm10 미세먼지 등급
     */
    public static int pm10Grade(Context mContext, String strAqi) {
        int aqi = Integer.parseInt(strAqi);
        boolean isWhoGrade = AppPreference.loadIsWhoGrade(mContext); // WHO 기준 여부

        if (aqi == -1) return -1;        //측정값 없음

        if (isWhoGrade) {
            if (aqi < 31) {
                return 0;
            } else if (aqi < 51) {
                return 1;
            } else if (aqi < 101) {
                return 2;
            } else {
                return 3;
            }

        } else {
            if (aqi < 31) {
                return 0;
            } else if (aqi < 81) {
                return 1;
            } else if (aqi < 151) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    /**
     * pm25 미세먼지 등급 계산 0 : 좋음, 1: 보통, 2: 나쁨, 3: 매우나쁨
     *
     * @param strAqi 미세먼지 농도
     * @return pm25 미세먼지 등급
     */
    public static int pm25Grade(Context mContext, String strAqi) {
        int aqi = Integer.parseInt(strAqi);
        boolean isWhoGrade = AppPreference.loadIsWhoGrade(mContext); // WHO 기준 여부

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
     *
     * @param grade 등급
     * @return
     */
    public static String getWholeStr(int grade) {
        switch (grade) {
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
     *
     * @param grade 등급
     * @return
     */
    public static String getMaskStr(int grade) {
        switch (grade) {
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
     *
     * @param grade 등급
     * @return
     */
    public static String getActivityStr(int grade) {
        switch (grade) {
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
     *
     * @param grade 등급
     * @return
     */
    public static String getLifeStr(int grade) {
        switch (grade) {
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
     *
     * @param grade 등급
     * @return
     */
    public static String getWindowStr(int grade) {
        switch (grade) {
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

    /**
     * 현재시간과 최근 측정값을 API 서버에서 불러온 시간을 비교해서 새로 측정값을 불러올 시간이 되었는지 여부 리턴
     *
     * @return 새로 측정값을 불러올 시간이 지났는지 여부
     */
    public static boolean shouldRefreshDetail(Context mContext) {
        try {
            LogHelper.e(TAG, "loadLastMeasureTime : " + AppPreference.loadLastMeasureTime(mContext));
            if (AppPreference.loadLastMeasureTime(mContext).equals("")) return true;

            Calendar lastMeasureCal = Calendar.getInstance();
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
            lastMeasureCal.setTime(input.parse(AppPreference.loadLastMeasureTime(mContext)));

            Calendar currentCal = Calendar.getInstance();

            //임시로 시간까지만 비교함. 나중에는 특정 분이 지날때 까지 새로고침 하도록 설정해야 함.
            return !(lastMeasureCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                    lastMeasureCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH) &&
                    lastMeasureCal.get(Calendar.DATE) == currentCal.get(Calendar.DATE) &&
                    lastMeasureCal.get(Calendar.HOUR_OF_DAY) == currentCal.get(Calendar.HOUR_OF_DAY));

            //년 월 일 시간까지 전부 같은 경우 새로고침 하지 않는다 (false 리턴)

        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
            return true;
        }
    }

    /**
     * 위치 설정 켜졌는지 여부 리턴
     *
     * @return 위치 설정 켜짐 여부
     */
    public static boolean isLocationServiceEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false, networkEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        }

        return gpsEnabled || networkEnabled;
    }
}
