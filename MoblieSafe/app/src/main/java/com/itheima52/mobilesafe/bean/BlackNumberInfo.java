package com.itheima52.mobilesafe.bean;

/**
 * Created by Administrator on 2016/1/1.
 */
public class BlackNumberInfo {

    private String number;
    private String mode;

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public BlackNumberInfo() {

    }

    public BlackNumberInfo(String number, String mode) {

        this.number = number;
        this.mode = mode;
    }
}
