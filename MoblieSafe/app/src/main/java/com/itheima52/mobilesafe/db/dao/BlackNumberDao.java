package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.itheima52.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public class BlackNumberDao {

    public MySqliteHelper helper;

    public BlackNumberDao(Context context) {
        helper = new MySqliteHelper(context);
    }

    public long add(BlackNumberInfo numberInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("number", numberInfo.getNumber());
        contentvalues.put("mode", numberInfo.getMode());
        long insert = db.insert("blacknumber", null, contentvalues);
        db.close();
        return insert;
    }

    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber", null);
        List<BlackNumberInfo> infoList = new ArrayList<BlackNumberInfo>();
        BlackNumberInfo numberInfo;
        while (cursor.moveToNext()) {
            numberInfo = new BlackNumberInfo();
            numberInfo.setNumber(cursor.getString(0));
            numberInfo.setMode(cursor.getString(1));
            infoList.add(numberInfo);
        }
        cursor.close();
        db.close();
        return infoList;
    }

    public List<BlackNumberInfo> findPar(int startIndex, int maxCount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{maxCount + "", startIndex + ""});
        List<BlackNumberInfo> infoList = new ArrayList<BlackNumberInfo>();
        BlackNumberInfo numberInfo;
        while (cursor.moveToNext()) {
            numberInfo = new BlackNumberInfo();
            numberInfo.setNumber(cursor.getString(0));
            numberInfo.setMode(cursor.getString(1));
            infoList.add(numberInfo);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(1000);
        return infoList;
    }

    public int getTotalNumber() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        int total = 0;
        if (cursor.moveToNext()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public int delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("blacknumber", "number=?", new String[]{number});
        db.close();
        return delete;
    }

    public void testdeleteall() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete("blacknumber", null, null);
        db.close();
    }

    public BlackNumberInfo findByNumber(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber where number=?", new String[]{number});
        if(cursor.moveToNext()){
            return new BlackNumberInfo(cursor.getString(0),cursor.getString(1));
        }
        return null;
    }
}
