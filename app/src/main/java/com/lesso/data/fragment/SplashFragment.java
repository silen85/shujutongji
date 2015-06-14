package com.lesso.data.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/6/9.
 */
public class SplashFragment extends Fragment {

    private LinearLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = new LinearLayout(getActivity());

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);

        view.setBackgroundResource(R.mipmap.splash_bg);

        return view;
    }
}
