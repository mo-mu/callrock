package com.momu.callrock.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.momu.callrock.item.SearchItem;

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

    /**
     * db를 readable형식으로 여는 메소드
     */
    public void open() {
        this.database = openHelper.getReadableDatabase();
    }

    /**
     * db 연결을 종료하는 메소드
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * 전체 주소 리스트를 가져오는 메소드
     * @return 전체 주소 리스트
     */
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

    /**
     * 검색한 결과를 가져와 리스트에 뿌려줄 데이터를 모아주는 메소드
     * @param s 검색결과
     * @return 검색한 결과가 들어있는 값들이 모여있는 list
     */
    public ArrayList<SearchItem> getSearchAddress(String s) {
        ArrayList<SearchItem> result = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM whole_address WHERE address LIKE '%"+s+"%'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(new SearchItem(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3)));
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }
}
