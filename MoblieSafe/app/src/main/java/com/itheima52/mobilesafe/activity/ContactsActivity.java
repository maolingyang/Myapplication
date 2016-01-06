package com.itheima52.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.itheima52.mobilesafe.R;

public class ContactsActivity extends Activity {
	private ListView mListView;
	private List<Map<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		mListView = (ListView) findViewById(R.id.lv_listcontacts);
		 data = getContacts();
		mListView.setAdapter(new SimpleAdapter(this, data,
				R.layout.contacts_list_items, new String[]{"name","phone"},new int[]{R.id.tv_name,R.id.tv_phone}));
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = data.get(position);
				System.out.println("setOnItemClickListener"+map);
				Intent intent = new Intent();
				intent.putExtra("phone", map.get("phone").replaceAll("-", "").replaceAll(" ", ""));
				setResult(Activity.RESULT_OK, intent);///将数据返回给上一个页面
				finish();
			}
		});
	}
	
	//获取到联系人信息
	private List<Map<String, String>> getContacts(){
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Uri rowContacstid = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor cursor = getContentResolver().query(rowContacstid, new String[]{"contact_id"}, null, null, null);
		while(cursor.moveToNext()){
			String contacts_id = cursor.getString(0);
			Uri contactsuri = Uri.parse("content://com.android.contacts/data");
			Cursor cursor2 = getContentResolver().query(contactsuri, new String[]{"data1","mimetype"}, "contact_id=?", new String[]{contacts_id}, null);
			Map<String, String> map = new HashMap<String, String>();
			while(cursor2.moveToNext()){
				String data1 = cursor2.getString(0);
				String mimetype = cursor2.getString(1);
				if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					//电话号码
					map.put("phone", data1);
				}else if("vnd.android.cursor.item/name".equals(mimetype)){
					//联系人名称
					map.put("name", data1);
				}
			}
			//不要忘记了关闭
			cursor2.close();
			data.add(map);
		}
		cursor.close();
		return data;
	}
	
}
