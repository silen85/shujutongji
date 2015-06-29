package com.lesso.data.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.ui.Globe;
import com.lesso.data.ui.RecordItem;
import com.lesso.data.ui.RecordView;

/**
 * Created by meisl on 2015/6/26.
 */
public class TestFragment extends Fragment {

    private MainActivity activity;

    private RecordItem item;

    private LinearLayout mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, null);

        if (Globe.density == 0f) {
            Globe.density = getResources().getDisplayMetrics().density;
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Globe.fullScreenHeight = dm.heightPixels;
        Globe.fullScreenWidth = dm.widthPixels;

        mLinearLayout = (LinearLayout) view.findViewById(R.id.scrollview_linearlayout);

        RecordItem item = new RecordItem();

        RecordItem.Field[] fields = new RecordItem.Field[2];

        RecordItem.Field field = item.new Field();
        field.coord = "x";
        field.id = "F1";
        field.name = "x";
        field.type = "string";
        field.decimal = 0;
        fields[0] = field;

        field = item.new Field();
        field.coord = "y";
        field.id = "F2";
        field.name = "y";
        field.type = "float";
        field.decimal = 0;
        fields[1] = field;

         /*item.new Field();
        field.coord = "z";
        field.id = "F3";
        field.name = "z";
        field.type = "float";
        field.decimal = 0;
        fields[2] = field;

       item.new Field();
        field.coord = "y";
        field.id = "F4";
        field.name = "国产维生素D3";
        field.type = "float";
        field.decimal = 2;
        fields[3] = field;*/

        item.setFields(fields);


        String[][] data = new String[20][item.getFields().length];

        data[0] = new String[]{"2015-01-01", "10.00"};
        data[1] = new String[]{"2015-02-01", "20.00"};
        data[2] = new String[]{"2015-03-01", "30.00"};
        data[3] = new String[]{"2015-04-01", "40.00"};
        data[4] = new String[]{"2015-05-01", "50.00"};
        data[5] = new String[]{"2015-06-01", "10.00"};
        data[6] = new String[]{"2015-07-01", "20.00"};
        data[7] = new String[]{"2015-08-01", "80.00"};
        data[8] = new String[]{"2015-09-01", "90.00"};
        data[9] = new String[]{"2015-10-01", "100.00"};
        data[10] = new String[]{"2015-10-11", "20.00"};
        data[11] = new String[]{"2015-10-12", "20.00"};
        data[12] = new String[]{"2015-10-13", "30.00"};
        data[13] = new String[]{"2015-10-14", "40.00"};
        data[14] = new String[]{"2015-10-15", "50.00"};
        data[15] = new String[]{"2015-10-16", "10.00"};
        data[16] = new String[]{"2015-10-17", "20.00"};
        data[17] = new String[]{"2015-10-18", "80.00"};
        data[18] = new String[]{"2015-10-19", "90.00"};
        data[19] = new String[]{"2015-10-20", "100.00"};

        item.setData(data);


        RecordItem.ShowForm[] forms = new RecordItem.ShowForm[1];

        RecordItem.ShowForm form = item.new ShowForm();
        form.content = "";
        form.type = RecordView.RECORD_XIANXING;
        form.title = "";
        forms[0] = form;

        item.setForms(forms);

        TextView textview = new TextView(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.setMargins(10, 20, 10, 20);
        RecordView recordView;

        recordView = new RecordView(activity);
        recordView.setType(RecordView.RECORD_XIANXING);
        recordView.setmItem(item);
        mLinearLayout.addView(recordView, lp);

        if (!form.content.equals("")) {
            textview = new TextView(activity);
            textview.setText(form.content);
            textview.setTextColor(Color.BLACK);
            mLinearLayout.addView(textview, lp);
        }


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
