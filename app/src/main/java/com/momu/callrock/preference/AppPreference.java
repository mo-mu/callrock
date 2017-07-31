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
}
