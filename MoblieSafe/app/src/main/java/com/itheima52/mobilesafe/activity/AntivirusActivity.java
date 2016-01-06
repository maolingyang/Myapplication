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

    // 扫描开始
    protected static final int BEGING = 1;
    // 扫描中
    protected static final int SCANING = 2;
    // 扫描结束
    protected static final int FINISH = 3;
    private Message message;
    @ViewInject(R.id.iv_animimage)
    private ImageView iv_animimage;
    @ViewInject(R.id.tv_text)
    private TextView tv_text;
    @ViewInject(R.id.pb_proce)
    private ProgressBar pb;
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
                case BEGING:
                    tv_text.setText("正在初始化杀毒引擎");
                    break;
                case SCANING:
                    tv_text.setText("正在使用本地病毒库杀毒");
                    ScanInfo info = (ScanInfo) msg.obj;
                    TextView textView = new TextView(AntivirusActivity.this);
                    if(info.desc){
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
                case FINISH:
                    tv_text.setText("查杀完成");
                    //停止动画
                    iv_animimage.clearAnimation();
                    break;
            }

        }
    };
    private void initData() {

        new Thread() {

            public void run() {

                message = Message.obtain();

                message.what = BEGING;

                PackageManager packageManager = getPackageManager();
                // 获取到所有安装的应用程序
                List<PackageInfo> installedPackages = packageManager
                        .getInstalledPackages(0);
                // 返回手机上面安装了多少个应用程序
                int size = installedPackages.size();
                // 设置进度条的最大值
                pb.setMax(size);

                int progress = 0;

                for (PackageInfo packageInfo : installedPackages) {

                    ScanInfo scanInfo = new ScanInfo();

                    // 获取到当前手机上面的app的名字
                    String appName = packageInfo.applicationInfo.loadLabel(
                            packageManager).toString();

                    scanInfo.appName = appName;

                    String packageName = packageInfo.applicationInfo.packageName;

                    scanInfo.packageName = packageName;

                    // 首先需要获取到每个应用程序的目录

                    String sourceDir = packageInfo.applicationInfo.sourceDir;
                    // 获取到文件的md5
                    String md5 = MD5Utils.getFileMd5(sourceDir);
                    // 判断当前的文件是否是病毒数据库里面
                    String desc = AntivirusDao.getAntivirus(md5);

                    System.out.println("-------------------------");

                    System.out.println(appName);

                    System.out.println(md5);

                    // 如果当前的描述信息等于null说明没有病毒
                    if (desc == null) {
                        scanInfo.desc = false;
                    } else {
                        scanInfo.desc = true;
                    }
                    progress++;

                    SystemClock.sleep(100);

                    pb.setProgress(progress);

                    message = Message.obtain();

                    message.what = SCANING;

                    message.obj = scanInfo;

                    handler.sendMessage(message);

                }

                message = Message.obtain();

                message.what = FINISH;

                handler.sendMessage(message);
            };
        }.start();

    }

    static class ScanInfo {
        boolean desc;
        String appName;
        String packageName;
    }
}
