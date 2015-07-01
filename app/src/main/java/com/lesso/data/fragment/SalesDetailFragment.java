package com.lesso.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.TimeChooserDialog;
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
 * Created by meisl on 2015/6/23.
 */
public class SalesDetailFragment extends ListFragment {

    private String TAG = "com.lesso.data.fragment.SalesDetailFragment";

    private MainActivity activity;

    private TimeChooserDialog timerDialog;
    private int timeType = 1;
    private RelativeLayout time_chooser;
    private String sBeginDate, sEndDate;

    List<Map<String, String>> list = new ArrayList();
    private SalesDetailAdapter adapter;
    private LinearLayout list_content;
    private LinearLayout header;

    private int tabType = 1;
    private LinearLayout tab_sales_amount, tab_sales_paper, tab_sales_car, tab_sales_type;

    private View view;

    private Animation roatAnim;
    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sales_detail, null);

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

        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(SalesDetailFragment.this);
            }
        });


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
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_sales_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_sales_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_sales_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 4) {
                    tabType = 4;
                    toogleHeader(tabType);
                    toogleTab(tabType);
                    sendRequest(generateParam());

                }
            }
        });

    }

    private void toogleHeader(int tabType) {

        list_content = (LinearLayout) view.findViewById(R.id.list_content);
        if (header != null) {
            list_content.removeView(header);
        }

        if (tabType == 2) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);
        } else if (tabType == 3) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid, null);
        } else if (tabType == 4) {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);
        } else {
            header = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.item_grid1, null);
        }

        TextView a = ((TextView) header.findViewById(R.id.colum1));
        a.setText(tabType == 4 ? "物料组" : "日期");
        a.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        a.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C5));
        TextView b = ((TextView) header.findViewById(R.id.colum2));
        b.setText(tabType == 2 ? "单据量" : tabType == 3 ? "车牌号码" : tabType == 4 ? "占比" : "销售额");
        b.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        b.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));

        if (tabType == 3) {
            TextView c = ((TextView) header.findViewById(R.id.colum3));
            c.setText("车次量");
            c.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            c.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
        }

        list_content.addView(header, 0);

    }

    private void toogleTab(int tabType) {

        tab_sales_amount.setSelected(tabType == 2 || tabType == 3 || tabType == 4 ? false : true);
        tab_sales_paper.setSelected(tabType == 2 ? true : false);
        tab_sales_car.setSelected(tabType == 3 ? true : false);
        tab_sales_type.setSelected(tabType == 4 ? true : false);

    }

    private void initData() {

        toogleHeader(tabType);

        /**
         * 发送请求
         */
        sendRequest(generateParam());

    }

    public void fillData(List<Map<String, String>> data) {

        if (data != null && data.size() > 0) {

            boolean flag = (tabType == 3 ? true : false);
            list.clear();
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "", coloum3 = "";

                if (tabType == 2) {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("ZMONTH");
                        coloum2 = data.get(i).get("ZTOTLEM");
                    } else {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZNUMBER");
                    }
                } else if (tabType == 3) {
                    coloum1 = data.get(i).get("ERDAT");
                    coloum2 = data.get(i).get("ZZCHHAO");
                    coloum3 = data.get(i).get("ZCOUNT");
                } else if (tabType == 4) {
                    coloum1 = data.get(i).get("MATKL");
                    coloum2 = data.get(i).get("ZTOTLE");
                } else {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("ZMONTH");
                        coloum2 = data.get(i).get("ZTOTLEM");
                    } else {
                        coloum1 = data.get(i).get("ZDATE");
                        coloum2 = data.get(i).get("ZTOTLE");
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

                adapter = new SalesDetailAdapter(activity, list, R.layout.item_grid1);
                adapter.setColoumCount(2);
                setListAdapter(adapter);

            } else {

                if (adapter.getColoumCount() == 2) {

                    if (flag) {
                        adapter.notifyDataSetInvalidated();
                        adapter = new SalesDetailAdapter(activity, list, R.layout.item_grid);
                        adapter.setColoumCount(3);
                        setListAdapter(adapter);
                    }

                } else if (adapter.getColoumCount() == 3) {

                    if (!flag) {

                        adapter.notifyDataSetInvalidated();
                        adapter = new SalesDetailAdapter(activity, list, R.layout.item_grid1);
                        adapter.setColoumCount(2);
                        setListAdapter(adapter);
                    }

                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private Map<String, String> generateParam() {

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
            parems.put("type", "CAR");
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

    private void sendRequest(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(activity, Constant.URL_REPORT_SALES, requestParams, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                Message message = mHandler.obtainMessage();
                message.what = HANDLER_SROAT;
                message.sendToTarget();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
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
                        Toast.makeText(activity, "数据内容为空", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "数据请求错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showTimerDialog() {

        timerDialog = new TimeChooserDialog(activity, timeType, sBeginDate, sEndDate);
        timerDialog.setCanceledOnTouchOutside(true);
        timerDialog.show();
        timerDialog.setClickListenerInterface(new TimeChooserDialog.ClickListenerInterface() {
            @Override
            public void doFinish() {

                if (timeType != timerDialog.getType()) {
                    timeType = timerDialog.getType();
                }
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    class SalesDetailAdapter extends BaseAdapter {

        private int coloumCount = 2;

        private Context context;
        private LayoutInflater layoutInflater;
        private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        private int layoutlistid;

        public SalesDetailAdapter(Context context, List<Map<String, String>> listobject, int listcontextid) {
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
                colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
            } else {
                colum1.setBackgroundColor(activity.getResources().getColor(R.color.REPORT_UI_C6));
                colum2.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
            }


            if (getColoumCount() == 3) {

                TextView colum3 = (TextView) listviewitem.findViewById(R.id.colum3);
                colum3.setText(map.get(colum3.getTag()));

                if (position % 2 > 0) {
                    colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left1));
                } else {
                    colum3.setBackground(activity.getResources().getDrawable(R.drawable.border_left));
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
