
package com.lesso.data.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class RecordScrollView extends ScrollView {
    public RecordScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RecordScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public RecordScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return false;

    }


}
