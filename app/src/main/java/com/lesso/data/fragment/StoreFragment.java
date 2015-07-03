package com.lesso.data.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.adapter.StoreAdapter;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.TimeChooserDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/21.
 */
public class StoreFragment extends ListFragment {

    private String TAG = "com.lesso.data.fragment.StoreFragment";

    private MainActivity activity;

    private TimeChooserDialog timerDialog;
    private int timeType = 1;
    private RelativeLayout time_chooser;
    private String sBeginDate, sEndDate;

    private RelativeLayout searcher;
    private EditText searcher_text;
    private ImageView searcher_icon;

    List<Map<String, String>> list = new ArrayList();
    private StoreAdapter adapter;
    private LinearLayout list_content;

    private int tabType = 1;
    private LinearLayout tab_store_out, tab_store_in, tab_store_all;

    private View view;

    private Animation roatAnim;
    private Button btn_toogle_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, null);

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

        list_content = (LinearLayout) view.findViewById(R.id.list_content);

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(StoreFragment.this);
            }
        });

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
                    sendRequest(generateParam());
                }
            }
        });

        tab_store_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

    }

    private void toogleTab(int tabType) {

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

    private void initData() {

        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }

    public void fillData(List<Map<String, String>> data) {

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
                adapter = new StoreAdapter(activity, list, R.layout.item_processbar);
                setListAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        parems.put("uid", "39");

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

    private void sendRequest(Map<String, String> parems) {

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
