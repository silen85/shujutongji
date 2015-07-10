package com.lesso.data.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.adapter.HorizontalBarAdapter;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.BarView2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/23.
 */
public class SalesFragment1 extends BaseGraphFragment {

    private String TAG = "com.lesso.data.fragment.SalesFragment";

    private int screenWidth, screenHeight;
    private int chart_sales_width;

    private LinearLayout data_view, list_content, chart_bar_container, data_view_sales;
    private BarView2 chart_bar;
    private HorizontalBarAdapter adapter;
    private ListView listView;

    private LinearLayout tab_sales_amount, tab_sales_paper, tab_sales_car, tab_sales_type;

    private float classTotal = 0.00f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_sales1, null);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initData();

        if (tabType == 2) {
            if (!activity.AUTHORITY_SALES_AMOUNT) {
                btn_toogle_fragment.setClickable(false);
            } else {
                btn_toogle_fragment.setClickable(true);
                hideAuthority();
            }
        } else {
            hideAuthority();
        }

    }

    protected void initView() {

        initTime();

        initBtnToogle();

        data_view = (LinearLayout) view.findViewById(R.id.data_view);

        chart_bar_container = (LinearLayout) view.findViewById(R.id.chart_bar_container);
        data_view_sales = (LinearLayout) view.findViewById(R.id.data_view_sales);
        chart_sales_width = screenWidth;

        list_content = (LinearLayout) view.findViewById(R.id.list_content);
        listView = (ListView) view.findViewById(R.id.listView);

        tab_sales_amount = (LinearLayout) view.findViewById(R.id.tab_sales_amount);
        tab_sales_paper = (LinearLayout) view.findViewById(R.id.tab_sales_paper);
        tab_sales_car = (LinearLayout) view.findViewById(R.id.tab_sales_car);
        tab_sales_type = (LinearLayout) view.findViewById(R.id.tab_sales_type);

        toogleTab(tabType);

        tab_sales_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    btn_toogle_fragment.setClickable(true);
                    toogleTab(tabType);
                    toogleTime();
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    sendRequest(generateParam());
                    hideAuthority();
                }
            }
        });

        tab_sales_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    if (activity.AUTHORITY_SALES_AMOUNT) {
                        btn_toogle_fragment.setClickable(true);
                        if (adapter != null) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        sendRequest(generateParam());
                    } else {
                        btn_toogle_fragment.setClickable(false);
                        displayAuthority();
                    }

                    toogleTab(tabType);
                    toogleTime();

                }
            }
        });

        tab_sales_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    btn_toogle_fragment.setClickable(true);
                    toogleTab(tabType);
                    toogleTime();
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    sendRequest(generateParam());
                    hideAuthority();
                }
            }
        });

        tab_sales_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 4) {
                    tabType = 4;
                    btn_toogle_fragment.setClickable(true);
                    toogleTab(tabType);
                    toogleTime();
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    sendRequest(generateParam());
                    hideAuthority();
                }
            }
        });

    }

    @Override
    protected void onBtnToogle() {
        if (adapter != null) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void toogleTab(int tabType) {
        super.toogleTab(tabType);
        tab_sales_amount.setSelected(tabType == 2 || tabType == 3 || tabType == 4 ? false : true);
        tab_sales_paper.setSelected(tabType == 2 ? true : false);
        tab_sales_car.setSelected(tabType == 3 ? true : false);
        tab_sales_type.setSelected(tabType == 4 ? true : false);

        if (tabType == 4) {
            chart_bar_container.setVisibility(View.GONE);
            list_content.setVisibility(View.VISIBLE);
        } else {
            chart_bar_container.setVisibility(View.VISIBLE);
            list_content.setVisibility(View.GONE);
        }

    }

    public void initData() {
        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {

        list.clear();
        classTotal = 0f;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "";

                if (tabType == 2) {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZTOTLE");
                    } else {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZTOTLE");
                    }
                } else if (tabType == 3) {
                    coloum1 = data.get(i).get("ERDAT");
                    coloum2 = data.get(i).get("ZCOUNT");
                } else if (tabType == 4) {
                    coloum1 = data.get(data.size() - 1 - i).get("WGBEZ") + " ";
                    coloum1 = coloum1.substring(coloum1.indexOf("-") > 0 ? coloum1.indexOf("-") + 1 : 0);
                    coloum2 = data.get(i).get("ZTOTLE");

                    classTotal += Float.parseFloat(coloum2);
                } else {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZTOTLE");
                    } else {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZTOTLE");
                    }
                }

                item.put("colum1", coloum1);
                item.put("colum2", coloum2);

                list.add(item);
            }
        }

        if (tabType == 4) {
            fillHorizontalBarData(list);
        } else {
            if (chart_bar != null) {
                data_view_sales.removeView(chart_bar);
            }
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_sales_width,
                    (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
            chart_bar = new BarView2(activity);
            chart_bar.setLayoutParams(lp);
            chart_bar.setScreenWidth(chart_sales_width);
            chart_bar.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
            data_view_sales.addView(chart_bar);

            fillBarData(list);
        }
    }

    private void fillHorizontalBarData(List<Map<String, String>> data) {

        if (data != null && data.size() > 0) {

            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = data.get(i);

                String coloum1, coloum2;

                coloum1 = item.get("colum1");
                coloum2 = item.get("colum2");

                item.put("product_name", coloum1);
                item.put("product_num", coloum2);
                item.put("product_percent", ((int) ((Float.parseFloat(coloum2)) / classTotal * 100)) + "");

            }

            if (adapter == null) {
                adapter = new HorizontalBarAdapter(activity, list, R.layout.item_processbar);
                listView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void fillBarData(List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {

            String[] fields = new String[list.size()];
            float[] dataArr = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(i).get("colum1");
                String ydata = list.get(i).get("colum2");

                fields[i] = xdata.substring(5);
                dataArr[i] = Float.parseFloat(ydata);

            }
            chart_bar.setData(dataArr);
            chart_bar.setField(fields);
        } else {
            chart_bar.setData(new float[]{});
            chart_bar.setField(new String[]{});
        }
        chart_bar.postInvalidate();
    }

    protected Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        parems.put("VKORG", "1250");

        if (sBeginDate != null && !"".equals(sBeginDate.trim()))
            parems.put("start", sBeginDate);
        if (sEndDate != null && !"".equals(sEndDate.trim()))
            parems.put("end", sEndDate);

        if (tabType == 2) {
            parems.put("VBELN", "00");
            if (timeType == 2) {
                parems.put("type", "NUMBER_MONTH");
            } else {
                parems.put("type", "NUMBER");
            }
        } else if (tabType == 3) {
            parems.put("VBELN", "01");
            if (timeType == 2) {
                parems.put("type", "CAR_MONTH");
            } else {
                parems.put("type", "CAR");
            }
        } else if (tabType == 4) {
            parems.put("VBELN", "00");
            parems.put("type", "CLASS");
        } else {
            parems.put("VBELN", "00");
            if (timeType == 2) {
                parems.put("type", "MONEY_MONTH");
            } else {
                parems.put("type", "MONEY");
            }
        }

        return parems;

    }

    protected void sendRequest(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {

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

        };

        asyncHttpResponseHandler.setCharset("GBK");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(activity, Constant.URL_REPORT_SALES, requestParams, asyncHttpResponseHandler);

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
                        List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");
                        fillData(viewtable);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + json);
                        fillData(null);
                        Toast.makeText(activity, getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
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
