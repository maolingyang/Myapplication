package com.itheima52.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

;

/**
 * Created by maoly on 2016/1/5.
 */
public class SystemInfoUtils {


    public static int getProcessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();
    }
    /**
     * 获取运行的剩余内存
     * @param context
     * @return
     */
    public static Long availMem(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        //获取到剩余运行内存
        return memoryInfo.availMem;

    }


    /**
     * 总的运行内存存在proc/meminfo文件夹下面
     * memoryInfo.totalMem方式也可以获取，但是无法向下Android系统兼容
     *
     * @return
     */
    public static long totalMem() {
        InputStream is =null;
        try {
            is = new FileInputStream(new File("proc/meminfo"));
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String readLine = buf.readLine();
            StringBuilder sb = new StringBuilder();
            for (char c : readLine.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return Long.parseLong(sb.toString()) * 1024;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
