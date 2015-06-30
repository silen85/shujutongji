package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.ui.BarView;

/**
 * Created by meisl on 2015/6/26.
 */
public class TestFragment extends Fragment {

    private MainActivity activity;

    private LinearLayout mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, null);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.scrollview_linearlayout);

        String[] fields = new String[]{"08-11","08-12","08-13","08-14","08-15","08-16","08-17","08-18","08-19","08-20","08-21","08-22","08-23","08-24","08-25","08-26","08-27","08-28","08-29"};
        float[] data = new float[]{998, 1278, 1086, 1688, 2263, 1722, 1982, 1126, 1055, 1488, 1699, 1866, 1200, 1326, 1855, 2500, 2400, 2000, 568};


        /*XYLineView dataGraph = new XYLineView(activity);
        dataGraph.setData(data);
        dataGraph.setField(fields);
        mLinearLayout.addView(dataGraph);*/

        BarView barView = new BarView(activity);
        barView.setData(data);
        barView.setField(fields);
        mLinearLayout.addView(barView);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

}
