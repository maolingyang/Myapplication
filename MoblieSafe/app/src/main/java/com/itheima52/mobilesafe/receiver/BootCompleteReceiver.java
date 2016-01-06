package com.itheima52.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences mPerf;

	@Override
	public void onReceive(Context context, Intent intent) {

		mPerf = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 只有防盗功能开启才能进入获取sim卡功能
		if (mPerf.getBoolean("protect", false)) {
			mPerf = context
					.getSharedPreferences("config", Context.MODE_PRIVATE);
			String simNumber = mPerf.getString("band_sim", null);
			System.out.println("开机启动的获取到getSharedPreferences  sim卡信息");
			if (!TextUtils.isEmpty(simNumber)) {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				// 获取到sim卡的序列号码
				String number = telephonyManager.getSimSerialNumber();
				// 如果改变了sim卡,则是发送给安全号码告知改变sim卡通知
				if (!simNumber.equals(number)) {
					System.out.println("sim改变，发送短信通知");
				}

			} else {
				System.out.println("没有绑定sim卡");
			}
		}

	}

}
