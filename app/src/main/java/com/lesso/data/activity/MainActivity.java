package com.lesso.data.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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
import com.lesso.data.fragment.UserFragment1;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/19.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ProgressDialog loadingDialog;

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
    private UserFragment1 userFragment;
    private UserDetailFragment userDetailFragment;

    private Map<String, String> salesDataCache = new HashMap();

    BroadcastReceiver finishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();
        if (loginUser == null) {
            finish();
        }

        IntentFilter finishFilter = new IntentFilter(Constant.FINISH_ACTION);
        registerReceiver(finishReceiver, finishFilter);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setVisibility(View.GONE);
        btn_back.setOnClickListener(this);

        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_title_txt = (TextView) findViewById(R.id.main_title_txt);
        main_setting = (ImageView) findViewById(R.id.main_setting);
        main_setting.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.MONTH, -1);
        sBeginDate = Constant.DATE_FORMAT_1.format(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        sEndDate = Constant.DATE_FORMAT_1.format(calendar.getTime());


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
                accessDetailFragment.setTimeType(((AccessFragment) fragment).getTimeType());
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
                accessDetailFragment.setTimeType(((AccessFragment) fragment).getTimeType());
                accessDetailFragment.setsBeginDate(((AccessFragment) fragment).getsBeginDate());
                accessDetailFragment.setsEndDate(((AccessFragment) fragment).getsEndDate());
                accessDetailFragment.setTabType(((AccessFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, accessDetailFragment);
            }
        } else if (fragment instanceof AccessDetailFragment) {
            if (accessFragment != null && accessFragment.isAdded()) {
                accessFragment.setTimeType(((AccessDetailFragment) fragment).getTimeType());
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
                accessFragment.setTimeType(((AccessDetailFragment) fragment).getTimeType());
                accessFragment.setsBeginDate(((AccessDetailFragment) fragment).getsBeginDate());
                accessFragment.setsEndDate(((AccessDetailFragment) fragment).getsEndDate());
                accessFragment.setTabType(((AccessDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, accessFragment);
            }
        } else if (fragment instanceof SalesFragment) {
            if (salesDetailFragment != null && salesDetailFragment.isAdded()) {
                salesDetailFragment.setTimeType(((SalesFragment) fragment).getTimeType());
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
                salesDetailFragment.setTimeType(((SalesFragment) fragment).getTimeType());
                salesDetailFragment.setsBeginDate(((SalesFragment) fragment).getsBeginDate());
                salesDetailFragment.setsEndDate(((SalesFragment) fragment).getsEndDate());
                salesDetailFragment.setTabType(((SalesFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, salesDetailFragment);
            }
        } else if (fragment instanceof SalesDetailFragment) {
            if (salesFragment != null && salesFragment.isAdded()) {
                salesFragment.setTimeType(((SalesDetailFragment) fragment).getTimeType());
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
                salesFragment.setTimeType(((SalesDetailFragment) fragment).getTimeType());
                salesFragment.setsBeginDate(((SalesDetailFragment) fragment).getsBeginDate());
                salesFragment.setsEndDate(((SalesDetailFragment) fragment).getsEndDate());
                salesFragment.setTabType(((SalesDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, salesFragment);
            }
        } else if (fragment instanceof StoreFragment) {
            if (storeDetailFragment != null && storeDetailFragment.isAdded()) {
                storeDetailFragment.setTimeType(((StoreFragment) fragment).getTimeType());
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
                storeDetailFragment.setTimeType(((StoreFragment) fragment).getTimeType());
                storeDetailFragment.setsBeginDate(((StoreFragment) fragment).getsBeginDate());
                storeDetailFragment.setsEndDate(((StoreFragment) fragment).getsEndDate());
                storeDetailFragment.setTabType(((StoreFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, storeDetailFragment);
            }
        } else if (fragment instanceof StoreDetailFragment) {
            if (storeFragment != null && storeFragment.isAdded()) {
                storeFragment.setTimeType(((StoreDetailFragment) fragment).getTimeType());
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
                storeFragment.setTimeType(((StoreDetailFragment) fragment).getTimeType());
                storeFragment.setsBeginDate(((StoreDetailFragment) fragment).getsBeginDate());
                storeFragment.setsEndDate(((StoreDetailFragment) fragment).getsEndDate());
                storeFragment.setTabType(((StoreDetailFragment) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, storeFragment);
            }
        } else if (fragment instanceof UserFragment1) {
            if (userDetailFragment != null && userDetailFragment.isAdded()) {
                userDetailFragment.setTimeType(((UserFragment1) fragment).getTimeType());
                userDetailFragment.setsBeginDate(((UserFragment1) fragment).getsBeginDate());
                userDetailFragment.setsEndDate(((UserFragment1) fragment).getsEndDate());
                if (userDetailFragment.getTabType() != ((UserFragment1) fragment).getTabType()) {
                    userDetailFragment.toogleTab(((UserFragment1) fragment).getTabType());
                }
                userDetailFragment.initTimeChooserVal();
                userDetailFragment.initData();
                fragmentTransaction.show(userDetailFragment);
            } else {
                userDetailFragment = new UserDetailFragment();
                userDetailFragment.setTimeType(((UserFragment1) fragment).getTimeType());
                userDetailFragment.setsBeginDate(((UserFragment1) fragment).getsBeginDate());
                userDetailFragment.setsEndDate(((UserFragment1) fragment).getsEndDate());
                userDetailFragment.setTabType(((UserFragment1) fragment).getTabType());
                fragmentTransaction.add(R.id.main_content, userDetailFragment);
            }
        } else if (fragment instanceof UserDetailFragment) {
            if (userFragment != null && userFragment.isAdded()) {
                userFragment.setTimeType(((UserDetailFragment) fragment).getTimeType());
                userFragment.setsBeginDate(((UserDetailFragment) fragment).getsBeginDate());
                userFragment.setsEndDate(((UserDetailFragment) fragment).getsEndDate());
                if (userFragment.getTabType() != ((UserDetailFragment) fragment).getTabType()) {
                    userFragment.toogleTab(((UserDetailFragment) fragment).getTabType());
                }
                userFragment.initTimeChooserVal();
                userFragment.initData();
                fragmentTransaction.show(userFragment);
            } else {
                userFragment = new UserFragment1();
                userFragment.setTimeType(((UserDetailFragment) fragment).getTimeType());
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
                userFragment = new UserFragment1();
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
                if (_fragment != null) {
                    fragmentTransaction.remove(_fragment);
                }
            }

            fragmentTransaction.add(R.id.main_content, fragment);
            fragmentTransaction.commit();

            btn_back.setVisibility(View.VISIBLE);

        }
    }

    private void backFragment() {

        if (mainFragment.isVisible()) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, getString(R.string.quit_press_again), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {

                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(mHomeIntent);

                //finish();
            }
        } else {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            List<Fragment> fragmentList = fragmentManager.getFragments();
            Iterator iterator = fragmentList.iterator();
            while (iterator.hasNext()) {
                Fragment _fragment = (Fragment) iterator.next();
                if (_fragment != null) {
                    fragmentTransaction.remove(_fragment);
                }
            }

            mainFragment = new MainFragment();
            fragmentTransaction.add(R.id.main_content, mainFragment);
            fragmentTransaction.commit();

            btn_back.setVisibility(View.GONE);
            toogleTitle(getString(R.string.app_title), true);

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

    /**
     * 加载对话框(显示)
     */
    public void loading() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingDialog.setMessage(getString(R.string.loading));
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
    }

    /**
     * 加载对话框(关闭)
     */
    public void disLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        backFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(finishReceiver);
        } catch (Exception e) {
        }

    }

    public Map<String, String> getSalesDataCache() {
        return salesDataCache;
    }
}
