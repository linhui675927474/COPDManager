package com.zju.biomedit.copdmanager.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.PefTask;
import com.zju.biomedit.copdmanager.support.AddressPickTask;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.TimeManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class PefHistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.rl_pef_history_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_history_week)
    TextView tvTime;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pef_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        setAxis();
        String startWeekDate = TimeManager.getTimesWeekmorning();
        String endWeekDate = TimeManager.getTimesWeeknight();
        Log.i("pefhistory", startWeekDate + endWeekDate);
        loadPefHistory(startWeekDate, endWeekDate);//只精确到日期
        tvTime.setText(TimeManager.tansferDataString(startWeekDate,0)+"-"+TimeManager.tansferDataString(endWeekDate,1));
        rlTime.setOnClickListener((View v) ->{
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    PefHistoryActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });
    }

    private void initChart(List<Entry> entries) {
        Legend legend = chart.getLegend();
        legend.setTextSize(12f);
        if(entries.size() != 0) {
            tvNoData.setVisibility(View.GONE);
            LineDataSet dataSet = new LineDataSet(entries, "PEF: L/Min"); // add entries to dataset
            dataSet.setColor(Color.parseColor("#40c4ff"));
            dataSet.setCircleColor(Color.parseColor("#40c4ff"));
            dataSet.setValueTextSize(14f);
            dataSet.setFormSize(14f);
            dataSet.setCircleRadius(8f);
            dataSet.setCircleHoleRadius(6f);
            dataSet.setLineWidth(3f);
            dataSet.setValueTextColor(Color.parseColor("#40c4ff"));
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                dataSet.setFillDrawable(drawable);
            }
            dataSet.setDrawFilled(true);
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.invalidate(); // refresh
            chart.animateX(1000);
        }else{
            tvNoData.setVisibility(View.VISIBLE);
            LineData lineData = new LineData();
            chart.setData(lineData);
            chart.invalidate(); // refresh
        }

    }

    private void setAxis() {
        chart.setNoDataText("正在加载PEF历史数据");
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(14f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(800f);
        final String[] quarters = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六", ""};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setAxisMaximum(8);
        xAxis.setAxisMinimum(0);

        int pefValue = PatientUserManager.getInstance().getPefStandard();
        LimitLine ll = new LimitLine(pefValue, "个人标准值: " + pefValue);
        ll.setLineColor(Color.RED);
        ll.setLineWidth(2.5f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        leftAxis.addLimitLine(ll);
    }

    private void loadPefHistory(String startMonthDate, String endMonthDate) {
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        jsonArgs.addProperty("recordType", com.zju.biomedit.copdmanager.support.Utils.PEF);
        jsonArgs.addProperty("start", startMonthDate);
        jsonArgs.addProperty("end", endMonthDate);
        String url = com.zju.biomedit.copdmanager.support.Utils.BASE_URL + com.zju.biomedit.copdmanager.support.Utils.GET_GENERIC_RECORDS;
        List<Entry> entries = new ArrayList<>();
        Ion.with(PefHistoryActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.i("pefHistory", result + "");
                if (e != null) {
                    Log.i("pefHistory", "load pef error");
                } else {
                    int flag = result.get("flag").getAsInt();
                    if (flag == com.zju.biomedit.copdmanager.support.Utils.SERVICE_RETURN_SUCCEED) {
                        Gson gson = new Gson();
                        List<PefTask> pefTaskList = new ArrayList<>();
                        JsonArray jsonCat = result.get("recordList").getAsJsonArray();
                        pefTaskList = gson.fromJson(jsonCat, new TypeToken<List<PefTask>>() {
                        }.getType());
                        if (pefTaskList != null) {
                            for (PefTask pefTask : pefTaskList) {
                                String time = pefTask.getMeasureTime();
                                int week = TimeManager.getDayOfWeek(time);
                                int pefValue = pefTask.getValue();
                                Log.i("pefhistory", week + " " + pefValue);
                                entries.add(new Entry(week, pefValue));
                            }
                        }
                        Log.i("entries", entries+"");
                        initChart(entries);

                    }
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        String startWeekDate = TimeManager.getGeneralTimesWeekmorning(date);
        String endWeekDate = TimeManager.getGeneralTimesWeeknight(date);
        loadPefHistory(startWeekDate, endWeekDate);
        tvTime.setText(TimeManager.tansferDataString(startWeekDate,0)+"-"+TimeManager.tansferDataString(endWeekDate,1));
    }

}
