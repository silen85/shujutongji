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
 * Created by meisl on 2015/6/21.
 */
public class StoreDetailFragment extends ListFragment {

    private MainActivity activity;

    List<Map<String, String>> list = new ArrayList();
    private StoreDetailAdapter adapter;

    private View view;

    private Button btn_toogle_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store_detail, null);

        initView();

        return view;
    }

    private void initView() {

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(StoreDetailFragment.this);
            }
        });

    }

    private void initData(){

        LinearLayout header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);

        TextView a = ((TextView) header.findViewById(R.id.colum1));
        a.setText("编号");
        a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView b = ((TextView) header.findViewById(R.id.colum2));
        b.setText("名称");
        b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        b.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView c = ((TextView) header.findViewById(R.id.colum3));
        c.setText("出货量");
        c.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        c.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("colum1", "987654321");
        item1.put("colum2", "万众型");
        item1.put("colum3", "8888 件");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("colum1", "987654321");
        item2.put("colum2", "万众型");
        item2.put("colum3", "8888 件");

        Map<String, String> item3 = new HashMap<String, String>();
        item3.put("colum1", "987654321");
        item3.put("colum2", "万众型");
        item3.put("colum3", "8888 件");

        Map<String, String> item4 = new HashMap<String, String>();
        item4.put("colum1", "987654321");
        item4.put("colum2", "万众型");
        item4.put("colum3", "8888 件");

        Map<String, String> item5 = new HashMap<String, String>();
        item5.put("colum1", "987654321");
        item5.put("colum2", "万众型");
        item5.put("colum3", "8888 件");

        Map<String, String> item6 = new HashMap<String, String>();
        item6.put("colum1", "987654321");
        item6.put("colum2", "万众型");
        item6.put("colum3", "8888 件");

        Map<String, String> item7 = new HashMap<String, String>();
        item7.put("colum1", "987654321");
        item7.put("colum2", "万众型");
        item7.put("colum3", "8888 件");

        Map<String, String> item8 = new HashMap<String, String>();
        item8.put("colum1", "987654321");
        item8.put("colum2", "万众型");
        item8.put("colum3", "8888 件");

        Map<String, String> item9 = new HashMap<String, String>();
        item9.put("colum1", "987654321");
        item9.put("colum2", "万众型");
        item9.put("colum3", "8888 件");

        Map<String, String> item10 = new HashMap<String, String>();
        item10.put("colum1", "987654321");
        item10.put("colum2", "万众型");
        item10.put("colum3", "8888 件");

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

        adapter = new StoreDetailAdapter(activity, list, R.layout.item_grid);

        getListView().addHeaderView(header);
        setListAdapter(adapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }


    class StoreDetailAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public StoreDetailAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
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

            View listviewitem = null;

            if (view != null) {
                listviewitem = view;
            } else {
                listviewitem = layoutInflater.inflate(this.layoutlistid, null);
            }

            this.writeData(listviewitem, i);

            return listviewitem;
        }


        private void writeData(View listviewitem, int position) {

            Map<String, String> map = list.get(position);


            TextView product_id = (TextView) listviewitem.findViewById(R.id.colum1);
            TextView product_name = (TextView) listviewitem.findViewById(R.id.colum2);
            TextView store_out_num = (TextView) listviewitem.findViewById(R.id.colum3);

            String id = map.get("colum1");
            String name = map.get("colum2");
            String num = map.get("colum3");

            product_id.setText(id);
            product_name.setText(name);
            store_out_num.setText(num);

            if (position % 2 > 0) {
                product_id.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                product_name.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                store_out_num.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
            } else {
                product_id.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                product_name.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                store_out_num.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
            }

        }

    }


}
