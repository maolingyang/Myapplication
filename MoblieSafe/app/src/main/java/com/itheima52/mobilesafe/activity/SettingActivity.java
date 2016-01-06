package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AddressDao;
import com.itheima52.mobilesafe.receiver.OutCallReceiver;
import com.itheima52.mobilesafe.service.AddressService;
import com.itheima52.mobilesafe.service.CallSafeService;
import com.itheima52.mobilesafe.utils.ServiceStatusUtils;
import com.itheima52.mobilesafe.utils.ToashUtils;
import com.itheima52.mobilesafe.view.SettingClickView;
import com.itheima52.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 *
 * @author Kevin
 */
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;// 设置升级
    private SharedPreferences mPref;
    private SettingItemView mSivAddress;
    private SettingClickView scvlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        mSivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        scvlocation = (SettingClickView) findViewById(R.id.scv_location);

        // 初始化来电显示服务
        initAddressView();

        // 归属地显示的点击时间
        addressLocation();

        //初始化黑名单
        initCallSafe();

        boolean autoUpdate = mPref.getBoolean("auto_update", true);

        if (autoUpdate) {
            // sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
            // sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    // 设置不勾选
                    sivUpdate.setChecked(false);
                    // sivUpdate.setDesc("自动更新已关闭");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    sivUpdate.setChecked(true);
                    // sivUpdate.setDesc("自动更新已开启");
                    // 更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }

    /**
     * 初始化黑名单
     */
    private void initCallSafe() {
       final SettingItemView callsafe = (SettingItemView) findViewById(R.id.siv_callsafe);
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.itheima52.mobilesafe.service.CallSafeService");

        if (serviceRunning) {
            callsafe.setChecked(true);
        } else {
            callsafe.setChecked(false);
        }
        callsafe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean checked = mSivAddress.isChecked();
                if (checked) {
                    callsafe.setChecked(false);
                    stopService(new Intent(SettingActivity.this,
                            CallSafeService.class));
                } else {
                    callsafe.setChecked(true);
                    startService(new Intent(SettingActivity.this,
                            CallSafeService.class));
                }
            }
        });
    }

    /**
     * 初始化来电归属地显示设置
     */
    private void initAddressView() {

        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this,
                "com.itheima52.mobilesafe.service.AddressService");

        if (serviceRunning) {
            mSivAddress.setChecked(true);
        } else {
            mSivAddress.setChecked(false);
        }
        mSivAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean checked = mSivAddress.isChecked();
                if (checked) {
                    mSivAddress.setChecked(false);
                    // 关闭来电归属地
                    stopService(new Intent(SettingActivity.this,
                            AddressService.class));
                } else {
                    mSivAddress.setChecked(true);
                    // 开启来电归属地服务
                    startService(new Intent(SettingActivity.this,
                            AddressService.class));
                }
            }
        });
    }

    private void addressLocation() {

        scvlocation.setTitle("归属地提示框显示位置");
        scvlocation.setDesc("设置归属地提示框的显示位置");
        scvlocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,
                        DrapActivity.class));
            }
        });
    }
}
