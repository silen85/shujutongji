package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.adapter.HorizontalBarAdapter;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.BarView2;
import com.lesso.data.ui.XYLineView2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/25.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "com.lesso.data.fragment.MainFragment";

    private int screenWidth, screenHeight;

    private String sBeginDate, sEndDate, yesterday, tomorrow;

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private LinearLayout fragment_access, fragment_sales, fragment_store, fragment_user;

    private List<Map<String, String>> storeList = new ArrayList<>();

    private int chart_access_width, chart_sales_width, chart_user_width;
    private LinearLayout data_view_access, data_view_sales, data_view_store, data_view_user;
    private XYLineView2 chart_access, chart_user;
    private BarView2 chart_sales;
    private ListView listview_store;
    private HorizontalBarAdapter horizontalBarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_main, null);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        sBeginDate = Constant.DATE_FORMAT_1.format(calendar.getTime());

        sEndDate = Constant.DATE_FORMAT_1.format(new Date());

        calendar.add(Calendar.DAY_OF_MONTH, 29);
        yesterday = Constant.DATE_FORMAT_1.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow = Constant.DATE_FORMAT_1.format(calendar.getTime());

        initView();

        initData();
    }

    private void initView() {

        fragment_access = (LinearLayout) view.findViewById(R.id.fragment_access);
        fragment_sales = (LinearLayout) view.findViewById(R.id.fragment_sales);
        fragment_store = (LinearLayout) view.findViewById(R.id.fragment_store);
        fragment_user = (LinearLayout) view.findViewById(R.id.fragment_user);

        fragment_access.setOnClickListener(this);
        fragment_sales.setOnClickListener(this);
        fragment_store.setOnClickListener(this);
        fragment_user.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.fragment_access_date)).setText(sBeginDate + " 至 " + yesterday);
        ((TextView) view.findViewById(R.id.fragment_sales_date)).setText(sBeginDate + " 至 " + yesterday);
        ((TextView) view.findViewById(R.id.fragment_store_date)).setText(sBeginDate + " 至 " + yesterday);
        ((TextView) view.findViewById(R.id.fragment_user_date)).setText(sBeginDate + " 至 " + yesterday);

        initAccessView();

        initSalesView();

        initStoreView();

        initUserView();

    }

    private void initData() {
        requestStoreData();
        requestStoreDataByYesterday();
    }

    private void initAccessView() {

        data_view_access = (LinearLayout) view.findViewById(R.id.data_view_access);
        data_view_access.setOnClickListener(this);
        chart_access_width = screenWidth;

        requestAccessData();

    }

    private void initSalesView() {

        data_view_sales = (LinearLayout) view.findViewById(R.id.data_view_sales);
        data_view_sales.setOnClickListener(this);
        chart_sales_width = screenWidth;

        requestSalesData();

    }

    private void initStoreView() {

        data_view_store = (LinearLayout) view.findViewById(R.id.data_view_store);
        listview_store = (ListView) data_view_store.findViewById(R.id.listview_store);

        data_view_store.setOnClickListener(this);
        listview_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activity.goToFragment(R.id.listview_store);
            }
        });

    }

    private void initUserView() {

        data_view_user = (LinearLayout) view.findViewById(R.id.data_view_user);
        data_view_user.setOnClickListener(this);
        chart_user_width = screenWidth;

        requestUserData();

    }

    private void initAccessData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_access_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_access));
        chart_access = new XYLineView2(activity);
        chart_access.setLayoutParams(lp);
        chart_access.setScreenWidth(chart_access_width);
        chart_access.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_access));
        chart_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_view_access.performClick();
            }
        });

        data_view_access.addView(chart_access);

        try {
            Map result = Tools.json2Map(json);

            String status = (String) result.get("status");
            //String desc = (String) result.get("msg");

            if (Constant.ACCESS_STATUS_CODE_SUCCESS.equals(status)) {

                List<Map<String, String>> dataList = (List<Map<String, String>>) result.get("data");
                if (dataList != null && dataList.size() > 0) {

                    String[] fields = new String[dataList.size()];
                    float[] data = new float[dataList.size()];

                    for (int i = 0; i < dataList.size(); i++) {

                        String xdata = dataList.get(i).get("created");
                        String ydata = dataList.get(i).get("uv");

                        fields[i] = xdata.substring(5);
                        data[i] = Float.parseFloat(ydata);

                        if (yesterday.equals(xdata)) {
                            try {
                                ((TextView) view.findViewById(R.id.fragment_access_amount)).setText(Integer.parseInt(ydata) + "");
                            } catch (Exception e) {
                                ((TextView) view.findViewById(R.id.fragment_access_amount)).setText("0");
                            }
                        }

                    }
                    chart_access.setData(data);
                    chart_access.setField(fields);
                } else {
                    ((TextView) view.findViewById(R.id.fragment_access_amount)).setText("0");
                    chart_access.setData(new float[]{});
                    chart_access.setField(new String[]{});
                }
            } else {
                ((TextView) view.findViewById(R.id.fragment_access_amount)).setText("0");
                chart_access.setData(new float[]{});
                chart_access.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initAccessData", e.getMessage());
            ((TextView) view.findViewById(R.id.fragment_access_amount)).setText("0");
            chart_access.setData(new float[]{});
            chart_access.setField(new String[]{});
        }
        chart_access.postInvalidate();
    }

    private void initSalesData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_sales_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_sales));
        chart_sales = new BarView2(activity);
        chart_sales.setLayoutParams(lp);
        chart_sales.setScreenWidth(chart_sales_width);
        chart_sales.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_sales));
        chart_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_view_sales.performClick();
            }
        });

        data_view_sales.addView(chart_sales);

        try {

            Map result = Tools.json2Map(json);

            List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");

            if (viewtable != null && viewtable.size() > 0) {

                String[] fields = new String[viewtable.size()];
                float[] data = new float[viewtable.size()];

                for (int i = 0; i < viewtable.size(); i++) {

                    String xdata = viewtable.get(i).get("ZDATE");
                    String ydata = viewtable.get(i).get("ZTOTLE");

                    fields[i] = xdata.substring(5);
                    data[i] = Float.parseFloat(ydata);

                    if (yesterday.equals(xdata)) {
                        try {
                            ((TextView) view.findViewById(R.id.fragment_sales_amount)).setText(((int) Float.parseFloat(ydata)) + "");
                        } catch (Exception e) {
                            ((TextView) view.findViewById(R.id.fragment_sales_amount)).setText("0");
                        }
                    }
                }
                chart_sales.setData(data);
                chart_sales.setField(fields);
            } else {
                ((TextView) view.findViewById(R.id.fragment_sales_amount)).setText("0");
                chart_sales.setData(new float[]{});
                chart_sales.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initSalesData", e.getMessage());
            ((TextView) view.findViewById(R.id.fragment_sales_amount)).setText("0");
            chart_sales.setData(new float[]{});
            chart_sales.setField(new String[]{});
        }
        chart_sales.postInvalidate();
    }

    private void initStoreData(String json) {

        try {
            Map result = Tools.json2Map(json);

            List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");

            if (viewtable != null && viewtable.size() > 0) {

                storeList.clear();
                //int count = 0;
                for (int i = 0; i < viewtable.size() && i < 8; i++) {

                    Map<String, String> item = new HashMap();

                    String coloum1, coloum2;

                    coloum1 = viewtable.get(i).get("prd_neme");
                    coloum2 = viewtable.get(i).get("number") + viewtable.get(i).get("sales");

                    item.put("product_name", coloum1);
                    item.put("product_num", coloum2);
                    item.put("product_percent", ((int) ((Float.parseFloat(viewtable.get(i).get("number"))) / (Float.parseFloat(viewtable.get(0).get("number"))) * 100)) + "");

                    //count += Integer.parseInt(viewtable.get(i).get("number"));

                    storeList.add(item);
                }

                //((TextView) view.findViewById(R.id.fragment_store_amount)).setText(count + "");

                horizontalBarAdapter = new HorizontalBarAdapter(activity, storeList, R.layout.item_processbar);

                listview_store.setAdapter(horizontalBarAdapter);

                listview_store.setVisibility(View.VISIBLE);
                view.findViewById(R.id.data_view_store_tips).setVisibility(View.GONE);

            } else {
                //((TextView) view.findViewById(R.id.fragment_store_amount)).setText("0");
                listview_store.setVisibility(View.GONE);
                view.findViewById(R.id.data_view_store_tips).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG + ":initStoreData", e.getMessage());

            //((TextView) view.findViewById(R.id.fragment_store_amount)).setText("0");
            listview_store.setVisibility(View.GONE);
            view.findViewById(R.id.data_view_store_tips).setVisibility(View.VISIBLE);
        }


    }

    private void initStoreDataByYesterday(String json) {
        try {
            Map result = Tools.json2Map(json);
            List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");
            if (viewtable != null && viewtable.size() > 0) {
                int count = 0;
                for (int i = 0; i < viewtable.size(); i++) {
                    count += ((int) Float.parseFloat(viewtable.get(i).get("number")));
                }
                ((TextView) view.findViewById(R.id.fragment_store_amount)).setText(count + "");
            } else {
                ((TextView) view.findViewById(R.id.fragment_store_amount)).setText("0");
            }
        } catch (Exception e) {
            Log.e(TAG + ":initStoreData", e.getMessage());
            ((TextView) view.findViewById(R.id.fragment_store_amount)).setText("0");
        }
    }

    private void initUserData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_user_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
        chart_user = new XYLineView2(activity);
        chart_user.setLayoutParams(lp);
        chart_user.setScreenWidth(chart_user_width);
        chart_user.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
        chart_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_view_user.performClick();
            }
        });

        data_view_user.addView(chart_user);

        try {

            Map result = Tools.json2Map(json);

            List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");

            if (viewtable != null && viewtable.size() > 0) {

                String[] fields = new String[viewtable.size()];
                float[] data = new float[viewtable.size()];

                for (int i = 0; i < viewtable.size(); i++) {

                    String xdata = viewtable.get(i).get("CREATETIME");
                    String ydata = viewtable.get(i).get("COUN");

                    fields[i] = xdata.substring(5);
                    data[i] = Float.parseFloat(ydata);

                    if (yesterday.equals(xdata)) {
                        try {
                            ((TextView) view.findViewById(R.id.fragment_user_amount)).setText(Integer.parseInt(ydata) + "");
                        } catch (Exception e) {
                            ((TextView) view.findViewById(R.id.fragment_user_amount)).setText("0");
                        }
                    }
                }
                chart_user.setData(data);
                chart_user.setField(fields);
            } else {
                ((TextView) view.findViewById(R.id.fragment_user_amount)).setText("0");
                chart_user.setData(new float[]{});
                chart_user.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initUserData", e.getMessage());
            ((TextView) view.findViewById(R.id.fragment_user_amount)).setText("0");
            chart_user.setData(new float[]{});
            chart_user.setField(new String[]{});
        }
        chart_user.postInvalidate();
    }


    private void requestAccessData() {

        Map<String, String> parems = new HashMap();
        parems.put("appkey", Constant.APP_KEY);
        parems.put("timestamp", (System.currentTimeMillis() / 1000) + "");

        String token = Constant.SECRET_KEY + Constant.DATE_FORMAT_1.format(new Date());
        MD5 md5 = new MD5();
        //token = Tools.md5(token);
        token = md5.GetMD5Code(token);
        parems.put("token", token);

        parems.put("st", sBeginDate);
        parems.put("et", yesterday);

        // parems.put("st", "2019-05-01");
        // parems.put("et", "2019-05-30");

        sendRequest(parems, HANDLER_DATA_ACCESS);

    }

    private void requestSalesData() {

        Map<String, String> parems = new HashMap();

        parems.put("VKORG", "1250");
        parems.put("start", sBeginDate);
        parems.put("end", yesterday);
        parems.put("VBELN", "00");
        parems.put("type", "MONEY");

        //  parems.put("start", "2019-05-01");
        // parems.put("end", "2019-05-30");

        sendRequest(parems, HANDLER_DATA_SALES);

    }

    private void requestStoreData() {

        Map<String, String> parems = new HashMap();

        parems.put("uid", "39");
        parems.put("type", "out");
        parems.put("start", sBeginDate);
        parems.put("end", yesterday);

        parems.put("pages", "1");
        parems.put("numbers", "8");

        //parems.put("start", "2015-05-01");
        //parems.put("end", "2015-05-30");

        sendRequest(parems, HANDLER_DATA_STORE);
    }

    private void requestStoreDataByYesterday() {

        Map<String, String> parems = new HashMap();

        parems.put("uid", "39");
        parems.put("type", "out");
        parems.put("start", yesterday);
        parems.put("end", yesterday);

        parems.put("pages", "1");
        parems.put("numbers", "10000");

        //parems.put("start", "2015-04-10");
        //parems.put("end", "2015-04-11");

        sendRequest(parems, HANDLER_DATA_STORE_YESTERDAY);

    }

    private void requestUserData() {

        Map<String, String> parems = new HashMap();

        parems.put("type", "nuser");
        parems.put("start", sBeginDate);
        parems.put("end", yesterday);

        //  parems.put("start", "2019-05-01");
        //  parems.put("end", "2019-05-30");

        sendRequest(parems, HANDLER_DATA_USER);

    }

    private void sendRequest(Map<String, String> parems, int handlerType) {

        String url;

        if (handlerType == HANDLER_DATA_ACCESS) {
            url = Constant.URL_REPORT_ACCESS;
        } else if (handlerType == HANDLER_DATA_SALES) {
            url = Constant.URL_REPORT_SALES;
        } else if (handlerType == HANDLER_DATA_STORE) {
            url = Constant.URL_REPORT_STORE;
        } else if (handlerType == HANDLER_DATA_USER) {
            url = Constant.URL_REPORT_USER;
        } else if (handlerType == HANDLER_DATA_STORE_YESTERDAY) {
            url = Constant.URL_REPORT_STORE;
        } else {
            return;
        }

        final int _handlerType = handlerType;

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

                if (statusCode == 200) {
                    Message message = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", responseString);
                    message.what = _handlerType;
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
        client.post(activity, url, requestParams, asyncHttpResponseHandler);

    }


    private final int HANDLER_NETWORK_ERR = 0;
    private final int HANDLER_DATA_ACCESS = 1;
    private final int HANDLER_DATA_SALES = 2;
    private final int HANDLER_DATA_STORE = 3;
    private final int HANDLER_DATA_STORE_YESTERDAY = 4;
    private final int HANDLER_DATA_USER = 5;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            try {
                String json = "";
                switch (msg.what) {
                    case HANDLER_DATA_ACCESS:
                        json = msg.getData().getString("json");
                        Log.d(TAG, "HANDLER_DATA_ACCESS:" + json);
                        initAccessData(json);
                        break;
                    case HANDLER_DATA_SALES:
                        json = msg.getData().getString("json");
                        Log.d(TAG, "HANDLER_DATA_SALES:" + json);
                        initSalesData(json);
                        break;
                    case HANDLER_DATA_STORE:
                        json = msg.getData().getString("json");
                        Log.d(TAG, "HANDLER_DATA_STORE:" + json);
                        initStoreData(json);
                        break;
                    case HANDLER_DATA_STORE_YESTERDAY:
                        json = msg.getData().getString("json");
                        Log.d(TAG, "HANDLER_DATA_USER:" + json);
                        initStoreDataByYesterday(json);
                        break;
                    case HANDLER_DATA_USER:
                        json = msg.getData().getString("json");
                        Log.d(TAG, "HANDLER_DATA_USER:" + json);
                        initUserData(json);
                        break;
                    case HANDLER_NETWORK_ERR:
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
            }

            super.handleMessage(msg);
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fragment_access:
            case R.id.data_view_access:

            case R.id.fragment_sales:
            case R.id.data_view_sales:

            case R.id.fragment_store:
            case R.id.data_view_store:

            case R.id.fragment_user:
            case R.id.data_view_user:
            case R.id.chart_user:
                activity.goToFragment(view.getId());
                break;
            default:
                break;
        }
    }
}
