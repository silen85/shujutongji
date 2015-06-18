package com.lesso.data.cusinterface;

import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by meisl on 2015/6/18.
 */
public interface FragmentListener {

    public boolean onTouchEvent(MotionEvent event);

    public boolean dispatchKeyEvent(KeyEvent event);

}
