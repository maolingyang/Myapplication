package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AddressDao;

public class AddressActivivty extends Activity {

	private EditText mEt_number;
	private TextView mTvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		mEt_number = (EditText) findViewById(R.id.et_number);
		mTvResult = (TextView) findViewById(R.id.tv_result);
		//设置输入框文字变化的监听
		mEt_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String address = AddressDao.getAddress(s.toString());
				mTvResult.setText(address);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}
	
	/**
	 * 查询归属地
	 * @param view
	 */
	public void query(View view){
		String number = mEt_number.getText().toString();
		if(TextUtils.isEmpty(number)){
			//抖动效果
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_number.setAnimation(shake);
			vibrate();
		}else{
			String address = AddressDao.getAddress(number);
			mTvResult.setText(address);
		}
	}
	/**
	 * 手机震动的效果
	 */
	private void vibrate(){
		Vibrator vibrator =(Vibrator) getSystemService(VIBRATOR_SERVICE);
//		vibrator.vibrate(new long[]{1000,2000,1000,3000}, -1);//抖动的频率以及循环位置-1 不循环，0 从头循环，1从1的位置循环
		vibrator.vibrate(2000);//震动的时间
	}
}
