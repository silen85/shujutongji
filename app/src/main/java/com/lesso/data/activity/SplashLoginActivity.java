package com.lesso.data.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lesso.data.R;
import com.lesso.data.fragment.LoginFragment;
import com.lesso.data.fragment.SplashFragment;


public class SplashLoginActivity extends FragmentActivity {

    private FragmentManager fragmentManager;

    private Fragment splashFragment;
    private Fragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        fragmentManager =getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        splashFragment = new SplashFragment();
        fragmentTransaction.add(R.id.splash_login, splashFragment);
        fragmentTransaction.commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                loginFragment = new LoginFragment();

                fragmentTransaction.remove(splashFragment);

                fragmentTransaction.add(R.id.splash_login, loginFragment);
                fragmentTransaction.commit();


            }
        }, 6000);



    }

}
