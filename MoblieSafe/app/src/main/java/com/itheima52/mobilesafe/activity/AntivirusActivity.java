package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AntivirusDao;
import com.itheima52.mobilesafe.utils.MD5Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class AntivirusActivity extends Activity {

    private static final int BEGIN = 0;
    private static final int SCANING = 1;
    private static final int END = 3;
    @ViewInject(R.id.iv_animimage)
    private ImageView iv_animimage;
    @ViewInject(R.id.tv_text)
    private TextView tv_text;
    @ViewInject(R.id.pb_proce)
    private ProgressBar pb_proce;
    @ViewInject(R.id.ll_listitems)
    private LinearLayout ll_listitems;
    @ViewInject(R.id.sc_scorll)
    private ScrollView sc_scorll;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        ViewUtils.inject(this);
        initUI();
        initData();
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        //旋转动画
        rotateAnimation = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(5000);
        //循环播放动画
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        iv_animimage.startAnimation(rotateAnimation);


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BEGIN:
                    tv_text.setText("正在初始化杀毒引擎");
                    break;
                case SCANING:
                    tv_text.setText("正在使用本地病毒库杀毒");
                    AntivirusInfo info = (AntivirusInfo) msg.obj;
                    TextView textView = new TextView(AntivirusActivity.this);
                    if(info.isAntivirus){
                        textView.setText(info.appName+" 有病毒");
                    }else{
                        textView.setText(info.appName+" 扫描安全");
                    }
                    ll_listitems.addView(textView);

                    //设置一直滚动
                    sc_scorll.post(new Runnable() {
                        @Override
                        public void run() {
                            //设置为向下滚动
                            sc_scorll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                    break;
                case END:
                    tv_text.setText("查杀完成");
                    //停止动画
                    iv_animimage.clearAnimation();
                    break;
            }

        }
    };
    private void initData(){
        new Thread(){
            @Override
            public void run() {
                PackageManager packageManager = getPackageManager();
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                AntivirusDao dao = new AntivirusDao();
                Message message = Message.obtain();
                message.what=BEGIN;
                handler.sendMessage(message);
                pb_proce.setMax(installedPackages.size());
                int progress=0;
                for (PackageInfo packageInfo: installedPackages
                        ) {
                    String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    String dataDir = packageInfo.applicationInfo.dataDir;
                    String md5 = MD5Utils.getFileMd5(dataDir);
                    String desc = dao.getAntivirus(md5);
                    message = Message.obtain();
                    AntivirusInfo info = new AntivirusInfo();
                    info.appName=appName;
                    info.desc = desc;
                    message.obj = info;
                    message.what = SCANING;
                    if(desc!=null){
                        //是病毒应用
                        info.isAntivirus = true;
                    }else{
                        info.isAntivirus = false;
                    }
                    handler.sendMessage(message);
                    progress++;
                    pb_proce.setProgress(progress);
                    SystemClock.sleep(200);
                }
                message = Message.obtain();
                message.what=END;
                handler.sendMessage(message);
            }
        }.start();
    }
    static class AntivirusInfo{
        String desc;
        String appName;
        boolean isAntivirus;
    }
}
