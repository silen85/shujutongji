package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.activity.LockActivity;
import com.lesso.data.activity.LockSetupActivity;
import com.lesso.data.activity.SplashLoginActivity;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.cusinterface.FragmentListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meisl on 2015/6/9.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, Handler.Callback, FragmentListener {

    String TAG = "com.lesso.data.fragment.LoginFragment";

    private static final int HANDLER_DATA = 1;
    private static final int HANDLER_NETWORK_ERR = 2;

    private int roatMargis[] = new int[4];
    private int layoutMargis[] = new int[4];

    private SplashLoginActivity activity;
    private InputMethodManager mSoftManager;

    private RelativeLayout view;

    private FrameLayout roating;
    private ImageView splash_leftroat, splash_centerroat, splash_rightroat;
    private LinearLayout login_input, login_account_icon, login_password_icon;
    private EditText accountEditText, passwordEditText;

    private Handler mHandler;
    private ImageView l, r;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mSoftManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        activity.setFragmentListener(this);

        mHandler = new Handler(this);

        view = (RelativeLayout) inflater.inflate(R.layout.fragment_login, null);

        initView();

        return view;
    }

    private void initView() {

        l = (ImageView) view.findViewById(R.id.splash_leftroat);
        r = (ImageView) view.findViewById(R.id.splash_rightroat);

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                Animation anim_r = AnimationUtils.loadAnimation(getActivity(), R.anim.roat_login_r);
                anim_r.setInterpolator(new LinearInterpolator());
                r.startAnimation(anim_r);

                Animation anim_l = AnimationUtils.loadAnimation(getActivity(), R.anim.roat_login_l);
                anim_l.setInterpolator(new LinearInterpolator());
                l.startAnimation(anim_l);

            }
        });

        roating = (FrameLayout) view.findViewById(R.id.roating);

        splash_leftroat = (ImageView) view.findViewById(R.id.splash_leftroat);
        splash_centerroat = (ImageView) view.findViewById(R.id.splash_centerroat);
        splash_rightroat = (ImageView) view.findViewById(R.id.splash_rightroat);

        login_input = (LinearLayout) view.findViewById(R.id.login_input);
        login_account_icon = (LinearLayout) view.findViewById(R.id.login_account_icon);
        login_password_icon = (LinearLayout) view.findViewById(R.id.login_password_icon);

        accountEditText = (EditText) view.findViewById(R.id.accountEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);

        accountEditText.setShowSoftInputOnFocus(true);
        passwordEditText.setShowSoftInputOnFocus(true);

        accountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    onKeybordShow();
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    onKeybordShow();
                }
            }
        });

        accountEditText.setOnClickListener(this);
        passwordEditText.setOnClickListener(this);

        roatMargis[0] = ((RelativeLayout.LayoutParams) roating.getLayoutParams()).leftMargin;
        roatMargis[1] = ((RelativeLayout.LayoutParams) roating.getLayoutParams()).topMargin;
        roatMargis[2] = ((RelativeLayout.LayoutParams) roating.getLayoutParams()).rightMargin;
        roatMargis[3] = ((RelativeLayout.LayoutParams) roating.getLayoutParams()).bottomMargin;

        layoutMargis[0] = ((RelativeLayout.LayoutParams) login_input.getLayoutParams()).leftMargin;
        layoutMargis[1] = ((RelativeLayout.LayoutParams) login_input.getLayoutParams()).topMargin;
        layoutMargis[2] = ((RelativeLayout.LayoutParams) login_input.getLayoutParams()).rightMargin;
        layoutMargis[3] = ((RelativeLayout.LayoutParams) login_input.getLayoutParams()).bottomMargin;


        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);

    }

    private void onKeybordShow() {

        roating.setVisibility(View.GONE);
        splash_leftroat.setVisibility(View.GONE);
        splash_centerroat.setVisibility(View.GONE);
        splash_rightroat.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(roatMargis[0], roatMargis[1], roatMargis[2], roatMargis[3]);
        login_input.setLayoutParams(params);

        login_account_icon.setVisibility(View.GONE);
        login_password_icon.setVisibility(View.GONE);

    }

    private void onKeybordHidden() {

        roating.setVisibility(View.VISIBLE);
        splash_leftroat.setVisibility(View.VISIBLE);
        splash_centerroat.setVisibility(View.VISIBLE);
        splash_rightroat.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(layoutMargis[0], layoutMargis[1], layoutMargis[2], layoutMargis[3]);
        login_input.setLayoutParams(params);

        if (accountEditText.length() != 0 || passwordEditText.length() != 0) {
            login_account_icon.setVisibility(View.GONE);
            login_password_icon.setVisibility(View.GONE);
        } else {
            login_account_icon.setVisibility(View.VISIBLE);
            login_password_icon.setVisibility(View.VISIBLE);
        }

    }

    private void hideSoftKeybord() {
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            mSoftManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:

                if ("".equals(accountEditText.getText().toString())) {
                    Toast.makeText(activity, getString(R.string.text_login_no_account), Toast.LENGTH_SHORT).show();
                } else if ("".equals(passwordEditText.getText().toString())) {
                    Toast.makeText(activity, getString(R.string.text_login_no_password), Toast.LENGTH_SHORT).show();
                } else {
                    doLogin(generateParams(accountEditText.getText().toString(), passwordEditText.getText().toString()));
                }
                break;
            case R.id.accountEditText:
            case R.id.passwordEditText:
                onKeybordShow();
            default:
                break;

        }
    }

    private Map<String, String> generateParams(String account, String password) {

        Map<String, String> parems = new HashMap();

        parems.put("type", "login");
        parems.put("uid", account);
        parems.put("pwd", password);

        return parems;
    }

    protected void doLogin(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString + throwable.getMessage());
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
        client.post(activity, Constant.URL_LOGIN, requestParams, asyncHttpResponseHandler);
    }

    private void handleLogin(JSONObject data) throws Exception {

        int state = Integer.parseInt((String) data.get("state"));

        if (state > Constant.LOGIN_STATUS_CODE_SUCCESS) {

            String uid = (String) data.get("uid");
            String username = accountEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String Scratchable_PWD = (String) data.get("Scratchable_PWD");

            LessoApplication.LoginUser loginUser = ((LessoApplication) activity.getApplication()).new LoginUser();
            loginUser.setUserid(uid);
            loginUser.setUserName(username);
            loginUser.setUserPassword(password);
            loginUser.setScratchable_PWD(Scratchable_PWD);

            ((LessoApplication) getActivity().getApplication()).setLoginUser(loginUser);

            if (Scratchable_PWD != null && !"".equals(Scratchable_PWD.trim())) {
                Intent intent = new Intent(activity, LockActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(activity, LockSetupActivity.class);
                startActivity(intent);
            }
            activity.finish();
        } else if (Constant.LOGIN_STATUS_CODE_WRONGPWD == state) {
            Toast.makeText(activity, getString(R.string.text_login_failed_infowrong), Toast.LENGTH_SHORT).show();
        } else if (Constant.LOGIN_STATUS_CODE_WRONGDONGPWD == state) {
            Toast.makeText(activity, getString(R.string.text_login_failed_dongpwdwrong), Toast.LENGTH_SHORT).show();
        } else if (Constant.LOGIN_STATUS_CODE_NODONGPWD == state) {
            Toast.makeText(activity, getString(R.string.text_login_failed_dongpwdnobind), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, getString(R.string.text_login_failed), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == HANDLER_DATA) {
            String json = msg.getData().getString("json");
            try {
                Map result = Tools.json2Map(json);
                JSONObject viewtable = (JSONObject) result.get("viewtable");
                if (viewtable != null && viewtable.length() > 0) {
                    handleLogin(viewtable);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.text_login_failed), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + json);
                Toast.makeText(activity, activity.getResources().getString(R.string.text_login_failed), Toast.LENGTH_SHORT).show();
            }
        } else if (msg.what == HANDLER_NETWORK_ERR) {
            Toast.makeText(activity, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            hideSoftKeybord();
            onKeybordHidden();
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (activity.getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);// 隐藏软键盘
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SplashLoginActivity) activity;
    }

}
