package com.lesso.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.ui.LockPatternView;

import java.util.List;


public class LockActivity extends Activity implements LockPatternView.OnPatternListener {

    private static final String TAG = "LockActivity";

    private int errcount = 5;

    private LessoApplication.LoginUser loginUser;

    private List<LockPatternView.Cell> lockPattern;
    private LockPatternView lockPatternView;

    private Animation shakeAnim;
    private TextView lock_input_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_lock);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();
        if (loginUser == null || loginUser.getScratchable_PWD() == null) {
            finish();
        }

        lock_input_tips = (TextView) findViewById(R.id.lock_input_tips);

        lockPattern = LockPatternView.stringToPattern(loginUser.getScratchable_PWD());
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (loginUser == null || loginUser.getScratchable_PWD() == null) {
            finish();
            return;
        }
        lockPattern = LockPatternView.stringToPattern(loginUser.getScratchable_PWD());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // disable back key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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

        if (pattern.equals(lockPattern)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (shakeAnim == null || lock_input_tips == null) {
                shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
                shakeAnim.setInterpolator(new LinearInterpolator());
            }

            errcount--;
            if (errcount <= 0) {
                ((LessoApplication) getApplication()).setLoginUser(null);
                finish();
            } else {

                lock_input_tips.setText("密码错误，还可以输入" + (errcount) + "次");
                lock_input_tips.startAnimation(shakeAnim);

                lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            }
        }

    }

}
