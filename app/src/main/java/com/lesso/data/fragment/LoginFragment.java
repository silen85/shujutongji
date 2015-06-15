package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lesso.data.R;
import com.lesso.data.activity.LockActivity;
import com.lesso.data.activity.LockSetupActivity;
import com.lesso.data.activity.SplashLoginActivity;

/**
 * Created by meisl on 2015/6/9.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, null);

        Button login = (Button) view.findViewById(R.id.login);

        login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {

            SharedPreferences preferences = getActivity().getSharedPreferences(SplashLoginActivity.LOCK, Activity.MODE_PRIVATE);

            String lockPattenString = preferences.getString(SplashLoginActivity.LOCK_KEY, null);

            if (lockPattenString != null) {
                Intent intent = new Intent(getActivity(), LockActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getActivity(), LockSetupActivity.class);
                startActivity(intent);
            }
            getActivity().finish();
        }

    }

}
