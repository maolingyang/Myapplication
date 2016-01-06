package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AddressDao;

/**
 * 手机来电归属地显示服务
 * 
 * @author Administrator
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private int startX;
	private int startY;
	private SharedPreferences mPref;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		// 监听电话的状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);
	}

	public class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 电话响铃
				// 通过电话号码获取到归属地
				String address = AddressDao.getAddress(incomingNumber);
				// ToashUtils.showLongToash(AddressService.this, address);
				showMyToash(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// 电话空闲（挂断）
				if (mWM != null && view != null) {
					mWM.removeView(view);
					view = null;
				}
				break;
			}

		}
	}
     
	/**
	 * 去电来段归属地的广播接收者
	 * 
	 * @author Administrator
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 直接回去到去电的电话号码
			String phonenumber = getResultData();
			String address = AddressDao.getAddress(phonenumber);
			// ToashUtils.showLongToash(context, address);
			showMyToash(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 停止对电话监听
		// 服务关闭的时候注销广播
		unregisterReceiver(receiver);
	}

	@SuppressWarnings("deprecation")
	public void showMyToash(String address) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		//获取屏幕的宽高
		final int winwidth = mWM.getDefaultDisplay().getWidth();
		final int winheight = mWM.getDefaultDisplay().getHeight();

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 没有焦点
				// | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 设置window窗口没有触摸事件
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;// 设置window布局界面的优先级
		params.gravity = Gravity.LEFT + Gravity.TOP;// 将重心位置放在左上角/默认重心是相对自己

		params.setTitle("Toast");

		//初始化归属地位置
		//获取到保存的位置
		params.x = mPref.getInt("drap_x", winwidth/2);
		params.y = mPref.getInt("drap_y", winheight/2);
		
		
		view = View.inflate(this, R.layout.toash_window, null);
		TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
		tv_address.setText(address);

		//设置电话界面的归属地窗口的触摸事件
		//需要权限
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//初始化坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//在屏幕上移动
					//移动到的坐标
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx =  endX-startX;
					int dy =  endY-startY;
					
					// 更新浮窗位置
					params.x+=dx;
					params.y+=dy;
					
					//优化防止移动坐标 出整个屏幕
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
					if(params.x > winwidth-view.getWidth()){
						params.x = winwidth-view.getWidth();
					}
					if(params.y>winheight-view.getHeight()){
						params.y = winheight-view.getHeight();
					}
					//更新界面
					mWM.updateViewLayout(view, params);
					//重新初始化坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					//离开屏幕
					//记录最后停留的位置坐标
					Editor edit = mPref.edit();
					edit.putInt("drap_x", params.x);
					edit.putInt("drap_y", params.y);
					edit.commit();
					break;
				}
				
				return false;
			}
		});
		
		mWM.addView(view, params);// 将view添加在屏幕上(Window)
	}

}
