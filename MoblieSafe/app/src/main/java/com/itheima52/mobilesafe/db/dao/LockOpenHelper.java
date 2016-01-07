package com.itheima52.mobilesafe.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maoly on 2016/1/7.
 */
public class LockOpenHelper extends SQLiteOpenHelper{


    public LockOpenHelper(Context context) {
        super(context, "appLock.db", null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table lock (_id integer primary key autoincrement,packageName varchar(20)");
    }

}
