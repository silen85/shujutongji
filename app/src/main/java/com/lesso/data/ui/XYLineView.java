package com.lesso.data.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/6/29.
 */
public class XYLineView extends View {

    private String TAG = "com.lesso.data.ui.XYLineView";

    private final int CONSTANTS_COUNT_X = 8;
    private final int CONSTANTS_COUNT_Y = 5;

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private float[] data;

    private String[] field;

    private int screenWidth = 0;

    private int screenHeight = 0;

    private int mHeight, mWidth;

    private int countX, countY;

    private int mSingleWidth, mSingleHeight, mPaddingLeft, mPaddingRight;

    private int mCalculateWidth;

    private int mDrawOffset;

    private int mSaveOffset;

    private float mTextHeight;

    private float mHeightOffset;

    private int downX;

    private int moveX;

    private int distance;

    private float ymax, ymin;

    private long timeCache;

    public XYLineView(Context context) {
        super(context);
    }

    public XYLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XYLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (screenWidth <= 0 || screenHeight <= 0 || mSingleWidth <= 0 || mSingleHeight <= 0) {
            return;
        }

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextHeight / 2.5f);

        /**
         * 画坐标图
         */

        mPaint.setShader(null);
        mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C6));
        canvas.drawRect(new Rect(0, 0, mPaddingLeft + mWidth + mPaddingRight, mHeight + mSingleHeight), mPaint);


        mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C4));
        for (int i = 0; i < countY; i++) {
            canvas.drawLine(0, i * mSingleHeight,
                    mPaddingLeft + mWidth + mPaddingRight,
                    i * mSingleHeight, mPaint);
        }

        //int cntOffset = Math.abs(mDrawOffset / mSingleWidth);
        for (int i = 0; i < field.length; i++) {
            canvas.drawLine(mPaddingLeft + mSingleWidth * i + mDrawOffset, 0,
                    mPaddingLeft + mSingleWidth * i + mDrawOffset, mHeight, mPaint);
        }

        canvas.save();

        mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C2));
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.clipRect(0, mHeight, mPaddingLeft + mWidth + mPaddingRight, mHeight + mSingleHeight);


        for (int i = 0; i < field.length; i++) {
            canvas.drawText(field[i], mPaddingLeft + mSingleWidth * i + mDrawOffset, mHeight + mHeightOffset, mPaint);
        }
        canvas.restore();


        /**
         * 画数据线
         */

        canvas.save();
        mPaint.setShader(null);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.clipRect(0, 0, mPaddingLeft + mWidth + mPaddingRight, mHeight);

        ymin = 0;
        ymax = data[0];
        for (int i = 0; i < data.length; i++) {
            if (ymax < data[i])
                ymax = data[i];
        }

        ymax += (ymax - ymin) / (countY - 1) / 2;

        float dataUnit = (ymax - ymin) / (countY - 1);

        for (int i = 0; i < data.length; i++) {

            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C2));

            if (i == data.length - 1) {

                float x1 = mPaddingLeft + mSingleWidth * i + mDrawOffset;
                float y1 = (data[i] / dataUnit) * (mHeight / (countY - 1));

                canvas.drawText(Integer.valueOf((int) data[i]).toString(), x1, (data[i - 1] <= data[i] ? mHeight - y1 - mTextHeight / 3 : mHeight - y1 + mTextHeight / 1.5f), mPaint);

                mPaint.setColor(getResources().getColor(colors[i % colors.length]));
                canvas.drawCircle(x1, mHeight - y1, 8, mPaint);

            } else {

                float x1 = mPaddingLeft + mSingleWidth * i + mDrawOffset;
                float y1 = (data[i] / dataUnit) * (mHeight / (countY - 1));
                float x2 = mPaddingLeft + mSingleWidth * (i + 1) + mDrawOffset;
                float y2 = (data[i + 1] / dataUnit) * (mHeight / (countY - 1));

                mPaint.setStrokeWidth(1.5f);
                canvas.drawLine(x1, mHeight - y1, x2, mHeight - y2, mPaint);
                canvas.drawText(Integer.valueOf((int) data[i]).toString(), x1, (data[i] > data[i + 1] ? mHeight - y1 - mTextHeight / 3 : mHeight - y1 + mTextHeight / 1.5f), mPaint);

                mPaint.setColor(getResources().getColor(colors[i % colors.length]));
                canvas.drawCircle(x1, mHeight - y1, 8, mPaint);
            }

        }

        canvas.restore();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (field == null || field.length == 0) {
            return;
        }

        if (data == null || data.length == 0) {
            return;
        }

        if (screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        if (field.length > CONSTANTS_COUNT_X) {
            countX = CONSTANTS_COUNT_X;
        } else {
            countX = field.length;
        }

        countY = CONSTANTS_COUNT_Y;

        mSingleWidth = screenWidth / countX;
        mPaddingLeft = mPaddingRight = mSingleWidth / 2;
        mWidth = mCalculateWidth = screenWidth - mPaddingLeft - mPaddingRight;

        mSingleHeight = screenHeight / countY;
        mHeight = screenHeight / CONSTANTS_COUNT_Y * (CONSTANTS_COUNT_Y - 1);
        mTextHeight = mSingleHeight / 2;
        mHeightOffset = mTextHeight / 2f;

        mSaveOffset = mDrawOffset = mWidth - mCalculateWidth;

        mCalculateWidth = mSingleWidth * (data.length - 1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mWidth >= mCalculateWidth) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                timeCache = System.currentTimeMillis();
                Log.d(TAG, "ACTION_DOWN--downX:" + downX);
                break;
            case MotionEvent.ACTION_UP:
                mSaveOffset = mDrawOffset;
                if (System.currentTimeMillis() - timeCache < 80) {
                    performClick();
                    return false;
                }
                Log.d(TAG, "ACTION_UP--mDrawOffset:" + mDrawOffset);
                Log.d(TAG, "ACTION_UP--mSaveOffset:" + mSaveOffset);
                break;
            case MotionEvent.ACTION_MOVE:
                if (System.currentTimeMillis() - timeCache >= 80) {
                    moveX = (int) event.getX();
                    distance = moveX - downX;
                    mDrawOffset = mSaveOffset + distance;
                    if (mDrawOffset > 0) {
                        mDrawOffset = mSaveOffset = 0;
                    } else if (mDrawOffset < mWidth - mCalculateWidth) {
                        mDrawOffset = mSaveOffset = mWidth - mCalculateWidth;
                    }
                    Log.d(TAG, "ACTION_MOVE--moveX:" + moveX);
                    Log.d(TAG, "ACTION_MOVE--downX:" + downX);
                    Log.d(TAG, "ACTION_MOVE--distance:" + distance);
                    Log.d(TAG, "ACTION_MOVE--mSaveOffset:" + mSaveOffset);
                    Log.d(TAG, "ACTION_MOVE--mDrawOffset:" + mDrawOffset);
                    Log.d(TAG, "ACTION_MOVE--mCalculateWidth:" + mCalculateWidth);
                    postInvalidate();
                }
                break;
        }
        return true;
    }


    public void setData(float[] data) {
        this.data = data;
    }

    public void setField(String[] field) {
        this.field = field;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}