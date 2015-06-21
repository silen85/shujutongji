package com.lesso.data.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lesso.data.R;
import com.lesso.data.fragment.StroeFragment;

/**
 * Created by meisl on 2015/6/19.
 */
public class MainActivity extends FragmentActivity {

    private FragmentManager fragmentManager;

    private StroeFragment stroeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        stroeFragment = new StroeFragment();
        fragmentTransaction.add(R.id.main_content, stroeFragment);
        fragmentTransaction.commit();


    }
}
