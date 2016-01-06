package com.itheima52.mobilesafe.receiver;

import java.util.ArrayList;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences mPref;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取设备策略服务
		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(context,
				AdminReceiver.class);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objects) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String address = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			if ("#*alarm*#".equals(body)) {
				MediaPlayer create = MediaPlayer.create(context, R.raw.ylzs);
				create.setVolume(1f, 1f);
				create.setLooping(true);// 设置歌曲循环
				create.start();
				abortBroadcast();
			} else if ("#*location*#".equals(body)) {
				Intent service = new Intent(context, LocationService.class);
				context.startService(service);
				mPref = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				String location = mPref.getString("location", "");
				SmsManager smsManager = SmsManager.getDefault();
				ArrayList<String> list = smsManager.divideMessage(location);
				for (String s : list) {
					smsManager.sendTextMessage(address, null, s, null, null);
				}
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(body)) {
				//激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
				// 判断设备管理器是否已经激活  
				if(mDPM.isAdminActive(mDeviceAdminSample)){
					mDPM.lockNow();
					//设置锁屏密码
					mDPM.resetPassword("123456", 0);
				}
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {
				if(mDPM.isAdminActive(mDeviceAdminSample)){
					mDPM.wipeData(0);
				}
				abortBroadcast();
			}
		}

	}
}
