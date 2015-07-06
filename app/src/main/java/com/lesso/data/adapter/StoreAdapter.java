package com.lesso.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lesso.data.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/25.
 */

public class StoreAdapter extends BaseAdapter {

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
                (context.getResources().getDrawable(processbar_stys[i % processbar_stys.length]));

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
