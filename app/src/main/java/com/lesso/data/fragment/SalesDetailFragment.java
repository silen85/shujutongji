package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/23.
 */
public class SalesDetailFragment extends ListFragment {

    private MainActivity activity;

    private SalesDetailAdapter adapter;

    private View view;

    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sales_detail, null);

        initView();

        return view;
    }

    private void initView() {

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(SalesDetailFragment.this);
            }
        });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);

        TextView a = ((TextView) header.findViewById(R.id.date));
        a.setText("日  期");
        a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView b = ((TextView) header.findViewById(R.id.amount));
        b.setText("销 售 额");
        b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        b.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("date", "2015-08-15");
        item1.put("amount", "895654");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("date", "2015-08-16");
        item2.put("amount", "8953454");

        Map<String, String> item3 = new HashMap<String, String>();
        item3.put("date", "2015-08-17");
        item3.put("amount", "894454");

        Map<String, String> item4 = new HashMap<String, String>();
        item4.put("date", "2015-08-18");
        item4.put("amount", "895354");

        Map<String, String> item5 = new HashMap<String, String>();
        item5.put("date", "2015-08-19");
        item5.put("amount", "895234");

        Map<String, String> item6 = new HashMap<String, String>();
        item6.put("date", "2015-08-20");
        item6.put("amount", "895674");

        Map<String, String> item7 = new HashMap<String, String>();
        item7.put("date", "2015-08-21");
        item7.put("amount", "895600");

        Map<String, String> item8 = new HashMap<String, String>();
        item8.put("date", "2015-08-22");
        item8.put("amount", "896754");

        Map<String, String> item9 = new HashMap<String, String>();
        item9.put("date", "2015-08-23");
        item9.put("amount", "895654");

        Map<String, String> item10 = new HashMap<String, String>();
        item10.put("date", "2015-08-24");
        item10.put("amount", "89094");

        Map<String, String> item11 = new HashMap<String, String>();
        item11.put("date", "2015-08-25");
        item11.put("amount", "83494");

        Map<String, String> item12 = new HashMap<String, String>();
        item12.put("date", "2015-08-26");
        item12.put("amount", "89089");

        Map<String, String> item13 = new HashMap<String, String>();
        item13.put("date", "2015-08-27");
        item13.put("amount", "89794");

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
        list.add(item11);
        list.add(item12);
        list.add(item13);

        adapter = new SalesDetailAdapter(activity, list, R.layout.item_grid1);

        getListView().addHeaderView(header);
        setListAdapter(adapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }


    class SalesDetailAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public SalesDetailAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(this.context);
            this.list = listobject;
            this.layoutlistid = listcontextid;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = layoutInflater.inflate(this.layoutlistid, null);
            }

            this.writeData(view, i);

            return view;
        }


        private void writeData(View listviewitem, int position) {

            Map<String, String> map = (Map<String, String>) getItem(position);

            TextView date = (TextView) listviewitem.findViewById(R.id.date);
            TextView amount = (TextView) listviewitem.findViewById(R.id.amount);

            date.setText(map.get(date.getTag()));
            amount.setText(map.get(amount.getTag()));

            if (position % 2 > 0) {
                date.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                amount.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
            } else {
                date.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                amount.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
            }

        }

    }

}
