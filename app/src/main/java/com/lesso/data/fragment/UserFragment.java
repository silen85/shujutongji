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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/24.
 */
public class UserFragment extends Fragment {

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private LinearLayout data_view;
    private PieChart mChart;

    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_user, null);

        initView();

        initData();

        return view;
    }

    private void initView() {

        data_view = (LinearLayout) view.findViewById(R.id.data_view);

        mChart = (PieChart) data_view.findViewById(R.id.pieChart);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
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

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(UserFragment.this);
            }
        });

    }

    private void initData() {

        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry((864f / (float) (864 + 280 + 560)), 0));
        yVals.add(new Entry((280f / (float) (864 + 280 + 560)), 1));
        yVals.add(new Entry((560f / (float) (864 + 280 + 560)), 2));


        String[] mParties = new String[]{"五金门店 864", "供应商 280", "经销商 560"};
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add(mParties[0]);
        xVals.add(mParties[1]);
        xVals.add(mParties[2]);

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

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

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

}
