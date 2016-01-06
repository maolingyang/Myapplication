package com.itheima52.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.receiver.AdminReceiver;

/**
 * 第4个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup4Activity extends BaseActivity {

	private CheckBox mProtect;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		// 获取设备策略服务
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);

		mProtect = (CheckBox) findViewById(R.id.cb_protect);
		boolean b = mPref.getBoolean("protect", false);
		if (b) {
			mProtect.setText("防盗保护已经开启");
			mProtect.setChecked(true);
		} else {
			mProtect.setText("防盗保护没有开启");
			mProtect.setChecked(false);
		}
	}

	// 开启关闭防盗保护的点击事件
	public void protect(View view) {
		boolean checked = mProtect.isChecked();
		// 判断是否为选择状态
		if (checked) {
			// 判断设备管理器是否已经激活
			if (!mDPM.isAdminActive(mDeviceAdminSample)) {
				activeDevice(mDeviceAdminSample);
			} else {
				// 保护开启状态
				mProtect.setChecked(true);
				mProtect.setText("防盗保护已经开启");
				mPref.edit().putBoolean("protect", true).commit();
			}

		} else {
			mProtect.setChecked(false);
			mProtect.setText("防盗保护没有开启");
			mPref.edit().putBoolean("protect", false).commit();
		}
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
		mPref.edit().putBoolean("configed", true).commit();// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}

	// 激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
	public void activeDevice(ComponentName mDeviceAdminSample) {
		Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				getString(R.string.add_admin_extra_app_text));
		startActivity(intent1);
	}

}
