package com.lesso.data.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.fragment.AccessDetailFragment;
import com.lesso.data.fragment.AccessFragment;
import com.lesso.data.fragment.MainFragment;
import com.lesso.data.fragment.SalesDetailFragment;
import com.lesso.data.fragment.SalesFragment;
import com.lesso.data.fragment.StoreDetailFragment;
import com.lesso.data.fragment.StoreFragment;
import com.lesso.data.fragment.UserDetailFragment;
import com.lesso.data.fragment.UserFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by meisl on 2015/6/19.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private long mExitTime;

    public boolean AUTHORITY_SALES_AMOUNT = false;

    private LessoApplication.LoginUser loginUser;

    private Button btn_back;
    private RelativeLayout main_title;
    private TextView main_title_txt;
    private ImageView main_setting;

    private String sBeginDate, sEndDate;

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

        loginUser = ((LessoApplication) getApplication()).getLoginUser();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_title_txt = (TextView) findViewById(R.id.main_title_txt);
        main_setting = (ImageView) findViewById(R.id.main_setting);
        main_setting.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);

        sBeginDate = Constant.DATE_FORMAT_1.format(calendar.getTime());
        sEndDate = Constant.DATE_FORMAT_1.format(new Date());


        fragmentManager = getSupportFragmentManager();

        initFragment();

    }

    private void toogleTitle(String title, boolean flag) {

        main_title_txt.setText(title);
        if (flag) {
            main_title.setBackgroundColor(getResources().getColor(R.color.REPORT_UI_C1));
            main_setting.setVisibility(View.VISIBLE);
        } else {
            main_title.setBackgroundColor(getResources().getColor(R.color.REPORT_UI_C2));
            main_setting.setVisibility(View.GONE);
        }

    }

    private void initFragment() {

        mainFragment = new MainFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.main_content, mainFragment);

        fragmentTransaction.commit();

    }

    public void toogleFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);

        if (fragment instanceof AccessFragment) {
            if (accessDetailFragment != null && accessDetailFragment.isAdded()) {
                accessDetailFragment.setsBeginDate(((AccessFragment) fragment).getsBeginDate());
                accessDetailFragment.setsEndDate(((AccessFragment) fragment).getsEndDate());
                if (accessDetailFragment.getTabType() != ((AccessFragment) fragment).getTabType()) {
                    accessDetailFragment.toogleTab(((AccessFragment) fragment).getTabType());
                }
                accessDetailFragment.initTimeChooserVal();
                accessDetailFragment.initData();
                fragmentTransaction.show(accessDetailFragment);
            } else {
                accessDetailFragment = new AccessDetailFragment();
                accessDetailFragment.setsBeginDate(((AccessFragment) fragment).getsBeginDate());
                accessDetailFragment.setsEndDate(((AccessFragment) fragment).getsEndDate());
                accessDetailFragment.setTabType(((AccessFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, accessDetailFragment);
            }
        } else if (fragment instanceof AccessDetailFragment) {
            if (accessFragment != null && accessFragment.isAdded()) {
                accessFragment.setsBeginDate(((AccessDetailFragment) fragment).getsBeginDate());
                accessFragment.setsEndDate(((AccessDetailFragment) fragment).getsEndDate());
                if (accessFragment.getTabType() != ((AccessDetailFragment) fragment).getTabType()) {
                    accessFragment.toogleTab(((AccessDetailFragment) fragment).getTabType());
                }
                accessFragment.initTimeChooserVal();
                accessFragment.initData();
                fragmentTransaction.show(accessFragment);
            } else {
                accessFragment = new AccessFragment();
                accessFragment.setsBeginDate(((AccessDetailFragment) fragment).getsBeginDate());
                accessFragment.setsEndDate(((AccessDetailFragment) fragment).getsEndDate());
                accessFragment.setTabType(((AccessDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, accessFragment);
            }
        } else if (fragment instanceof SalesFragment) {
            if (salesDetailFragment != null && salesDetailFragment.isAdded()) {
                salesDetailFragment.setsBeginDate(((SalesFragment) fragment).getsBeginDate());
                salesDetailFragment.setsEndDate(((SalesFragment) fragment).getsEndDate());
                if (salesDetailFragment.getTabType() != ((SalesFragment) fragment).getTabType()) {
                    salesDetailFragment.toogleTab(((SalesFragment) fragment).getTabType());
                }
                salesDetailFragment.initTimeChooserVal();
                salesDetailFragment.initData();
                fragmentTransaction.show(salesDetailFragment);
            } else {
                salesDetailFragment = new SalesDetailFragment();
                salesDetailFragment.setsBeginDate(((SalesFragment) fragment).getsBeginDate());
                salesDetailFragment.setsEndDate(((SalesFragment) fragment).getsEndDate());
                salesDetailFragment.setTabType(((SalesFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, salesDetailFragment);
            }
        } else if (fragment instanceof SalesDetailFragment) {
            if (salesFragment != null && salesFragment.isAdded()) {
                salesFragment.setsBeginDate(((SalesDetailFragment) fragment).getsBeginDate());
                salesFragment.setsEndDate(((SalesDetailFragment) fragment).getsEndDate());
                if (salesFragment.getTabType() != ((SalesDetailFragment) fragment).getTabType()) {
                    salesFragment.toogleTab(((SalesDetailFragment) fragment).getTabType());
                }
                salesFragment.initTimeChooserVal();
                salesFragment.initData();
                fragmentTransaction.show(salesFragment);
            } else {
                salesFragment = new SalesFragment();
                salesFragment.setsBeginDate(((SalesDetailFragment) fragment).getsBeginDate());
                salesFragment.setsEndDate(((SalesDetailFragment) fragment).getsEndDate());
                salesFragment.setTabType(((SalesDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, salesFragment);
            }
        } else if (fragment instanceof StoreFragment) {
            if (storeDetailFragment != null && storeDetailFragment.isAdded()) {
                storeDetailFragment.setsBeginDate(((StoreFragment) fragment).getsBeginDate());
                storeDetailFragment.setsEndDate(((StoreFragment) fragment).getsEndDate());
                if (storeDetailFragment.getTabType() != ((StoreFragment) fragment).getTabType()) {
                    storeDetailFragment.toogleTab(((StoreFragment) fragment).getTabType());
                }
                storeDetailFragment.initTimeChooserVal();
                storeDetailFragment.initData();
                fragmentTransaction.show(storeDetailFragment);
            } else {
                storeDetailFragment = new StoreDetailFragment();
                storeDetailFragment.setsBeginDate(((StoreFragment) fragment).getsBeginDate());
                storeDetailFragment.setsEndDate(((StoreFragment) fragment).getsEndDate());
                storeDetailFragment.setTabType(((StoreFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, storeDetailFragment);
            }
        } else if (fragment instanceof StoreDetailFragment) {
            if (storeFragment != null && storeFragment.isAdded()) {
                storeFragment.setsBeginDate(((StoreDetailFragment) fragment).getsBeginDate());
                storeFragment.setsEndDate(((StoreDetailFragment) fragment).getsEndDate());
                if (storeFragment.getTabType() != ((StoreDetailFragment) fragment).getTabType()) {
                    storeFragment.toogleTab(((StoreDetailFragment) fragment).getTabType());
                }
                storeFragment.initTimeChooserVal();
                storeFragment.initData();
                fragmentTransaction.show(storeFragment);
            } else {
                storeFragment = new StoreFragment();
                storeFragment.setsBeginDate(((StoreDetailFragment) fragment).getsBeginDate());
                storeFragment.setsEndDate(((StoreDetailFragment) fragment).getsEndDate());
                storeFragment.setTabType(((StoreDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, storeFragment);
            }
        } else if (fragment instanceof UserFragment) {
            if (userDetailFragment != null && userDetailFragment.isAdded()) {
                userDetailFragment.setsBeginDate(((UserFragment) fragment).getsBeginDate());
                userDetailFragment.setsEndDate(((UserFragment) fragment).getsEndDate());
                if (userDetailFragment.getTabType() != ((UserFragment) fragment).getTabType()) {
                    userDetailFragment.toogleTab(((UserFragment) fragment).getTabType());
                }
                userDetailFragment.initTimeChooserVal();
                userDetailFragment.initData();
                fragmentTransaction.show(userDetailFragment);
            } else {
                userDetailFragment = new UserDetailFragment();
                userDetailFragment.setsBeginDate(((UserFragment) fragment).getsBeginDate());
                userDetailFragment.setsEndDate(((UserFragment) fragment).getsEndDate());
                userDetailFragment.setTabType(((UserFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, userDetailFragment);
            }
        } else if (fragment instanceof UserDetailFragment) {
            if (userFragment != null && userFragment.isAdded()) {
                userFragment.setsBeginDate(((UserDetailFragment) fragment).getsBeginDate());
                userFragment.setsEndDate(((UserDetailFragment) fragment).getsEndDate());
                if (userFragment.getTabType() != ((UserDetailFragment) fragment).getTabType()) {
                    userFragment.toogleTab(((UserDetailFragment) fragment).getTabType());
                }
                userFragment.initTimeChooserVal();
                userFragment.initData();
                fragmentTransaction.show(userFragment);
            } else {
                userFragment = new UserFragment();
                userFragment.setsBeginDate(((UserDetailFragment) fragment).getsBeginDate());
                userFragment.setsEndDate(((UserDetailFragment) fragment).getsEndDate());
                userFragment.setTabType(((UserDetailFragment) fragment).getTabType());
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
                accessFragment = new AccessFragment();
                accessFragment.setsBeginDate(sBeginDate);
                accessFragment.setsEndDate(sEndDate);
                fragment = accessFragment;
                toogleTitle(getString(R.string.main_access), false);
                break;
            case R.id.fragment_sales:
            case R.id.data_view_sales:
                salesFragment = new SalesFragment();
                salesFragment.setsBeginDate(sBeginDate);
                salesFragment.setsEndDate(sEndDate);
                fragment = salesFragment;
                toogleTitle(getString(R.string.main_sales), false);
                break;
            case R.id.fragment_store:
            case R.id.data_view_store:
            case R.id.listview_store:
                storeFragment = new StoreFragment();
                storeFragment.setsBeginDate(sBeginDate);
                storeFragment.setsEndDate(sEndDate);
                fragment = storeFragment;
                toogleTitle(getString(R.string.main_store), false);
                break;
            case R.id.fragment_user:
            case R.id.data_view_user:
                userFragment = new UserFragment();
                userFragment.setsBeginDate(sBeginDate);
                userFragment.setsEndDate(sEndDate);
                fragment = userFragment;
                toogleTitle(getString(R.string.main_user), false);
                break;
            default:
                return;
        }

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            List<Fragment> fragmentList = fragmentManager.getFragments();
            Iterator iterator = fragmentList.iterator();
            while (iterator.hasNext()) {
                Fragment _fragment = (Fragment) iterator.next();
                if (_fragment != null && !(_fragment instanceof MainFragment)) {
                    fragmentTransaction.remove(_fragment);
                }
            }

            fragmentTransaction.hide(mainFragment);
            fragmentTransaction.add(R.id.main_content, fragment);

            fragmentTransaction.commit();

        }
    }

    private void backFragment() {

        if (mainFragment.isVisible()) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, getString(R.string.quit_press_again), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            List<Fragment> fragmentList = fragmentManager.getFragments();
            Iterator iterator = fragmentList.iterator();
            while (iterator.hasNext()) {
                Fragment _fragment = (Fragment) iterator.next();
                if (_fragment != null && !(_fragment instanceof MainFragment)) {
                    fragmentTransaction.remove(_fragment);
                }
            }

            fragmentTransaction.show(mainFragment);
            fragmentTransaction.commit();
            toogleTitle(getString(R.string.app_title_name1), true);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                backFragment();
                break;
            case R.id.main_setting:
                startActivity(new Intent(this, SetupActivity.class));
                finish();
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
