package com.lesso.data.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.MD5;
import com.lesso.data.common.Tools;
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
public class AccessDetailFragment extends BaseListFragment {

    private String TAG = "com.lesso.data.fragment.AccessDetailFragment";

    private AccessDetailAdapter adapter;
    private LinearLayout list_content;
    private LinearLayout header;

    private LinearLayout tab_uv, tab_pv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_access_detail, null);

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

        initTime();

        initBtnToogle();

        adapter = new AccessDetailAdapter(activity, list, R.layout.item_grid1);
        setListAdapter(adapter);

        tab_uv = (LinearLayout) view.findViewById(R.id.tab_uv);
        tab_pv = (LinearLayout) view.findViewById(R.id.tab_pv);

        toogleHeader(tabType);
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
        if (adapter != null) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }

    protected void toogleHeader(int tabType) {

        if (header != null) {
            list_content.removeView(header);
        }

        header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);

        TextView a = ((TextView) header.findViewById(R.id.colum1));
        a.setText("日期");
        a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView b = ((TextView) header.findViewById(R.id.colum2));
        b.setText("访问量");
        b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        b.setBackgroundResource(R.drawable.border_left1);
        //b.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));

        list_content.addView(header, 0);

    }

    public void toogleTab(int tabType) {
        super.toogleTab(tabType);
        tab_uv.setSelected(tabType != 2 ? true : false);
        tab_pv.setSelected(tabType == 2 ? true : false);
    }

    public void initData() {

        toogleHeader(tabType);

        sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {
        list.clear();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                item.put("colum1", data.get(data.size() - 1 - i).get("created"));

                if (tabType == 2)
                    item.put("colum2", data.get(data.size() - 1 - i).get("pv"));
                else
                    item.put("colum2", data.get(data.size() - 1 - i).get("uv"));

                list.add(item);
            }
        }
        adapter.notifyDataSetChanged();
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
                            if (list != null && list.size() > 0) {
                                list.clear();
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage(),e);
                        if (list != null && list.size() > 0) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    roatStart();
                    break;
                case HANDLER_EROAT:
                    //roatEnd();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class AccessDetailAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public AccessDetailAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(this.context);
            this.list = listobject;
            this.layoutlistid = listcontextid;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = layoutInflater.inflate(this.layoutlistid, null);
            }

            this.writeData(view, i);

            return view;
        }


        private void writeData(View listviewitem, int position) {

            Map<String, String> map = (Map<String, String>) getItem(position);

            TextView date = (TextView) listviewitem.findViewById(R.id.colum1);
            TextView amount = (TextView) listviewitem.findViewById(R.id.colum2);

            date.setText(map.get(date.getTag()));
            amount.setText(map.get(amount.getTag()));

            if (position % 2 > 0) {
                date.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                amount.setBackgroundResource(R.drawable.border_left1);
                //amount.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
            } else {
                date.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                amount.setBackgroundResource(R.drawable.border_left);
                //amount.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
            }

        }

    }

}
