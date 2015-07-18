package com.lesso.data.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.UpdateManager;
import com.lesso.data.cusinterface.FragmentListener;
import com.lesso.data.fragment.LoginFragment;
import com.lesso.data.fragment.SplashFragment;


public class SplashLoginActivity extends FragmentActivity {

    private LessoApplication.LoginUser loginUser;

    private FragmentManager fragmentManager;

    private Fragment splashFragment;
    private Fragment loginFragment;

    private FragmentListener fragmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_login);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        splashFragment = new SplashFragment();
        fragmentTransaction.add(R.id.splash_login, splashFragment);
        fragmentTransaction.commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (loginUser == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    loginFragment = new LoginFragment();

                    fragmentTransaction.remove(splashFragment);
                    fragmentTransaction.add(R.id.splash_login, loginFragment);

                    fragmentTransaction.commit();

                    try {
                        UpdateManager mUpdateManager = new UpdateManager(SplashLoginActivity.this);
                        mUpdateManager.sendUpdateRequest();
                    } catch (Exception e) {
                    }

                } else {

                    String Scratchable_PWD = loginUser.getScratchable_PWD();

                    if (Scratchable_PWD != null && !"".equals(Scratchable_PWD.trim())) {
                        Intent intent = new Intent(SplashLoginActivity.this, LockActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashLoginActivity.this, LockSetupActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }

                /*SharedPreferences preferences = getSharedPreferences(SplashLoginActivity.LOCK, Activity.MODE_PRIVATE);

                String lockPattenString = preferences.getString(SplashLoginActivity.LOCK_KEY, null);

                if (lockPattenString != null) {
                    Intent intent = new Intent(SplashLoginActivity.this, LockActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    loginFragment = new LoginFragment();

                    fragmentTransaction.remove(splashFragment);
                    fragmentTransaction.add(R.id.splash_login, loginFragment);

                    fragmentTransaction.commit();
                }*/
            }
        }, 1500);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (fragmentListener != null) {
            return fragmentListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (fragmentListener != null) {
            fragmentListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    @Override
    public void onBackPressed() {
        sendBroadcast(new Intent(Constant.FINISH_ACTION));
        super.onBackPressed();
    }
}
