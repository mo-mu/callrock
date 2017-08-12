package com.momu.callrock.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-08-12.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new SQLiteHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getReadableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getWholeAddress(){
        List<String> result = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM whole_address",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
}
