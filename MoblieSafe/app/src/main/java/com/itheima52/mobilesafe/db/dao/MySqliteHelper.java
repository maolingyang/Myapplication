package com.itheima52.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itheima52.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

public class MySqliteHelper extends SQLiteOpenHelper {

	private SQLiteDatabase db = getWritableDatabase();
	public MySqliteHelper(Context context) {
		super(context, "safe.db", null, 1);
	}

	//number黑名单号码，mode拦截模式：1电话拦截 2短信拦截  3全部拦截
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


}
