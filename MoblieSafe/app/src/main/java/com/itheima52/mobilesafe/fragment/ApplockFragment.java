package com.itheima52.mobilesafe.fragment;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.Appinfo;
import com.itheima52.mobilesafe.db.dao.ApplockDao;
import com.itheima52.mobilesafe.engin.AppInfoEngin;

import java.util.ArrayList;
import java.util.List;

public class ApplockFragment extends Fragment {

    private ListView listview;
    private TextView tv_count;
    private ApplockDao dao;
    private List<Appinfo> locks;
    private LockAdatper adatper;

    public ApplockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applock, null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dao = new ApplockDao(getActivity());
        AppInfoEngin appInfoEngin = new AppInfoEngin();
        List<Appinfo> appinfos = appInfoEngin.getAppinfo(getActivity());
        locks = new ArrayList<>();
        for (Appinfo info : appinfos
                ) {
            String akpPackage = info.getAkpPackage();
            if (dao.isLock(akpPackage)) {
                //程序已经锁上
                locks.add(info);
            } else {
            }
        }
        adatper = new LockAdatper();
        listview.setAdapter(adatper);
    }

    public class LockAdatper extends BaseAdapter {

        @Override
        public int getCount() {
            tv_count.setText("已上锁(" + locks.size() + ")个");
            return locks.size();
        }

        @Override
        public Object getItem(int position) {
            return locks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final View view;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_applock_list, null);
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_appName = (TextView) view.findViewById(R.id.tv_appName);
                viewHolder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
                view.setTag(viewHolder);
            } else {
                view =convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Appinfo appinfo = locks.get(position);
            viewHolder.iv_icon.setImageDrawable(appinfo.getIcon());
            viewHolder.tv_appName.setText(appinfo.getApkName());
            viewHolder.iv_lock.setBackgroundResource(R.drawable.list_button_unlock_default);

            viewHolder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TranslateAnimation anim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                            TranslateAnimation.RELATIVE_TO_SELF, -1.0f,
                            TranslateAnimation.RELATIVE_TO_SELF, 0,
                            TranslateAnimation.RELATIVE_TO_SELF, 0);
                    anim.setDuration(3000);
                    view.startAnimation(anim);
                    new Thread() {
                        @Override
                        public void run() {
                            SystemClock.sleep(3000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dao.delete(locks.get(position).getAkpPackage());
                                    locks.remove(position);
                                    adatper.notifyDataSetChanged();
                                }
                            });

                        }
                    }.start();
                }
            });
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_appName;
        ImageView iv_lock;
    }
}
