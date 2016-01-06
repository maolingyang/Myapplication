package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service{

	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("定位服务已经创建 ");
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		super.onCreate();
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	
		Criteria criteria =new Criteria();
		//使用一个合适的定位精度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//是否使用付费方式定位 例如使用网络
		criteria.setCostAllowed(true);
		String provider = lm.getBestProvider(criteria, true);
		MyLocationListenter listener = new MyLocationListenter();
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	public class MyLocationListenter implements LocationListener{

		//位置发生变化调用
		@Override
		public void onLocationChanged(Location location) {
			double jlongitude = location.getLongitude();//经度
			double wlatitude = location.getLatitude();//纬度
			mPref.edit().putString("location", "经度:"+jlongitude+"  纬度:"+wlatitude).commit();
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}
		
	}
}
