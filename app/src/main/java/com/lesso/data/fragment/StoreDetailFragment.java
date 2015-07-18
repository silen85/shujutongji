package com.lesso.data.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meisl on 2015/6/21.
 */
public class StoreDetailFragment extends BaseListFragment {

    private String TAG = "com.lesso.data.fragment.StoreDetailFragment";

    private RelativeLayout searcher;
    private EditText searcher_text;
    private ImageView searcher_icon;

    private StoreDetailAdapter adapter;
    private LinearLayout list_content;
    private LinearLayout header;

    private LinearLayout tab_store_out, tab_store_in, tab_store_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store_detail, null);

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
                    toogleHeader(tabType);
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
                    toogleHeader(tabType);
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
                    tabType = 3;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    toogleTime();

                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    //sendRequest(generateParam());
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

        if (header == null) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);

            TextView a = ((TextView) header.findViewById(R.id.colum1));
            a.setText("编号");
            a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
            TextView b = ((TextView) header.findViewById(R.id.colum2));
            b.setText("名称");
            b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            b.setBackgroundResource(R.drawable.border_left1);
            //b.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
            TextView c = ((TextView) header.findViewById(R.id.colum3));
            c.setText(tabType == 2 ? "收货量" : tabType == 3 ? "库存" : "出货量");
            c.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            c.setBackgroundResource(R.drawable.border_left1);
            //c.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));

            list_content.addView(header, 0);

        } else {

            TextView c = ((TextView) header.findViewById(R.id.colum3));
            c.setText(tabType == 2 ? "收货量" : tabType == 3 ? "库存" : "出货量");

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
            btn_toogle_fragment.setVisibility(View.GONE);

        } else {

            time_chooser.setVisibility(View.VISIBLE);
            searcher.setVisibility(View.GONE);
            btn_toogle_fragment.setVisibility(View.VISIBLE);

        }


    }


    public void initData() {

        toogleHeader(tabType);

        /**
         * 发送请求
         */
        if (tabType != 3)
            sendRequest(generateParam());

    }

    protected void fillData(List<Map<String, String>> data) {

        if (data != null && data.size() > 0) {

            list.clear();
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "", coloum3 = "";

                coloum1 = data.get(i).get("prd_num");
                coloum2 = data.get(i).get("prd_neme");
                coloum3 = data.get(i).get("number") + data.get(i).get("sales");


                item.put("colum1", coloum1);
                item.put("colum2", coloum2);
                item.put("colum3", coloum3);

                list.add(item);
            }

            if (adapter == null) {
                adapter = new StoreDetailAdapter(activity, list, R.layout.item_grid);
                setListAdapter(adapter);
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
                activity.loading();
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
                activity.disLoading();
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
                        Log.e(TAG,e.getMessage(),e);
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


    class StoreDetailAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public StoreDetailAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
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

            View listviewitem = null;

            if (view != null) {
                listviewitem = view;
            } else {
                listviewitem = layoutInflater.inflate(this.layoutlistid, null);
            }

            this.writeData(listviewitem, i);

            return listviewitem;
        }


        private void writeData(View listviewitem, int position) {

            Map<String, String> map = list.get(position);


            TextView colum1 = (TextView) listviewitem.findViewById(R.id.colum1);
            TextView colum2 = (TextView) listviewitem.findViewById(R.id.colum2);
            TextView colum3 = (TextView) listviewitem.findViewById(R.id.colum3);

            colum1.setText(map.get(colum1.getTag()));
            colum2.setText(map.get(colum2.getTag()));
            colum3.setText(map.get(colum3.getTag()));


            if (position % 2 > 0) {
                colum1.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                colum2.setBackgroundResource(R.drawable.border_left1);
                colum3.setBackgroundResource(R.drawable.border_left1);
                //colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
                //colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
            } else {
                colum1.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                colum2.setBackgroundResource(R.drawable.border_left);
                colum3.setBackgroundResource(R.drawable.border_left);
                //colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
                //colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
            }

        }

    }


}
