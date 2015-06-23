package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
public class StoreFragment extends ListFragment {

    private MainActivity activity;

    private StoreAdapter adapter;

    private View view;

    private Button btn_toogle_fragment;

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

        Map<String, String> item8 = new HashMap<String, String>();
        item8.put("product_name", "万tr型");
        item8.put("product_num", "44 件");
        item8.put("product_percent", "66");

        Map<String, String> item9 = new HashMap<String, String>();
        item9.put("product_name", "万we型");
        item9.put("product_num", "44 件");
        item9.put("product_percent", "45");

        Map<String, String> item10 = new HashMap<String, String>();
        item10.put("product_name", "万ds型");
        item10.put("product_num", "44 件");
        item10.put("product_percent", "77");

        Map<String, String> item11 = new HashMap<String, String>();
        item11.put("product_name", "万ds型");
        item11.put("product_num", "44 件");
        item11.put("product_percent", "94");

        Map<String, String> item12 = new HashMap<String, String>();
        item12.put("product_name", "万ds型");
        item12.put("product_num", "44 件");
        item12.put("product_percent", "12");

        Map<String, String> item13 = new HashMap<String, String>();
        item13.put("product_name", "万ds型");
        item13.put("product_num", "44 件");
        item13.put("product_percent", "43");

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

        adapter = new StoreAdapter(activity, list, R.layout.item_processbar);

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, null);

        initView();

        return view;
    }

    private void initView() {

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(StoreFragment.this);
            }
        });

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*RelativeLayout time_chooser = (RelativeLayout) view.findViewById(R.id.time_chooser);

        int hh = ((FrameLayout.LayoutParams) time_chooser.getLayoutParams()).height;

        int h = time_chooser.getHeight();

        ((FrameLayout.LayoutParams) getListView().getLayoutParams()).setMargins(0, h + 10, 0, 0);*/

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    class StoreAdapter extends BaseAdapter {

        private int[] processbar_stys = {R.drawable.processbar_sty1, R.drawable.processbar_sty2, R.drawable.processbar_sty3,
                R.drawable.processbar_sty4, R.drawable.processbar_sty5, R.drawable.processbar_sty6,
                R.drawable.processbar_sty7, R.drawable.processbar_sty8};

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public StoreAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
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

            ViewHolder viewHolder;
            if (view == null) {
                view = layoutInflater.inflate(this.layoutlistid, null);

                viewHolder = new ViewHolder();
                viewHolder.product_name = (TextView) view.findViewById(R.id.product_name);
                viewHolder.product_num = (TextView) view.findViewById(R.id.product_num);
                viewHolder.product_percent = (ProgressBar) view.findViewById(R.id.product_percent);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.product_percent.setProgressDrawable
                    (getResources().getDrawable(processbar_stys[i % processbar_stys.length]));

            this.writeData(viewHolder, i);

            return view;
        }


        private void writeData(ViewHolder viewHolder, int position) {

            Map<String, String> itemData = (Map) getItem(position);

            viewHolder.product_name.setText(itemData.get("product_name"));
            viewHolder.product_num.setText(itemData.get("product_num"));
            viewHolder.product_percent.setProgress(Integer.parseInt(itemData.get("product_percent")));

        }

        class ViewHolder {

            TextView product_name;
            TextView product_num;
            ProgressBar product_percent;

        }


    }

}
