package com.lesso.data.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/6/29.
 */
public class XYLineView extends View {

    private final int CONSTANTS_COUNT_X = 8;
    private final int CONSTANTS_COUNT_Y = 5;

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private float[] data;

    private String[] field;

    private float density = 0f;

    public static int fullScreenWidth = 0;

    public static int fullScreenHeight = 0;

    private int mHeight, mWidth, graphDpHeight = 300;

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


        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextHeight / 3);

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

        int cntOffset = Math.abs(mDrawOffset / mSingleWidth);
        for (int i = 0; i < countX + cntOffset; i++) {
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

        if (getField().length > CONSTANTS_COUNT_X) {
            countX = CONSTANTS_COUNT_X;
        } else {
            countX = getField().length;
        }

        countY = CONSTANTS_COUNT_Y;

        if (density == 0f) {
            density = getResources().getDisplayMetrics().density;

            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            fullScreenHeight = dm.heightPixels;
            fullScreenWidth = dm.widthPixels;
        }

        mSingleWidth = fullScreenWidth / countX;
        mPaddingLeft = mPaddingRight = mSingleWidth / 2;
        mWidth = mCalculateWidth = fullScreenWidth - mPaddingLeft - mPaddingRight;

        mSingleHeight = (int) (graphDpHeight * density / countY);
        mHeight = (int) (graphDpHeight * density / CONSTANTS_COUNT_Y * (CONSTANTS_COUNT_Y - 1));
        mTextHeight = mSingleHeight / 2;
        mHeightOffset = mTextHeight / 2f;

        mSaveOffset = mDrawOffset = mWidth - mCalculateWidth;

        mCalculateWidth = mSingleWidth * (data.length - 1);

        setMeasuredDimension(fullScreenWidth, ((int) (graphDpHeight * density)));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mWidth >= mCalculateWidth) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mSaveOffset = mDrawOffset;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                distance = moveX - downX;
                mDrawOffset = mSaveOffset + distance;
                if (mDrawOffset > 0) {
                    mDrawOffset = 0;
                } else if (mDrawOffset < mWidth - mCalculateWidth) {
                    mDrawOffset = mWidth - mCalculateWidth;
                }
                postInvalidate();
                break;

            default:
                break;
        }

        super.onTouchEvent(event);
        return true;
    }

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public String[] getField() {
        return field;
    }

    public void setField(String[] field) {
        this.field = field;
    }

}