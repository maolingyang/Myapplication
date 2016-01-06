package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.service.ProcessService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ProcessSettingActivity extends Activity {

    @ViewInject(R.id.cb_screen)
    private CheckBox cb_screen;

    @ViewInject(R.id.cb_2hour)
    private CheckBox cb_2hour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);
        ViewUtils.inject(this);

        final  Intent intent = new Intent(this, ProcessService.class);
        cb_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_screen.isChecked()){
                    startService(intent);
                }else{
                    stopService(intent);
                }

            }
        });
    }



}
