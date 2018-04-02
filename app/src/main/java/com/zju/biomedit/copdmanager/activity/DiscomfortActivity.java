package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.DiscomfortTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import javax.microedition.khronos.opengles.GL;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscomfortActivity extends AppCompatActivity {
    @BindView(R.id.rl_uncomfort_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_discomfort)
    TextView tvDiscomfort;
    @BindView(R.id.tv_no_discomfort)
    TextView tvNoDiscomfort;
    @BindView(R.id.ll_discomfort)
    LinearLayout llDiscomfort;
    @BindView(R.id.tv_uncomfort_inflammation_1)
    TextView tvInflammation1;
    @BindView(R.id.tv_uncomfort_inflammation_2)
    TextView tvInflammation2;
    @BindView(R.id.tv_uncomfort_inflammation_3)
    TextView tvInflammation3;
    @BindView(R.id.tv_uncomfort_inflammation_4)
    TextView tvInflammation4;
    @BindView(R.id.tv_uncomfort_lung_1)
    TextView tvLung1;
    @BindView(R.id.tv_uncomfort_lung_2)
    TextView tvLung2;
    @BindView(R.id.tv_uncomfort_lung_3)
    TextView tvLung3;
    @BindView(R.id.tv_uncomfort_heart_1)
    TextView tvHeart1;
    @BindView(R.id.tv_uncomfort_heart_2)
    TextView tvHeart2;
    @BindView(R.id.tv_uncomfort_heart_3)
    TextView tvHeart3;
    @BindView(R.id.tv_uncomfort_heart_4)
    TextView tvHeart4;
    @BindView(R.id.tv_uncomfort_breath_1)
    TextView tvBreath1;
    @BindView(R.id.tv_uncomfort_breath_2)
    TextView tvBreath2;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_discomfort_note)
    EditText etNote;
    @BindView(R.id.btn_discomfort_save)
    Button btnSave;
    private TimePopupWidow timePopupWheelView;
    private String submitTime;
    private String note;
    private int inflammation;
    private int lung;
    private int heart;
    private int breath;
    private int isDiscomfort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discomfort);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        setClick();
        initTimePicker();
        submit();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_DISCOMFORT);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_DISCOMFORT);
        super.onResume();
    }


    private void setClick() {
        isDiscomfort = -1;
        inflammation = 0;
        lung = 0;
        heart = 0;
        breath = 0;//按位存储不适信息
        tvDiscomfort.setOnClickListener((View v) ->{
            if(isDiscomfort == -1 || isDiscomfort == 0){
                tvDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.red));
                tvNoDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvNoDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.grey));
                isDiscomfort = 1;
                llDiscomfort.setVisibility(View.VISIBLE);
            }else{
                tvDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.grey));
                isDiscomfort = -1;
                llDiscomfort.setVisibility(View.GONE);
            }
        });
        tvNoDiscomfort.setOnClickListener((View v) ->{
            if(isDiscomfort == -1 || isDiscomfort == 1){
                tvNoDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_nodiscomfort_selected));
                tvNoDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.green));
                tvDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.grey));
                isDiscomfort = 0;
                llDiscomfort.setVisibility(View.GONE);
            }else{
                tvNoDiscomfort.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvNoDiscomfort.setTextColor(ContextCompat.getColor(this,R.color.grey));
                isDiscomfort = -1;
                llDiscomfort.setVisibility(View.GONE);
            }
        });
        tvInflammation1.setOnClickListener((View v) ->{
            if((inflammation & 4) ==0){
                tvInflammation1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvInflammation1.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvInflammation1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvInflammation1.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            inflammation = inflammation ^ 4;//异或操作实现特定位取反
        });
        tvInflammation2.setOnClickListener((View v) ->{
            if((inflammation & 2) ==0){
                tvInflammation2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvInflammation2.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvInflammation2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvInflammation2.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            inflammation = inflammation ^ 2;
        });
        tvInflammation3.setOnClickListener((View v) ->{
            if((inflammation & 1) ==0){
                tvInflammation3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvInflammation3.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvInflammation3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvInflammation3.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            inflammation = inflammation ^ 1;
        });
        tvInflammation4.setOnClickListener((View v) ->{
            if((inflammation & 8) ==0){
                tvInflammation4.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvInflammation4.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvInflammation4.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvInflammation4.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            inflammation = inflammation ^ 8;
        });

        tvLung1.setOnClickListener((View v) ->{
            if((lung & 4) == 0){
                tvLung1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvLung1.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvLung1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvLung1.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            lung = lung ^ 4;
        });

        tvLung2.setOnClickListener((View v) ->{
            if((lung & 2) == 0){
                tvLung2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvLung2.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvLung2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvLung2.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            lung = lung ^ 2;
        });

        tvLung3.setOnClickListener((View v) ->{
            if((lung & 1) == 0){
                tvLung3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvLung3.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvLung3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvLung3.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            lung = lung ^ 1;
        });

        tvHeart1.setOnClickListener((View v) ->{
            if((heart & 4) == 0){
                tvHeart1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvHeart1.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvHeart1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvHeart1.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            heart = heart ^ 4;
        });

        tvHeart2.setOnClickListener((View v) ->{
            if((heart & 2) == 0){
                tvHeart2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvHeart2.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvHeart2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvHeart2.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            heart = heart ^ 2;

        });

        tvHeart3.setOnClickListener((View v) ->{
            if((heart & 1) == 0){
                tvHeart3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvHeart3.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvHeart3.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvHeart3.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            heart = heart ^ 1;
        });

        tvHeart4.setOnClickListener((View v) ->{
            if((heart & 8) == 0){
                tvHeart4.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvHeart4.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvHeart4.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvHeart4.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            heart = heart ^ 8;
        });

        tvBreath1.setOnClickListener((View v) ->{
            if((breath & 2) == 0){
                tvBreath1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvBreath1.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvBreath1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvBreath1.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            breath = breath ^ 2;
        });

        tvBreath2.setOnClickListener((View v) ->{
            if((breath & 1) == 0){
                tvBreath2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort_selected));
                tvBreath2.setTextColor(ContextCompat.getColor(this,R.color.red));
            }else{
                tvBreath2.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_discomfort));
                tvBreath2.setTextColor(ContextCompat.getColor(this,R.color.grey));
            }
            breath = breath ^ 1;
        });


    }

    private void initTimePicker () {
        submitTime = TimeManager.getStrDateTime();
        tvTime.setText(TimeManager.getStrTime());
        timePopupWheelView = new TimePopupWidow(DiscomfortActivity.this);
        rlTime.setOnClickListener((View v) -> {
            timePopupWheelView.showAtLocation(DiscomfortActivity.this.findViewById(R.id.main), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            backgroundAlpha(0.3f);
            timePopupWheelView.setTime(tvTime.getText().toString());
        });
        timePopupWheelView.onClickCorrectListener((View v) ->{
            timePopupWheelView.dismiss();
            String changeTime = timePopupWheelView.getTime();
            tvTime.setText(changeTime);
            submitTime = TimeManager.getStrDate()+" "+ changeTime+":00";

        });
        timePopupWheelView.onClickDeleteListener((View v) -> {
            timePopupWheelView.dismiss();
        });
    }


    private void submit() {
        btnSave.setOnClickListener((View v) ->{
            note = etNote.getText().toString();
            if((isDiscomfort ==1 & inflammation == 0 && lung ==0 && heart ==0 && breath ==0) || (isDiscomfort == -1)){
                GlobalMethod.showToast(getApplicationContext(),"您没有填写任何不适记录！",Toast.LENGTH_SHORT);
            }else{
                DiscomfortTask discomfortTask = new DiscomfortTask();
                if(isDiscomfort == 1){
                    discomfortTask.setInflammation(inflammation);
                    discomfortTask.setLung(lung);
                    discomfortTask.setHeart(heart);
                    discomfortTask.setBreath(breath);
                    discomfortTask.setMeasureTime(submitTime);
                    discomfortTask.setMemo(note);
                }else{
                    discomfortTask.setInflammation(0);
                    discomfortTask.setLung(0);
                    discomfortTask.setHeart(0);
                    discomfortTask.setBreath(0);
                    discomfortTask.setMeasureTime(submitTime);
                }

                MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                        .title(R.string.tv_notice)
                        .content("记录症状："+GlobalMethod.getLatestDiscomfort(discomfortTask)+"\n\n提交后无法修改！")
                        .positiveText(R.string.tv_sure)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        commitDataToCloud(discomfortTask);

                    }
                })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO
                                dialog.dismiss();
                            }
                        });
            }
        });
    }

    // support models
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void commitDataToCloud(DiscomfortTask discomfortTask) {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }


        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(discomfortTask);

        Log.d("wzypef", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.DISCOMFORT);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(DiscomfortActivity.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("wzycpef", "the error is: " + e);
                Log.d("wzypef", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                    }else{
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
                        GlobalMethod.updateLatestDiscomfortRecord(discomfortTask);
                        Intent intent = new Intent(DiscomfortActivity.this,DiscomfortResultActivity.class);
                        intent.putExtra("dis", GlobalMethod.getLatestDiscomfort(discomfortTask));
                        startActivity(intent);
                    }
                }
            }
        });



    }


}
