package com.itheima52.mobilesafe.engin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.AppProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class AppProcessInfoEngin {
    public static List<AppProcessInfo> getAppProcessInfo(Context context) {

        PackageManager packageManager = context.getPackageManager();

        List<AppProcessInfo> processInfos = new ArrayList<AppProcessInfo>();

        // 获取到进程管理器
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);
        // 获取到手机上面所有运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : appProcesses) {

            AppProcessInfo processInfo = new AppProcessInfo();

            // 获取到进程的名字
            String processName = runningAppProcessInfo.processName;

            processInfo.setPackageName(processName);

            try {
                // 获取到内存基本信息
                /**
                 * 这个里面一共只有一个数据
                 */
                Debug.MemoryInfo[] memoryInfo = activityManager
                        .getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                // Dirty弄脏
                // 获取到总共弄脏多少内存(当前应用程序占用多少内存)
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;


//				System.out.println("==========="+totalPrivateDirty);

                processInfo.setMemorySize(totalPrivateDirty);

                PackageInfo packageInfo = packageManager.getPackageInfo(
                        processName, 0);

                // /获取到图片
                Drawable icon = packageInfo.applicationInfo
                        .loadIcon(packageManager);

                processInfo.setIcon(icon);
                // 获取到应用的名字
                String appName = packageInfo.applicationInfo.loadLabel(
                        packageManager).toString();

                processInfo.setAppName(appName);

                System.out.println("-------------------");
                System.out.println("processName="+processName);
                System.out.println("appName="+appName);
                //获取到当前应用程序的标记
                //packageInfo.applicationInfo.flags 我们写的答案
                //ApplicationInfo.FLAG_SYSTEM表示老师的该卷器
                int flags = packageInfo.applicationInfo.flags;
                //ApplicationInfo.FLAG_SYSTEM 表示系统应用程序
                if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ){
                    //系统应用
                    processInfo.setIsuser(false);
                }else{
//					/用户应用
                    processInfo.setIsuser(true);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // 系统核心库里面有些系统没有图标。必须给一个默认的图标

                processInfo.setAppName(processName);
                processInfo.setIcon(context.getResources().getDrawable(
                        R.drawable.ic_launcher));
            }

            processInfos.add(processInfo);
        }

        return processInfos;
    }

}
