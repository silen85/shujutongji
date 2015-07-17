package com.lesso.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/7/14.
 */
public class LESSODBHelper extends SQLiteOpenHelper {

    private String TAG = "com.lesso.data.dao.LESSODBHelper";

    private static final String DATABASE_NAME = "lessobi.db";
    private static final int DB_VERSION = 1;

    private Context context;
    private static LESSODBHelper instance;

    private LESSODBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static LESSODBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new LESSODBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] sql_arrays = context.getResources().getStringArray(
                R.array.create_table_sql_array);
        try {
            for (String sql : sql_arrays) {
                db.execSQL(sql);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}