package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.bean.BlackNumberInfo;
import com.itheima52.mobilesafe.db.dao.BlackNumberDao;
import com.itheima52.mobilesafe.utils.ToashUtils;

import java.util.ArrayList;
import java.util.List;


public class CallsafeActivity extends Activity {

    private ListView lvBn;
    private int startindex = 0;
    private int maxindex = 20;
    private List<BlackNumberInfo> list;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llpb.setVisibility(View.INVISIBLE);
            if (adapter == null) {
                adapter = new Myadapter(list, CallsafeActivity.this);
                lvBn.setAdapter(adapter);
            } else {
                //重新加载列表数据
                adapter.notifyDataSetChanged();
            }
        }
    };
    protected BlackNumberDao dao;
    protected int totalNumber;
    protected Myadapter adapter;
    protected LinearLayout llpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        initData();
        initUI();

    }

    private void initUI() {

        llpb = (LinearLayout) findViewById(R.id.ll_pb);
        //展示加载的圆圈
        llpb.setVisibility(View.VISIBLE);

        lvBn = (ListView) findViewById(R.id.lv_blackNumber);
        list = new ArrayList<>();
        lvBn.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动的空闲状态
                        //获取到最后一条显示的数据
                        int lastVisiblePosition = lvBn.getLastVisiblePosition();
                        if (lastVisiblePosition == list.size() - 1) {
                            startindex += maxindex;
                            //如果滑动的条目数大于数据库总数，则直接返回
                            if (startindex >= totalNumber) {
                                ToashUtils.showShortToash(CallsafeActivity.this, "已经没有数据了");
                                return;
                            }
                            initData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public class Myadapter extends MyBaseAdapter<BlackNumberInfo> {

        public Myadapter(List<BlackNumberInfo> list, Context context) {
            super(list, context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler hodler;
            if (convertView == null) {
                convertView = View.inflate(CallsafeActivity.this, R.layout.item_call_safe, null);
                hodler = new ViewHodler();
                hodler.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                hodler.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
                hodler.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
            }
            final String number = list.get(position).getNumber();
            final int positionDelete = position;
            hodler.tvNumber.setText(number);
            String mode = list.get(position).getMode();
            if (mode.equals("1")) {
                //电话短信都拦截
                hodler.tvMode.setText("来电拦截+短信");
            } else if (mode.equals("2")) {
                //电话拦截
                hodler.tvMode.setText("电话拦截");

            } else if (mode.equals("3")) {
                //短信拦截
                hodler.tvMode.setText("短信拦截");
            }

            //删除按钮的点击事件
            hodler.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.size() == 10) {
                        startindex += list.size();
                        dao.delete(number);
                        initData();
                    } else {
                        dao.delete(number);
                        list.remove(positionDelete);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }
    }

    private void initData() {
        dao = new BlackNumberDao(this);
        totalNumber = dao.getTotalNumber();
        new Thread() {
            @Override
            public void run() {
                //如果是第一次加载，这直接查询
                if (list == null || list.size() == 0) {
                    list = dao.findPar(startindex, maxindex);
                } else {
                    //如果有数据以后则将之后查询的数据直接添加到list中去
                    list.addAll(dao.findPar(startindex, maxindex));
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View inflate = View.inflate(this, R.layout.dialog_addblacknumber, null);
        Button btOk = (Button) inflate.findViewById(R.id.btn_ok);
        Button btCancel = (Button) inflate.findViewById(R.id.btn_cancel);
        final EditText etNumber = (EditText) inflate.findViewById(R.id.et_blacknumber);
        final CheckBox cbPhone = (CheckBox) inflate.findViewById(R.id.cb_phone);
        final CheckBox cbsms = (CheckBox) inflate.findViewById(R.id.cb_sms);


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etNumber.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    ToashUtils.showShortToash(CallsafeActivity.this, "请输入黑名单号码");
                    return;
                }

                final String mode;
                if (cbPhone.isChecked() && cbsms.isChecked()) {
                    mode = "1";
                } else if (cbPhone.isChecked()) {
                    mode = "2";
                } else if (cbsms.isChecked()) {
                    mode = "3";
                } else {
                    ToashUtils.showShortToash(CallsafeActivity.this, "请勾选拦截模式");
                    return;
                }

                BlackNumberInfo numberInfo = new BlackNumberInfo(number, mode);
                dao.add(numberInfo);
                list.add(0, numberInfo);
                if (adapter == null) {
                    adapter = new Myadapter(list, CallsafeActivity.this);
                    lvBn.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(inflate);
        dialog.show();
    }

    static class ViewHodler {
        TextView tvNumber;
        TextView tvMode;
        ImageView ivDelete;
    }
}
