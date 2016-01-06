package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.itheima52.mobilesafe.bean.BlackNumberInfo;
import com.itheima52.mobilesafe.db.dao.BlackNumberDao;

import java.util.Objects;

public class CallSafeService extends Service {

    private CallSafeSmsReceiver smsReceiver;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentfilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentfilter.setPriority(Integer.MAX_VALUE);
        smsReceiver = new CallSafeSmsReceiver();
        registerReceiver(smsReceiver,intentfilter);
    }


    /**
     * 短信的广播接收者
     */
    protected class CallSafeSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String address = smsMessage.getOriginatingAddress();
                BlackNumberDao dao = new BlackNumberDao(CallSafeService.this);
                BlackNumberInfo numberInfo = dao.findByNumber(address);
                if(numberInfo!=null){
                    String mode = numberInfo.getMode();
                    if (mode.equals("1")) {
                        abortBroadcast();
                    } else if (mode.equals("3")) {
                        abortBroadcast();
                    }
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
