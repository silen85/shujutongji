package com.lesso.data.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lesso.data.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by meisl on 2015/6/9.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.login)
    private Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, null);

        ButterKnife.inject(this, view);

        login.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            getActivity().finish();
        }

    }

}
