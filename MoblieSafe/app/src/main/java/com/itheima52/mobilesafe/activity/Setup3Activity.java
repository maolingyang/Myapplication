package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.ToashUtils;

/**
 * 第3个设置向导页
 * 
 * @author Kevin
 * 
 */
public class Setup3Activity extends BaseActivity {

	private TextView mEt_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		String safe_phone = mPref.getString("safe_phone", "");
		mEt_phone = (TextView) findViewById(R.id.et_phone);
		mEt_phone.setText(safe_phone);
	}

	/**
	 * 获取到联系人的点击事件
	 * @param view
	 */
	public void contacts(View view){
		Intent intent = new Intent(this,ContactsActivity.class);
		startActivityForResult(intent, 1);
		//此处不能finish();  否则不能返回页面
	}
	//选择联系人之后返回的结果由这个方法接受
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//这个是点击确定以后可以获取到数据
		if(Activity.RESULT_OK==resultCode){
			String phone = data.getExtras().get("phone").toString();
			mEt_phone.setText(phone);
			//将安全号码保存起来
			mPref.edit().putString("safe_phone", phone).commit();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void showNextPage() {
		String phone = mEt_phone.getText().toString();
		mPref.edit().putString("safe_phone", phone).commit();
		if(TextUtils.isEmpty(phone)){
			ToashUtils.showShortToash(this, "请设置安全联系人");
		}else{
			startActivity(new Intent(this, Setup4Activity.class));
			finish();
			// 两个界面切换的动画
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
		}
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();

		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画

	}
}
