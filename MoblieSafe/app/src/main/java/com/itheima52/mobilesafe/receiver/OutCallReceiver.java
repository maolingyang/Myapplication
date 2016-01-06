package com.itheima52.mobilesafe.receiver;

import com.itheima52.mobilesafe.db.dao.AddressDao;
import com.itheima52.mobilesafe.utils.ToashUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * 检测去电 的广播接收者
 * @author Administrator
 *
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//直接回去到去电的电话号码
		String phonenumber = getResultData();
		String address = AddressDao.getAddress(phonenumber);
		ToashUtils.showLongToash(context, address);
		
	}

}
