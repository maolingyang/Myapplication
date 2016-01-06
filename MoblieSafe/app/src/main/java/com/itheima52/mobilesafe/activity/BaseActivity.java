package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * activity 的基类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseActivity extends Activity {
	public GestureDetector mDetector;
	public SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		mDetector = new GestureDetector(this, new SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				// 如果用户滑动的速度过慢 这也不进行上一页和下一页的切换
				if (velocityX < 100) {
					return true;
				}

				// 如果用户滑动的纵向距离过大 则不会滑动进入上一页或下一页的页面
				if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
					return true;
				}

				// 向右滑动，，，页面进入上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
				}

				// 向左滑动 页面进入下一页
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 将点击事件交由给mDetector进行处理
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// 点击下一页 进入下一页的页面
	public void next(View view) {
		showNextPage();
	}

	// 进入下一页页面 以及动画效果
	public abstract void showNextPage();

	// 上一页
	public void previous(View view) {
		showPreviousPage();
	}
	//进入上一页 以及动画效果
	public abstract void showPreviousPage();
}
