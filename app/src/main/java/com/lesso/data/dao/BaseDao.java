package com.lesso.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by meisl on 2015/7/14.
 */
public class BaseDao {

    private String TAG = "com.lesso.data.dao.BaseDao";

    protected String TABLE_NAME;
    protected Context context;

    private SQLiteDatabase db;

    public BaseDao(Context context) {
        this.context = context;

        db = LESSODBHelper.getInstance(context).getWritableDatabase();

    }

    public SQLiteDatabase getConnection() {
        return db;
    }

    public boolean isOpen() {
        return db.isOpen();
    }

    public void closeDB() {

        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            db = null;
        }
    }

}
