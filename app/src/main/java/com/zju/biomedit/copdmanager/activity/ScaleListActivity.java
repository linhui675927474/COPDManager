package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity1;
import com.zju.biomedit.copdmanager.activity.cat.CatActivity1;
import com.zju.biomedit.copdmanager.activity.depression.DepressionActivity1;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScaleListActivity extends AppCompatActivity {

    @BindView(R.id.card_cat)
    CardView cardCat;
    @BindView(R.id.card_depression)
    CardView cardDepression;
    @BindView(R.id.card_anxiety)
    CardView cardAnxiety;
    @BindView(R.id.tv_cat)
    TextView tvCat;
    @BindView(R.id.tv_depression)
    TextView tvDepression;
    @BindView(R.id.tv_anxiety)
    TextView tvAnxiety;
    private boolean isCatOver;
    private boolean isPhqOver;
    private boolean isGadOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_SCALE_LIST);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_SCALE_LIST);
        super.onResume();
        initCard();
        initView();
    }

    private void initCard() {

        cardCat.setOnClickListener((View v) -> {
            if(isCatOver){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(ScaleListActivity.this)
                        .title(R.string.tv_hint)
                        .content(R.string.tv_cat_scale_text)
                        .positiveText(R.string.tv_continue)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        Intent intentCat = new Intent(ScaleListActivity.this,CatActivity1.class);
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
                Intent intentCat = new Intent(ScaleListActivity.this,CatActivity1.class);
                startActivity(intentCat);
            }

        });

        cardDepression.setOnClickListener((View v) -> {
            if(isPhqOver){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(ScaleListActivity.this)
                        .title(R.string.tv_hint)
                        .content(R.string.tv_phq_scale_text)
                        .positiveText(R.string.tv_continue)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        Intent intentCat = new Intent(ScaleListActivity.this,DepressionActivity1.class);
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
                Intent intentCat = new Intent(ScaleListActivity.this,DepressionActivity1.class);
                startActivity(intentCat);
            }
        });

        cardAnxiety.setOnClickListener((View v) -> {
            if(isGadOver){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(ScaleListActivity.this)
                        .title(R.string.tv_hint)
                        .content(R.string.tv_gad_scale_text)
                        .positiveText(R.string.tv_continue)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        Intent intentCat = new Intent(ScaleListActivity.this,AnxietyActivity1.class);
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
                Intent intentCat = new Intent(ScaleListActivity.this,AnxietyActivity1.class);
                startActivity(intentCat);
            }
        });
    }

    private void initView(){
        isCatOver = false;
        isPhqOver = false;
        isGadOver = false;
        String startWeekDate = TimeManager.getTimesWeekmorning();
        String endWeekDate = TimeManager.getTimesWeeknight();
        String startMonthDate = TimeManager.getTimesMonthmorning();
        String endMonthDate = TimeManager.getTimesMonthnight();
        Log.i("wzycat",startWeekDate+" "+ endWeekDate+ " "+ startMonthDate + " " +endMonthDate);
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        jsonArgs.addProperty("recordType", Utils.CAT);
        jsonArgs.addProperty("start", startWeekDate);
        jsonArgs.addProperty("end", endWeekDate);
        Log.i("wzycat",jsonArgs+"");
        String url = Utils.BASE_URL + Utils.GET_GENERIC_RECORDS;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_GET_SCALE_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        Ion.with(ScaleListActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.i("wzycat", result+"");
                if (e != null) {
                    Log.i("wzycat", "load cat error");
                }else {
                    int flag = result.get("flag").getAsInt();
                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                        Gson gson = new Gson();
                        List<ScaleTask> catTasks = new ArrayList<>();
                        JsonArray jsonCat = result.get("recordList").getAsJsonArray();
                        catTasks = gson.fromJson(jsonCat, new TypeToken<List<ScaleTask>>() {
                        }.getType());
                        if (catTasks != null && catTasks.size() > 0) {
                            isCatOver = true;
                            tvCat.setText("本周已填写");
                        }
                    }
                }
                JsonObject jsonArgs = new JsonObject();
                jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
                jsonArgs.addProperty("recordType", Utils.PHQ);
                jsonArgs.addProperty("start", startMonthDate);
                jsonArgs.addProperty("end", endMonthDate);
                Log.i("wzyphq",jsonArgs+"");
                Ion.with(ScaleListActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("wzyphq", result+"");
                        if (e != null) {
                            Log.i("wzyphq", "load phq error");
                        }else {
                            int flag = result.get("flag").getAsInt();
                            if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                                Gson gson = new Gson();
                                List<ScaleTask> phqTasks = new ArrayList<>();
                                JsonArray jsonPhq = result.get("recordList").getAsJsonArray();
                                phqTasks = gson.fromJson(jsonPhq, new TypeToken<List<ScaleTask>>() {
                                }.getType());
                                if (phqTasks != null && phqTasks.size() > 0) {
                                    isPhqOver = true;
                                    tvDepression.setText("本月已填写");
                                }
                            }
                        }

                        JsonObject jsonArgs = new JsonObject();
                        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
                        jsonArgs.addProperty("recordType", Utils.GAD);
                        jsonArgs.addProperty("start", startMonthDate);
                        jsonArgs.addProperty("end", endMonthDate);
                        Log.i("wzygad",jsonArgs+"");
                        Ion.with(ScaleListActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                dialog.dismiss();
                                Log.i("wzygad", result+"");
                                if (e != null) {
                                    Log.i("wzygad", "load gad error");
                                }else {
                                    int flag = result.get("flag").getAsInt();
                                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                                        Gson gson = new Gson();
                                        List<ScaleTask> gadTasks = new ArrayList<>();
                                        JsonArray jsonGad = result.get("recordList").getAsJsonArray();
                                        gadTasks = gson.fromJson(jsonGad, new TypeToken<List<ScaleTask>>() {
                                        }.getType());
                                        if (gadTasks != null && gadTasks.size() > 0) {
                                            isGadOver = true;
                                            tvAnxiety.setText("本月已填写");
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}
