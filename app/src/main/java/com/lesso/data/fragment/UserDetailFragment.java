package com.lesso.data.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Arith;
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
 * Created by meisl on 2015/6/24.
 */
public class UserDetailFragment extends BaseListFragment {

    private String TAG = "com.lesso.data.fragment.UserDetailFragment";

    private AccessDetailAdapter adapter;
    private LinearLayout list_content;
    private LinearLayout header;

    private LinearLayout tab_user_new, tab_user_total, tab_user_supplier, tab_user_fivemetal;

    private int topMargin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_detail, null);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initData();

    }

    protected void initView() {

        list_content = (LinearLayout) view.findViewById(R.id.list_content);

        initTime();

        initBtnToogle();

        tab_user_new = (LinearLayout) view.findViewById(R.id.tab_user_new);
        tab_user_total = (LinearLayout) view.findViewById(R.id.tab_user_total);
        tab_user_supplier = (LinearLayout) view.findViewById(R.id.tab_user_supplier);
        tab_user_fivemetal = (LinearLayout) view.findViewById(R.id.tab_user_fivemetal);

        toogleTab(tabType);

        tab_user_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 1) {
                    tabType = 1;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    toogleTime();
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    toogleTime();
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    toogleTime();
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_fivemetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 4) {
                    tabType = 4;
                    toogleHeader(tabType);
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

        if (tabType == 2) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);
        } else if (tabType == 3) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);
        } else if (tabType == 4) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);
        } else {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);
        }

        TextView a = ((TextView) header.findViewById(R.id.colum1));
        a.setText(tabType == 2 ? "用户类别" : tabType == 3 ? "所在地" : tabType == 4 ? "所在地" : "日期");
        a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView b = ((TextView) header.findViewById(R.id.colum2));
        b.setText(tabType == 2 || tabType == 3 || tabType == 4 ? "数量" : "新增用户");
        b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        b.setBackgroundResource(R.drawable.border_left1);
        //b.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));

        if (tabType == 2 || tabType == 3 || tabType == 4) {
            TextView c = ((TextView) header.findViewById(R.id.colum3));
            c.setText("比例");
            c.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            c.setBackgroundResource(R.drawable.border_left1);
            //c.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
        }

        list_content.addView(header, 0);

    }

    public void toogleTab(int tabType) {
        super.toogleTab(tabType);
        tab_user_new.setSelected(tabType == 2 || tabType == 3 || tabType == 4 ? false : true);
        tab_user_total.setSelected(tabType == 2 ? true : false);
        tab_user_supplier.setSelected(tabType == 3 ? true : false);
        tab_user_fivemetal.setSelected(tabType == 4 ? true : false);

        if (tabType == 2 || tabType == 3 || tabType == 4) {
            time_chooser.setVisibility(View.GONE);
            ((FrameLayout.LayoutParams) list_content.getLayoutParams()).setMargins(0, 0, 0, 0);
        } else {
            time_chooser.setVisibility(View.VISIBLE);
            topMargin = ((FrameLayout.LayoutParams) list_content.getLayoutParams()).topMargin;
            if (topMargin <= 0) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                topMargin = (int) (48f * dm.density);
            }
            ((FrameLayout.LayoutParams) list_content.getLayoutParams()).setMargins(0, topMargin, 0, 0);
        }

    }

    public void initData() {

        toogleHeader(tabType);

        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }


    protected void fillData(List<Map<String, String>> data) {

        if (data != null && data.size() > 0) {

            boolean flag = (tabType == 2 || tabType == 3 || tabType == 4 ? true : false);
            list.clear();
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "", coloum3 = "";

                if (tabType == 2) {
                    coloum1 = data.get(i).get("CUSTOMTYPE");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "%";
                    } catch (Exception e) {
                    }
                } else if (tabType == 3) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "%";
                    } catch (Exception e) {
                    }
                } else if (tabType == 4) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 2) + "%";
                    } catch (Exception e) {
                    }
                } else {
                    if (timeType == 2) {
                        coloum1 = data.get(data.size() - 1 - i).get("CREATETIME");
                        coloum2 = data.get(data.size() - 1 - i).get("COUN");
                    } else {
                        coloum1 = data.get(data.size() - 1 - i).get("CREATETIME");
                        coloum2 = data.get(data.size() - 1 - i).get("COUN");
                    }
                }

                item.put("colum1", coloum1);
                item.put("colum2", coloum2);

                if (flag) {
                    item.put("colum3", coloum3);
                }

                list.add(item);
            }

            if (adapter == null) {

                if (flag) {
                    adapter = new AccessDetailAdapter(activity, list, R.layout.item_grid);
                    adapter.setColoumCount(3);
                    setListAdapter(adapter);
                } else {
                    adapter = new AccessDetailAdapter(activity, list, R.layout.item_grid1);
                    adapter.setColoumCount(2);
                    setListAdapter(adapter);
                }

            } else {

                if (adapter.getColoumCount() == 2) {

                    if (flag) {
                        adapter.notifyDataSetInvalidated();
                        adapter = new AccessDetailAdapter(activity, list, R.layout.item_grid);
                        adapter.setColoumCount(3);
                        setListAdapter(adapter);
                    }

                } else if (adapter.getColoumCount() == 3) {

                    if (!flag) {

                        adapter.notifyDataSetInvalidated();
                        adapter = new AccessDetailAdapter(activity, list, R.layout.item_grid1);
                        adapter.setColoumCount(2);
                        setListAdapter(adapter);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    protected Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        if (tabType == 2) {
            parems.put("type", "Tatol");
        } else if (tabType == 3) {
            parems.put("type", "supplier");
        } else if (tabType == 4) {
            parems.put("type", "store");
        } else {
            if (sBeginDate != null && !"".equals(sBeginDate.trim()))
                parems.put("start", sBeginDate);
            if (sEndDate != null && !"".equals(sEndDate.trim()))
                parems.put("end", sEndDate);

            if (timeType == 2) {
                parems.put("type", "nuser_moth");
                try {
                    parems.put("start", sBeginDate.substring(0, 7));
                    parems.put("end", sEndDate.substring(0, 7));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                parems.put("type", "nuser");
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
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_EROAT;
                message.sendToTarget();
                activity.disLoading();
            }
        };
        asyncHttpResponseHandler.setCharset("GBK");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.CONNECT_TIMEOUT);
        client.post(activity, Constant.URL_REPORT_USER, requestParams, asyncHttpResponseHandler);

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
                        Log.e(TAG, e.getMessage(), e);
                        if (list != null && list.size() > 0) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case HANDLER_SROAT:
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    roatStart();
                    break;
                case HANDLER_EROAT:
                    //btn_toogle_fragment.clearAnimation();
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

    class AccessDetailAdapter extends BaseAdapter {

        private int coloumCount = 2;

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

            TextView colum1 = (TextView) listviewitem.findViewById(R.id.colum1);
            TextView colum2 = (TextView) listviewitem.findViewById(R.id.colum2);

            colum1.setText(map.get(colum1.getTag()));
            colum2.setText(map.get(colum2.getTag()));

            if (position % 2 > 0) {
                colum1.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
                colum2.setBackgroundResource(R.drawable.border_left1);
                //colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
            } else {
                colum1.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                colum2.setBackgroundResource(R.drawable.border_left);
                //colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
            }


            if (getColoumCount() == 3) {

                TextView colum3 = (TextView) listviewitem.findViewById(R.id.colum3);
                colum3.setText(map.get(colum3.getTag()));

                if (position % 2 > 0) {
                    colum3.setBackgroundResource(R.drawable.border_left1);
                    //colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
                } else {
                    colum3.setBackgroundResource(R.drawable.border_left);
                    //colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
                }
            }
        }

        public int getColoumCount() {
            return coloumCount;
        }

        public void setColoumCount(int coloumCount) {
            this.coloumCount = coloumCount;
        }
    }

}
