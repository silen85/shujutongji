package com.lesso.data.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meisl on 2015/7/15.
 */
public class SetupPWDActivity extends Activity {

    private String TAG = "com.lesso.data.fragment.SetupPWDActivity";

    private LessoApplication.LoginUser loginUser;

    private Button btn_back, submit;

    private RelativeLayout main_title;
    private TextView main_title_txt;
    private ImageView main_setting;

    private LinearLayout modifypwd_layer;
    private EditText passwordEditText, newpasswordEditText, confirmpasswordEditText;

    private LinearLayout authority_layer;
    private ImageView lock_icon;
    private EditText authorityEditText;
    private Button authority_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setuppwd);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();
        if (loginUser == null) {
            finish();
        }

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_title_txt = (TextView) findViewById(R.id.main_title_txt);
        main_setting = (ImageView) findViewById(R.id.main_setting);

        main_title.setBackgroundColor(getResources().getColor(R.color.REPORT_UI_C2));
        main_title_txt.setText("密码修改");
        main_setting.setVisibility(View.GONE);


        authority_layer = (LinearLayout) findViewById(R.id.authority_layer);
        if (authority_layer != null) {
            lock_icon = (ImageView) findViewById(R.id.lock_icon);
            authorityEditText = (EditText) findViewById(R.id.authorityEditText);
            authority_button = (Button) findViewById(R.id.authority_button);

            authorityEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (authorityEditText.length() != 0 || authorityEditText.length() != 0) {
                        lock_icon.setSelected(true);
                        authority_layer.setSelected(true);
                        authority_button.setVisibility(View.VISIBLE);
                    } else {
                        lock_icon.setSelected(false);
                        authority_layer.setSelected(false);
                        authority_button.setVisibility(View.GONE);
                    }
                }
            });

            authority_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    authority();
                    authorityEditText.clearFocus();
                    InputMethodManager mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
                                .getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);// 隐藏软键盘
                    }
                }
            });

        }

        modifypwd_layer = (LinearLayout) findViewById(R.id.modifypwd_layer);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        newpasswordEditText = (EditText) findViewById(R.id.newpasswordEditText);
        confirmpasswordEditText = (EditText) findViewById(R.id.confirmpasswordEditText);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


    }

    private void authority() {

        Map<String, String> parems = new HashMap();

        parems.put("type", "logkey");
        parems.put("id", loginUser.getUserid());
        parems.put("key", authorityEditText.getText().toString());

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString + throwable.getMessage());
                Toast.makeText(SetupPWDActivity.this, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, responseString);

                if (statusCode == Constant.HTTP_STATUS_CODE_SUCCESS) {
                    String json = responseString;
                    try {
                        Map result = Tools.json2Map(json);
                        JSONObject viewtable = (JSONObject) result.get("viewtable");
                        int state = Integer.parseInt((String) viewtable.get("state"));
                        if (state > 0) {
                            hideAuthority();
                            modifypwd_layer.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(SetupPWDActivity.this, SetupPWDActivity.this.getResources().getString(R.string.text_authority_failed_dongpwdwrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + json);
                        Toast.makeText(SetupPWDActivity.this, SetupPWDActivity.this.getResources().getString(R.string.text_authority_failed_dongpwdwrong), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        asyncHttpResponseHandler.setCharset("GBK");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(SetupPWDActivity.this, Constant.URL_PWD_AUTHORITY, requestParams, asyncHttpResponseHandler);

    }

    private void hideAuthority() {
        if (authority_layer != null) {
            authority_layer.setVisibility(View.GONE);
        }
    }

    private void submit() {

        if ("".equals(passwordEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.text_setup_password), Toast.LENGTH_SHORT).show();
        } else if ("".equals(newpasswordEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.text_setup_password1), Toast.LENGTH_SHORT).show();
        } else if ("".equals(confirmpasswordEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.text_setup_password2), Toast.LENGTH_SHORT).show();
        } else if (!passwordEditText.getText().toString().equals(loginUser.getUserPassword())) {
            Toast.makeText(this, getString(R.string.text_setup_password_tips1), Toast.LENGTH_SHORT).show();
        } else if (!newpasswordEditText.getText().toString().equals(confirmpasswordEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.text_setup_password_tips2), Toast.LENGTH_SHORT).show();
        } else if (newpasswordEditText.getText().toString().length() < 6 || newpasswordEditText.getText().toString().matches("[0-9]+")) {
            Toast.makeText(this, getString(R.string.text_setup_password_tips3), Toast.LENGTH_SHORT).show();
        } else {


            Map<String, String> parems = new HashMap();

            parems.put("type", "PwdUP");
            parems.put("id", loginUser.getUserid());
            parems.put("pwd", newpasswordEditText.getText().toString());
            parems.put("OldPwd", passwordEditText.getText().toString());

            RequestParams requestParams = new RequestParams(parems);

            AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, responseString + throwable.getMessage());
                    Toast.makeText(SetupPWDActivity.this, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d(TAG, responseString);

                    if (statusCode == Constant.HTTP_STATUS_CODE_SUCCESS) {
                        String json = responseString;
                        try {
                            Map result = Tools.json2Map(json);
                            JSONObject viewtable = (JSONObject) result.get("viewtable");
                            int state = Integer.parseInt((String) viewtable.get("state"));
                            if (state > 0) {
                                Toast.makeText(SetupPWDActivity.this, SetupPWDActivity.this.getResources().getString(R.string.text_setup_password_success), Toast.LENGTH_SHORT).show();
                                loginUser.setUserPassword(newpasswordEditText.getText().toString());
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(SetupPWDActivity.this, SetupPWDActivity.this.getResources().getString(R.string.text_setup_password_failed), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage() + json);
                            Toast.makeText(SetupPWDActivity.this, SetupPWDActivity.this.getResources().getString(R.string.text_setup_password_failed), Toast.LENGTH_SHORT).show();
                            finish();
                        }
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
            client.post(SetupPWDActivity.this, Constant.URL_SETUP_MODIFYPWD, requestParams, asyncHttpResponseHandler);

        }

    }

    @Override
    public void onBackPressed() {
        btn_back.performClick();
    }

}
