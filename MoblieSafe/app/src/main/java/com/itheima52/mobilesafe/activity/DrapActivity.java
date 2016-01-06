package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

/**
 * 拖拽的活动activity
 * 
 * @author maoly
 * 
 */
public class DrapActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;
	private ImageView ivDrap;
	private int startX;
	private int startY;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		ivDrap = (ImageView) findViewById(R.id.iv_drag);
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		Display display = getWindowManager().getDefaultDisplay();
		final int winwidth = display.getWidth();
		final int winheight = display.getHeight();
		// 获取到保存的位置
		int x = mPref.getInt("drap_x", 0);
		int y = mPref.getInt("drap_y", 0);

		// 上下 两个TextView的位置
		if (y > winheight / 2) {
			// 上面隐藏，下面显示
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}
		// 获取imageview 布局中的参数
		final RelativeLayout.LayoutParams params = (LayoutParams) ivDrap
				.getLayoutParams();
		// 参数的设置和 离开屏幕的对应
		params.leftMargin = x;
		params.topMargin = y;
		ivDrap.setLayoutParams(params);// 重新布置imageview的位置

		// 点击次数
		final long[] mHits = new long[2];
		ivDrap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					//双击后重新布局，此时imageview已经出现可以直接layout定位布局
					ivDrap.layout(winwidth / 2 - ivDrap.getWidth()/2,
							ivDrap.getTop(),
							winwidth / 2 + ivDrap.getWidth()/2,
							ivDrap.getBottom());
				}
			}
		});

		ivDrap.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 在屏幕上移动
					// 移动到的坐标
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx = endX - startX;
					int dy = endY - startY;
					int l = ivDrap.getLeft() + dx;
					int r = ivDrap.getRight() + dx;

					int t = ivDrap.getTop() + dy;
					int b = ivDrap.getBottom() + dy;

					// 边界的判断，如果移动出屏幕边界 则直接返回
					if (l < 0 || r > winwidth || t < 0 || b > winheight - 25) {
						break;
					}

					// 上下 两个TextView的位置
					if (t > winheight / 2) {
						// 上面隐藏，下面显示
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {

						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}

					// l Left position, relative to parent
					// t Top position, relative to parent
					// r Right position, relative to parent
					// b Bottom position, relative to parent
					// 更新界面
					ivDrap.layout(l, t, r, b);

					// 重新初始化坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 离开屏幕
					// 记录最后停留的位置坐标
					Editor edit = mPref.edit();
					edit.putInt("drap_x", ivDrap.getLeft());
					edit.putInt("drap_y", ivDrap.getTop());
					edit.commit();
					break;
				}
				// 需要设置为false，否则点击事件不响应
				return false;
			}
		});
	}
}
