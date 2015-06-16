package com.lesso.data.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lesso.data.R;

/**
 * Created by meisl on 2015/6/9.
 */
public class SplashFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_splash,null);

        ImageView l = (ImageView) view.findViewById(R.id.splash_leftroat);
        ImageView r = (ImageView) view.findViewById(R.id.splash_rightroat);

        Animation anim_r = AnimationUtils.loadAnimation(getActivity(),R.anim.roat_login_r);
        anim_r.setInterpolator(new LinearInterpolator());
        r.startAnimation(anim_r);

        Animation anim_l = AnimationUtils.loadAnimation(getActivity(),R.anim.roat_login_l);
        anim_l.setInterpolator(new LinearInterpolator());
        l.startAnimation(anim_l);

        return view;
    }
}
