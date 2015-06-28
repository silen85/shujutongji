package com.nina.test;

import android.app.Activity;
import android.os.Bundle;

public class MyChart_XYActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        XYChartView xyChart = (XYChartView)findViewById(R.id.xyChart);

        String[] coordinateX = new String[]{"1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"  };

        int[] line1 = new int[]{20, 84, 56, 70, 130, 125, 10, 40, 60, 45, 77, 195};
        int[] line2 = new int[]{10, 150, 20, 210, 15, 42, 67, 94, 12, 99, 110, 66};

        xyChart.setUnitX("月");
        xyChart.addLine("电冰箱", coordinateX, line1);
        xyChart.addLine("电脑", coordinateX, line2);
    }
}