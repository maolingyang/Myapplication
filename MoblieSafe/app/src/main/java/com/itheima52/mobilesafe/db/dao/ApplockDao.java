package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by maoly on 2016/1/7.
 */
public class ApplockDao {

    private LockOpenHelper helper;

    public ApplockDao(Context context) {
        helper = new LockOpenHelper(context);
    }

    public void  add(String packageName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("packageName",packageName);
        db.insert("lock",null, contentValues);
        db.close();
    }
    public boolean isLock(String packageName){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("lock", null, "packageName=?", new String[]{packageName}, null, null, null);
        if(cursor.moveToNext()){
            return true;
        }
        return  false;
    }
}
