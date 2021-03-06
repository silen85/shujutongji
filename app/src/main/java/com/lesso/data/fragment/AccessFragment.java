package com.lesso.data.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.XYLineView2;
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

    private int screenWidth, screenHeight;
    private int chart_access_width;

    private HorizontalScrollView scrollview_linearlayout_access;

    private LinearLayout data_view, chart_xy_container, data_view_access;
    private XYLineView2 chart_xy;

    private LinearLayout tab_uv, tab_pv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_access, null);

        scrollview_linearlayout_access = (HorizontalScrollView) view.findViewById(R.id.scrollview_linearlayout_access);

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
        chart_access_width = screenWidth;

        tab_uv = (LinearLayout) view.findViewById(R.id.tab_uv);
        tab_pv = (LinearLayout) view.findViewById(R.id.tab_pv);


        toogleTab(tabType);

        tab_uv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    toogleTab(tabType);
                    toogleTime();
                    sendRequest(generateParam());
                }
            }
        });

        tab_pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabType != 2) {
                    tabType = 2;
                    toogleTab(tabType);
                    toogleTime();
                    sendRequest(generateParam());
                }
            }
        });
    }

    @Override
    protected void onBtnToogle() {

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

        if (chart_xy != null) {
            data_view_access.removeView(chart_xy);
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_access_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
        chart_xy = new XYLineView2(activity);
        chart_xy.setLayoutParams(lp);
        chart_xy.setScreenWidth(chart_access_width);
        chart_xy.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_access));
        data_view_access.addView(chart_xy);

        fillXYLineData(list);

    }

    private void fillXYLineData(List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {

            String[] fields = new String[list.size()];
            float[] dataArr = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(i).get("colum1");
                String ydata = list.get(i).get("colum2");

                fields[i] = xdata.substring(5);
                dataArr[i] = Float.parseFloat(ydata);

            }
            chart_xy.setData(dataArr);
            chart_xy.setField(fields);
        } else {
            chart_xy.setData(new float[]{});
            chart_xy.setField(new String[]{});
        }
        chart_xy.postInvalidate();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (chart_xy != null && chart_xy.getDataSize() > 5) {
                    scrollview_linearlayout_access.scrollTo(chart_xy.getRight(), 0);
                }
            }
        }, 800);

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
        client.setTimeout(Constant.CONNECT_TIMEOUT);
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
                Log.e(TAG,throwable.getMessage(),throwable);
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
                            fillData((List<Map<String, String>>) result.get("data"));
                        } else {
                            fillData(null);
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage(),e);

                        fillData(null);
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    roatStart();
                    break;
                case HANDLER_EROAT:
                    //btn_toogle_fragment.clearAnimation();
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
