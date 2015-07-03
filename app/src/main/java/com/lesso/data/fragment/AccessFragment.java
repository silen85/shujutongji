package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.TimeChooserDialog;
import com.lesso.data.ui.XYLineView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/24.
 */
public class AccessFragment extends Fragment {

    private String TAG = "com.lesso.data.fragment.AccessFragment";

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private int chart_access_width;

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private TimeChooserDialog timerDialog;
    private int timeType = 1;
    private RelativeLayout time_chooser;
    private String sBeginDate, sEndDate;

    private View view;

    private List<Map<String, String>> dataCache;
    private List<Map<String, String>> list = new ArrayList();

    private LinearLayout data_view, chart_xy_container, data_view_access;
    private XYLineView chart_xy;

    private int tabType = 1;
    private LinearLayout tab_uv, tab_pv;

    private Animation roatAnim;
    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_access, null);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }

    private void initView() {

        time_chooser = (RelativeLayout) view.findViewById(R.id.time_chooser);
        time_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        sBeginDate = Constant.DATE_FORMAT_1.format(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 6));
        ((TextView) time_chooser.findViewById(R.id.time_chooser_f)).setText(sBeginDate);
        time_chooser.findViewById(R.id.time_chooser_f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        sEndDate = Constant.DATE_FORMAT_1.format(new Date());
        ((TextView) time_chooser.findViewById(R.id.time_chooser_t)).setText(sEndDate);
        time_chooser.findViewById(R.id.time_chooser_t).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        data_view = (LinearLayout) view.findViewById(R.id.data_view);
        chart_xy_container = (LinearLayout) view.findViewById(R.id.chart_xy_container);
        data_view_access = (LinearLayout) view.findViewById(R.id.data_view_access);
        data_view_access.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                data_view_access.getViewTreeObserver().removeOnPreDrawListener(this);
                chart_access_width = data_view_access.getWidth();
                Log.d(TAG, "chart_user_width;" + chart_access_width);
                return true;
            }
        });

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(AccessFragment.this);
            }
        });

        tab_uv = (LinearLayout) view.findViewById(R.id.tab_uv);
        tab_pv = (LinearLayout) view.findViewById(R.id.tab_pv);


        toogleTab(tabType);

        tab_uv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    toogleTab(tabType);
                    fillData(dataCache);
                }
            }
        });

        tab_pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabType != 2) {
                    tabType = 2;
                    toogleTab(tabType);
                    fillData(dataCache);
                }
            }
        });
    }

    private void toogleTab(int tabType) {

        tab_uv.setSelected(tabType == 2 ? false : true);
        tab_pv.setSelected(tabType == 2 ? true : false);

    }

    private void initData() {

        sendRequest(generateParam());

    }

    public void fillData(List<Map<String, String>> data) {

        list.clear();
        if (data != null && data.size() > 0) {

            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                item.put("colum1", data.get(i).get("created"));

                if (tabType == 2)
                    item.put("colum2", data.get(i).get("pv"));
                else
                    item.put("colum2", data.get(i).get("uv"));

                list.add(item);
            }

        }

        if (chart_xy == null) {

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_access_width,
                    (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
            chart_xy = new XYLineView(activity);
            chart_xy.setLayoutParams(lp);
            chart_xy.setScreenWidth(chart_access_width);
            chart_xy.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_access));
            data_view_access.addView(chart_xy);
        }
        fillXYLineData(list);

    }

    private void fillXYLineData(List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {

            String[] fields = new String[list.size()];
            float[] dataArr = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(list.size() - 1 - i).get("colum1");
                String ydata = list.get(list.size() - 1 - i).get("colum2");

                fields[i] = xdata.substring(6);
                dataArr[i] = Float.parseFloat(ydata);

            }
            chart_xy.setData(dataArr);
            chart_xy.setField(fields);
        } else {
            chart_xy.setData(new float[]{});
            chart_xy.setField(new String[]{});
        }
        chart_xy.postInvalidate();
    }

    private Map<String, String> generateParam() {

        /**
         * 发送请求
         */
        Map<String, String> parems = new HashMap();
        parems.put("appkey", Constant.APP_KEY);
        parems.put("timestamp", (System.currentTimeMillis() / 1000) + "");

        String token = Constant.SECRET_KEY + Constant.DATE_FORMAT_1.format(new Date());
        MD5 md5 = new MD5();
        //token = Tools.md5(token);
        token = md5.GetMD5Code(token);
        parems.put("token", token);

        if (timeType == 2)
            parems.put("type", "month");

        if (sBeginDate != null && !"".equals(sBeginDate.trim()))
            parems.put("st", sBeginDate);
        if (sEndDate != null && !"".equals(sEndDate.trim()))
            parems.put("et", sEndDate);

        return parems;

    }

    private void sendRequest(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(activity, Constant.URL_REPORT_ACCESS, requestParams, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_SROAT;
                message.sendToTarget();
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

                if (statusCode == 200) {
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
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_EROAT;
                message.sendToTarget();
            }
        });

    }

    private final int HANDLER_DATA = 1;
    private final int HANDLER_SROAT = 2;
    private final int HANDLER_EROAT = 3;
    private final int HANDLER_NETWORK_ERR = 4;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_DATA:

                    String json = msg.getData().getString("json");

                    try {
                        Map result = Tools.json2Map(json);
                        String status = (String) result.get("status");
                        //String desc = (String) result.get("msg");

                        if (Constant.ACCESS_STATUS_CODE_SUCCESS.equals(status)) {
                            dataCache = (List<Map<String, String>>) result.get("data");
                            fillData(dataCache);
                        } else {
                            fillData(null);
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + json);

                        fillData(null);
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    if (roatAnim == null) {
                        roatAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.roat);
                        roatAnim.setInterpolator(new LinearInterpolator());
                    }
                    btn_toogle_fragment.startAnimation(roatAnim);
                    break;
                case HANDLER_EROAT:
                    btn_toogle_fragment.clearAnimation();
                    break;
                case HANDLER_NETWORK_ERR:
                    fillData(null);
                    Toast.makeText(activity, getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showTimerDialog() {

        if (timerDialog == null) {
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
        }
        timerDialog.show();
    }

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

}
