package com.lesso.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by meisl on 2015/7/14.
 */
public class DataCacheDao extends BaseDao {

    protected String TABLE_NAME = "datacache";

    public static final String COLUMN_BEGINDATE = "sbegindate";
    public static final String COLUMN_ENDDATE = "senddate";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATA = "data";

    public DataCacheDao(Context context) {
        super(context);
    }

    public String getCacheData(String sBegindate, String sEnddate, int type) {

        String data = "";
        if (isOpen()) {

            String sql = "select " + COLUMN_DATA + " from " + TABLE_NAME + " where " + COLUMN_BEGINDATE +
                    "=? and " + COLUMN_ENDDATE + "=? and " + COLUMN_TYPE + "=? ";

            String[] args = new String[]{sBegindate, sEnddate, type + ""};

            Cursor cursor = getConnection().rawQuery(sql, args);

            if (cursor.moveToFirst()) {
                data = cursor.getString(0);
            }

            cursor.close();

        }

        return data;
    }

    public void putCacheData(String sBegindate, String sEnddate, int type, String data) {

        if (isOpen()) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_BEGINDATE, sBegindate);
            values.put(COLUMN_ENDDATE, sEnddate);
            values.put(COLUMN_TYPE, type);
            values.put(COLUMN_DATA, data);

            getConnection().replace(TABLE_NAME, null, values);

        }

    }

    public void clearCacheData(String sBegindate, String sEnddate) {

        if (isOpen()) {

            getConnection().delete(TABLE_NAME, COLUMN_BEGINDATE + "<>? and " + COLUMN_ENDDATE + "<>? ", new String[]{sBegindate, sEnddate});

        }

    }

}
