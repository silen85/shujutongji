package com.lesso.data.common;

import android.content.Context;

/**
 * Created by meisl on 2015/7/14.
 */
public class DataCacheDBAdapter extends CommonDBAdapter{

    public DataCacheDBAdapter(Context context) {
        super(context);
        TABLE_NAME = "datacache";
    }



}
