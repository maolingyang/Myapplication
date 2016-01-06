package com.itheima52.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	public static final String PATH = "data/data/com.itheima52.mobilesafe/files/address.db";

	public static String getAddress(String number) {
		String address = "未知号码";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		// 使用正则表达式选择手机号码
		if (number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		} else {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 8:
			case 7:
				address = "本地电话";
				break;

			default:
				if (number.startsWith("0") && number.length() > 10) {// 有可能是长途电话
					// 先查询4位区号
					Cursor cursor = database.rawQuery(
							"select location from data2 where area =?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();
						// 查询3位区号
						cursor = database.rawQuery(
								"select location from data2 where area =?",
								new String[] { number.substring(1, 3) });
						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		database.close();//关闭数据库连接
		return address;
	}
}
