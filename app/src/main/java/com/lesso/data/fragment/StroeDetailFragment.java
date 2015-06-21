package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;

/**
 * Created by meisl on 2015/6/21.
 */
public class StroeDetailFragment extends ListFragment {

    private MainActivity activity;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater.inflate(R.layout.fragment_store,null);




        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
    }


}
