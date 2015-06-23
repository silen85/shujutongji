package com.lesso.data.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lesso.data.R;
import com.lesso.data.fragment.SalesDetailFragment;
import com.lesso.data.fragment.SalesFragment;
import com.lesso.data.fragment.StoreDetailFragment;
import com.lesso.data.fragment.StoreFragment;

/**
 * Created by meisl on 2015/6/19.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;

    private SalesFragment salesFragment;
    private SalesDetailFragment salesDetailFragment;
    private StoreFragment storeFragment;
    private StoreDetailFragment storeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initFragment();

    }

    private void initFragment() {

        salesFragment = new SalesFragment();
        salesDetailFragment = new SalesDetailFragment();
        storeFragment = new StoreFragment();
        storeDetailFragment = new StoreDetailFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.main_content, salesFragment);

        fragmentTransaction.commit();

    }

    public void toogleFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        if (fragment instanceof StoreFragment) {
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
        }else if (fragment instanceof SalesFragment) {
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
        }

        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_back:

                break;
            default:
                break;
        }

    }
}
