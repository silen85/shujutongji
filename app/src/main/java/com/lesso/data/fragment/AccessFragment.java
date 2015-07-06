package com.lesso.data.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.XYLineView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/24.
 */
public class AccessFragment extends BaseGraphFragment {

    private String TAG = "com.lesso.data.fragment.AccessFragment";

    private int chart_access_width;

    private List<Map<String, String>> dataCache;

    private LinearLayout data_view, chart_xy_container, data_view_access;
    private XYLineView chart_xy;

    private LinearLayout tab_uv, tab_pv;

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

    protected void initView() {

        initTime();

        initBtnToogle();

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

    public void toogleTab(int tabType) {
        super.toogleTab(tabType);
        tab_uv.setSelected(tabType == 2 ? false : true);
        tab_pv.setSelected(tabType == 2 ? true : false);

    }

    public void initData() {

        sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {

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

    protected Map<String, String> generateParam() {

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

    protected void sendRequest(Map<String, String> parems) {

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

}
