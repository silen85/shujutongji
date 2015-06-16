package com.lesso.data.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.lesso.data.R;
import com.lesso.data.activity.LockSetupActivity;

/**
 * Created by meisl on 2015/6/9.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, null);

        ImageView l = (ImageView) view.findViewById(R.id.splash_leftroat);
        ImageView r = (ImageView) view.findViewById(R.id.splash_rightroat);

        Animation anim_r = AnimationUtils.loadAnimation(getActivity(), R.anim.roat_login_r);
        anim_r.setInterpolator(new LinearInterpolator());
        r.startAnimation(anim_r);

        Animation anim_l = AnimationUtils.loadAnimation(getActivity(),R.anim.roat_login_l);
        anim_l.setInterpolator(new LinearInterpolator());
        l.startAnimation(anim_l);


        Button login = (Button) view.findViewById(R.id.login);

        login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {

            Intent intent = new Intent(getActivity(), LockSetupActivity.class);
            startActivity(intent);
            getActivity().finish();

        }

    }

}
