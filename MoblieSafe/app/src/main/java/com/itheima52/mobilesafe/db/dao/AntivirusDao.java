package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by maoly on 2016/1/6.
 */
public class AntivirusDao {

    public static final String PATH = "data/data/com.itheima52.mobilesafe/files/antivirus.db";

    public static String getAntivirus(String md5) {
        String desc = null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
        if (cursor.moveToNext()) {
            desc = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }

    public static void addVirus(String desc,String md5){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues contentValues = new ContentValues();
        contentValues.put("desc",desc);
        contentValues.put("md5",md5);
        db.insert("datable",null, contentValues);
        db.close();
    }
}
