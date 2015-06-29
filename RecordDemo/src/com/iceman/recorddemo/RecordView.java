
package com.iceman.recorddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.iceman.recorddemo.RecordItem.Field;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecordView extends View {
    private int mHeight, mWidth;

    private RecordItem mItem;

    private int type;

    private ArrayList<Integer> yFields = new ArrayList<Integer>();

    private ArrayList<Integer> zFields = new ArrayList<Integer>();

    private int mCalculateWidth;

    private float mCalculateSingleWidth;

    private int mDrawOffset = 0;

    private DecimalFormat format = new DecimalFormat("0.00");

    public static final int RECORD_TEXT = 0;// 文字

    public static final int RECORD_TUPIAN = 1;// 图片

    public static final int RECORD_BIAOGE = 2;// 表格

    public static final int RECORD_XIANXING = 3;// 线形

    public static final int RECORD_ZHUXING = 4;// 柱形

    public static final int RECORD_SHUANGZHOU = 5;// 双坐标

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mSingleWidth;

    private int mFieldRows = 1;

    private int mLeftPanding, mRightPanding;

    private float mTextHeightOffset;

    private int xIndex;

    private Float ymax, zmax;

    private Float ymin, zmin;

    private float mMargin = (float) (20 * Globe.density / 1.5);

    private int[][] mShaderColors = new int[][]{
            {
                    0xffff9a24, 0xffca6400
            }, {
            0xff50e80c, 0xff028b12
    }, {
            0xfff33b3b, 0xffa92223
    }, {
            0xff22aaf8, 0xff217acd
    }, {
            0xffeb7bff, 0xffab50d1
    }, {
            0xffffb400, 0xffff8a00
    }, {
            0xfffb6286, 0xffc73e5b
    }, {
            0xffeaff00, 0xffc6a800
    }, {
            0xffa6f22f, 0xff6b8f0b
    }, {
            0xff16cbf2, 0xff109cad
    },

    };

    private float mTextHeight;

    private int downX;

    private int moveX;

    private int distance;

    private int mSaveOffset;

    private ArrayList<Float> mFieldWidths = new ArrayList<Float>();

    private ArrayList<Integer> mFieldPaintIndexs = new ArrayList<Integer>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;

        switch (type) {
            case RECORD_BIAOGE:
                mTextHeight = (float) (mTextHeight * 1.5);
                break;
            case RECORD_XIANXING:
            case RECORD_ZHUXING:
                break;
            case RECORD_SHUANGZHOU:
                break;

            default:
                break;
        }
    }

    public RecordItem getmItem() {
        return mItem;
    }

    public void setmItem(RecordItem mItem) {
        mFieldWidths.clear();
        float temp = 0;
        int indextemp = -1;
        this.mItem = mItem;
        Field[] fields;
        String[][] data;
        // mLeftPanding = (int) (50 * Globe.density / 1.5);
        for (int i = 0; i < mItem.getFields().length; i++) {
            if (mItem.getFields()[i].coord.equals("x")) {
                xIndex = i;
            } else if (mItem.getFields()[i].coord.equals("y")) {
                yFields.add(i);
            } else {
                zFields.add(i);
            }
        }
        switch (type) {
            case RECORD_BIAOGE:
                for (Field str : mItem.getFields()) {
                    if (mCalculateSingleWidth < mPaint.measureText(str.name)) {
                        mCalculateSingleWidth = mPaint.measureText(str.name);
                    }
                }
                mSingleWidth = (int) ((Globe.fullScreenWidth - mMargin) / 4);
                if (mCalculateSingleWidth > mSingleWidth) {
                    mSingleWidth = (int) mCalculateSingleWidth;
                }
                mCalculateWidth = mSingleWidth * mItem.getFields().length;
                break;
            case RECORD_XIANXING:
                mCalculateWidth = mWidth;
                data = mItem.getData();
                ymax = Float.MIN_VALUE;
                ymin = Float.MAX_VALUE;
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < yFields.size(); j++) {
                        if (mLeftPanding < mPaint.measureText(data[i][yFields.get(j)]) + 5) {
                            mLeftPanding = (int) (mPaint.measureText(data[i][yFields.get(j)]) + 5);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) >= ymax) {
                            ymax = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) <= ymin) {
                            ymin = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                    }
                }
                ymax = ymax + ymax / 10;
                for (int i = 0; i < yFields.size(); i++) {
                    temp += (mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10
                            + mTextHeight + 5);
                    indextemp++;
                    if (temp >= Globe.fullScreenWidth - mMargin - mLeftPanding) {
                        mFieldRows++;
                        temp -= (mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10
                                + mTextHeight + 5);
                        mFieldWidths.add(temp);
                        mFieldPaintIndexs.add(indextemp);
                        i--;
                        temp = 0;
                    }
                }
                mFieldPaintIndexs.add(1000);
                mFieldWidths.add(temp);
                break;
            case RECORD_ZHUXING:
                for (String str : mItem.getData()[0]) {
                    if (mCalculateSingleWidth < mPaint.measureText(str)) {
                        mCalculateSingleWidth = mPaint.measureText(str);
                    }
                }
                mSingleWidth = (int) ((yFields.size() + 2) * 20 * Globe.density / 1.5);
                if (mCalculateSingleWidth > mSingleWidth) {
                    mSingleWidth = (int) mCalculateSingleWidth;
                }
                mCalculateWidth = mItem.getData().length * mSingleWidth;
                data = mItem.getData();
                ymax = Float.MIN_VALUE;
                ymin = Float.MAX_VALUE;
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < yFields.size(); j++) {
                        if (mLeftPanding < mPaint.measureText(data[i][yFields.get(j)]) + 5) {
                            mLeftPanding = (int) (mPaint.measureText(data[i][yFields.get(j)]) + 5);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) >= ymax) {
                            ymax = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) <= ymin) {
                            ymin = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                    }
                }
                ymax = ymax + ymax / 10;
                for (int i = 0; i < yFields.size(); i++) {
                    temp += (mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10
                            + mTextHeight + 5);
                    indextemp++;
                    if (temp >= Globe.fullScreenWidth - mMargin - mLeftPanding) {
                        mFieldRows++;
                        temp -= (mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10
                                + mTextHeight + 5);
                        mFieldWidths.add(temp);
                        i--;
                        temp = 0;
                        mFieldPaintIndexs.add(indextemp);
                    }
                }
                mFieldPaintIndexs.add(1000);
                mFieldWidths.add(temp);
                break;
            case RECORD_SHUANGZHOU:
                for (String str : mItem.getData()[0]) {
                    if (mCalculateSingleWidth < mPaint.measureText(str)) {
                        mCalculateSingleWidth = mPaint.measureText(str);
                    }
                }
                mSingleWidth = (int) ((yFields.size() + 2) * 20 * Globe.density / 1.5);
                if (mCalculateSingleWidth > mSingleWidth) {
                    mSingleWidth = (int) mCalculateSingleWidth;
                }
                mCalculateWidth = mItem.getData().length * mSingleWidth;
                fields = mItem.getFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].coord.equals("x")) {
                        xIndex = i;
                    }
                }
                data = mItem.getData();
                ymax = Float.MIN_VALUE;
                ymin = Float.MAX_VALUE;
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < yFields.size(); j++) {
                        if (mLeftPanding < mPaint.measureText(data[i][yFields.get(j)]) + 5) {
                            mLeftPanding = (int) (mPaint.measureText(data[i][yFields.get(j)]) + 5);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) >= ymax) {
                            ymax = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                        if (Float.parseFloat(data[i][yFields.get(j)]) <= ymin) {
                            ymin = Float.parseFloat(data[i][yFields.get(j)]);
                        }
                    }
                }
                ymax = ymax + ymax / 10;

                zmax = Float.MIN_VALUE;
                zmin = Float.MAX_VALUE;
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < zFields.size(); j++) {
                        if (mRightPanding < mPaint.measureText(data[i][zFields.get(j)]) + 5) {
                            mRightPanding = (int) (mPaint.measureText(data[i][zFields.get(j)]) + 5);
                        }
                        if (Float.parseFloat(data[i][zFields.get(j)]) >= zmax) {
                            zmax = Float.parseFloat(data[i][zFields.get(j)]);
                        }
                        if (Float.parseFloat(data[i][zFields.get(j)]) <= zmin) {
                            zmin = Float.parseFloat(data[i][zFields.get(j)]);
                        }
                    }
                }
                zmax = zmax + zmax / 10;
                for (int i = 0; i < yFields.size(); i++) {
                    temp += (mTextHeight + 5
                            + mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10);
                    indextemp++;
                    if (temp >= Globe.fullScreenWidth - mMargin - mLeftPanding - mRightPanding) {
                        mFieldRows++;
                        temp -= (mTextHeight + 5
                                + mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10);
                        mFieldWidths.add(temp);
                        mFieldPaintIndexs.add(indextemp);
                        temp = 0;
                        i--;
                    }
                }
                for (int i = 0; i < zFields.size(); i++) {
                    temp += (mTextHeight + 5
                            + mPaint.measureText(mItem.getFields()[zFields.get(i)].name) + 10);
                    indextemp++;
                    if (temp >= Globe.fullScreenWidth - mMargin - mLeftPanding - mRightPanding) {
                        mFieldRows++;
                        temp -= (mTextHeight + 5
                                + mPaint.measureText(mItem.getFields()[zFields.get(i)].name) + 10);
                        mFieldWidths.add(temp);
                        mFieldPaintIndexs.add(indextemp);
                        i--;
                        temp = 0;
                    }
                }
                // if (mFieldPaintIndexs.size() == 0) {
                mFieldPaintIndexs.add(1000);
                mFieldWidths.add(temp);
                // }
                break;
            default:
                break;
        }
        invalidate();
    }

    public RecordView(Context context) {
        super(context);
        mPaint.setTextSize(15);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setStyle(Style.FILL);
        mTextHeightOffset = mPaint.getFontMetrics().bottom;
        mTextHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().ascent;
    }

    public RecordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (type) {
            case RECORD_BIAOGE:
                paintBiaoGeLine(canvas);
                paintBiaoGeData(canvas);
                break;
            case RECORD_XIANXING:
                paintXianXingLine(canvas);
                paintXianXingData(canvas);
                break;
            case RECORD_ZHUXING:
                paintZhuXingLine(canvas);
                paintZhuXingData(canvas);
                break;
            case RECORD_SHUANGZHOU:
                paintShuangZhouLine(canvas);
                paintShuangZhouData(canvas);
                break;

            default:
                break;
        }

    }

    /**
     * 画双轴数据
     *
     * @param canvas
     */
    private void paintShuangZhouData(Canvas canvas) {
        paintZhuXingData(canvas);
        canvas.save();
        canvas.clipRect(mLeftPanding, 0, mWidth - mRightPanding, mHeight - mTextHeight);
        mPaint.setShader(null);
        for (int i = 0; i < zFields.size(); i++) {
            mPaint.setColor(mShaderColors[yFields.size() + i][1]);
            for (int j = 0; j < mItem.getData().length - 1; j++) {
                canvas.drawLine(mLeftPanding + mSingleWidth * j + mDrawOffset + mSingleWidth / 2,
                        getXianXingzH(Float.parseFloat(mItem.getData()[j][zFields.get(i)])),
                        mLeftPanding + mSingleWidth * (j + 1) + mDrawOffset + mSingleWidth / 2,
                        getXianXingzH(Float.parseFloat(mItem.getData()[j + 1][zFields.get(i)])),
                        mPaint);
                canvas.drawCircle(mLeftPanding + mSingleWidth * j + mDrawOffset + mSingleWidth / 2,
                        getXianXingzH(Float.parseFloat(mItem.getData()[j][zFields.get(i)])), 5,
                        mPaint);
            }
            canvas.drawCircle(mLeftPanding + mSingleWidth * (mItem.getData().length - 1)
                    + mDrawOffset + mSingleWidth / 2, getXianXingzH(Float.parseFloat(mItem
                    .getData()[mItem.getData().length - 1][zFields.get(i)])), 5, mPaint);
        }
        canvas.restore();

    }

    /**
     * 画双轴框架
     *
     * @param canvas
     */
    private void paintShuangZhouLine(Canvas canvas) {
        mPaint.setShader(null);
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(mLeftPanding, 0, mWidth - mRightPanding,
                (int) (mHeight - mTextHeight * (mFieldRows + 1))), mPaint);
        mPaint.setColor(Color.BLUE);
        int singleY = (int) ((mHeight - mTextHeight * (mFieldRows + 1)) / 5);
        // y轴标尺
        float singleNumber = (ymax - ymin) / 5;
        for (int i = 0; i < 5; i++) {
            String dd = format.format(ymax - (i + 1) * singleNumber);
            canvas.drawText(dd, mLeftPanding / 2, singleY * (i + 1) + mTextHeight / 2
                    - mTextHeightOffset, mPaint);
        }
        canvas.drawText(format.format(ymax), mLeftPanding / 2, mTextHeight - mTextHeightOffset,
                mPaint);
        if (zFields.size() != 0) {
            // z轴标尺
            singleNumber = (zmax - zmin) / 5;
            for (int i = 0; i < 5; i++) {
                String dd = format.format(zmax - (i + 1) * singleNumber);
                canvas.drawText(dd, mWidth - mRightPanding / 2, singleY * (i + 1) + mTextHeight / 2
                        - mTextHeightOffset, mPaint);
            }
            canvas.drawText(format.format(zmax), mWidth - mRightPanding / 2, mTextHeight
                    - mTextHeightOffset, mPaint);
        }
        canvas.save();
        canvas.clipRect(mLeftPanding - mSingleWidth / 2, mHeight - mTextHeight * (mFieldRows + 1),
                mWidth + mSingleWidth / 2, mHeight - mTextHeight * mFieldRows);
        for (int i = 0; i < mItem.getData().length; i++) {
            canvas.drawText(mItem.getData()[i][xIndex], mLeftPanding + mSingleWidth / 2
                    + mSingleWidth * i + mDrawOffset, mHeight - mFieldRows * mTextHeight
                    - mTextHeightOffset, mPaint);
        }
        canvas.restore();
        mPaint.setTextAlign(Align.LEFT);

        int fieldindex = 0;
        int rowindex = 0;
        float heighttemp = (mFieldRows - 1) * mTextHeight;
        float offsetTemp = mLeftPanding
                + (mWidth - mLeftPanding - mRightPanding - mFieldWidths.get(rowindex)) / 2;
        for (int i = 0; i < yFields.size(); i++) {
            mPaint.setColor(mShaderColors[i][1]);
            canvas.drawRect(offsetTemp, mHeight - heighttemp - mTextHeight + mTextHeight / 6,
                    offsetTemp + mTextHeight, mHeight - heighttemp - mTextHeight / 6, mPaint);
            offsetTemp += (mTextHeight + 5);
            canvas.drawText(mItem.getFields()[yFields.get(i)].name, offsetTemp, mHeight
                    - heighttemp - mTextHeightOffset, mPaint);
            offsetTemp += mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10;
            if (i == mFieldPaintIndexs.get(fieldindex) - 1) {
                rowindex++;
                offsetTemp = mLeftPanding
                        + (mWidth - mLeftPanding - mRightPanding - mFieldWidths.get(rowindex)) / 2;
                fieldindex++;
                heighttemp -= mTextHeight;
            }
        }
        for (int i = 0; i < zFields.size(); i++) {
            mPaint.setColor(mShaderColors[yFields.size() + i][1]);
            // mPaint.setColor(Color.RED);
            canvas.drawRect(offsetTemp, mHeight - heighttemp - mTextHeight / 2 - 2, offsetTemp
                    + mTextHeight, mHeight - heighttemp - mTextHeight / 2 + 2, mPaint);
            offsetTemp += (mTextHeight + 5);
            canvas.drawText(mItem.getFields()[zFields.get(i)].name, offsetTemp, mHeight
                    - heighttemp - mTextHeightOffset, mPaint);
            offsetTemp += mPaint.measureText(mItem.getFields()[zFields.get(i)].name) + 5;
            if (i + yFields.size() == mFieldPaintIndexs.get(fieldindex) - 1) {
                rowindex++;
                offsetTemp = mLeftPanding + (mWidth - mLeftPanding - mFieldWidths.get(rowindex))
                        / 2;
                fieldindex++;
                heighttemp -= mTextHeight;
            }
        }
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.record_bg_color));
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(mLeftPanding, i * (mHeight - mTextHeight * 2) / 5, mWidth
                    - mRightPanding, i * (mHeight - mTextHeight * 2) / 5, mPaint);
        }
        for (int i = 0; i < mItem.getData().length; i++) {
            canvas.drawLine(mLeftPanding + mSingleWidth * i + mDrawOffset, 0, mLeftPanding
                    + mSingleWidth * i + mDrawOffset, mHeight - mTextHeight * 2, mPaint);
        }
        mPaint.setColor(Color.BLACK);
        // canvas.drawLine(mLeftPanding, 0, mLeftPanding, mHeight - mTextHeight
        // * 2 + 2, mPaint);
        if (ymin < 0) {
            canvas.drawLine(mLeftPanding - 5,
                    (float) (ymax / (ymax - ymin) * 250 * Globe.density / 1.5), mWidth,
                    (float) (ymax / (ymax - ymin) * 250 * Globe.density / 1.5), mPaint);
        }
    }

    /**
     * 柱形图的数据
     *
     * @param canvas
     */
    private void paintZhuXingData(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mLeftPanding, 0, mWidth - mRightPanding, mHeight - mTextHeight);

        if (ymin < 0 && ymax > 0) {
            float ym = (float) (ymax / (ymax - ymin) * 250 * Globe.density / 1.5);
            float dataoffset = mSingleWidth / (yFields.size() + 2);
            for (int j = 0; j < mItem.getData().length; j++) {
                for (int i = 0; i < yFields.size(); i++) {
                    float position = getZhuXingyH(Float.parseFloat(mItem.getData()[j][yFields
                            .get(i)]));
                    if (position >= 0) {
                        mPaint.setShader(new LinearGradient(mLeftPanding + mSingleWidth * j
                                + mDrawOffset + dataoffset, ym - position, mLeftPanding
                                + mSingleWidth * j + mDrawOffset + dataoffset
                                + (float) (20 * Globe.density / 1.5), ym, mShaderColors[i][0],
                                mShaderColors[i][1], Shader.TileMode.MIRROR));
                        canvas.drawRect(mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset,
                                ym - position, mLeftPanding + mSingleWidth * j + mDrawOffset
                                        + dataoffset + (float) (20 * Globe.density / 1.5), ym,
                                mPaint);
                    } else {
                        mPaint.setShader(new LinearGradient(mLeftPanding + mSingleWidth * j
                                + mDrawOffset + dataoffset, ym, mLeftPanding + mSingleWidth * j
                                + mDrawOffset + dataoffset + (float) (20 * Globe.density / 1.5), ym
                                - position, mShaderColors[i][0], mShaderColors[i][1],
                                Shader.TileMode.MIRROR));
                        canvas.drawRect(mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset,
                                ym, mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset
                                        + (float) (20 * Globe.density / 1.5), ym - position, mPaint);
                    }
                    dataoffset += mSingleWidth / (yFields.size() + 2);
                }
                dataoffset = mSingleWidth / (yFields.size() + 2);
            }

        } else if (ymin >= 0) {
            float dataoffset = mSingleWidth / (yFields.size() + 2);
            for (int j = 0; j < mItem.getData().length; j++) {
                for (int i = 0; i < yFields.size(); i++) {
                    mPaint.setShader(new LinearGradient(mLeftPanding + mSingleWidth * j
                            + mDrawOffset + dataoffset, getXianXingyH(Float.parseFloat(mItem
                            .getData()[j][yFields.get(i)])), mLeftPanding + mSingleWidth * j
                            + mDrawOffset + dataoffset + (float) (20 * Globe.density / 1.5),
                            mHeight - mTextHeight * 2, mShaderColors[i][0], mShaderColors[i][1],
                            Shader.TileMode.MIRROR));
                    canvas.drawRect(mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset,
                            getXianXingyH(Float.parseFloat(mItem.getData()[j][yFields.get(i)])),
                            mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset
                                    + (float) (20 * Globe.density / 1.5),
                            mHeight - mTextHeight * 2, mPaint);
                    dataoffset += mSingleWidth / (yFields.size() + 2);
                }
                dataoffset = mSingleWidth / (yFields.size() + 2);
            }
        } else {
            float dataoffset = mSingleWidth / (yFields.size() + 2);
            for (int j = 0; j < mItem.getData().length; j++) {
                for (int i = 0; i < yFields.size(); i++) {
                    // mPaint.setColor(mColors[i]);
                    mPaint.setShader(new LinearGradient(mLeftPanding + mSingleWidth * j
                            + mDrawOffset + dataoffset, 0, mLeftPanding + mSingleWidth * j
                            + mDrawOffset + dataoffset + (float) (20 * Globe.density / 1.5),
                            getXianXingyH(Float.parseFloat(mItem.getData()[j][yFields.get(i)])),
                            mShaderColors[i][0], mShaderColors[i][1], Shader.TileMode.MIRROR));
                    canvas.drawRect(mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset, 0,
                            mLeftPanding + mSingleWidth * j + mDrawOffset + dataoffset
                                    + (float) (20 * Globe.density / 1.5),
                            getXianXingyH(Float.parseFloat(mItem.getData()[j][yFields.get(i)])),
                            mPaint);
                    dataoffset += mSingleWidth / (yFields.size() + 2);
                }
                dataoffset = mSingleWidth / (yFields.size() + 2);
            }
        }
        canvas.restore();
    }

    /**
     * 柱形图的框架
     *
     * @param canvas
     */
    private void paintZhuXingLine(Canvas canvas) {
        mPaint.setShader(null);
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(mLeftPanding, 0, mCalculateWidth, (int) (mHeight - mTextHeight
                * (mFieldRows + 1))), mPaint);
        mPaint.setColor(Color.BLUE);
        int singleY = (int) ((mHeight - mTextHeight * (mFieldRows + 1)) / 5);
        float singleNumber = (ymax - ymin) / 5;
        for (int i = 0; i < 5; i++) {
            String dd = format.format(ymax - (i + 1) * singleNumber);
            canvas.drawText(dd, mLeftPanding / 2, singleY * (i + 1) + mTextHeight / 2
                    - mTextHeightOffset, mPaint);
        }
        canvas.drawText(format.format(ymax), mLeftPanding / 2, mTextHeight - mTextHeightOffset,
                mPaint);
        canvas.save();
        canvas.clipRect(mLeftPanding - mSingleWidth / 2, mHeight - mTextHeight * (mFieldRows + 1),
                mCalculateWidth + mSingleWidth / 2, mHeight - mTextHeight * mFieldRows);
        for (int i = 0; i < mItem.getData().length; i++) {
            canvas.drawText(mItem.getData()[i][xIndex], mLeftPanding + mSingleWidth / 2
                    + mSingleWidth * i + mDrawOffset, mHeight - mTextHeight * mFieldRows
                    - mTextHeightOffset, mPaint);
        }
        canvas.restore();
        // for (int i = 0; i < yFields.size(); i++) {
        // mPaint.setColor(mColors[i]);
        // canvas.drawText(mItem.getFields()[yFields.get(i)].name, mLeftPanding
        // + (i + 1)
        // * (mWidth - mLeftPanding) / (yFields.size() + 1), mHeight -
        // mTextHeightOffset,
        // mPaint);
        // canvas.drawRect(mLeftPanding + (i + 1) * (mWidth - mLeftPanding) /
        // (yFields.size() + 1)
        // - mPaint.measureText(mItem.getFields()[yFields.get(i)].name) / 2 - 10
        // - 10,
        // mHeight - mTextHeight + 3,
        // mLeftPanding + (i + 1) * (mWidth - mLeftPanding) / (yFields.size() +
        // 1)
        // - mPaint.measureText(mItem.getFields()[yFields.get(i)].name) / 2 -
        // 10,
        // mHeight - 3, mPaint);
        // }

        mPaint.setTextAlign(Align.LEFT);
        int fieldindex = 0;
        int rowindex = 0;
        float heighttemp = (mFieldRows - 1) * mTextHeight;
        float offsetTemp = mLeftPanding + (mWidth - mLeftPanding - mFieldWidths.get(rowindex)) / 2;
        for (int i = 0; i < yFields.size(); i++) {
            mPaint.setColor(mShaderColors[i][1]);
            canvas.drawRect(offsetTemp, mHeight - heighttemp - mTextHeight + mTextHeight / 6,
                    offsetTemp + mTextHeight, mHeight - heighttemp - mTextHeight / 6, mPaint);
            offsetTemp += (mTextHeight + 5);
            canvas.drawText(mItem.getFields()[yFields.get(i)].name, offsetTemp, mHeight
                    - heighttemp - mTextHeightOffset, mPaint);
            offsetTemp += mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10;
            if (i == mFieldPaintIndexs.get(fieldindex) - 1) {
                rowindex++;
                offsetTemp = mLeftPanding + (mWidth - mLeftPanding - mFieldWidths.get(rowindex))
                        / 2;
                fieldindex++;
                heighttemp -= mTextHeight;
            }
        }
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.record_bg_color));
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(mLeftPanding, i * (mHeight - mTextHeight * 2) / 5, mCalculateWidth, i
                    * (mHeight - mTextHeight * 2) / 5, mPaint);
        }
        for (int i = 0; i < mItem.getData().length; i++) {
            canvas.drawLine(mLeftPanding + mSingleWidth * i + mDrawOffset, 0, mLeftPanding
                    + mSingleWidth * i + mDrawOffset, mHeight - mTextHeight * 2, mPaint);
        }
        mPaint.setColor(Color.BLACK);
        // canvas.drawLine(mLeftPanding, 0, mLeftPanding, mHeight - mTextHeight
        // * 2 + 2, mPaint);
        if (ymin < 0) {
            canvas.drawLine(mLeftPanding - 5,
                    (float) (ymax / (ymax - ymin) * 250 * Globe.density / 1.5), mWidth,
                    (float) (ymax / (ymax - ymin) * 250 * Globe.density / 1.5), mPaint);
        }
    }

    /**
     * 线性图的数据
     *
     * @param canvas
     */
    private void paintXianXingData(Canvas canvas) {
        // canvas.save();
        // canvas.clipRect(mLeftPanding, 0, mWidth, mHeight - mTextHeight);
        for (int i = 0; i < yFields.size(); i++) {
            mPaint.setColor(mShaderColors[i][1]);
            for (int j = 0; j < mItem.getData().length - 1; j++) {
                canvas.drawLine(mLeftPanding + mSingleWidth * j + mDrawOffset,
                        getXianXingyH(Float.parseFloat(mItem.getData()[j][yFields.get(i)])),
                        mLeftPanding + mSingleWidth * (j + 1) + mDrawOffset,
                        getXianXingyH(Float.parseFloat(mItem.getData()[j + 1][yFields.get(i)])),
                        mPaint);
                // canvas.drawCircle(mLeftPanding + mSingleWidth * j +
                // mDrawOffset,
                // getXianXingyH(Float.parseFloat(mItem.getData()[j][yFields.get(i)])),
                // 5,
                // mPaint);
            }
            // canvas.drawCircle(
            // mLeftPanding + mSingleWidth * (mItem.getData().length - 1) +
            // mDrawOffset,
            // getXianXingyH(Float.parseFloat(mItem.getData()[mItem.getData().length
            // - 1][yFields
            // .get(i)])), 5, mPaint);
        }
        // canvas.restore();
    }

    /**
     * 线性图的框架
     *
     * @param canvas
     */
    private void paintXianXingLine(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(mLeftPanding, 0, mCalculateWidth, (int) (mHeight - mTextHeight
                * (mFieldRows + 1))), mPaint);
        mPaint.setColor(Color.BLUE);
        int singleY = (int) ((mHeight - mTextHeight * (mFieldRows + 1)) / 5);
        float singleNumber = (ymax - ymin) / 5;
        for (int i = 0; i < 5; i++) {
            String dd = format.format(ymax - (i + 1) * singleNumber);
            canvas.drawText(dd, mLeftPanding / 2, singleY * (i + 1) + mTextHeight / 2
                    - mTextHeightOffset, mPaint);
        }
        canvas.drawText(format.format(ymax), mLeftPanding / 2, mTextHeight - mTextHeightOffset,
                mPaint);
        // canvas.save();
        // canvas.clipRect(mLeftPanding - mSingleWidth / 2, mHeight -
        // mTextHeight * 2, mCalculateWidth+mSingleWidth / 2, mHeight
        // - mTextHeight);
        // for (int i = 0; i < mItem.getData().length; i++) {
        // canvas.drawText(mItem.getData()[i][xIndex], mLeftPanding +
        // mSingleWidth * i
        // + mDrawOffset, mHeight - mTextHeight - mTextHeightOffset, mPaint);
        // }
        // canvas.restore();
        canvas.drawText(mItem.getData()[0][xIndex], mLeftPanding, mHeight - mTextHeight
                * mFieldRows - mTextHeightOffset, mPaint);
        canvas.drawText(mItem.getData()[mItem.getData().length - 1][xIndex],
                mWidth - mPaint.measureText(mItem.getData()[mItem.getData().length - 1][xIndex])
                        / 2, mHeight - mTextHeight * mFieldRows - mTextHeightOffset, mPaint);
        // int index = 0;
        // for (int i = 0; i < mItem.getFields().length; i++) {
        // if (mItem.getFields()[i].coord.equals("y")) {
        // mPaint.setColor(mColors[index]);
        // canvas.drawText(mItem.getFields()[i].name, mLeftPanding + (index + 1)
        // * (mWidth - mLeftPanding) / mItem.getFields().length, mHeight
        // - mTextHeightOffset, mPaint);
        // canvas.drawCircle(mLeftPanding + (index + 1) * (mCalculateWidth -
        // mLeftPanding)
        // / mItem.getFields().length -
        // mPaint.measureText(mItem.getFields()[i].name)
        // / 2 - 10, mHeight - mTextHeight / 2, 5, mPaint);
        // index++;
        // }
        // }
        mPaint.setTextAlign(Align.LEFT);
        int fieldindex = 0;
        int rowindex = 0;
        float heighttemp = (mFieldRows - 1) * mTextHeight;
        float offsetTemp = mLeftPanding + (mWidth - mLeftPanding - mFieldWidths.get(rowindex)) / 2;
        for (int i = 0; i < yFields.size(); i++) {
            mPaint.setColor(mShaderColors[i][1]);
            canvas.drawRect(offsetTemp, mHeight - heighttemp - mTextHeight / 2 - 2, offsetTemp
                    + mTextHeight, mHeight - heighttemp - mTextHeight / 2 + 2, mPaint);
            offsetTemp += (mTextHeight + 5);
            canvas.drawText(mItem.getFields()[yFields.get(i)].name, offsetTemp, mHeight
                    - heighttemp - mTextHeightOffset, mPaint);
            offsetTemp += mPaint.measureText(mItem.getFields()[yFields.get(i)].name) + 10;
            if (i == mFieldPaintIndexs.get(fieldindex) - 1) {
                rowindex++;
                offsetTemp = mLeftPanding + (mWidth - mLeftPanding - mFieldWidths.get(rowindex))
                        / 2;
                fieldindex++;
                heighttemp -= mTextHeight;
            }
        }

        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.record_bg_color));
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(mLeftPanding, i * (mHeight - mTextHeight * 2) / 5, mCalculateWidth, i
                    * (mHeight - mTextHeight * 2) / 5, mPaint);
        }
        for (int i = 0; i < mItem.getData().length; i++) {
            canvas.drawLine(mLeftPanding + mSingleWidth * i + mDrawOffset, 0, mLeftPanding
                    + mSingleWidth * i + mDrawOffset, mHeight - mTextHeight * 2, mPaint);
        }
        mPaint.setColor(Color.BLACK);
        // canvas.drawLine(mLeftPanding, 0, mLeftPanding, mHeight - mTextHeight
        // * 2 + 2, mPaint);
        if (ymin < 0) {
            canvas.drawLine(mLeftPanding - 5, getXianXingyH(0), mCalculateWidth, getXianXingyH(0),
                    mPaint);
        }
    }

    /**
     * 表格的框架
     *
     * @param canvas
     */
    private void paintBiaoGeLine(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.record_text_bg_color));
        canvas.drawRect(new Rect(0, 0, mWidth, (int) mTextHeight), mPaint);
        float y = 0f;
        for (int i = 0; i < mItem.getFields().length + 1; i++) {
            canvas.drawLine(i * mSingleWidth + mDrawOffset, 0, i * mSingleWidth + mDrawOffset,
                    mHeight, mPaint);
        }
        y = mTextHeight * 2 - 1;
        for (int i = 0; i < mItem.getData().length + 1; i++) {
            canvas.drawLine(0, y, mWidth, y, mPaint);
            y += mTextHeight;
        }
    }

    /**
     * 表格的数据
     *
     * @param canvas
     */
    private void paintBiaoGeData(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        float x = 0f;
        float y = 0f;
        y = mTextHeight;
        for (int i = 0; i < mItem.getFields().length; i++) {
            canvas.drawText(mItem.getFields()[i].name, x + mSingleWidth / 2 + mDrawOffset, y
                    - mTextHeight / 4 - mTextHeightOffset, mPaint);
            x += mSingleWidth;
        }
        y += mTextHeight;
        x = 0f;
        for (int i = 0; i < mItem.getData().length; i++) {
            for (int j = 0; j < mItem.getFields().length; j++) {
                canvas.drawText(mItem.getData()[i][j], x + mSingleWidth / 2 + mDrawOffset, y
                        - mTextHeight / 4 - mTextHeightOffset, mPaint);
                x += mSingleWidth;
            }
            x = 0f;
            y += mTextHeight;
        }
        // canvas.drawText(mItem.getForms()[0].title, x + mWidth / 2, y -
        // mTextHeight / 4
        // - mTextHeightOffset, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        calculateLayout();
        setMeasuredDimension(mWidth, mHeight);
    }

    private void calculateLayout() {
        if (mItem == null || mItem.getData().length == 0) {
            mWidth = mHeight = 0;
            return;
        }
        switch (type) {
            case RECORD_BIAOGE:
                mWidth = (int) (Globe.fullScreenWidth - mMargin);
                if (mWidth > mCalculateWidth) {
                    mWidth = mCalculateWidth;
                }
                mHeight = (int) (mTextHeight * (mItem.getData().length + 1));
                break;
            case RECORD_XIANXING:
                mHeight = (int) (mTextHeight * (mFieldRows + 1) + 250 * Globe.density / 1.5);
                mWidth = (int) (Globe.fullScreenWidth - mMargin);
                mSingleWidth = (mWidth - mLeftPanding) / (mItem.getData().length - 1);
                mCalculateWidth = mWidth;
                break;
            case RECORD_ZHUXING:
                mWidth = (int) (Globe.fullScreenWidth - mMargin);
                mHeight = (int) (mTextHeight * (mFieldRows + 1) + 250 * Globe.density / 1.5);
                mSaveOffset = mDrawOffset = mWidth - mLeftPanding - mCalculateWidth;
                break;
            case RECORD_SHUANGZHOU:
                mWidth = (int) (Globe.fullScreenWidth - mMargin);
                mHeight = (int) (mTextHeight * (mFieldRows + 1) + 250 * Globe.density / 1.5);
                mSaveOffset = mDrawOffset = mWidth - mLeftPanding - mRightPanding - mCalculateWidth;
                break;

            default:
                mWidth = mHeight = 0;
                break;
        }
    }

    private float getXianXingyH(float h) {
        return (float) ((ymax - h) / (ymax - ymin) * 250 * Globe.density / 1.5);
    }

    private float getXianXingzH(float h) {
        return (float) ((zmax - h) / (zmax - zmin) * 250 * Globe.density / 1.5);
    }

    private float getZhuXingyH(float h) {
        return (float) (h / (ymax - ymin) * 250 * Globe.density / 1.5);
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
                } else if (mDrawOffset < mWidth - mLeftPanding - mRightPanding - mCalculateWidth) {
                    mDrawOffset = mWidth - mLeftPanding - mRightPanding - mCalculateWidth;
                }
                postInvalidate();
                break;

            default:
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

}
