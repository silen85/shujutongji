package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.ui.VerticalProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/23.
 */
public class SalesFragment extends Fragment {

    private int[] processbar_stys = {R.drawable.processbar_sty1, R.drawable.processbar_sty2, R.drawable.processbar_sty3,
            R.drawable.processbar_sty4, R.drawable.processbar_sty5, R.drawable.processbar_sty6,
            R.drawable.processbar_sty7, R.drawable.processbar_sty8};

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private LinearLayout data_listview;
    private Button btn_toogle_fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        list.clear();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);
        list.add(item10);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_sales, null);

        initView();

        initData();

        return view;
    }

    private void initView() {

        data_listview = (LinearLayout) view.findViewById(R.id.data_listview);

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(SalesFragment.this);
            }
        });

    }

    private void initData() {
        fillData();
    }

    private void fillData() {

        data_listview.removeAllViews();

        for (int i = 0; i < list.size(); i++) {

            LinearLayout item = (LinearLayout) layoutInflater.inflate(R.layout.item_vprocessbar, null);

            TextView amount = (TextView) item.findViewById(R.id.amount);
            VerticalProgressBar process = (VerticalProgressBar) item.findViewById(R.id.process);
            TextView date = (TextView) item.findViewById(R.id.date);

            process.setProgressDrawable
                    (activity.getResources().getDrawable(processbar_stys[i % processbar_stys.length]));

            int p = Integer.parseInt(list.get(i).get(process.getTag()));
            process.setProgress(p);

            amount.setText(list.get(i).get(amount.getTag()));
            date.setText(list.get(i).get(date.getTag()));

            data_listview.addView(item);

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

}
