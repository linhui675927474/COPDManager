package com.zju.biomedit.copdmanager.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.bugly.crashreport.CrashReport;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.AppointResultActivity;
import com.zju.biomedit.copdmanager.activity.AppointmentActivity;
import com.zju.biomedit.copdmanager.activity.DiscomfortActivity;
import com.zju.biomedit.copdmanager.activity.DrugActivity;
import com.zju.biomedit.copdmanager.activity.PefMonitorActivity;
import com.zju.biomedit.copdmanager.activity.PefRecordActivity;
import com.zju.biomedit.copdmanager.activity.ReminderActivity;
import com.zju.biomedit.copdmanager.activity.ScaleListActivity;
import com.zju.biomedit.copdmanager.activity.SixMinuteWalkTestActivity;
import com.zju.biomedit.copdmanager.model.DiscomfortTask;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.PefTask;
import com.zju.biomedit.copdmanager.model.SixMWDTask;
import com.zju.biomedit.copdmanager.model.WeatherAir;
import com.zju.biomedit.copdmanager.model.WeatherBasic;
import com.zju.biomedit.copdmanager.model.WeatherNow;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.MyApplication;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;
import com.zju.biomedit.copdmanager.view.CircleProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomepageFragment extends Fragment {
    @BindView(R.id.tv_appointment)
    TextView tvAppointment;
    @BindView(R.id.tv_pef_newest)
    TextView tvPef;
    @BindView(R.id.tv_drug_newest)
    TextView tvDrug;
    @BindView(R.id.tv_discomfort_newest)
    TextView tvDiscomfort;
    @BindView(R.id.tv_evaluation)
    TextView tvEvaluation;
    @BindView(R.id.circle_progress_bar)
    CircleProgress circleProgress;
    @BindView(R.id.card_scale)
    CardView cdScale;
    @BindView(R.id.card_pef)
    CardView cdPef;
    @BindView(R.id.card_drug)
    CardView cdDrug;
    @BindView(R.id.card_discomfort)
    CardView cdDiscomfort;
    @BindView(R.id.card_6MWD)
    CardView cd6MWD;

    @BindView(R.id.iv_remind)
    ImageView ivReminder;
    @BindView(R.id.tv_weather)
    TextView tvWeather;

    private boolean isPefOver;
    private String strWeather;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } //使状态栏变透明，将布局扩展到状态栏（这个设置应用到当前Activity）

        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this,view);

        setClicklistener();
        setNewestRecord();
        setTotalPoints(view);
        weather();
        return view;
    }

    private void setNewestRecord() {
        isPefOver = false;
        PefTask pefTask = PatientUserManager.getInstance().getLatestRecord().getPefTask();
        DiscomfortTask discomfortTask = PatientUserManager.getInstance().getLatestRecord().getDiscomfortTask();
        MedicineTask medicineTask = PatientUserManager.getInstance().getLatestRecord().getMedicineTask();
        SixMWDTask sixMWDTask=PatientUserManager.getInstance().getLatestRecord().getSixMWDTask();

        if (pefTask != null){
            String strPef = "上次记录："+ pefTask.getValue()+"L/Min";
            tvPef.setText(strPef);
            if(TimeManager.isThisWeek(pefTask.getMeasureTime())){
                isPefOver = true;
            }
        }
        if (medicineTask != null){
            String drugName = medicineTask.getMedicineName();
            if(drugName.length()>5){
                drugName = drugName.substring(0,5);
            }
            String strDrug = "上次记录："+ drugName;
            tvDrug.setText(strDrug);
        }
        if (discomfortTask != null){
            String latestDiscomfort = GlobalMethod.getLatestDiscomfort(discomfortTask);
            Log.i("discomfort",latestDiscomfort+" "+latestDiscomfort.length());
            if(latestDiscomfort.length()>3){//不止一条记录
                latestDiscomfort = latestDiscomfort.substring(0,2)+"...";
            }
            String strDiscomfort = "上次记录：<font color='#ff5722'>"+ latestDiscomfort.trim()+"</font>";
            tvDiscomfort.setText(Html.fromHtml(strDiscomfort));
        }

    }

    private void setTotalPoints(View view) {
        circleProgress = (CircleProgress) view.findViewById(R.id.circle_progress_bar);
        int totalPoints = GlobalMethod.calculateEvaluationValue();
        if(totalPoints == 20){
            circleProgress.setValue(20);
            circleProgress.setHint("较差");
            tvEvaluation.setText(getResources().getString(R.string.total_20));
        }else if(totalPoints == 40){
            circleProgress.setValue(40);
            circleProgress.setHint("不佳");
            tvEvaluation.setText(getResources().getString(R.string.total_40));
        }else if(totalPoints == 60){
            circleProgress.setValue(60);
            circleProgress.setHint("中等");
            tvEvaluation.setText(getResources().getString(R.string.total_60));
        }else if(totalPoints == 80){
            circleProgress.setValue(80);
            circleProgress.setHint("良好");
            tvEvaluation.setText(getResources().getString(R.string.total_80));
        }else{
            circleProgress.setValue(0);
            circleProgress.setHint("未测评");
            tvEvaluation.setText(getResources().getString(R.string.total_0));
        }

    }

    private void setClicklistener() {
        tvAppointment.setOnClickListener((View v) ->{
            Intent intentAppointment = new Intent(getActivity(), AppointmentActivity.class);
            startActivity(intentAppointment);
        });

        cdScale.setOnClickListener((View v) -> {
            Intent intentScale = new Intent(getActivity(), ScaleListActivity.class);
            startActivity(intentScale);
//            CrashReport.testJavaCrash();
        });

        cd6MWD.setOnClickListener((View v) -> {
            Intent intent6MWD = new Intent(getActivity(), SixMinuteWalkTestActivity.class);
            startActivity(intent6MWD);
//            CrashReport.testJavaCrash();
        });


        cdPef.setOnClickListener((View v) -> {
            if(isPefOver){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getActivity())
                        .title(R.string.tv_hint)
                        .content(R.string.tv_pef_text)
                        .positiveText(R.string.tv_continue)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        Intent intentCat = new Intent(getActivity(),PefRecordActivity.class);
                        startActivity(intentCat);

                    }
                })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO
                                dialog.dismiss();
                            }
                        });
            }else{
                Intent intentPef = new Intent(getActivity(), PefRecordActivity.class);
                startActivity(intentPef);
            }

        });

        cdDrug.setOnClickListener((View v) -> {
            Intent intentDrug = new Intent(getActivity(), DrugActivity.class);
            startActivity(intentDrug);
        });

        cdDiscomfort.setOnClickListener((View v) -> {
            Intent intentUncomfort = new Intent(getActivity(), DiscomfortActivity.class);
            startActivity(intentUncomfort);
        });

        ivReminder.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), ReminderActivity.class);
            startActivity(intent);
        });
    }

    private void weather() {
        strWeather = "实时天气：";
        String ip = GlobalMethod.IP_URL;
        String url = Utils.WEATHER_NOW_URL + "?location="+ip+"&key=2cdd7469a2a74d3db626655df8e1e74a";
        Log.d("weather", url);
        Ion.with(MyApplication.getContext()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("weather", e+" "+result+"");
                if (e != null) {
                    Log.d("weather", "load weather error");
                }else {
                    Gson gson = new Gson();
                    WeatherBasic weatherBasic = new WeatherBasic();
                    WeatherNow weatherNow = new WeatherNow();
                    JsonArray weather6 = result.getAsJsonArray("HeWeather6");
                    if(weather6.size()!=0){
                        JsonObject weather = weather6.get(0).getAsJsonObject();
                        JsonObject basic = weather.getAsJsonObject("basic");
                        weatherBasic = gson.fromJson(basic, new TypeToken<WeatherBasic>() {}.getType());
                        if(weatherBasic != null){
                            strWeather += weatherBasic.getLocation()+" ";
                        }
                        JsonObject now = weather.getAsJsonObject("now");
                        weatherNow = gson.fromJson(now, new TypeToken<WeatherNow>() {}.getType());
                        if(weatherNow != null){
                            strWeather += weatherNow.getTmp()+"℃ ";
                            strWeather += weatherNow.getCond_txt()+" ";
                        }
                    }else{
                        strWeather += "未知 ";
                    }
                    String url = Utils.WEATHER_AIR_URL + "?location="+ip+"&key=2cdd7469a2a74d3db626655df8e1e74a";
                    Log.d("weather", url);
                    Ion.with(MyApplication.getContext()).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("weather", e+" "+result+"");
                            if (e != null) {
                                Log.d("weather", "load weather error");
                            }else {
                                Gson gson = new Gson();
                                WeatherAir weatherAir = new WeatherAir();
                                JsonArray weather6 = result.getAsJsonArray("HeWeather6");
                                if(weather6.size() != 0){
                                    JsonObject weather = weather6.get(0).getAsJsonObject();
                                    JsonObject air = weather.getAsJsonObject("air_now_city");
                                    weatherAir = gson.fromJson(air, new TypeToken<WeatherAir>() {}.getType());
                                    if(weatherAir != null){
                                        strWeather += "空气质量" + weatherAir.getQlty();
                                        tvWeather.setText(strWeather);
                                    }else{
                                        tvWeather.setText("实时天气获取失败");
                                    }
                                }else{
                                    strWeather += "空气质量未知";
                                }
                                tvWeather.setText(strWeather);
                            }
                        }
                    });
                }
            }
        });
    }



}