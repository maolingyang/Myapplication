package com.itheima52.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.fragment.AppUnlockFragment;
import com.itheima52.mobilesafe.fragment.ApplockFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ApplockActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.fl_content)
    private FrameLayout fl_content;
    @ViewInject(R.id.tv_unlock)
    private TextView tv_unlock;
    @ViewInject(R.id.tv_lock)
    private TextView tv_lock;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        ViewUtils.inject(this);
        initUI();
    }

    private void initUI() {
        fragmentManager = getSupportFragmentManager();
        AppUnlockFragment unlockFragment = new AppUnlockFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_content, unlockFragment).commit();
        tv_unlock.setOnClickListener(this);
        tv_lock.setOnClickListener(this);
    }


    public void appunlockList(View view) {
        AppUnlockFragment unlockFragment = new AppUnlockFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_content, unlockFragment).commit();

    }

    public void applockList(View view) {
        ApplockFragment applockFragment = new ApplockFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_content, applockFragment).commit();
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.tv_unlock:
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_lock.setBackgroundResource(R.drawable.tab_right_default);
                AppUnlockFragment appUnlockFragment = new AppUnlockFragment();
                ft.replace(R.id.fl_content, appUnlockFragment);
                break;
            case R.id.tv_lock:
                ApplockFragment applockFragment = new ApplockFragment();
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                ft.replace(R.id.fl_content, applockFragment);
                break;
        }
        ft.commit();
    }
}
