package com.lesso.data.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/24.
 */
public class AccessFragment extends Fragment {

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private LinearLayout data_view;
    private LineChart lineChart;

    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_access, null);

        initView();

        initData();

        return view;
    }

    private void initView() {

        data_view = (LinearLayout) view.findViewById(R.id.data_view);

        lineChart = (LineChart) data_view.findViewById(R.id.lineChart);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("暂无数据显示");
        lineChart.setHighlightEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.animateX(800, Easing.EasingOption.EaseInOutExpo);
        lineChart.animateY(800, Easing.EasingOption.EaseInOutExpo);
        lineChart.getLegend().setEnabled(false);

        lineChart.enableScroll();
        lineChart.canScrollHorizontally(1000);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setEnabled(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setStartAtZero(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(AccessFragment.this);
            }
        });

    }

    private void initData() {

        lineChart.getAxisLeft().setAxisMaxValue(2500);
        lineChart.getAxisLeft().setAxisMinValue(800);

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

        lineChart.setData(data);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

}
