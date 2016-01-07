package com.itheima52.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.fragment.AppUnlockFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ApplockActivity extends FragmentActivity {

    @ViewInject(R.id.fl_content)
    private FrameLayout fl_content;
    @ViewInject(R.id.tv_count)
    private TextView tv_count;
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
        fragmentManager.beginTransaction().replace(R.id.fl_content,unlockFragment);

    }


    public void appunlockList(View view){

    }

    public void applockList(View view){

    }
}
