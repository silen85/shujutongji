
package com.iceman.recorddemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.iceman.recorddemo.RecordItem.ShowForm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {
    private RecordItem item;

    private LinearLayout mLinearLayout;

    private Bitmap mBitmap;

    private ImageView mImageView;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);
        if (Globe.density == 0f) {
            Globe.density = getResources().getDisplayMetrics().density;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Globe.fullScreenHeight = dm.heightPixels;
        Globe.fullScreenWidth = dm.widthPixels;
        System.out.println(Globe.density + ";" + Globe.fullScreenHeight);
        mLinearLayout = (LinearLayout) findViewById(R.id.scrollview_linearlayout);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    mImageView.setImageBitmap(mBitmap);
                }
            }
        };

        item = JsonUtil.getRecordItem(JsonUtil.getJsonFromAssets(this, "recorddemo.json"));
        for (final ShowForm f : item.getForms()) {
            TextView textview = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.setMargins(10, 20, 10, 20);
            RecordView recordView;
            switch (f.type) {
                case RecordView.RECORD_TEXT:
                    textview.setText(f.title);
                    textview.append("\n" + f.content);
                    textview.setTextColor(Color.BLACK);
                    mLinearLayout.addView(textview, lp);
                    break;
                case RecordView.RECORD_BIAOGE:
                    recordView = new RecordView(this);
                    recordView.setType(RecordView.RECORD_BIAOGE);
                    recordView.setmItem(item);
                    mLinearLayout.addView(recordView, lp);

                    if (!f.content.equals("")) {
                        textview = new TextView(this);
                        textview.setText(f.content);
                        textview.setTextColor(Color.BLACK);
                        mLinearLayout.addView(textview, lp);
                    }
                    break;
                case RecordView.RECORD_TUPIAN:
                    mImageView = new ImageView(MainActivity.this);
                    new Thread() {
                        @Override
                        public void run() {
                            getImageView(f.content);
                        }

                    }.start();
                    mLinearLayout.addView(mImageView, lp);

                    break;
                case RecordView.RECORD_XIANXING:
                    recordView = new RecordView(this);
                    recordView.setType(RecordView.RECORD_XIANXING);
                    recordView.setmItem(item);
                    mLinearLayout.addView(recordView, lp);

                    if (!f.content.equals("")) {
                        textview = new TextView(this);
                        textview.setText(f.content);
                        textview.setTextColor(Color.BLACK);
                        mLinearLayout.addView(textview, lp);
                    }

                    break;
                case RecordView.RECORD_ZHUXING:
                    recordView = new RecordView(this);
                    recordView.setType(RecordView.RECORD_ZHUXING);
                    recordView.setmItem(item);
                    mLinearLayout.addView(recordView, lp);

                    if (!f.content.equals("")) {
                        textview = new TextView(this);
                        textview.setText(f.content);
                        textview.setTextColor(Color.BLACK);
                        mLinearLayout.addView(textview, lp);
                    }

                    break;
                case RecordView.RECORD_SHUANGZHOU:
                    recordView = new RecordView(this);
                    recordView.setType(RecordView.RECORD_SHUANGZHOU);
                    recordView.setmItem(item);
                    mLinearLayout.addView(recordView, lp);

                    if (!f.content.equals("")) {
                        textview = new TextView(this);
                        textview.setText(f.content);
                        textview.setTextColor(Color.BLACK);
                        mLinearLayout.addView(textview, lp);
                    }

                    break;

                default:
                    recordView = new RecordView(this);
                    recordView.setType(RecordView.RECORD_BIAOGE);
                    recordView.setmItem(item);
                    mLinearLayout.addView(recordView, lp);

                    if (!f.content.equals("")) {
                        textview = new TextView(this);
                        textview.setText(f.content);
                        textview.setTextColor(Color.BLACK);
                        mLinearLayout.addView(textview, lp);
                    }
                    break;
            }
        }

    }

    private void getImageView(String content) {

        URL myFileUrl = null;
        try {
            myFileUrl = new URL(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(0);
    }

}
