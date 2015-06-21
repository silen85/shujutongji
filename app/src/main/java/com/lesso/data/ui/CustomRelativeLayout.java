package com.lesso.data.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by meisl on 2015/6/18.
 */
public class CustomRelativeLayout extends RelativeLayout{

    private OnSoftKeyboardListener onSoftKeyboardListener;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (onSoftKeyboardListener != null) {
            final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
            final int oldSpec = getMeasuredHeight();
            // If layout became smaller, that means something forced it to resize. Probably soft keyboard :)
            if (oldSpec > newSpec){
                onSoftKeyboardListener.onShown();
            } else {
                onSoftKeyboardListener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public final void setOnSoftKeyboardListener(OnSoftKeyboardListener onSoftKeyboardListener) {
        this.onSoftKeyboardListener = onSoftKeyboardListener;
    }


    public interface OnSoftKeyboardListener {
        public void onShown();
        public void onHidden();
    }

}
