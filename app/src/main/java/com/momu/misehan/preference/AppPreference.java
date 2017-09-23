package com.momu.misehan.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by knulps on 2017. 7. 31..
 */

public class AppPreference {
    /**
     * @return WHO 기준 사용 여부 불러옴
     */
    public static boolean loadIsWhoGrade(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean("isWhoGrade", false);
    }

    /**
     * WHO 기준 사용 여부 저장
     *
     * @param state WHO 기준 사용 여부
     */
    public static void saveIsWhoGrade(Context mContext, boolean state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isWhoGrade", state);
        editor.apply();
    }

    /**
     * @return 가장 최근 측정값 가져온 측정소명, 검색 주소 Object 불러옴
     */
    public static String loadLastMeasureStation(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString("lastMeasurePlace", "");
    }

    /**
     * 가장 최근 측정값 가져온 측정소명, 검색 주소 Object 저장
     *
     * @param object 가장 최근 측정값 가져온 기준 주소 Object
     */
    public static void saveLastMeasurePlace(Context mContext, String object) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasurePlace", object);
        editor.apply();
    }

    /**
     * @return 가장 최근 측정값 가져온 기준 주소 불러옴
     */
    public static String loadLastMeasureAddr(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString("lastMeasureAddr", "");
    }

    /**
     * 가장 최근 측정값 가져온 주소명 저장
     *
     * @param address 가장 최근 측정값 가져온 주소명
     */
    public static void saveLastMeasureAddr(Context mContext, String address) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasureAddr", address);
        editor.apply();
    }

    /**
     * @return 가장 최근 측정값 가져온 시간 (YYYY-MM-DD HH:mm) 불러옴
     */
    public static String loadLastMeasureTime(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString("lastMeasureTime", "");
    }

    /**
     * 가장 최근 측정값 가져온 시간 저장
     *
     * @param time 가장 최근 측정값 가져온 시간 (YYYY-MM-DD HH:mm) 저장
     */
    public static void saveLastMeasureTime(Context mContext, String time) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasureTime", time);
        editor.apply();
    }

    /**
     * @return 가장 최근 측정값 Object 불러옴 (측정소명 추가함)
     */
    public static String loadLastMeasureDetail(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString("lastMeasureDetail", "");
    }

    /**
     * 가장 최근 측정값 Object 저장 (측정소명 추가함)
     *
     * @param detail 가장 최근 측정값 Object
     */
    public static void saveLastMeasureDetail(Context mContext, String detail) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasureDetail", detail);
        editor.apply();
    }

    /**
     * 측정소 설정 불러옴
     *
     * @return true : 검색해서 찾은 측정소 사용, false : 현재 위치 기준 측정소 사용
     */
    public static boolean loadIsUseSearchedStation(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean("useSearchedStation", false);
    }

    /**
     * 측정소 설정 저장
     *
     * @param state true : 검색해서 찾은 측정소 사용, false : 현재 위치 기준 측정소 사용
     */
    public static void saveIsUseSearchedStation(Context mContext, boolean state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("useSearchedStation", state);
        editor.apply();
    }

    /**
     * @return 퍼미션 보여주는 페이지 관련 값 불러옴
     */
    public static boolean loadIsFirst(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getBoolean("isFirst", false);
    }

    /**
     * 측정소 설정 저장
     *
     * @param state true : 퍼미션 페이지 보여줌, false : 퍼미션페이지 보여주지 않음
     */
    public static void saveIsFirst(Context mContext, boolean state) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst", state);
        editor.apply();
    }
}
