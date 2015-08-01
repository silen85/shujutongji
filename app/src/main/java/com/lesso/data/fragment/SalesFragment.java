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

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/23.
 */
public class SalesFragment extends BaseGraphFragment {

    private String TAG = "com.lesso.data.fragment.SalesFragment";

    private int screenWidth, screenHeight;
    private int chart_sales_width;

    private HorizontalScrollView scrollview_linearlayout_sales;

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

        scrollview_linearlayout_sales = (HorizontalScrollView) view.findViewById(R.id.scrollview_linearlayout_sales);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (tabType == 1) {
            if (!activity.AUTHORITY_SALES_AMOUNT) {
                btn_toogle_fragment.setClickable(false);
            } else {
                btn_toogle_fragment.setClickable(true);
                hideAuthority();
                initData();
            }
        } else {
            hideAuthority();
            initData();
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
                    toogleTab(tabType);
                    toogleTime();
                    if (activity.AUTHORITY_SALES_AMOUNT) {
                        btn_toogle_fragment.setClickable(true);
                        if (adapter != null) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        initData();
                    } else {
                        btn_toogle_fragment.setClickable(false);
                        displayAuthority();
                    }
                }
            }
        });

        tab_sales_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    btn_toogle_fragment.setClickable(true);
                    toogleTab(tabType);
                    toogleTime();
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    initData();
                    hideAuthority();
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
                    initData();
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
                    initData();
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


        Message message = mHandler.obtainMessage();
        message.what = HANDLER_SROAT;
        message.sendToTarget();

        String cache = null;
        if (sBeginDate.equals(activity.getSalesDataCache().get("sBeginDate"))
                && sEndDate.equals(activity.getSalesDataCache().get("sEndDate"))
                && (tabType + "").equals(activity.getSalesDataCache().get("tabType"))
                && (timeType + "").equals(activity.getSalesDataCache().get("timeType"))) {

            String dataType = "";
            if (tabType == 2) {
                dataType = "NUMBER";
            } else if (tabType == 3) {
                dataType = "CAR";
            } else if (tabType == 4) {
                dataType = "CLASS";
            } else {
                dataType = "MONEY";
            }

            if (timeType == 2)
                dataType += "_MONTH";

            cache = activity.getSalesDataCache().get(dataType);
        }


        if (cache != null && !"".equals(cache.trim())) {
            Message _message = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("json", cache);
            _message.what = HANDLER_DATA;
            _message.setData(bundle);
            _message.sendToTarget();
        } else {
            sendRequest(generateParam());
        }
    }

    private void sortMoneyMonth() throws ParseException {

        if (list != null && list.size() > 0) {
            Object[] data = list.toArray();
            for (int i = 0; i < data.length; i++) {
                for (int j = i + 1; j < data.length; j++) {
                    Date a = Constant.DATE_FORMAT_3.parse(((Map<String, String>) data[i]).get("colum1"));
                    Date b = Constant.DATE_FORMAT_3.parse(((Map<String, String>) data[j]).get("colum1"));
                    int k = a.compareTo(b);
                    if (k >= 1) {
                        Map<String, String> temp = ((Map<String, String>) data[i]);
                        data[i] = data[j];
                        data[j] = temp;
                    }
                }
            }

            list.clear();
            for (int i = 0; i < data.length; i++) {
                list.add((Map<String, String>) data[i]);
            }
        }
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
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("ZMONTH");
                        coloum2 = data.get(i).get("ZTOTLE");
                    } else {
                        coloum1 = data.get(i).get("ERDAT");
                        coloum2 = data.get(i).get("ZCOUNT");
                    }
                } else if (tabType == 4) {
                    coloum1 = data.get(i).get("WGBEZ") + " ";
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

            /**
             * 因为后台按月查询没有排序
             */
            if (tabType == 1 && timeType == 2) {
                try {
                    sortMoneyMonth();
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
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
                item.put("product_percent", ((int) ((Float.parseFloat(coloum2)) / (Float.parseFloat(data.get(0).get("colum2"))) * 10000)) + "");
                //item.put("product_percent", ((int) ((Float.parseFloat(coloum2)) / classTotal * 10000)) + "");

            }

            if (adapter == null) {
                adapter = new HorizontalBarAdapter(activity, list, R.layout.item_processbar);
                listView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
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

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (chart_bar != null && chart_bar.getDataSize() > 5) {
                    scrollview_linearlayout_sales.scrollTo(chart_bar.getRight(), 0);
                }
            }
        }, 800);

    }

    protected Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        parems.put("VKORG", "1250");

        if (sBeginDate != null && !"".equals(sBeginDate.trim()))
            parems.put("start", sBeginDate);
        if (sEndDate != null && !"".equals(sEndDate.trim()))
            parems.put("end", sEndDate);

        /**
         * 这里是四合一请求时的参数

         parems.put("VBELN", "00");
         if (timeType == 2) {
         parems.put("type", "MONTH");
         } else {
         parems.put("type", "day");
         }
         */

        /**这里是单个请求时的参数
         *
         **/
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
            parems.put("VBELN", "03");
            parems.put("type", "CLASS_MONTH");
        } else {
            parems.put("VBELN", "02");
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
                activity.loading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
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
                /*Message message = mHandler.obtainMessage();
                message.what = HANDLER_EROAT;
                message.sendToTarget();*/
                activity.disLoading();
            }

        };

        asyncHttpResponseHandler.setCharset("GBK");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.CONNECT_TIMEOUT);
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

                        if (viewtable != null && viewtable.size() > 0) {
                            activity.getSalesDataCache().put("sBeginDate", sBeginDate);
                            activity.getSalesDataCache().put("sEndDate", sEndDate);
                            activity.getSalesDataCache().put("tabType", tabType + "");
                            activity.getSalesDataCache().put("timeType", timeType + "");

                            String dataType = "";
                            if (tabType == 2) {
                                dataType = "NUMBER";
                            } else if (tabType == 3) {
                                dataType = "CAR";
                            } else if (tabType == 4) {
                                dataType = "CLASS";
                            } else {
                                dataType = "MONEY";
                            }

                            if (timeType == 2)
                                dataType += "_MONTH";

                            activity.getSalesDataCache().put(dataType, json);
                        }

                        fillData(viewtable);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
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
