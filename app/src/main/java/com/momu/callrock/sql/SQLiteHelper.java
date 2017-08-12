package com.momu.callrock.sql;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by songm on 2017-08-12.
 */

public class SQLiteHelper extends SQLiteAssetHelper {
    
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "callrock.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
}
