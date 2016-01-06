package com.itheima52.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/1/4.
 */
public class SmsBackup {

    public interface Smsprocess {
        public void befor(int count);

        public void process(int process);
    }

    //备份短信
    public boolean backupsms(Context context, Smsprocess smsprocess) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"address",
                "date", "type", "body"}, null, null, null);
        File file = new File(Environment.getExternalStorageDirectory(),
                "backup.xml");
        smsprocess.befor(cursor.getCount());
        int process = 0;
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            XmlSerializer xs = Xml.newSerializer();
            xs.setOutput(os, "utf-8");
            xs.startDocument("utf-8", true);
            xs.startTag(null, "smss");
            while (cursor.moveToNext()) {
                xs.startTag(null, "sms");
                xs.startTag(null, "address");
                xs.text(cursor.getString(0));
                xs.endTag(null, "address");

                xs.startTag(null, "date");
                xs.text(cursor.getString(1));
                xs.endTag(null, "date");

                xs.startTag(null, "type");
                xs.text(cursor.getString(2));
                xs.endTag(null, "type");

                xs.startTag(null, "body");
                xs.text(cursor.getString(3));
                xs.endTag(null, "body");
                process++;
                smsprocess.process(process);
                SystemClock.sleep(200);
                xs.endTag(null, "sms");
            }
            xs.endTag(null, "smss");
            xs.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
