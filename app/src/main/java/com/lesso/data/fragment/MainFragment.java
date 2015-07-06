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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.adapter.StoreAdapter;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.BarView;
import com.lesso.data.ui.XYLineView;
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

    private int[] processbar_stys = {R.drawable.processbar_sty1, R.drawable.processbar_sty2, R.drawable.processbar_sty3,
            R.drawable.processbar_sty4, R.drawable.processbar_sty5, R.drawable.processbar_sty6,
            R.drawable.processbar_sty7, R.drawable.processbar_sty8};

    private String sBeginDate, sEndDate, yesterday, tomorrow;

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private View view;

    private LinearLayout fragment_access, fragment_sales, fragment_store, fragment_user;

    private List<Map<String, String>> storeList = new ArrayList<>();

    private int chart_access_width, chart_sales_width, chart_user_width;
    private LinearLayout data_view_access, data_view_sales, data_view_store, data_view_user;
    private XYLineView chart_access, chart_user;
    private BarView chart_sales;
    private ListView listview_store;
    private StoreAdapter storeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        ((TextView) view.findViewById(R.id.fragment_access_date)).setText(sBeginDate + " 至 " + sEndDate);
        ((TextView) view.findViewById(R.id.fragment_sales_date)).setText(sBeginDate + " 至 " + sEndDate);
        ((TextView) view.findViewById(R.id.fragment_store_date)).setText(yesterday + " 至 " + yesterday);
        ((TextView) view.findViewById(R.id.fragment_user_date)).setText(sBeginDate + " 至 " + sEndDate);

        initAccessView();

        initSalesView();

        initStoreView();

        initUserView();

    }

    private void initData() {
        requestStoreData();
    }

    private void initAccessView() {

        data_view_access = (LinearLayout) view.findViewById(R.id.data_view_access);
        data_view_access.setOnClickListener(this);
        data_view_access.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                data_view_access.getViewTreeObserver().removeOnPreDrawListener(this);
                chart_access_width = data_view_access.getWidth();
                Log.d(TAG, "chart_access_width;" + chart_access_width);
                return true;
            }
        });

        requestAccessData();

    }

    private void initSalesView() {

        data_view_sales = (LinearLayout) view.findViewById(R.id.data_view_sales);
        data_view_sales.setOnClickListener(this);
        data_view_sales.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                data_view_sales.getViewTreeObserver().removeOnPreDrawListener(this);
                chart_sales_width = data_view_sales.getWidth();
                Log.d(TAG, "chart_sales_width;" + chart_sales_width);
                return true;
            }
        });

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
        data_view_user.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                data_view_user.getViewTreeObserver().removeOnPreDrawListener(this);
                chart_user_width = data_view_user.getWidth();
                Log.d(TAG, "chart_user_width;" + chart_user_width);
                return true;
            }
        });

        requestUseData();

    }

    private void initAccessData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_access_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_access));
        chart_access = new XYLineView(activity);
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

                        String xdata = dataList.get(dataList.size() - 1 - i).get("created");
                        String ydata = dataList.get(dataList.size() - 1 - i).get("uv");

                        fields[i] = xdata.substring(6);
                        data[i] = Float.parseFloat(ydata);

                        if (yesterday.equals(xdata)) {
                            ((TextView) view.findViewById(R.id.fragment_access_amount)).setText(ydata);
                        }

                    }
                    chart_access.setData(data);
                    chart_access.setField(fields);
                } else {
                    chart_access.setData(new float[]{});
                    chart_access.setField(new String[]{});
                }
            } else {
                chart_access.setData(new float[]{});
                chart_access.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initAccessData", e.getMessage());
            chart_access.setData(new float[]{});
            chart_access.setField(new String[]{});
        }
        chart_access.postInvalidate();
    }

    private void initSalesData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_sales_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_sales));
        chart_sales = new BarView(activity);
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

                    fields[i] = xdata.substring(6);
                    data[i] = Float.parseFloat(ydata);

                    if (yesterday.equals(xdata)) {
                        ((TextView) view.findViewById(R.id.fragment_sales_amount)).setText(((int) data[i]) + "");
                    }
                }
                chart_sales.setData(data);
                chart_sales.setField(fields);
            } else {
                chart_sales.setData(new float[]{});
                chart_sales.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initSalesData", e.getMessage());
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
                int count = 0;
                for (int i = 0; i < viewtable.size() && i < 8; i++) {

                    Map<String, String> item = new HashMap();

                    String coloum1, coloum2;

                    coloum1 = viewtable.get(i).get("prd_neme");
                    coloum2 = viewtable.get(i).get("number") + viewtable.get(i).get("sales");

                    item.put("product_name", coloum1);
                    item.put("product_num", coloum2);
                    item.put("product_percent", ((int) ((Float.parseFloat(viewtable.get(i).get("number"))) / (Float.parseFloat(viewtable.get(0).get("number"))) * 100)) + "");

                    count += Integer.parseInt(viewtable.get(i).get("number"));

                    storeList.add(item);
                }

                ((TextView) view.findViewById(R.id.fragment_store_amount)).setText(count + "");

                storeAdapter = new StoreAdapter(activity, storeList, R.layout.item_processbar);

                listview_store.setAdapter(storeAdapter);

                listview_store.setVisibility(View.VISIBLE);
                view.findViewById(R.id.data_view_store_tips).setVisibility(View.GONE);

            } else {
                listview_store.setVisibility(View.GONE);
                view.findViewById(R.id.data_view_store_tips).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG + ":initStoreData", e.getMessage());

            listview_store.setVisibility(View.GONE);
            view.findViewById(R.id.data_view_store_tips).setVisibility(View.VISIBLE);
        }


    }

    private void initUserData(String json) {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_user_width,
                (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
        chart_user = new XYLineView(activity);
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

                    String xdata = viewtable.get(viewtable.size() - 1 - i).get("CREATETIME");
                    String ydata = viewtable.get(viewtable.size() - 1 - i).get("COUN");

                    fields[i] = xdata.substring(6);
                    data[i] = Float.parseFloat(ydata);

                    if (yesterday.equals(xdata)) {
                        ((TextView) view.findViewById(R.id.fragment_user_amount)).setText(ydata);
                    }

                }
                chart_user.setData(data);
                chart_user.setField(fields);
            } else {
                chart_user.setData(new float[]{});
                chart_user.setField(new String[]{});
            }
        } catch (Exception e) {
            Log.e(TAG + ":initUserData", e.getMessage());
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
        parems.put("et", tomorrow);

        // parems.put("st", "2019-05-01");
        // parems.put("et", "2019-05-30");

        sendRequest(parems, HANDLER_DATA_ACCESS);

    }

    private void requestSalesData() {

        Map<String, String> parems = new HashMap();

        parems.put("VKORG", "1250");
        parems.put("start", sBeginDate);
        parems.put("end", tomorrow);
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
        parems.put("start", yesterday);
        parems.put("end", sEndDate);

        parems.put("pages", "1");
        parems.put("numbers", "10000");

        parems.put("start", "2015-05-01");
        parems.put("end", "2015-05-30");

        sendRequest(parems, HANDLER_DATA_STORE);
    }

    private void requestUseData() {

        Map<String, String> parems = new HashMap();

        parems.put("type", "nuser");
        parems.put("start", sBeginDate);
        parems.put("end", tomorrow);

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
    private final int HANDLER_DATA_USER = 4;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

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
