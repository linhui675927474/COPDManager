package com.zju.biomedit.copdmanager.activity.depression;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.MainActivity;
import com.zju.biomedit.copdmanager.activity.ScaleListActivity;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity1;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity2;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity3;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity4;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity5;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity6;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity7;
import com.zju.biomedit.copdmanager.model.DepressionScale;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DepressionActivity9 extends AppCompatActivity {
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.tv_sometimes)
    TextView tvSometimes;
    @BindView(R.id.tv_half)
    TextView tvHalf;
    @BindView(R.id.tv_everyday)
    TextView tvEveryday;
    @BindView(R.id.btn_depression)
    Button btnSubmit;
    public static int DEPRESSION_SCALE_ANS9 = -1;
    private ArrayList<Integer> scoreList;
    private int sum;
    private String answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depression9);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_SCALE_DEPRESS);
        super.onPause();
    }

    private void initData() {
        tvNot.setOnClickListener((View v) -> {
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare_pressed));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS9 = 0;
        });

        tvSometimes.setOnClickListener((View v) -> {
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare_pressed));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.white));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS9 = 1;
        });

        tvHalf.setOnClickListener((View v) -> {
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare_pressed));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS9 = 2;
        });

        tvEveryday.setOnClickListener((View v) -> {
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare_pressed));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity9.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity9.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS9 = 3;
        });

        btnSubmit.setOnClickListener((View v) -> {
            getScore();
            sum = 0;
            answers = "";
            int isFinished = 1;
            for (int i=0;i<9;i++){
                if(scoreList.get(i) == -1){
                    Toast.makeText(DepressionActivity9.this,"您还有题目未填写",Toast.LENGTH_SHORT).show();//实际只有第九题可能没填
                    isFinished = 0;
                    break;
                }else {
                    sum = sum + scoreList.get(i);
                    answers = answers + scoreList.get(i) + "";
                    if (i < 8) {
                        answers = answers + ",";
                    }
                }

            }
            if (isFinished ==1 ){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(DepressionActivity9.this)
                        .title(R.string.tv_notice)
                        .content(R.string.tv_notice_text)
                        .positiveText(R.string.tv_sure)
                        .negativeText(R.string.tv_cancel);
                MaterialDialog dialog = builder.build();
                dialog.show();
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                        commitDataToCloud();
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

    private void getScore() {
        scoreList = new ArrayList<>();
        scoreList.add(DepressionActivity1.DEPRESSION_SCALE_ANS1);
        scoreList.add(DepressionActivity2.DEPRESSION_SCALE_ANS2);
        scoreList.add(DepressionActivity3.DEPRESSION_SCALE_ANS3);
        scoreList.add(DepressionActivity4.DEPRESSION_SCALE_ANS4);
        scoreList.add(DepressionActivity5.DEPRESSION_SCALE_ANS5);
        scoreList.add(DepressionActivity6.DEPRESSION_SCALE_ANS6);
        scoreList.add(DepressionActivity7.DEPRESSION_SCALE_ANS7);
        scoreList.add(DepressionActivity8.DEPRESSION_SCALE_ANS8);
        scoreList.add(DepressionActivity9.DEPRESSION_SCALE_ANS9);
    }

    private void commitDataToCloud() {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        ScaleTask phqTask = new ScaleTask();
        phqTask.setId(0);
        phqTask.setScore(sum);
        phqTask.setDuration(0);
        phqTask.setMeasureTime(TimeManager.getStrDateTime());
        phqTask.setAnswer(answers);

        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(phqTask);

        Log.d("wzyphq", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.PHQ);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(DepressionActivity9.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("wzyphq", "the error is: " + e);
                Log.d("wzyphq", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                    }else{
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
                        Intent intent = new Intent(DepressionActivity9.this,AnxietyActivity1.class);
                        startActivity(intent);
                    }
                }
            }
        });



    }


}
