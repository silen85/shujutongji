package com.lesso.data.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.lesso.data.R;
import com.lesso.data.common.Arith;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.XYLineView2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/24.
 */
public class UserFragment extends BaseGraphFragment {

    private String TAG = "com.lesso.data.fragment.UserDetailFragment";

    private int screenWidth, screenHeight;
    private int chart_user_width;

    private LinearLayout data_view, chart_xy_container, data_view_user;
    private XYLineView2 chart_xy;
    private PieChart mChart;

    private LinearLayout tab_user_new, tab_user_total, tab_user_supplier, tab_user_fivemetal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_user, null);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }

    protected void initView() {

        initTime();

        initBtnToogle();

        data_view = (LinearLayout) view.findViewById(R.id.data_view);
        chart_xy_container = (LinearLayout) view.findViewById(R.id.chart_xy_container);
        data_view_user = (LinearLayout) view.findViewById(R.id.data_view_user);
        chart_user_width = screenWidth;

        mChart = (PieChart) data_view.findViewById(R.id.pieChart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setNoDataText("");
        mChart.setDrawCenterText(false);
        mChart.highlightValues(null);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColorTransparent(false);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.animateY(800, Easing.EasingOption.EaseInOutExpo);
        mChart.animateX(800, Easing.EasingOption.EaseInOutExpo);
        mChart.getLegend().setEnabled(false);

        tab_user_new = (LinearLayout) view.findViewById(R.id.tab_user_new);
        tab_user_total = (LinearLayout) view.findViewById(R.id.tab_user_total);
        tab_user_supplier = (LinearLayout) view.findViewById(R.id.tab_user_supplier);
        tab_user_fivemetal = (LinearLayout) view.findViewById(R.id.tab_user_fivemetal);

        toogleTab(tabType);

        tab_user_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_fivemetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 4) {
                    tabType = 4;
                    toogleTab(tabType);
                    sendRequest(generateParam());

                }
            }
        });

    }

    public void toogleTab(int tabType) {
        super.toogleTab(tabType);
        tab_user_new.setSelected(tabType == 2 || tabType == 3 || tabType == 4 ? false : true);
        tab_user_total.setSelected(tabType == 2 ? true : false);
        tab_user_supplier.setSelected(tabType == 3 ? true : false);
        tab_user_fivemetal.setSelected(tabType == 4 ? true : false);

        if (tabType == 2 || tabType == 3 || tabType == 4) {
            time_chooser.setVisibility(View.GONE);
            chart_xy_container.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);
        } else {
            time_chooser.setVisibility(View.VISIBLE);
            chart_xy_container.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
        }

    }

    public void initData() {
        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {

        list.clear();
        if (data != null && data.size() > 0) {

            boolean flag = (tabType == 2 || tabType == 3 || tabType == 4 ? true : false);
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "", coloum3 = "";

                if (tabType == 2) {
                    coloum1 = data.get(i).get("CUSTOMTYPE");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "";
                    } catch (Exception e) {
                    }
                } else if (tabType == 3) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "";
                    } catch (Exception e) {
                    }
                } else if (tabType == 4) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "";
                    } catch (Exception e) {
                    }
                } else {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("CREATETIME");
                        coloum2 = data.get(i).get("COUN");
                    } else {
                        coloum1 = data.get(i).get("CREATETIME");
                        coloum2 = data.get(i).get("COUN");
                    }
                }

                item.put("colum1", coloum1);
                item.put("colum2", coloum2);

                if (flag) {
                    item.put("colum3", coloum3);
                }

                list.add(item);
            }
        }

        if (tabType == 2 || tabType == 3 || tabType == 4) {
            fillPieChartData(list);
        } else {
            if (chart_xy != null) {
                data_view_user.removeView(chart_xy);
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_user_width,
                    (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
            chart_xy = new XYLineView2(activity);
            chart_xy.setLayoutParams(lp);
            chart_xy.setScreenWidth(chart_user_width);
            chart_xy.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
            data_view_user.addView(chart_xy);

            fillXYLineData(list);
        }
    }

    private void fillXYLineData(List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {

            String[] fields = new String[list.size()];
            float[] dataArr = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(list.size() - 1 - i).get("colum1");
                String ydata = list.get(list.size() - 1 - i).get("colum2");

                fields[i] = xdata.substring(5);
                dataArr[i] = Float.parseFloat(ydata);

            }
            chart_xy.setData(dataArr);
            chart_xy.setField(fields);
        } else {
            chart_xy.setData(new float[]{});
            chart_xy.setField(new String[]{});
        }
        chart_xy.postInvalidate();
    }

    private void fillPieChartData(List<Map<String, String>> list) {

        ArrayList<Entry> yVals = new ArrayList<>(list.size());
        ArrayList<String> xVals = new ArrayList<>(list.size());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(i).get("colum1") + " " + list.get(i).get("colum2");
                String ydata = list.get(i).get("colum3");

                xVals.add(xdata);
                yVals.add(new Entry(Float.parseFloat(ydata), i));

            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(activity.getResources().getColor(R.color.REPORT_UI_C2));

        int[] colorArr = new int[xVals.size()];
        for (int i = 0; i < xVals.size(); i++) {
            colorArr[i] = activity.getResources().getColor(colors[i % colors.length]);
        }
        dataSet.setColors(colorArr);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(activity.getResources().getColor(R.color.REPORT_UI_C2));

        mChart.setData(data);
        mChart.invalidate();
    }

    protected Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        if (tabType == 2) {
            parems.put("type", "Tatol");
        } else if (tabType == 3) {
            parems.put("type", "supplier");
        } else if (tabType == 4) {
            parems.put("type", "store");
        } else {
            parems.put("type", "nuser");
            if (sBeginDate != null && !"".equals(sBeginDate.trim()))
                parems.put("start", sBeginDate);
            if (sEndDate != null && !"".equals(sEndDate.trim()))
                parems.put("end", sEndDate);

            if (timeType == 2) {
                // parems.put("type", "nuser");
            } else {
                // parems.put("type", "nuser");
            }
        }

        return parems;

    }

    protected void sendRequest(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_SROAT;
                message.sendToTarget();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString + throwable.getMessage());
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_NETWORK_ERR;
                message.sendToTarget();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.d(TAG, responseString);
                if (statusCode == 200) {
                    Message message = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", responseString);
                    message.what = HANDLER_DATA;
                    message.setData(bundle);
                    message.sendToTarget();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_EROAT;
                message.sendToTarget();
            }
        };
        asyncHttpResponseHandler.setCharset("GBK");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(activity, Constant.URL_REPORT_USER, requestParams, asyncHttpResponseHandler);

    }

    private final int HANDLER_DATA = 1;
    private final int HANDLER_SROAT = 2;
    private final int HANDLER_EROAT = 3;
    private final int HANDLER_NETWORK_ERR = 4;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_DATA:

                    String json = msg.getData().getString("json");
                    try {
                        Map result = Tools.json2Map(json);
                        List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");
                        fillData(viewtable);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + json);
                        fillData(null);
                        Toast.makeText(activity, getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    roatStart();
                    break;
                case HANDLER_EROAT:
                    //btn_toogle_fragment.clearAnimation();
                    break;
                case HANDLER_NETWORK_ERR:
                    fillData(null);
                    Toast.makeText(activity, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


}
