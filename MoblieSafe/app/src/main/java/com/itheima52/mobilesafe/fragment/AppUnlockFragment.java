package com.itheima52.mobilesafe.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.itheima52.mobilesafe.R;

public class AppUnlockFragment extends Fragment {

    private ListView listview;

    public AppUnlockFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_unlock, null);
        listview = (ListView) view.findViewById(R.id.listview);
        return view;
    }

    /**
     * 加载数据
     */
    @Override
    public void onStart() {
        super.onStart();
    }
}
