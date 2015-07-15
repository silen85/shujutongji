package com.lesso.data.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by meisl on 2015/7/14.
 */
public class CommonDBAdapter {

    private String TAG = "com.lesso.data.common.CommonDBAdapter";

    protected String TABLE_NAME;
    protected SQLiteDatabase db;
    protected LESSODBHelper myDataHepler;
    protected Context context;

    public CommonDBAdapter(Context context) {
        myDataHepler = new LESSODBHelper(context);
        this.context = context;
        this.open();
    }

    private void open() throws SQLiteException {
        try {
            db = myDataHepler.getWritableDatabase();
        } catch (SQLiteException ex) {
        }
    }

    public void closeDB() {
        if ((this.db != null) && (this.db.isOpen()))
            this.db.close();
    }

}
