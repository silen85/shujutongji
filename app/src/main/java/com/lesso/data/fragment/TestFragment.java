package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;

/**
 * Created by meisl on 2015/6/26.
 */
public class TestFragment extends Fragment {

    private MainActivity activity;

    private DatePicker datepicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_datepicker, null);

        datepicker = (DatePicker) view.findViewById(R.id.datepicker);

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
