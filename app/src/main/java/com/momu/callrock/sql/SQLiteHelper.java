package com.momu.callrock.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.momu.callrock.item.WholeCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-08-12.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "address.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE whole_city (id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, address TEXT, longitude REAL, latitude REAL);";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void setInitData(){
        SQLiteDatabase db = getWritableDatabase();
        int id =0;
        String address = "강남";
        double longitude =1, latitude=1 ;
        db.execSQL("INSERT INTO whole_city VALUES("+id+", '"+address+"',"+longitude+","+latitude+")");
        db.close();
    }

    public List<String> getWholeAddress(){
        SQLiteDatabase db = getReadableDatabase();
        List<String> result = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT address FROM whole_city;",null);
        while(c.moveToNext()){
            result.add(c.getString(0));
        }

        return result;
    }

    public WholeCity getCityData(int i){
        int id = -1;
        String address = "";
        double longitude = -1, latitude = -1;
        SQLiteDatabase db = getReadableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM whole_city WHERE id = "+i+";",null);
        while(c.moveToNext()){
            id = c.getInt(0);
            address = c.getString(1);
            longitude = c.getDouble(2);
            latitude = c.getDouble(3);
        }


        return new WholeCity(id,address,longitude,latitude);
    }
}
