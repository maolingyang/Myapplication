package com.itheima52.mobilesafe;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.itheima52.mobilesafe.bean.BlackNumberInfo;
import com.itheima52.mobilesafe.db.dao.BlackNumberDao;
import com.itheima52.mobilesafe.engin.AppInfoEngin;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/1/1.
 */
public class ApplicationTest extends AndroidTestCase {

    public void testcase() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            Long number = 13300000000l + i;
            dao.add(new BlackNumberInfo(number + "", String.valueOf(random.nextInt(3) + 1)));
        }

    }


    public void findall() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        List<BlackNumberInfo> infos = dao.findAll();
        System.out.println(infos.size());
        for (BlackNumberInfo blackNumberInfo : infos) {
            Log.i("test", blackNumberInfo.getMode() + "" + blackNumberInfo.getNumber());
            System.out.println(blackNumberInfo.getMode() + "" + blackNumberInfo.getNumber());
        }

    }

    public void findlimit() {
        BlackNumberDao dao = new BlackNumberDao(getContext());
        List<BlackNumberInfo> infos = dao.findPar(19, 20);
        System.out.println(infos);
    }

    public void testdeleteall(){
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.testdeleteall();
    }

    public void testappinfo(){
        AppInfoEngin engin =  new AppInfoEngin();
        engin.getAppinfo(getContext());
    }
}
