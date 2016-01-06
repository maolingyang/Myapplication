package com.itheima52.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.receiver.MyAppWidget;
import com.itheima52.mobilesafe.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

public class KillProcesWidgetService extends Service {
    public KillProcesWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AppWidgetManager widgetManager = AppWidgetManager.getInstance(KillProcesWidgetService.this);
                //这个是把当前的布局文件添加进行
                /**
                 * 初始化一个远程的view
                 * Remote 远程
                 */
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                views.setTextViewText(R.id.process_count, "正在运行的软件:" + SystemInfoUtils.getProcessCount(KillProcesWidgetService.this));
                views.setTextViewText(R.id.process_memory, "可用内存:" + Formatter.formatFileSize(KillProcesWidgetService.this, SystemInfoUtils.totalMem()));

                Intent intent = new Intent();
                //发送一个隐式意图，处理点击事件
                intent.setAction("com.itheima52.mobilesafe");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(KillProcesWidgetService.this, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

                //第一个参数表示上下文
                //第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
                ComponentName componentName = new ComponentName(getApplicationContext(), MyAppWidget.class);
                widgetManager.updateAppWidget(componentName, views);
            }
        }, 0, 5000);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
