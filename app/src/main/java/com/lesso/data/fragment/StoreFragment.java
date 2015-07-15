package com.lesso.data.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.adapter.HorizontalBarAdapter;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/21.
 */
public class StoreFragment extends BaseGraphFragment {

    private String TAG = "com.lesso.data.fragment.StoreFragment";

    private RelativeLayout searcher;
    private EditText searcher_text;
    private ImageView searcher_icon;

    private HorizontalBarAdapter adapter;
    private LinearLayout list_content;
    private ListView listView;

    private LinearLayout tab_store_out, tab_store_in, tab_store_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_store, null);

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }

    protected void initView() {

        list_content = (LinearLayout) view.findViewById(R.id.list_content);
        listView = (ListView) list_content.findViewById(R.id.listView);

        searcher = (RelativeLayout) view.findViewById(R.id.searcher);
        searcher_text = (EditText) view.findViewById(R.id.searcher_text);
        searcher_icon = (ImageView) view.findViewById(R.id.searcher_icon);
        searcher_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searcher.setSelected(b);
                searcher_icon.setSelected(b);
            }
        });
        searcher_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(generateParam());
            }
        });

        initTime();

        initBtnToogle();

        tab_store_out = (LinearLayout) view.findViewById(R.id.tab_store_out);
        tab_store_in = (LinearLayout) view.findViewById(R.id.tab_store_in);
        tab_store_all = (LinearLayout) view.findViewById(R.id.tab_store_all);

        toogleTab(tabType);

        tab_store_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    toogleTab(tabType);
                    toogleTime();

                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    sendRequest(generateParam());
                }
            }
        });

        tab_store_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    toogleTab(tabType);
                    toogleTime();

                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    sendRequest(generateParam());
                }
            }
        });

        tab_store_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    //tabType = 3;
                    //toogleTab(tabType);
                    //toogleTime();
                    //sendRequest(generateParam());

                    tabType = 3;
                    btn_toogle_fragment.performClick();
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
        tab_store_out.setSelected(tabType == 2 || tabType == 3 ? false : true);
        tab_store_in.setSelected(tabType == 2 ? true : false);
        tab_store_all.setSelected(tabType == 3 ? true : false);

        if (tabType == 3) {

            time_chooser.setVisibility(View.GONE);
            searcher.setVisibility(View.VISIBLE);

        } else {

            time_chooser.setVisibility(View.VISIBLE);
            searcher.setVisibility(View.GONE);

        }
    }

    public void initData() {

        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {

        if (data != null && data.size() > 0) {

            list.clear();
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1, coloum2;

                coloum1 = data.get(i).get("prd_neme");
                coloum2 = data.get(i).get("number") + data.get(i).get("sales");

                item.put("product_name", coloum1);
                item.put("product_num", coloum2);
                item.put("product_percent", ((int) ((Float.parseFloat(data.get(i).get("number"))) / (Float.parseFloat(data.get(0).get("number"))) * 100)) + "");

                list.add(item);
            }

            if (adapter == null) {
                adapter = new HorizontalBarAdapter(activity, list, R.layout.item_processbar);
                listView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

    protected Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        parems.put("uid", "39");

        parems.put("pages", "1");
        parems.put("numbers", "10000");

        if (tabType == 3) {
            Log.d(TAG, Tools.encodeContent(Tools.encodeContent(searcher_text.getText().toString())));
            parems.put("txt", Tools.encodeContent(Tools.encodeContent(searcher_text.getText().toString())));
        }

        if (sBeginDate != null && !"".equals(sBeginDate.trim()))
            parems.put("start", sBeginDate);
        if (sEndDate != null && !"".equals(sEndDate.trim()))
            parems.put("end", sEndDate);

        if (tabType == 2) {
            parems.put("type", "in");
        } else if (tabType == 3) {
            parems.put("type", "sel");
        } else {
            parems.put("type", "out");
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
        client.setTimeout(Constant.CONNECT_TIMEOUT);
        client.get(activity, Constant.URL_REPORT_STORE, requestParams, asyncHttpResponseHandler);

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

                            List<Map<String, String>> dataCache = viewtable;
                            fillData(dataCache);
                        } else {
                            if (list != null && list.size() > 0) {
                                list.clear();
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + json);
                        if (list != null && list.size() > 0) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    if (tabType != 3) {
                        roatStart();
                    }
                    break;
                case HANDLER_EROAT:
                    /*if (tabType != 3) {
                        btn_toogle_fragment.clearAnimation();
                    }*/
                    break;
                case HANDLER_NETWORK_ERR:
                    if (list != null && list.size() > 0) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_data_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
