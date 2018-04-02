package com.zju.biomedit.copdmanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.DrugHistoryAdapter;
import com.zju.biomedit.copdmanager.model.HistoryDrugRecord;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.EventDecorator;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugHistoryActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    private DrugHistoryAdapter drugHistoryAdapter;
    @BindView(R.id.recyclerView_drug_history)
    RecyclerView mRecyclerView;
    List<MedicineTask> medicineTaskList;

    private static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean isInitialized = false;
    private List<CalendarDay> calendarDayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_history);
        ButterKnife.bind(this);
        setCalendar();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_MEDICINE_HISTORY);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_MEDICINE_HISTORY);
        super.onResume();
    }

    public void back(View v){
        finish();
    }

    private void initRecyclerView(List<MedicineTask> dataList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(DrugHistoryActivity.this));
        mRecyclerView.setHasFixedSize(true);
        medicineTaskList = new ArrayList<>();
        medicineTaskList.addAll(dataList);
        drugHistoryAdapter = new DrugHistoryAdapter(medicineTaskList);
        mRecyclerView.setAdapter(drugHistoryAdapter);
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        isInitialized = true;

    }

    private void setCalendar() {
        calendarDayList = new ArrayList<>();
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        widget.setHeaderTextAppearance(R.style.CustomMonthTextAppearance);
        widget.setWeekDayTextAppearance(R.style.CustomWeekTextAppearance);
        widget.setDateTextAppearance(R.style.CustomDayTextAppearance);
        widget.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        widget.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        //widget.setTileHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));
        widget.setTitleAnimationOrientation(MaterialCalendarView.VERTICAL);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        CalendarDay today = CalendarDay.today();
        widget.setCurrentDate(today);
        widget.setSelectedDate(today);

        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        //GlobalMethod.showToast(this,getSelectedDatesString(), Toast.LENGTH_LONG);
        String today = getSelectedDatesString();
        List<MedicineTask> dataList = HistoryDrugRecord.getInstance().getDayDrugRecords(today);
        medicineTaskList.clear();
        medicineTaskList.addAll(dataList);
        drugHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions
        String startDate = DateTimeFormat.format(date.getDate());
        int year = date.getYear();
        int month = date.getMonth();//月份会少一个
        String endDate = startDate.substring(0,7) + "-" + TimeManager.getDayCountFromMonth(year,month+1);
        //GlobalMethod.showToast(this,startDate + " " + endDate+" "+year+ " "+ month, Toast.LENGTH_LONG);
        loadDrugHistory(startDate,endDate);

    }

    private void loadDrugHistory(String startMonthDate, String endMonthDate){
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        jsonArgs.addProperty("recordType", Utils.MEDICATION);
        jsonArgs.addProperty("start", startMonthDate);
        jsonArgs.addProperty("end", endMonthDate);
        Log.i("drugHistory",jsonArgs+"");
        String url = Utils.BASE_URL + Utils.GET_GENERIC_RECORDS;
        Ion.with(DrugHistoryActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.i("drugHistory", result+"");
                if (e != null) {
                    Log.i("drugHistory", "load cat error");
                }else {
                    int flag = result.get("flag").getAsInt();
                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                        Gson gson = new Gson();
                        List<MedicineTask> medicineTaskList = new ArrayList<>();
                        JsonArray jsonCat = result.get("recordList").getAsJsonArray();
                        medicineTaskList = gson.fromJson(jsonCat, new TypeToken<List<MedicineTask>>() {}.getType());
                        if (medicineTaskList!= null) {
                            HistoryDrugRecord.getInstance().updateDrugRecordMap(medicineTaskList, startMonthDate.substring(0, 7));
                            if(!isInitialized) {
                                initRecyclerView(HistoryDrugRecord.getInstance().getDayDrugRecords(TimeManager.getStrDate()));
                            }
                            for(MedicineTask medicineTask : medicineTaskList){
                                String time = medicineTask.getMeasureTime().substring(0,10);
                                CalendarDay day = CalendarDay.from(TimeManager.parseStrDate(time));
                                calendarDayList.add(day);
                            }
                            widget.addDecorator(new EventDecorator(Color.GREEN, calendarDayList));
                            widget.invalidateDecorators();
                        }
                    }
                }
            }
        });
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return DateTimeFormat.format(date.getDate());
    }

}
