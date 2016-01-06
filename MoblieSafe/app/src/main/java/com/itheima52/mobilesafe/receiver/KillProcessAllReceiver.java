package com.itheima52.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itheima52.mobilesafe.utils.ToashUtils;

import java.util.List;

/**
 * Created by maoly on 2016/1/6.
 */
public class KillProcessAllReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info: runningAppProcesses
             ) {
            activityManager.killBackgroundProcesses(info.processName);
        }
        ToashUtils.showShortToash(context,"清理完毕");
    }

}
