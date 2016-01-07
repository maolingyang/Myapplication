package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.SmsBackup;
import com.itheima52.mobilesafe.utils.UIUtils;

public class AToolsActivity extends Activity {

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void numberAddressQuery(View view) {
        startActivity(new Intent(this, AddressActivivty.class));
    }

    public void blackupsms(View view) {
        pd = new ProgressDialog(AToolsActivity.this);
        pd.setTitle("备份短信");
        pd.setMessage("正在备份短信 请稍等");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.show();

        new Thread() {
            @Override
            public void run() {
                SmsBackup smsBackup = new SmsBackup();
                boolean result = smsBackup.backupsms(AToolsActivity.this, new SmsBackup.Smsprocess() {
                    @Override
                    public void befor(int count) {
                        pd.setMax(count);
                    }

                    @Override
                    public void process(int process) {
                        pd.setProgress(process);
                    }
                });
                if (result) {
                    //安全弹吐司的方法
                    UIUtils.showToast(AToolsActivity.this, "备份成功");
                } else {
                    UIUtils.showToast(AToolsActivity.this, "备份失败");
                }
                pd.dismiss();
            }
        }.start();
    }

    public void applock(View view){
        Intent intent = new Intent(this,ApplockActivity.class);
        startActivity(intent);
    }
}
