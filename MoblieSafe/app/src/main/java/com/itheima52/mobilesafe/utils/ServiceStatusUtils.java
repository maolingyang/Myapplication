package com.itheima52.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {

	public static boolean isServiceRunning(Context context,String serviceName){

				ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

				//获取到多少服务
				List<RunningServiceInfo> list = am.getRunningServices(100);
				for (RunningServiceInfo info : list) {
					String className = info.service.getClassName();
					//服务列表中有serviceName的服务
					if(className.equals(serviceName)){
						return true;
			}
		}
		return false;
	}
}
