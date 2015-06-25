package com.lesso.data.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.adapter.StoreAdapter;
import com.lesso.data.ui.VerticalProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/25.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private int[] processbar_stys = {R.drawable.processbar_sty1, R.drawable.processbar_sty2, R.drawable.processbar_sty3,
            R.drawable.processbar_sty4, R.drawable.processbar_sty5, R.drawable.processbar_sty6,
            R.drawable.processbar_sty7, R.drawable.processbar_sty8};

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private LinearLayout fragment_access, fragment_sales, fragment_store, fragment_user;

    private List<Map<String, String>> salesList = new ArrayList<>();
    private List<Map<String, String>> storeList = new ArrayList<>();

    private LinearLayout data_view_access, data_view_sales, data_view_store, data_view_user;
    private LineChart chart_access, chart_user;
    private ListView listview_store;
    private StoreAdapter storeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_main, null);

        initView();

        initData();

        return view;
    }

    private void initView() {

        fragment_access = (LinearLayout) view.findViewById(R.id.fragment_access);
        fragment_sales = (LinearLayout) view.findViewById(R.id.fragment_sales);
        fragment_store = (LinearLayout) view.findViewById(R.id.fragment_store);
        fragment_user = (LinearLayout) view.findViewById(R.id.fragment_user);

        fragment_access.setOnClickListener(this);
        fragment_sales.setOnClickListener(this);
        fragment_store.setOnClickListener(this);
        fragment_user.setOnClickListener(this);

        initAccessView();

        initSalesView();

        initStoreView();

        initUserView();

    }

    private void initData() {

        initAccessData();


        initStoreData();

        initUserData();
    }

    private void initAccessView() {

        data_view_access = (LinearLayout) view.findViewById(R.id.data_view_access);
        chart_access = (LineChart) data_view_access.findViewById(R.id.chart_access);

        data_view_access.setOnClickListener(this);
        chart_access.setOnClickListener(this);

        chart_access.setDrawGridBackground(false);
        chart_access.setDescription("");
        chart_access.setNoDataTextDescription("暂无数据显示");
        chart_access.setHighlightEnabled(true);
        chart_access.setTouchEnabled(true);
        chart_access.setDragEnabled(false);
        chart_access.setScaleEnabled(false);
        chart_access.setPinchZoom(false);
        chart_access.animateX(800, Easing.EasingOption.EaseInOutExpo);
        chart_access.animateY(800, Easing.EasingOption.EaseInOutExpo);
        chart_access.getLegend().setEnabled(false);

        chart_access.enableScroll();
        chart_access.canScrollHorizontally(1000);

        YAxis leftAxis = chart_access.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setEnabled(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setStartAtZero(false);

        YAxis rightAxis = chart_access.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = chart_access.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    private void initSalesView() {

        initSalesData();

        data_view_sales = (LinearLayout) view.findViewById(R.id.data_view_sales);

        data_view_sales.setOnClickListener(this);

        for (int i = 0; i < salesList.size(); i++) {

            LinearLayout item = (LinearLayout) layoutInflater.inflate(R.layout.item_vprocessbar, null);

            TextView amount = (TextView) item.findViewById(R.id.colum2);
            VerticalProgressBar process = (VerticalProgressBar) item.findViewById(R.id.process);
            TextView date = (TextView) item.findViewById(R.id.colum1);

            process.setProgressDrawable
                    (activity.getResources().getDrawable(processbar_stys[i % processbar_stys.length]));

            int p = Integer.parseInt(salesList.get(i).get(process.getTag()));
            process.setProgress(p);

            amount.setText(salesList.get(i).get(amount.getTag()));
            date.setText(salesList.get(i).get(date.getTag()));

            data_view_sales.addView(item, i);

        }

    }

    private void initStoreView() {

        data_view_store = (LinearLayout) view.findViewById(R.id.data_view_store);
        listview_store = (ListView) data_view_store.findViewById(R.id.listview_store);

        data_view_store.setOnClickListener(this);
        listview_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activity.goToFragment(R.id.listview_store);
            }
        });

    }

    private void initUserView() {

        data_view_user = (LinearLayout) view.findViewById(R.id.data_view_user);
        chart_user = (LineChart) data_view_user.findViewById(R.id.chart_user);

        data_view_user.setOnClickListener(this);
        chart_user.setOnClickListener(this);

        chart_user.setDrawGridBackground(false);
        chart_user.setDescription("");
        chart_user.setNoDataTextDescription("暂无数据显示");
        chart_user.setHighlightEnabled(true);
        chart_user.setTouchEnabled(true);
        chart_user.setDragEnabled(false);
        chart_user.setScaleEnabled(false);
        chart_user.setPinchZoom(false);
        chart_user.animateX(800, Easing.EasingOption.EaseInOutExpo);
        chart_user.animateY(800, Easing.EasingOption.EaseInOutExpo);
        chart_user.getLegend().setEnabled(false);

        chart_user.enableScroll();
        chart_user.canScrollHorizontally(1000);

        YAxis leftAxis = chart_user.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setEnabled(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setStartAtZero(false);

        YAxis rightAxis = chart_user.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = chart_user.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    private void initAccessData() {

        chart_access.getAxisLeft().setAxisMaxValue(2500);
        chart_access.getAxisLeft().setAxisMinValue(800);

        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            xVals.add("8-" + (i + 1));
        }

        List<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(998, 0));
        yVals.add(new Entry(1278, 1));
        yVals.add(new Entry(1086, 2));
        yVals.add(new Entry(1688, 3));
        yVals.add(new Entry(2263, 4));
        yVals.add(new Entry(1722, 5));
        yVals.add(new Entry(1982, 6));
        yVals.add(new Entry(1126, 7));
        yVals.add(new Entry(998, 8));
        yVals.add(new Entry(1278, 9));
        yVals.add(new Entry(1086, 10));
        yVals.add(new Entry(1688, 11));
        yVals.add(new Entry(2263, 12));
        yVals.add(new Entry(1722, 13));
        yVals.add(new Entry(1982, 14));
        yVals.add(new Entry(1546, 15));
        yVals.add(new Entry(1996, 16));
        yVals.add(new Entry(1236, 17));
        yVals.add(new Entry(1982, 18));
        yVals.add(new Entry(1126, 19));
        yVals.add(new Entry(2254, 20));
        yVals.add(new Entry(1098, 21));
        yVals.add(new Entry(1446, 22));
        yVals.add(new Entry(1348, 23));
        yVals.add(new Entry(2003, 24));
        yVals.add(new Entry(1232, 25));
        yVals.add(new Entry(1762, 26));
        yVals.add(new Entry(1996, 27));

        int[] colorArr = new int[xVals.size()];
        for (int i = 0; i < xVals.size(); i++) {
            colorArr[i] = activity.getResources().getColor(colors[i % colors.length]);
        }

        LineDataSet set1 = new LineDataSet(yVals, "");
        set1.setColor(activity.getResources().getColor(R.color.REPORT_TABLE_C4));
        set1.setCircleColors(colorArr);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(10);
        set1.setDrawFilled(false);
        set1.setFillAlpha(0);
        set1.setFillColor(Color.WHITE);

        List<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);

        chart_access.setData(data);

    }

    private void initSalesData() {

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("amount", "23564");
        item1.put("process", "80");
        item1.put("date", "8-11");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("amount", "32221");
        item2.put("process", "36");
        item2.put("date", "8-12");

        Map<String, String> item3 = new HashMap<String, String>();
        item3.put("amount", "28530");
        item3.put("process", "44");
        item3.put("date", "8-13");

        Map<String, String> item4 = new HashMap<String, String>();
        item4.put("amount", "7865");
        item4.put("process", "99");
        item4.put("date", "8-14");

        Map<String, String> item5 = new HashMap<String, String>();
        item5.put("amount", "4534");
        item5.put("process", "66");
        item5.put("date", "8-15");

        Map<String, String> item6 = new HashMap<String, String>();
        item6.put("amount", "67687");
        item6.put("process", "55");
        item6.put("date", "8-16");

        Map<String, String> item7 = new HashMap<String, String>();
        item7.put("amount", "34343");
        item7.put("process", "66");
        item7.put("date", "8-17");

        Map<String, String> item8 = new HashMap<String, String>();
        item8.put("amount", "34343");
        item8.put("process", "20");
        item8.put("date", "8-18");

        Map<String, String> item9 = new HashMap<String, String>();
        item9.put("amount", "44656");
        item9.put("process", "77");
        item9.put("date", "8-19");

        Map<String, String> item10 = new HashMap<String, String>();
        item10.put("amount", "6565");
        item10.put("process", "89");
        item10.put("date", "8-20");

        salesList.clear();
        salesList.add(item1);
        salesList.add(item2);
        salesList.add(item3);
        salesList.add(item4);
        salesList.add(item5);
        salesList.add(item6);
        salesList.add(item7);
        salesList.add(item8);
        salesList.add(item9);
        salesList.add(item10);

    }

    private void initStoreData() {

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("product_name", "万众型灯管");
        item1.put("product_num", "9999 件");
        item1.put("product_percent", "80");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("product_name", "万众型");
        item2.put("product_num", "8888 件");
        item2.put("product_percent", "55");

        Map<String, String> item3 = new HashMap<String, String>();
        item3.put("product_name", "万t型");
        item3.put("product_num", "44 件");
        item3.put("product_percent", "44");

        Map<String, String> item4 = new HashMap<String, String>();
        item4.put("product_name", "万jj型");
        item4.put("product_num", "44 件");
        item4.put("product_percent", "70");

        Map<String, String> item5 = new HashMap<String, String>();
        item5.put("product_name", "万uy型");
        item5.put("product_num", "44 件");
        item5.put("product_percent", "99");

        Map<String, String> item6 = new HashMap<String, String>();
        item6.put("product_name", "万i型");
        item6.put("product_num", "44 件");
        item6.put("product_percent", "78");

        Map<String, String> item7 = new HashMap<String, String>();
        item7.put("product_name", "万yt型");
        item7.put("product_num", "44 件");
        item7.put("product_percent", "36");

        storeList.add(item1);
        storeList.add(item2);
        storeList.add(item3);
        storeList.add(item4);
        storeList.add(item5);
        storeList.add(item6);
        storeList.add(item7);

        storeAdapter = new StoreAdapter(activity, storeList, R.layout.item_processbar);

        listview_store.setAdapter(storeAdapter);

    }

    private void initUserData() {

        chart_user.getAxisLeft().setAxisMaxValue(2500);
        chart_user.getAxisLeft().setAxisMinValue(800);

        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            xVals.add("8-" + (i + 1));
        }

        List<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(998, 0));
        yVals.add(new Entry(1278, 1));
        yVals.add(new Entry(1086, 2));
        yVals.add(new Entry(1688, 3));
        yVals.add(new Entry(2263, 4));
        yVals.add(new Entry(1722, 5));
        yVals.add(new Entry(1982, 6));
        yVals.add(new Entry(1126, 7));
        yVals.add(new Entry(998, 8));
        yVals.add(new Entry(1278, 9));
        yVals.add(new Entry(1086, 10));
        yVals.add(new Entry(1688, 11));
        yVals.add(new Entry(2263, 12));
        yVals.add(new Entry(1722, 13));
        yVals.add(new Entry(1982, 14));
        yVals.add(new Entry(1546, 15));
        yVals.add(new Entry(1996, 16));
        yVals.add(new Entry(1236, 17));
        yVals.add(new Entry(1982, 18));
        yVals.add(new Entry(1126, 19));
        yVals.add(new Entry(2254, 20));
        yVals.add(new Entry(1098, 21));
        yVals.add(new Entry(1446, 22));
        yVals.add(new Entry(1348, 23));
        yVals.add(new Entry(2003, 24));
        yVals.add(new Entry(1232, 25));
        yVals.add(new Entry(1762, 26));
        yVals.add(new Entry(1996, 27));

        int[] colorArr = new int[xVals.size()];
        for (int i = 0; i < xVals.size(); i++) {
            colorArr[i] = activity.getResources().getColor(colors[i % colors.length]);
        }

        LineDataSet set1 = new LineDataSet(yVals, "");
        set1.setColor(activity.getResources().getColor(R.color.REPORT_TABLE_C4));
        set1.setCircleColors(colorArr);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(10);
        set1.setDrawFilled(false);
        set1.setFillAlpha(0);
        set1.setFillColor(Color.WHITE);

        List<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);

        chart_user.setData(data);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fragment_access:
            case R.id.data_view_access:
            case R.id.chart_access:

            case R.id.fragment_sales:
            case R.id.data_view_sales:

            case R.id.fragment_store:
            case R.id.data_view_store:

            case R.id.fragment_user:
            case R.id.data_view_user:
            case R.id.chart_user:
                activity.goToFragment(view.getId());
                break;
            default:
                break;
        }

    }
}
