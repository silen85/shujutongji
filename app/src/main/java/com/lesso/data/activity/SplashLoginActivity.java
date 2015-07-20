package com.lesso.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.common.UpdateManager;
import com.lesso.data.cusinterface.FragmentListener;
import com.lesso.data.fragment.LoginFragment;
import com.lesso.data.fragment.SplashFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SplashLoginActivity extends FragmentActivity implements Handler.Callback {

    private String TAG = "com.lesso.data.activity.SplashLoginActivity";

    private static final int HANDLER_DATA = 1;
    private static final int HANDLER_NETWORK_ERR = 2;

    private FragmentManager fragmentManager;

    private Fragment splashFragment;
    private Fragment loginFragment;

    private FragmentListener fragmentListener;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_login);

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        splashFragment = new SplashFragment();
        fragmentTransaction.add(R.id.splash_login, splashFragment);
        fragmentTransaction.commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (((LessoApplication) getApplication()).getLoginUser() == null) {
                    mHandler = new Handler(SplashLoginActivity.this);
                    checkSession();
                } else {
                    gotoScratLock();
                }
            }
        }, 1500);

    }

    private void gotoLogin() {

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

    }

    private void gotoScratLock() {

        String Scratchable_PWD = ((LessoApplication) getApplication()).getLoginUser().getScratchable_PWD();

        if (Scratchable_PWD != null && !"".equals(Scratchable_PWD.trim())) {
            Intent intent = new Intent(SplashLoginActivity.this, LockActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashLoginActivity.this, LockSetupActivity.class);
            startActivity(intent);
        }
        finish();

    }

    private void checkSession() {

        SharedPreferences sp = getSharedPreferences(Constant.LESSOBI, Activity.MODE_PRIVATE);
        final String uid = sp.getString(Constant.LESSOBI_USERID, null);
        final String username = sp.getString(Constant.LESSOBI_USERNAME, null);
        final String password = sp.getString(Constant.LESSOBI_USERPASSWORD, null);
        final String Scratchable_PWD = sp.getString(Constant.LESSOBI_USERSCRATPWD, null);

        if (uid == null || username == null || password == null || Scratchable_PWD == null) {
            gotoLogin();
            return;
        }

        Map<String, String> parems = new HashMap();

        parems.put("type", "login");
        parems.put("uid", username);
        parems.put("pwd", password);

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_NETWORK_ERR;
                message.sendToTarget();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, responseString);

                if (statusCode == Constant.HTTP_STATUS_CODE_SUCCESS) {
                    Message message = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();

                    bundle.putString("uid", uid);
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    bundle.putString("Scratchable_PWD", Scratchable_PWD);

                    bundle.putString("json", responseString);
                    message.what = HANDLER_DATA;
                    message.setData(bundle);
                    message.sendToTarget();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        asyncHttpResponseHandler.setCharset("GBK");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.CONNECT_TIMEOUT);
        client.post(SplashLoginActivity.this, Constant.URL_LOGIN, requestParams, asyncHttpResponseHandler);

    }

    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == HANDLER_DATA) {
            String json = msg.getData().getString("json");
            try {
                Map result = Tools.json2Map(json);
                JSONObject viewtable = (JSONObject) result.get("viewtable");
                if (viewtable != null && viewtable.length() > 0) {

                    int state = Integer.parseInt((String) viewtable.get("state"));

                    if (state > Constant.LOGIN_STATUS_CODE_SUCCESS) {

                        LessoApplication.LoginUser loginUser = ((LessoApplication) getApplication()).new LoginUser();
                        loginUser.setUserid((String) msg.getData().get("uid"));
                        loginUser.setUserName((String) msg.getData().get("username"));
                        loginUser.setUserPassword((String) msg.getData().get("password"));
                        loginUser.setScratchable_PWD((String) msg.getData().get("Scratchable_PWD"));

                        ((LessoApplication) getApplication()).setLoginUser(loginUser);

                        gotoScratLock();

                    } else {
                        gotoLogin();
                    }
                } else {
                    gotoLogin();
                }
            } catch (Exception e) {
                gotoLogin();
            }
        } else if (msg.what == HANDLER_NETWORK_ERR) {
            gotoLogin();
        }

        return true;
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
