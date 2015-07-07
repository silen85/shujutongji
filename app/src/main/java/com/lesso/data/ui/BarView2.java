package com.lesso.data.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/7/7.
 */
public class BarView2 extends View {

    private String TAG = "com.lesso.data.ui.BarView";

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

    private float mTextHeight;

    private float mHeightOffset;

    private float ymax, ymin;

    public BarView2(Context context) {
        super(context);
    }

    public BarView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initSize() {

        if (field == null || data == null) {
            return;
        }

        if (screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        if (field.length == 0 || field.length > CONSTANTS_COUNT_X) {
            countX = CONSTANTS_COUNT_X;
        } else {
            countX = field.length;
        }

        countY = CONSTANTS_COUNT_Y;


        mSingleWidth = screenWidth / countX;
        mPaddingLeft = mPaddingRight = mSingleWidth / 2;
        mWidth = mCalculateWidth = screenWidth - mPaddingLeft - mPaddingRight;

        mSingleHeight = screenHeight / countY;
        mHeight = screenHeight / countY * (countY - 1);
        mTextHeight = mSingleHeight / 2;
        mHeightOffset = mTextHeight / 2f;

        mCalculateWidth = mSingleWidth * (data.length - 1);

        Log.d(TAG, "initSize :" + screenWidth + screenHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        Log.d(TAG, "onDraw");

        if (screenWidth <= 0 || screenHeight <= 0 || mSingleWidth <= 0 || mSingleHeight <= 0) {
            Log.d(TAG, "screenWidth <= 0 || screenHeight <= 0 || mSingleWidth <= 0 || mSingleHeight <= 0");
            return;
        }

        if (data.length == 0 || field.length == 0) {

            Log.d(TAG, "data.length == 0 || field.length == 0");

            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextSize(mTextHeight * 1.1f);
            mPaint.setShader(null);
            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C6));
            canvas.drawRect(new Rect(0, 0, mPaddingLeft + mWidth + mPaddingRight, mHeight + mSingleHeight), mPaint);

            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C2));
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.no_data_tips),
                    (mPaddingLeft + mWidth + mPaddingRight) / 2,
                    (mHeight + mSingleHeight) / 2, mPaint);

        } else {

            Log.d(TAG, "data.length=" + data.length + " field.length=" + field.length);

            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setTextSize(mTextHeight / 2.1f);

            /**
             * 画坐标图
             */

            mPaint.setShader(null);
            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C6));

            if (mWidth >= mCalculateWidth) {
                canvas.drawRect(new Rect(0, 0, mPaddingLeft + mWidth + mPaddingRight, mHeight + mSingleHeight), mPaint);
            }else {
                canvas.drawRect(new Rect(0, 0, mPaddingLeft + mCalculateWidth + mPaddingRight, mHeight + mSingleHeight), mPaint);
            }


            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C4));
            canvas.drawLine(0, (countY - 1) * mSingleHeight,
                    mPaddingLeft + (mWidth >= mCalculateWidth ? mWidth : mCalculateWidth) + mPaddingRight,
                    (countY - 1) * mSingleHeight, mPaint);


            mPaint.setColor(getResources().getColor(R.color.REPORT_UI_C2));
            mPaint.setTextAlign(Paint.Align.CENTER);
            for (int i = 0; i < field.length; i++) {
                canvas.drawText(field[i], mPaddingLeft + mSingleWidth * i, mHeight + mHeightOffset, mPaint);
            }


            /**
             * 画柱状图
             */
            mPaint.setShader(null);
            mPaint.setTextAlign(Paint.Align.CENTER);

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

                float x1 = (mPaddingLeft + mSingleWidth * i) - (mSingleWidth / 6);
                float y1 = mHeight - (data[i] / dataUnit) * (mHeight / (countY - 1));
                float x2 = (mPaddingLeft + mSingleWidth * i) + (mSingleWidth / 6);
                float y2 = mHeight;

                canvas.drawText(Integer.valueOf((int) data[i]).toString(), (mPaddingLeft + mSingleWidth * i), y1 - mTextHeight / 3, mPaint);

                mPaint.setColor(getResources().getColor(colors[i % colors.length]));
                canvas.drawRect(x1, y1, x2, y2, mPaint);

            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        screenWidth = resolveMeasured(widthMeasureSpec, screenWidth);
        screenHeight = resolveMeasured(heightMeasureSpec, screenHeight);

        initSize();

        if(mWidth>=mCalculateWidth){
            setMeasuredDimension(screenWidth,screenHeight);
        }else {
            setMeasuredDimension(mCalculateWidth,screenHeight);
        }

        Log.d(TAG, "onMeasure :" + screenWidth + screenHeight);

    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
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
