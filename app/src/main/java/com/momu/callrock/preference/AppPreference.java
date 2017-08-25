package com.momu.callrock.preference;

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
     * @return 가장 최근 측정값 가져온 측정소명 불러옴
     */
    public static String loadLastMeasureStation(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString("lastMeasureStation", "");
    }

    /**
     * 가장 최근 측정값 가져온 기준 주소 저장
     *
     * @param station 가장 최근 측정값 가져온 기준 주소
     */
    public static void saveLastMeasureStation(Context mContext, String station) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasureStation", station);
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
     * 가장 최근 측정값 가져온 측정소명 저장
     *
     * @param station 가장 최근 측정값 가져온 측정소명
     */
    public static void saveLastMeasureAddr(Context mContext, String station) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastMeasureAddr", station);
        editor.apply();
    }

    /**
     * @return 가장 최근 측정값 가져온 시간 (timemillis) 불러옴
     */
    public static long loadLastMeasureTime(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getLong("lastMeasureTime", 0);
    }

    /**
     * 가장 최근 측정값 가져온 시간 (timemillis) 저장
     *
     * @param time 가장 최근 측정값 가져온 시간
     */
    public static void saveLastMeasureTime(Context mContext, long time) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastMeasureTime", time);
        editor.apply();
    }
}
