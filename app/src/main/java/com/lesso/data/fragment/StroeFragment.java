package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
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
public class StroeFragment extends ListFragment {

    private MainActivity activity;

    private StoreAdapter adapter;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> item1 = new HashMap<String, String>();
        item1.put("product_name", "万众型灯管");
        item1.put("product_num", "9999 件");
        item1.put("product_percent", "80");

        Map<String, String> item2 = new HashMap<String, String>();
        item2.put("product_name", "万众型");
        item2.put("product_num", "8888 件");
        item2.put("product_percent", "55");

        Map<String, String> item3 = new HashMap<String, String>();
        item3.put("product_name", "万型");
        item3.put("product_num", "44 件");
        item3.put("product_percent", "55");

        Map<String, String> item4 = new HashMap<String, String>();
        item4.put("product_name", "万型");
        item4.put("product_num", "44 件");
        item4.put("product_percent", "55");

        Map<String, String> item5 = new HashMap<String, String>();
        item5.put("product_name", "万型");
        item5.put("product_num", "44 件");
        item5.put("product_percent", "55");

        Map<String, String> item6 = new HashMap<String, String>();
        item6.put("product_name", "万型");
        item6.put("product_num", "44 件");
        item6.put("product_percent", "55");

        Map<String, String> item7 = new HashMap<String, String>();
        item7.put("product_name", "万型");
        item7.put("product_num", "44 件");
        item7.put("product_percent", "55");

        Map<String, String> item8 = new HashMap<String, String>();
        item8.put("product_name", "万型");
        item8.put("product_num", "44 件");
        item8.put("product_percent", "55");

        Map<String, String> item9 = new HashMap<String, String>();
        item9.put("product_name", "万型");
        item9.put("product_num", "44 件");
        item9.put("product_percent", "55");

        Map<String, String> item10 = new HashMap<String, String>();
        item10.put("product_name", "万型");
        item10.put("product_num", "44 件");
        item10.put("product_percent", "55");

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

        adapter = new StoreAdapter(activity, list, R.layout.processbar_item);

        setListAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, null);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    class StoreAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public StoreAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
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


            TextView product_name = (TextView) listviewitem.findViewById(R.id.product_name);
            TextView product_num = (TextView) listviewitem.findViewById(R.id.product_num);

            ProgressBar product_percent = (ProgressBar) listviewitem.findViewById(R.id.product_percent);

            String name = map.get("product_name");
            String num = map.get("product_num");
            String percent = map.get("product_percent");

            product_name.setText(name);
            product_num.setText(num);

            product_percent.setMax(100);
            product_percent.setProgress(80);


        }


    }

}
