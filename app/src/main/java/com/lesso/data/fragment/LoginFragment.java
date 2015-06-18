package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesso.data.R;
import com.lesso.data.activity.LockSetupActivity;
import com.lesso.data.activity.SplashLoginActivity;
import com.lesso.data.cusinterface.FragmentListener;
import com.lesso.data.ui.CustomRelativeLayout;
import com.lesso.data.ui.EditTextHolder;

/**
 * Created by meisl on 2015/6/9.
 */
public class LoginFragment extends Fragment implements View.OnClickListener,Handler.Callback,
        EditTextHolder.EditTextListener,FragmentListener {

    String TAG = "com.lesso.data.fragment.LoginFragment";

    private static final int HANDLER_LOGIN_HAS_FOCUS = 1;
    private static final int HANDLER_LOGIN_HAS_NO_FOCUS = 2;

    private int roatMargis[] = new int[4];
    private int layoutMargis[] = new int[4];

    private SplashLoginActivity activity;
    private InputMethodManager mSoftManager;

    private RelativeLayout view;

    private FrameLayout roating;
    private ImageView splash_leftroat,splash_centerroat,splash_rightroat;
    private LinearLayout login_input,login_account_icon,login_password_icon;
    private EditText accountEditText,passwordEditText;

    private Handler mHandler;
    private ImageView l,r;

    EditTextHolder mEditUserNameEt;
    EditTextHolder mEditPassWordEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mSoftManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        activity.setFragmentListener(this);

        mHandler = new Handler(this);

        view = (RelativeLayout) inflater.inflate(R.layout.activity_login,null);

        initView();

        return view;
    }

        private void initView(){

            l = (ImageView) view.findViewById(R.id.splash_leftroat);
            r = (ImageView) view.findViewById(R.id.splash_rightroat);

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    Animation anim_r = AnimationUtils.loadAnimation(getActivity(), R.anim.roat_login_r);
                    anim_r.setInterpolator(new LinearInterpolator());
                    r.startAnimation(anim_r);

                    Animation anim_l = AnimationUtils.loadAnimation(getActivity(),R.anim.roat_login_l);
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

            mEditUserNameEt = new EditTextHolder(accountEditText, this);
            mEditPassWordEt = new EditTextHolder(passwordEditText, this);


            accountEditText.addTextChangedListener(new TextWatcher(){

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(accountEditText.length()!=0 || passwordEditText.length()!=0){
                        onKeybordShow();
                    }
                }
            });

            passwordEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(accountEditText.length()!=0 || passwordEditText.length()!=0){
                        onKeybordShow();
                    }
                }
            });

            roatMargis[0] = ((RelativeLayout.LayoutParams)roating.getLayoutParams()).leftMargin;
            roatMargis[1] = ((RelativeLayout.LayoutParams)roating.getLayoutParams()).topMargin;
            roatMargis[2] = ((RelativeLayout.LayoutParams)roating.getLayoutParams()).rightMargin;
            roatMargis[3] = ((RelativeLayout.LayoutParams)roating.getLayoutParams()).bottomMargin;

            layoutMargis[0] = ((RelativeLayout.LayoutParams)login_input.getLayoutParams()).leftMargin;
            layoutMargis[1] = ((RelativeLayout.LayoutParams)login_input.getLayoutParams()).topMargin;
            layoutMargis[2] = ((RelativeLayout.LayoutParams)login_input.getLayoutParams()).rightMargin;
            layoutMargis[3] = ((RelativeLayout.LayoutParams)login_input.getLayoutParams()).bottomMargin;

            /*view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = view.getRootView().getHeight() - view.getHeight();
                    Log.v(TAG, "键盘:"+heightDiff);
                    if (heightDiff > 100)
                    { // 说明键盘是弹出状态
                        Log.v(TAG, "键盘弹出状态");
                        onKeybordShow();
                    } else{
                        Log.v(TAG, "键盘收起状态");
                        onKeybordHidden();
                    }
                }
            });*/

            Button login = (Button) view.findViewById(R.id.login);
            login.setOnClickListener(this);

        }

        private void onKeybordShow(){

            roating.setVisibility(View.GONE);
            splash_leftroat.setVisibility(View.GONE);
            splash_centerroat.setVisibility(View.GONE);
            splash_rightroat.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(roatMargis[0],roatMargis[1],roatMargis[2],roatMargis[3]);
            login_input.setLayoutParams(params);

            if(accountEditText.length()==0 && passwordEditText.length()==0){
                login_account_icon.setVisibility(View.VISIBLE);
                login_password_icon.setVisibility(View.VISIBLE);
            }else{
                login_account_icon.setVisibility(View.GONE);
                login_password_icon.setVisibility(View.GONE);
            }

        }

        private void onKeybordHidden(){

            roating.setVisibility(View.VISIBLE);
            splash_leftroat.setVisibility(View.VISIBLE);
            splash_centerroat.setVisibility(View.VISIBLE);
            splash_rightroat.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(layoutMargis[0], layoutMargis[1], layoutMargis[2], layoutMargis[3]);
            login_input.setLayoutParams(params);

            if(accountEditText.length()==0 && passwordEditText.length()==0){
                login_account_icon.setVisibility(View.VISIBLE);
                login_password_icon.setVisibility(View.VISIBLE);
            }else{
                login_account_icon.setVisibility(View.GONE);
                login_password_icon.setVisibility(View.GONE);
            }

        }

        public void hideSoftKeybord(){
            if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            Message mess = Message.obtain();
            mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
            mHandler.sendMessage(mess);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.login:
                    Intent intent = new Intent(activity, LockSetupActivity.class);
                    startActivity(intent);
                    activity.finish();
                    break;
                case R.id.accountEditText:
                case R.id.passwordEditText:
                    Message mess = Message.obtain();
                    mess.what = HANDLER_LOGIN_HAS_FOCUS;
                    mHandler.sendMessage(mess);
                    break;

            }
        }

        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == HANDLER_LOGIN_HAS_FOCUS) {
                onKeybordShow();
            } else if (msg.what == HANDLER_LOGIN_HAS_NO_FOCUS) {
                onKeybordHidden();
            }

        return false;
    }

    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {
        Message mess = Message.obtain();
        switch (v.getId()) {
            case R.id.accountEditText:
            case R.id.passwordEditText:
                if (hasFocus) {
                    mess.what = HANDLER_LOGIN_HAS_FOCUS;
                }else{
                    mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
                }
                mHandler.sendMessage(mess);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if(i== EditorInfo.IME_ACTION_DONE){
            hideSoftKeybord();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideSoftKeybord();
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        /*event.getKeyCode();
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_ESCAPE:
            case KeyEvent.KEYCODE_ENTER:
                hideSoftKeybord();
                break;
            default:
                break;
        }*/

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
