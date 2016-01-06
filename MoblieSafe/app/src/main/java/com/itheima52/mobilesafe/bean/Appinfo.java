package com.itheima52.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/1/2.
 */
public class Appinfo {

    /**
     * 应用的图标
     */
    private Drawable icon;

    private String apkName;
    //是否为第三方应用
    private boolean isUserApp;
    private String akpPackage;
    //应用的安装位置
    private boolean akpRom;

    //程序的大小
    private long apkSize;

    public Appinfo() {
    }

    public Appinfo(Drawable icon, String apkName, String akpPackage, long apkSize) {
        this.icon = icon;
        this.apkName = apkName;
        this.akpPackage = akpPackage;
        this.apkSize = apkSize;
    }

    @Override
    public String toString() {
        return "Appinfo{" +
                "icon=" + icon +
                ", apkName='" + apkName + '\'' +
                ", isUserApp=" + isUserApp +
                ", akpPackage='" + akpPackage + '\'' +
                ", akpRom=" + akpRom +
                ", apkSize=" + apkSize +
                '}';
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }


    public String getAkpPackage() {
        return akpPackage;
    }

    public void setAkpPackage(String akpPackage) {
        this.akpPackage = akpPackage;
    }

    public boolean isAkpRom() {
        return akpRom;
    }

    public void setAkpRom(boolean akpRom) {
        this.akpRom = akpRom;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }
}
