package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.receiver.AdminReceiver;

/**
 * 手机防盗页面
 * 
 * @author Kevin
 * 
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPrefs;
	private TextView mTvSafePhoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPrefs.getBoolean("configed", false);// 判断是否进入过设置向导
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			ImageView ivlock = (ImageView) findViewById(R.id.iv_lock);
			mTvSafePhoneNumber = (TextView) findViewById(R.id.tv_safephonenumber);
			String safe_phone = mPrefs.getString("safe_phone", "");
			System.out.println(safe_phone);
			mTvSafePhoneNumber.setText(safe_phone);
			boolean protect = mPrefs.getBoolean("protect", false);
			if (protect) {
				ivlock.setImageResource(R.drawable.lock);
			} else {
				ivlock.setImageResource(R.drawable.unlock);
			}
		} else {
			// 跳转设置向导页
			startActivity(new Intent(this, Setup1Activity.class));
			finish();
		}
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
