package com.lesso.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.LockPatternView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockSetupActivity extends Activity implements
        LockPatternView.OnPatternListener {

    private static final String TAG = "LockSetupActivity";

    private LessoApplication.LoginUser loginUser;

    private LockPatternView lockPatternView;

    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 继续
    private static final int STEP_4 = 4; // 第二次设置手势完成

    private int step;

    private List<LockPatternView.Cell> choosePattern;

    private boolean confirm = false;

    private Animation shakeAnim;
    private TextView setup_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_lock_setup);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();
        if (loginUser == null) {
            finish();
        }

        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);

        setup_tips = (TextView) findViewById(R.id.setup_tips);
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
        shakeAnim.setInterpolator(new LinearInterpolator());

        step = STEP_1;
        updateView();
    }

    private void updateView() {
        switch (step) {
            case STEP_1:
                choosePattern = null;
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_2:
                setup_tips.setText(getString(R.string.text_lock_setup_again));
                lockPatternView.disableInput();
                step = STEP_3;
                updateView();
                break;
            case STEP_3:
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_4:
                if (confirm) {
                    lockPatternView.disableInput();
                    setupScratPWD();
                } else {

                    setup_tips.setText(getString(R.string.text_lock_setup_wrong));
                    setup_tips.startAnimation(shakeAnim);

                    lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    lockPatternView.enableInput();

                    step = STEP_3;
                    updateView();
                }

                break;

            default:
                break;
        }
    }

    private void setupScratPWD() {

        if (loginUser == null) {
            finish();
            return;
        }

        final String pattern = LockPatternView.patternToString(choosePattern);

        Map<String, String> parems = new HashMap();

        parems.put("type", "ScratUP");
        parems.put("id", loginUser.getUserid());
        parems.put("Scrat", pattern);

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpClient client = new AsyncHttpClient();
        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
                Toast.makeText(LockSetupActivity.this, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, responseString);

                try {
                    if (statusCode == Constant.HTTP_STATUS_CODE_SUCCESS) {
                        Map result = Tools.json2Map(responseString);
                        JSONObject viewtable = (JSONObject) result.get("viewtable");
                        int state = Integer.parseInt((String) viewtable.get("state"));
                        if (state > 0) {
                            loginUser.setScratchable_PWD(pattern);
                            Intent intent = new Intent(LockSetupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LockSetupActivity.this, getResources().getString(R.string.text_scratpwd_failed_setup), Toast.LENGTH_SHORT).show();
                            step = STEP_1;
                            updateView();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LockSetupActivity.this, getResources().getString(R.string.text_scratpwd_failed_setup), Toast.LENGTH_SHORT).show();
                    step = STEP_1;
                    updateView();
                }
            }
        };
        asyncHttpResponseHandler.setCharset("GBK");
        client.setTimeout(Constant.CONNECT_TIMEOUT);
        client.post(this, Constant.URL_SETUP_SCRATPWD, requestParams, asyncHttpResponseHandler);
    }

    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
        Log.d(TAG, "onPatternCellAdded");
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {

            setup_tips.setText(getString(R.string.text_lock_setup_too_short));
            setup_tips.startAnimation(shakeAnim);

            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList(pattern);
            step = STEP_2;
            updateView();
            return;
        }

        if (choosePattern.equals(pattern)) {
            confirm = true;
        } else {
            confirm = false;
        }

        step = STEP_4;
        updateView();

    }

    @Override
    public void onBackPressed() {
        sendBroadcast(new Intent(Constant.FINISH_ACTION));
        super.onBackPressed();
    }

}
