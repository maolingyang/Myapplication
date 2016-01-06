package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.ToashUtils;
import com.itheima52.mobilesafe.view.SettingItemView;

/**
 * 第2个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup2Activity extends BaseActivity {

	private SettingItemView msivSim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		msivSim = (SettingItemView) findViewById(R.id.siv_sim);
		String simNum = mPref.getString("band_sim", null);
		//如果是空 则没有绑定
		if(TextUtils.isEmpty(simNum)){
			msivSim.setChecked(false);
		}else{
			msivSim.setChecked(true);
		}
		msivSim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = msivSim.isChecked();
				//如果绑定了sim卡，点击之后则取消绑定，如果没有绑定则现在进行绑定
				if(checked){
					msivSim.setChecked(false);
					mPref.edit().remove("band_sim").commit();
				}else{
					msivSim.setChecked(true);
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					//获取到sim卡的序列号码
					String number = telephonyManager.getSimSerialNumber();
					mPref.edit().putString("band_sim", number).commit();
				}
			}
		});
	}
	//进入下一页页面 以及动画效果
	@Override
	public void showNextPage(){
		String simNum = mPref.getString("band_sim", null);
		if(TextUtils.isEmpty(simNum)){
			msivSim.setChecked(false);
			ToashUtils.showShortToash(this, "sim没有绑定，请绑定sim卡");
			return;
		}else{
			msivSim.setChecked(true);
		}
		
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
	}
	
	//进入上一页 以及动画效果
	@Override
	public void showPreviousPage(){
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
