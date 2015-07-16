package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
import com.lesso.data.activity.MainActivity;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.TimeChooserDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/7/4.
 */
public abstract class BaseGraphFragment extends Fragment {

    String TAG = "com.lesso.data.fragment.BaseGraphFragment";

    protected int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    protected LayoutInflater layoutInflater;
    protected MainActivity activity;

    protected TimeChooserDialog timerDialog;
    protected int timeType = 1;
    protected RelativeLayout time_chooser;
    protected String sBeginDate, sEndDate;

    protected View view;

    protected List<Map<String, String>> list = new ArrayList();

    protected int tabType = 1;

    protected Animation roatAnim;
    protected Button btn_toogle_fragment;

    private LinearLayout authority_layer;
    private ImageView lock_icon;
    private EditText authorityEditText;
    private Button authority_button;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        authority_layer = (LinearLayout) view.findViewById(R.id.authority_layer);

        if (authority_layer != null) {
            lock_icon = (ImageView) view.findViewById(R.id.lock_icon);
            authorityEditText = (EditText) view.findViewById(R.id.authorityEditText);
            authority_button = (Button) view.findViewById(R.id.authority_button);

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
                    InputMethodManager mSoftManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (activity.getCurrentFocus() != null) {
                        mSoftManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);// 隐藏软键盘
                    }
                }
            });

        }

    }

    protected void displayAuthority() {
        if (authority_layer != null) {
            authority_layer.setVisibility(View.VISIBLE);
            btn_toogle_fragment.setClickable(false);
        }
    }

    private void authority() {

        LessoApplication.LoginUser loginUser = ((LessoApplication) activity.getApplication()).getLoginUser();

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
                Log.e(TAG,throwable.getMessage(),throwable);
                Toast.makeText(activity, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
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
                            activity.AUTHORITY_SALES_AMOUNT = true;
                            hideAuthority();
                            initData();
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.text_authority_failed_dongpwdwrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage(),e);
                        Toast.makeText(activity, activity.getResources().getString(R.string.text_authority_failed_dongpwdwrong), Toast.LENGTH_SHORT).show();
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
        client.post(activity, Constant.URL_PWD_AUTHORITY, requestParams, asyncHttpResponseHandler);

    }

    protected void hideAuthority() {
        if (authority_layer != null) {
            authority_layer.setVisibility(View.GONE);
            btn_toogle_fragment.setClickable(true);
        }
    }

    protected void initTime() {

        initTimeChooser();

        initTimeChooserVal();

    }

    protected void initTimeChooser() {

        time_chooser = (RelativeLayout) view.findViewById(R.id.time_chooser);
        time_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        time_chooser.findViewById(R.id.time_chooser_f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        time_chooser.findViewById(R.id.time_chooser_t).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

    }

    public void initTimeChooserVal() {

        if (time_chooser == null)
            return;

        ((TextView) time_chooser.findViewById(R.id.time_chooser_f)).setText(sBeginDate);
        ((TextView) time_chooser.findViewById(R.id.time_chooser_t)).setText(sEndDate);

    }

    protected void showTimerDialog() {

        timerDialog = new TimeChooserDialog(activity, timeType, sBeginDate, sEndDate);
        timerDialog.setCanceledOnTouchOutside(true);
        timerDialog.setClickListenerInterface(new TimeChooserDialog.ClickListenerInterface() {
            @Override
            public void doFinish() {

                timeType = timerDialog.getType();
                sBeginDate = timerDialog.getsBeaginDate();
                sEndDate = timerDialog.getsEndDate();

                ((TextView) time_chooser.findViewById(R.id.time_chooser_f)).setText(sBeginDate);
                ((TextView) time_chooser.findViewById(R.id.time_chooser_t)).setText(sEndDate);

                /**
                 * 发送请求
                 */
                sendRequest(generateParam());

            }
        });
        timerDialog.show();

    }

    protected void initBtnToogle() {

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnToogle();
                activity.toogleFragment(BaseGraphFragment.this);
            }
        });
    }

    public void toogleTab(int tabType) {
        this.tabType = tabType;
    }

    protected void toogleTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.MONTH, -1);
        sBeginDate = Constant.DATE_FORMAT_1.format(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        sEndDate = Constant.DATE_FORMAT_1.format(calendar.getTime());

        initTimeChooserVal();

    }

    protected void roatStart() {
        if (roatAnim == null) {
            roatAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.roat);
            roatAnim.setInterpolator(new LinearInterpolator());
        }
        btn_toogle_fragment.startAnimation(roatAnim);
    }

    protected void roatEnd() {
        btn_toogle_fragment.clearAnimation();
    }

    protected abstract void initView();

    protected abstract void onBtnToogle();

    public abstract void initData();

    protected abstract void fillData(List<Map<String, String>> data);

    protected abstract Map<String, String> generateParam();

    protected abstract void sendRequest(Map<String, String> parems);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public String getsBeginDate() {
        return sBeginDate;
    }

    public void setsBeginDate(String sBeginDate) {
        this.sBeginDate = sBeginDate;
    }

    public String getsEndDate() {
        return sEndDate;
    }

    public void setsEndDate(String sEndDate) {
        this.sEndDate = sEndDate;
    }
}
