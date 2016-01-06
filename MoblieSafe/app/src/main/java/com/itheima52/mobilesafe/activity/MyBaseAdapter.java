package com.itheima52.mobilesafe.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter{

    List<T> list ;
    public Context mContext;

    public MyBaseAdapter(List<T> list,Context context) {
        this.list = list;
        mContext = context;
    }
    protected MyBaseAdapter() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
