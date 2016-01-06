package com.itheima52.mobilesafe.engin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima52.mobilesafe.bean.Appinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/2.
 */
public class AppInfoEngin {

    public List<Appinfo> getAppinfo(Context context) {

        List<Appinfo> appinfos = new ArrayList<Appinfo>();

        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        Appinfo appinfo;
        for (PackageInfo packageInfo : installedPackages
                ) {
            Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
            //包名
            String packageName = packageInfo.applicationInfo.packageName;
            //获取到应用程序的名字
            String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            //获取到apk资源的路径,从而获得apk的大小
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();
            appinfo = new Appinfo(icon, apkName, packageName, apkSize);


            //获取到安装应用程序的标记
            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //表示系统app
                appinfo.setIsUserApp(false);
            } else {
                appinfo.setIsUserApp(true);
            }

            if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
                //表示在SD卡上
                appinfo.setAkpRom(false);
            }else{
                appinfo.setAkpRom(true);
            }
            appinfos.add(appinfo);
        }
        return appinfos;
    }
}
