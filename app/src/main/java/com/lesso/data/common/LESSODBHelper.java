package com.lesso.data.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/7/14.
 */
public class LESSODBHelper extends SQLiteOpenHelper {

    private String TAG = "com.lesso.data.common.LESSODBHelper";

    private Context context;
    private static final String DATABASE_NAME = "lessobi.db";
    private static final int DB_VERSION = 1;

    public LESSODBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
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
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
