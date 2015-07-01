package com.lesso.data.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lesso.data.R;
import com.lesso.data.fragment.AccessDetailFragment;
import com.lesso.data.fragment.AccessFragment;
import com.lesso.data.fragment.MainFragment1;
import com.lesso.data.fragment.MainFragment;
import com.lesso.data.fragment.SalesDetailFragment;
import com.lesso.data.fragment.SalesFragment;
import com.lesso.data.fragment.StoreDetailFragment;
import com.lesso.data.fragment.StoreFragment;
import com.lesso.data.fragment.TestFragment;
import com.lesso.data.fragment.UserDetailFragment;
import com.lesso.data.fragment.UserFragment;

import java.util.List;

/**
 * Created by meisl on 2015/6/19.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Button btn_back;

    private FragmentManager fragmentManager;

    private MainFragment mainFragment;
    private SalesFragment salesFragment;
    private SalesDetailFragment salesDetailFragment;
    private StoreFragment storeFragment;
    private StoreDetailFragment storeDetailFragment;
    private AccessFragment accessFragment;
    private AccessDetailFragment accessDetailFragment;
    private UserFragment userFragment;
    private UserDetailFragment userDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

        initFragment();

    }

    private void initFragment() {

        mainFragment = new MainFragment();
        accessFragment = new AccessFragment();
        accessDetailFragment = new AccessDetailFragment();
        salesFragment = new SalesFragment();
        salesDetailFragment = new SalesDetailFragment();
        storeFragment = new StoreFragment();
        storeDetailFragment = new StoreDetailFragment();
        userFragment = new UserFragment();
        userDetailFragment = new UserDetailFragment();

        Fragment testFragment = new TestFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.main_content, mainFragment);

        fragmentTransaction.commit();

    }

    public void toogleFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);

        if (fragment instanceof AccessFragment) {
            if (accessDetailFragment.isAdded()) {
                fragmentTransaction.show(accessDetailFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, accessDetailFragment);
            }
        } else if (fragment instanceof AccessDetailFragment) {
            if (accessFragment.isAdded()) {
                fragmentTransaction.show(accessFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, accessFragment);
            }
        } else if (fragment instanceof SalesFragment) {
            if (salesDetailFragment.isAdded()) {
                fragmentTransaction.show(salesDetailFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, salesDetailFragment);
            }
        } else if (fragment instanceof SalesDetailFragment) {
            if (salesFragment.isAdded()) {
                fragmentTransaction.show(salesFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, salesFragment);
            }
        } else if (fragment instanceof StoreFragment) {
            if (storeDetailFragment.isAdded()) {
                fragmentTransaction.show(storeDetailFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, storeDetailFragment);
            }
        } else if (fragment instanceof StoreDetailFragment) {
            if (storeFragment.isAdded()) {
                fragmentTransaction.show(storeFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, storeFragment);
            }
        } else if (fragment instanceof UserFragment) {
            if (userDetailFragment.isAdded()) {
                fragmentTransaction.show(userDetailFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, userDetailFragment);
            }
        } else if (fragment instanceof UserDetailFragment) {
            if (userFragment.isAdded()) {
                fragmentTransaction.show(userFragment);
            } else {
                fragmentTransaction.add(R.id.main_content, userFragment);
            }
        }

        fragmentTransaction.commit();

    }

    public void goToFragment(int fragmentId) {

        Fragment fragment;

        switch (fragmentId) {

            case R.id.fragment_access:
            case R.id.data_view_access:
            case R.id.chart_access:
                fragment = accessFragment;
                break;
            case R.id.fragment_sales:
            case R.id.data_view_sales:
            case R.id.chart_sales:
                fragment = salesFragment;
                break;
            case R.id.fragment_store:
            case R.id.data_view_store:
            case R.id.listview_store:
                fragment = storeFragment;
                break;
            case R.id.fragment_user:
            case R.id.data_view_user:
            case R.id.chart_user:
                fragment = userFragment;
                break;
            default:
                return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(mainFragment);

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.main_content, fragment);
        }

        fragmentTransaction.commit();
    }

    private void backFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragmentList = fragmentManager.getFragments();

        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isVisible() && !(fragment instanceof MainFragment1)) {
                fragmentTransaction.hide(fragment);
            }
        }

        if (mainFragment.isVisible()) {
            finish();
        } else {
            fragmentTransaction.show(mainFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                backFragment();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        backFragment();
    }
}
