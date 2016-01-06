package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.AppProcessInfo;
import com.itheima52.mobilesafe.engin.AppProcessInfoEngin;
import com.itheima52.mobilesafe.utils.SystemInfoUtils;
import com.itheima52.mobilesafe.utils.ToashUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class PrcocessManagerActivity extends Activity {


    @ViewInject(R.id.tv_procCount)
    private TextView tv_procCount;
    @ViewInject(R.id.tv_menmeryinfo)
    private TextView tv_menmeryinfo;
    @ViewInject(R.id.lv_process)
    private ListView lv_process;
    private List<AppProcessInfo> appProcessInfo;
    private List<AppProcessInfo> appProcessUserInfo;
    private List<AppProcessInfo> appProcessSysInfo;
    private MyProcessAdapter adapter;
    private int processcount;
    private Long availMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prcocess_manager);
        ViewUtils.inject(this);
        initUI();
        initData();

    }

    /**
     * 加载数据
     */
    private void initData() {
        appProcessUserInfo = new ArrayList<>();
        appProcessSysInfo = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                AppProcessInfoEngin infoEngin = new AppProcessInfoEngin();
                appProcessInfo = infoEngin.getAppProcessInfo(PrcocessManagerActivity.this);
                for (AppProcessInfo info : appProcessInfo
                        ) {
                    if (info.isuser()) {
                        appProcessUserInfo.add(info);
                    } else {
                        appProcessSysInfo.add(info);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MyProcessAdapter();
                        lv_process.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        processcount = SystemInfoUtils.getProcessCount(this);
        tv_procCount.setText("进程:" + processcount);
        availMem = SystemInfoUtils.availMem(this);
        tv_menmeryinfo.setText("剩余/总内存:" + Formatter.formatFileSize(this, availMem)
                + "/" + Formatter.formatFileSize(this,SystemInfoUtils.totalMem()));


        lv_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                Object obj = adapterView.getItemAtPosition(postion);
                if(obj !=null&&obj instanceof AppProcessInfo){
                    AppProcessInfo processInfo = (AppProcessInfo) obj;
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    //如果是本应用 则点击事件直接进行返回
                    if(processInfo.getPackageName().equals(getPackageName())){
                        return ;
                    }

                    if(processInfo.isCheck()){
                        viewHolder.cb_status.setChecked(false);
                        processInfo.setCheck(false);
                    }else{
                        viewHolder.cb_status.setChecked(true);
                        processInfo.setCheck(true);
                    }

                }

            }
        });
    }

    private class MyProcessAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return appProcessUserInfo.size()+appProcessSysInfo.size()+2;
        }

        @Override
        public Object getItem(int position) {

            if (position == 0) {
                return null;
            }
            if (position == appProcessUserInfo.size() + 1) {
                return null;
            }
            //用户应用
            if (position < appProcessUserInfo.size() + 1) {
                return appProcessUserInfo.get(position-1);
            } else {
                //系统应用
                return appProcessSysInfo.get(position-appProcessUserInfo.size()-2);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (position == 0) {

                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户：" + appProcessUserInfo.size() + "个");
                return tv;
            }
            if (position == appProcessUserInfo.size() + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统：" + appProcessSysInfo.size() + "个");
                return tv;
            }


            ViewHolder viewHolder ;

            if (view != null && view instanceof LinearLayout) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                viewHolder = new ViewHolder();
                view = View.inflate(PrcocessManagerActivity.this, R.layout.item_processinfor_list, null);
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_apkName = (TextView) view.findViewById(R.id.tv_apkName);
                viewHolder.tv_memory = (TextView) view.findViewById(R.id.tv_memory);
                viewHolder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
                view.setTag(viewHolder);
            }

            //用户应用
            if (position < appProcessUserInfo.size() + 1) {
                AppProcessInfo processInfo = appProcessUserInfo.get(position - 1);
                viewHolder.iv_icon.setImageDrawable(processInfo.getIcon());
                viewHolder.tv_apkName.setText(processInfo.getAppName());
                viewHolder.tv_memory.setText(Formatter.formatFileSize(PrcocessManagerActivity.this, processInfo.getMemorySize()));
                viewHolder.cb_status.setChecked(processInfo.isCheck());
                //如果是本身引用 则不显示checkbox
                if(processInfo.getPackageName().equals(getPackageName())){
                    viewHolder.cb_status.setVisibility(View.INVISIBLE);
                    viewHolder.cb_status.setChecked(false);
                }

            } else {
                //系统应用
                AppProcessInfo processInfo = appProcessSysInfo.get(position-appProcessUserInfo.size()-2);
                viewHolder.iv_icon.setImageDrawable(processInfo.getIcon());
                viewHolder.tv_apkName.setText(processInfo.getAppName());
                viewHolder.tv_memory.setText(Formatter.formatFileSize(PrcocessManagerActivity.this, processInfo.getMemorySize()));
                viewHolder.cb_status.setChecked(processInfo.isCheck());
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_apkName;
        TextView tv_memory;
        CheckBox cb_status;
    }


    /**
     * 全选按钮
     * @param view
     */
    public void selectAll(View view){
        for (AppProcessInfo info: appProcessUserInfo
             ) {
            if(info.getPackageName().equals(getPackageName())){
                info.setCheck(false);
                continue;
            }
            info.setCheck(true);
        }
        for (AppProcessInfo info: appProcessSysInfo
             ) {
            info.setCheck(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选按钮
     * @param view
     */
    public void selectOppsite(View view){
        for (AppProcessInfo info: appProcessUserInfo
                ) {
            if(info.getPackageName().equals(getPackageName())){
                info.setCheck(false);
                continue;
            }
            info.setCheck(!info.isCheck());
        }
        for (AppProcessInfo info: appProcessSysInfo
                ) {
            info.setCheck(!info.isCheck());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理按钮
     * @param view
     */
    public void killProcess(View view){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<AppProcessInfo> killinfo = new ArrayList<>();
        int count = 0;
        long killMem =0;
        //遍历出需要清楚的AppProcessInfo
        for (AppProcessInfo info: appProcessUserInfo
                ) {
            if(info.isCheck()){

                killinfo.add(info);
                //杀死进程
                activityManager.killBackgroundProcesses(info.getPackageName());

                killMem+=info.getMemorySize();
                count++;
            }
        }
        for (AppProcessInfo info: appProcessSysInfo
                ) {
            if(info.isCheck()){
                killinfo.add(info);
                //杀死进程
                activityManager.killBackgroundProcesses(info.getPackageName());
                killMem+=info.getMemorySize();
                count++;
            }
        }

        //
        for (AppProcessInfo info: killinfo
                ) {
            if(info.isuser()){
                appProcessUserInfo.remove(info);
            }else{
                appProcessSysInfo.remove(info);
            }
        }
        processcount-=count;
        tv_procCount.setText("进程:" +processcount);
        tv_menmeryinfo.setText("剩余/总内存:" + Formatter.formatFileSize(this,availMem+killMem) + "/" + Formatter.formatFileSize(this,SystemInfoUtils.totalMem()));
        adapter.notifyDataSetChanged();
        ToashUtils.showShortToash(this,"杀死"+count+"个进程,释放内存:"+Formatter.formatFileSize(this,killMem));
    }

    /**
     * 打开设置页面
     */
    public void openSetting(){

    }
}
