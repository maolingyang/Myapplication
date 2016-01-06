package com.itheima52.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToashUtils {

	public static  void showShortToash(Context context , String content){
		Toast.makeText(context, content,Toast.LENGTH_SHORT).show();
	}

	public static void showLongToash(Context context,
			String content) {
		Toast.makeText(context, content,Toast.LENGTH_LONG).show();		
	}
}
