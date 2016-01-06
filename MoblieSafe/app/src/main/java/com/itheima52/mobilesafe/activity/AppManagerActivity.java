package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.Appinfo;
import com.itheima52.mobilesafe.engin.AppInfoEngin;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity {

    private ListView mLvApp;
    private List<Appinfo> appinfos;
    private List<Appinfo> userAppinfos;
    private List<Appinfo> sysAppinfos;
    @ViewInject(R.id.tv_rominfo)
    private TextView tv_rominfo;
    @ViewInject(R.id.tv_sdcardinfo)
    private TextView tv_sdcardinfo;
    @ViewInject(R.id.tv_isuser)
    private TextView tv_isuser;
    Appinfo checkedAppinfo;
    int height;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ViewUtils.inject(this);
        mLvApp = (ListView) findViewById(R.id.lv_app);
        initUI();
        initData();
    }

    //初始化页面
    private void initUI() {
        tv_rominfo.setText("内存可用:" + Formatter.formatFileSize(this, Environment.getDataDirectory().getFreeSpace()));
        tv_sdcardinfo.setText("sd卡可用:" + Formatter.formatFileSize(this, Environment.getExternalStorageDirectory().getFreeSpace()));

        //设置显示用户APP或系统APP的数量
        mLvApp.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (appinfos != null) {
                    if (firstVisibleItem < userAppinfos.size() + 1) {
                        tv_isuser.setText("用户程序(" + userAppinfos.size() + ")");
                    } else {
                        tv_isuser.setText("系统程序(" + userAppinfos.size() + ")");
                    }
                    popupWindowDismiss();
                }
            }
        });

        //应用的列表点击事件
        mLvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemobjec = mLvApp.getItemAtPosition(position);
                if (itemobjec != null && itemobjec instanceof Appinfo) {
                    popupWindowDismiss();
                    checkedAppinfo = (Appinfo) itemobjec;
                    View inflate = View.inflate(AppManagerActivity.this, R.layout.item_pupo, null);
                    popupWindow = new PopupWindow(inflate, LayoutParams.WRAP_CONTENT, height);
                    int[] location = new int[2];
                    //获取view展示到窗体上面的位置
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 70, location[1]);
                }
            }
        });
    }

    //点击事件处理
    public void itemclick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_uninstall:
                //卸载应用
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + checkedAppinfo.getAkpPackage()));
                startActivity(intent);
                popupWindowDismiss();
                break;
            case R.id.ll_detail:
                //查看详情
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + checkedAppinfo.getAkpPackage()));
                startActivity(intent);
                popupWindowDismiss();
                break;
            case R.id.ll_share:
                //分享
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Hi！推荐您使用软件：" + checkedAppinfo.getApkName() + "下载地址:"
                                + "https://play.google.com/store/apps/details?id=" + checkedAppinfo.getAkpPackage());
                this.startActivity(Intent.createChooser(intent, "分享"));
                popupWindowDismiss();
                break;
            case R.id.ll_start:
                //运行应用
                intent = getPackageManager().getLaunchIntentForPackage(checkedAppinfo.getAkpPackage());
                startActivity(intent);
                popupWindowDismiss();
                break;
        }

    }


    private void popupWindowDismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ApkManageAdapter adapter = new ApkManageAdapter();
            mLvApp.setAdapter(adapter);
            ListAdapter listAdapter = mLvApp.getAdapter();
            View view = listAdapter.getView(1, null, mLvApp);
            view.measure(0, 0);
            height = view.getMeasuredHeight();
        }
    };

    /**
     * 获取到app列表数据，并处理数据
     */
    protected void initData() {

        new Thread() {
            @Override
            public void run() {
                AppInfoEngin appInfoEngin = new AppInfoEngin();
                appinfos = appInfoEngin.getAppinfo(AppManagerActivity.this);
                userAppinfos = new ArrayList<Appinfo>();
                sysAppinfos = new ArrayList<Appinfo>();
                for (Appinfo info : appinfos
                        ) {
                    //判断是否为系统应用
                    if (info.isUserApp()) {
                        userAppinfos.add(info);
                    } else {
                        sysAppinfos.add(info);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * APK列表的adapter
     */
    private class ApkManageAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return appinfos.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            Appinfo appinfo;
            if (position == 0) {
                return null;
            } else if (position == userAppinfos.size() + 1) {
                return null;
            } else {

                if (position < userAppinfos.size() + 1) {
                    appinfo = userAppinfos.get(position - 1);
                } else {
                    //显示系统应用
                    int location = userAppinfos.size() + 2;
                    appinfo = sysAppinfos.get(position - location);
                }
                return appinfo;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            //第一行显示
            if (position == 0) {
                TextView oneLine = new TextView(AppManagerActivity.this);
                oneLine.setTextColor(Color.WHITE);
                oneLine.setBackgroundColor(Color.GRAY);
                oneLine.setText("用户程序(" + userAppinfos.size() + ")");

                return oneLine;
            } else if (position == userAppinfos.size() + 1) {
                //显示系统应用
                TextView sysline = new TextView(AppManagerActivity.this);
                sysline.setTextColor(Color.WHITE);
                sysline.setBackgroundColor(Color.GRAY);
                sysline.setText("系统程序(" + sysAppinfos.size() + ")");
                return sysline;
            }


            Appinfo appinfo;
            //显示第三方应用
            if (position < userAppinfos.size() + 1) {
                appinfo = userAppinfos.get(position - 1);
            } else {
                //显示系统应用
                int location = userAppinfos.size() + 2;
                appinfo = sysAppinfos.get(position - location);
            }

          /*  if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(AppManagerActivity.this, R.layout.item_appinfo_list, null);
                viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_apkName = (TextView) convertView.findViewById(R.id.tv_apkName);
                viewHolder.tv_apksize = (TextView) convertView.findViewById(R.id.tv_apksize);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }*/

            if (convertView != null && convertView instanceof LinearLayout) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                viewHolder = new ViewHolder();
                convertView = View.inflate(AppManagerActivity.this, R.layout.item_appinfo_list, null);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_apkName = (TextView) convertView.findViewById(R.id.tv_apkName);
                viewHolder.tv_apksize = (TextView) convertView.findViewById(R.id.tv_apksize);
                viewHolder.tv_rom = (TextView) convertView.findViewById(R.id.tv_rom);
                convertView.setTag(viewHolder);
            }

            viewHolder.iv_icon.setImageDrawable(appinfo.getIcon());
            viewHolder.tv_apkName.setText(appinfo.getApkName());
            viewHolder.tv_apksize.setText(Formatter.formatFileSize(AppManagerActivity.this, appinfo.getApkSize()));
            if (appinfo.isAkpRom()) {
                viewHolder.tv_rom.setText("手机内存");
            } else {
                viewHolder.tv_rom.setText("SD卡内存");
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_apkName;
        TextView tv_apksize;
        TextView tv_rom;
    }

    @Override
    protected void onDestroy() {
        popupWindowDismiss();
        super.onDestroy();
    }
}
