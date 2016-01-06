package com.itheima52.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by maoly on 2016/1/5.
 */
public class AppProcessInfo {

    private Drawable icon;
    private String appName;
    private String packageName;
    private long memorySize;
    private boolean isuser;
    /**
     * 判断当前的item的条目是否被勾选上
     */
    private boolean check;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isuser() {
        return isuser;
    }

    public void setIsuser(boolean isuser) {
        this.isuser = isuser;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
