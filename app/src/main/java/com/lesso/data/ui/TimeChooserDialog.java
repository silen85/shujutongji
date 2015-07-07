package com.lesso.data.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.data.R;
import com.lesso.data.common.Constant;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by meisl on 2015/6/27.
 */
public class TimeChooserDialog extends Dialog {

    private String TAG = "com.lesso.data.ui.TimeChooserDialog";

    String[] txtWeek = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    private int type = 1;       //1:按日  2:按月
    private int dateType = 1;   //1:开始时间  2:结束时间

    private String sBeaginDate = "";
    private String sEndDate = "";
    private Date tempBeginDate;
    private Date tempEndDate;

    private Context context;

    private ClickListenerInterface clickListenerInterface;

    public TimeChooserDialog(Context context, int type, String sBeaginDate, String sEndDate) {
        super(context);
        this.context = context;

        this.type = type;

        if (sBeaginDate != null && !"".equals(sBeaginDate.trim()))
            this.sBeaginDate = sBeaginDate;

        if (sEndDate != null && !"".equals(sEndDate.trim()))
            this.sEndDate = sEndDate;
    }

    public interface ClickListenerInterface {
        void doFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout dialog = (LinearLayout) inflater.inflate(R.layout.dialog_datepicker, null);

        setContentView(dialog);

        final TextView byday = (TextView) dialog.findViewById(R.id.byday);
        final TextView bymonth = (TextView) dialog.findViewById(R.id.bymonth);
        if (type == 2) {
            byday.setSelected(false);
            bymonth.setSelected(true);
        } else {
            byday.setSelected(true);
            bymonth.setSelected(false);
        }

        byday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                byday.setSelected(true);
                bymonth.setSelected(false);
            }
        });

        bymonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                byday.setSelected(false);
                bymonth.setSelected(true);
            }
        });

        TextView datepicker_finish = (TextView) dialog.findViewById(R.id.datepicker_finish);
        datepicker_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tempBeginDate == null) {
                    Toast.makeText(context, "请输入开始日期", Toast.LENGTH_SHORT).show();
                } else if (tempEndDate == null) {
                    Toast.makeText(context, "请输入结束日期", Toast.LENGTH_SHORT).show();
                } else {

                    sBeaginDate = Constant.DATE_FORMAT_1.format(tempBeginDate);
                    sEndDate = Constant.DATE_FORMAT_1.format(tempEndDate);

                    if (sEndDate.compareTo(sBeaginDate) >= 0) {
                        //Toast.makeText(context, sBeaginDate + "-" + sEndDate, Toast.LENGTH_SHORT).show();
                        clickListenerInterface.doFinish();
                        TimeChooserDialog.this.dismiss();
                    } else {
                        Toast.makeText(context, "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        final TextView datepicker_bdate = (TextView) dialog.findViewById(R.id.datepicker_bdate);
        final TextView datepicker_edate = (TextView) dialog.findViewById(R.id.datepicker_edate);
        if (dateType == 2) {
            datepicker_bdate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C2));
            datepicker_edate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C1));
        } else {
            datepicker_bdate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C1));
            datepicker_edate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C2));
        }

        datepicker_bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType = 1;
                datepicker_bdate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C1));
                datepicker_edate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C2));
            }
        });

        datepicker_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateType = 2;
                datepicker_bdate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C2));
                datepicker_edate.setTextColor(context.getResources().getColor(R.color.REPORT_UI_C1));
            }
        });

        /**
         * 初始化开始时间控件
         */
        final Calendar calendar = Calendar.getInstance();

        try {
            if (sBeaginDate != null && !"".equals(sBeaginDate.trim())) {
                tempBeginDate = Constant.DATE_FORMAT_1.parse(sBeaginDate);
            } else {
                tempBeginDate = new Date();
            }
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            tempBeginDate = new Date();
        }

        calendar.setTime(tempBeginDate);
        datepicker_bdate.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日  周" + txtWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);

        /**
         *
         * 初始化结束时间控件
         */

        try {
            if (sEndDate != null && !"".equals(sEndDate.trim())) {
                tempEndDate = Constant.DATE_FORMAT_1.parse(sEndDate);
            } else {
                tempEndDate = new Date();
            }
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            tempEndDate = new Date();
        }

        calendar.setTime(tempEndDate);
        datepicker_edate.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日  周" + txtWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);

        /**
         * 初始化datepicker控件
         */
        String date = (dateType == 2 ? sEndDate : sBeaginDate);
        Date _date = null;
        try {
            if (date != null && !"".equals(date.trim())) {
                _date = Constant.DATE_FORMAT_1.parse(date);
            } else {
                _date = new Date();
            }
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            _date = new Date();
        }

        calendar.setTime(_date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        final int week = calendar.get(Calendar.DAY_OF_WEEK);

        DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datepicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                if (dateType == 2) {
                    tempEndDate = calendar.getTime();
                    datepicker_edate.setText(i + "年" + (i1+1) + "月" + i2 + "日  周" + txtWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                } else {
                    tempBeginDate = calendar.getTime();
                    datepicker_bdate.setText(i + "年" + (i1+1) + "月" + i2 + "日  周" + txtWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                }
            }
        });

    }

    public int getType() {
        return type;
    }

    public String getsBeaginDate() {
        return sBeaginDate;
    }

    public String getsEndDate() {
        return sEndDate;
    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

}
