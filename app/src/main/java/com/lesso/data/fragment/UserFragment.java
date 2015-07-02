package com.lesso.data.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.lesso.data.R;
import com.lesso.data.activity.MainActivity;
import com.lesso.data.common.Arith;
import com.lesso.data.common.Constant;
import com.lesso.data.common.Tools;
import com.lesso.data.ui.TimeChooserDialog;
import com.lesso.data.ui.XYLineView;
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
public class UserFragment extends Fragment {

    private String TAG = "com.lesso.data.fragment.UserDetailFragment";

    int[] colors = new int[]{R.color.REPORT_TABLE_C1, R.color.REPORT_TABLE_C2, R.color.REPORT_TABLE_C3, R.color.REPORT_TABLE_C4, R.color.REPORT_TABLE_C5,
            R.color.REPORT_TABLE_C6, R.color.REPORT_TABLE_C7, R.color.REPORT_TABLE_C8};

    private int chart_user_width;

    private LayoutInflater layoutInflater;
    private MainActivity activity;

    private TimeChooserDialog timerDialog;
    private int timeType = 1;
    private RelativeLayout time_chooser;
    private String sBeginDate, sEndDate;

    private View view;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private LinearLayout data_view, chart_xy_container, data_view_user;
    private XYLineView chart_xy;
    private PieChart mChart;

    private int tabType = 1;
    private LinearLayout tab_user_new, tab_user_total, tab_user_supplier, tab_user_fivemetal;

    private Animation roatAnim;
    private Button btn_toogle_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;

        view = layoutInflater.inflate(R.layout.fragment_user, null);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initData();

    }

    private void initView() {

        data_view = (LinearLayout) view.findViewById(R.id.data_view);
        chart_xy_container = (LinearLayout) view.findViewById(R.id.chart_xy_container);

        mChart = (PieChart) data_view.findViewById(R.id.pieChart);

        data_view_user = (LinearLayout) view.findViewById(R.id.data_view_user);
        data_view_user.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                data_view_user.getViewTreeObserver().removeOnPreDrawListener(this);
                chart_user_width = data_view_user.getWidth();
                Log.d(TAG, "chart_user_width;" + chart_user_width);
                return true;
            }
        });

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
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 2) {
                    tabType = 2;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 3) {
                    tabType = 3;
                    toogleTab(tabType);
                    sendRequest(generateParam());
                }
            }
        });

        tab_user_fivemetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabType != 4) {
                    tabType = 4;
                    toogleTab(tabType);
                    sendRequest(generateParam());

                }
            }
        });

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setDrawCenterText(false);
        mChart.highlightValues(null);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColorTransparent(false);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.animateY(800, Easing.EasingOption.EaseInOutExpo);
        mChart.animateX(800, Easing.EasingOption.EaseInOutExpo);
        mChart.getLegend().setEnabled(false);


        btn_toogle_fragment = (Button) view.findViewById(R.id.btn_toogle_fragment);
        btn_toogle_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.toogleFragment(UserFragment.this);
            }
        });

    }

    private void toogleTab(int tabType) {

        tab_user_new.setSelected(tabType == 2 || tabType == 3 || tabType == 4 ? false : true);
        tab_user_total.setSelected(tabType == 2 ? true : false);
        tab_user_supplier.setSelected(tabType == 3 ? true : false);
        tab_user_fivemetal.setSelected(tabType == 4 ? true : false);

        if (tabType == 2 || tabType == 3 || tabType == 4) {
            time_chooser.setVisibility(View.GONE);
            chart_xy_container.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);
        } else {
            time_chooser.setVisibility(View.VISIBLE);
            chart_xy_container.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
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

            boolean flag = (tabType == 2 || tabType == 3 || tabType == 4 ? true : false);
            list.clear();
            for (int i = 0; i < data.size(); i++) {

                Map<String, String> item = new HashMap();

                String coloum1 = "", coloum2 = "", coloum3 = "";

                if (tabType == 2) {
                    coloum1 = data.get(i).get("CUSTOMTYPE");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = ((int) Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 0)) + "%";
                    } catch (Exception e) {
                    }
                } else if (tabType == 3) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = ((int) Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 0)) + "%";
                    } catch (Exception e) {
                    }
                } else if (tabType == 4) {
                    coloum1 = data.get(i).get("NAME");
                    coloum2 = data.get(i).get("COUN");
                    try {
                        coloum3 = ((int) Arith.round(Double.parseDouble(data.get(i).get("PROPORTION")), 0)) + "%";
                    } catch (Exception e) {
                    }
                } else {
                    if (timeType == 2) {
                        coloum1 = data.get(i).get("CREATETIME");
                        coloum2 = data.get(i).get("COUN");
                    } else {
                        coloum1 = data.get(i).get("CREATETIME");
                        coloum2 = data.get(i).get("COUN");
                    }
                }

                item.put("colum1", coloum1);
                item.put("colum2", coloum2);

                if (flag) {
                    item.put("colum3", coloum3);
                }

                list.add(item);
            }

            if (chart_xy == null) {

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(chart_user_width,
                        (int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
                chart_xy = new XYLineView(activity);
                chart_xy.setLayoutParams(lp);
                chart_xy.setScreenWidth(chart_user_width);
                chart_xy.setScreenHeight((int) activity.getResources().getDimension(R.dimen.report_graph_size_height_user));
                chart_xy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data_view_user.performClick();
                    }
                });
                data_view_user.addView(chart_xy);

                fillNewuserData(list);

            } else {
                if (tabType == 2 || tabType == 3 || tabType == 4) {
                    fillPieChartData(list);
                } else {
                    fillNewuserData(list);
                }
            }
        }
    }

    private void fillNewuserData(List<Map<String, String>> list) {


        if (list != null && list.size() > 0) {

            String[] fields = new String[list.size()];
            float[] dataArr = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(list.size() - 1 - i).get("colum1");
                String ydata = list.get(list.size() - 1 - i).get("colum2");

                fields[i] = xdata.substring(6);
                dataArr[i] = Float.parseFloat(ydata);

            }
            chart_xy.setData(dataArr);
            chart_xy.setField(fields);
        } else {
            chart_xy.setData(new float[]{});
            chart_xy.setField(new String[]{});
        }
        chart_xy.postInvalidate();

    }

    private void fillPieChartData(List<Map<String, String>> list) {

        if (list != null && list.size() > 0) {

            ArrayList<Entry> yVals = new ArrayList<>(list.size());
            ArrayList<String> xVals = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {

                String xdata = list.get(i).get("colum1") + "  " + list.get(i).get("colum2");
                String ydata = list.get(i).get("colum3");

                xVals.add(xdata);
                yVals.add(new Entry(Float.parseFloat(ydata), i));

            }

            PieDataSet dataSet = new PieDataSet(yVals, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            int[] colorArr = new int[xVals.size()];
            for (int i = 0; i < xVals.size(); i++) {
                colorArr[i] = activity.getResources().getColor(colors[i % colors.length]);
            }
            dataSet.setColors(colorArr);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(10f);
            data.setValueTextColor(activity.getResources().getColor(R.color.REPORT_UI_C2));

            mChart.setData(data);
            mChart.invalidate();

        } else {


        }

    }

    private Map<String, String> generateParam() {

        Map<String, String> parems = new HashMap();

        if (tabType == 2) {
            parems.put("type", "Tatol");
        } else if (tabType == 3) {
            parems.put("type", "supplier");
        } else if (tabType == 4) {
            parems.put("type", "store");
        } else {
            parems.put("type", "nuser");
            if (sBeginDate != null && !"".equals(sBeginDate.trim()))
                parems.put("start", sBeginDate);
            if (sEndDate != null && !"".equals(sEndDate.trim()))
                parems.put("end", sEndDate);

            if (timeType == 2) {
                // parems.put("type", "nuser");
            } else {
                // parems.put("type", "nuser");
            }
        }

        return parems;

    }

    private void sendRequest(Map<String, String> parems) {

        RequestParams requestParams = new RequestParams(parems);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(activity, Constant.URL_REPORT_USER, requestParams, new TextHttpResponseHandler() {

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

                        List<Map<String, String>> viewtable = (List<Map<String, String>>) result.get("viewtable");

                        if (viewtable != null && viewtable.size() > 0) {
                            List<Map<String, String>> dataCache = viewtable;
                            fillData(dataCache);
                        } else {
                            if (list != null && list.size() > 0) {
                                list.clear();
                            }
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_tips), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        if (list != null && list.size() > 0) {
                            list.clear();
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

}
