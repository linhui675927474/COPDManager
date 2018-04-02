package com.zju.biomedit.copdmanager.activity.cat;

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
import com.zju.biomedit.copdmanager.activity.CatScaleActivity;
import com.zju.biomedit.copdmanager.activity.EvaluationActivity;
import com.zju.biomedit.copdmanager.activity.MainActivity;
import com.zju.biomedit.copdmanager.activity.ScaleListActivity;
import com.zju.biomedit.copdmanager.activity.anxiety.AnxietyActivity7;
import com.zju.biomedit.copdmanager.activity.depression.DepressionActivity1;
import com.zju.biomedit.copdmanager.model.AnxietyScale;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatActivity8 extends AppCompatActivity {
    @BindView(R.id.tv_ans0)
    TextView tvAns0;
    @BindView(R.id.tv_ans1)
    TextView tvAns1;
    @BindView(R.id.tv_ans2)
    TextView tvAns2;
    @BindView(R.id.tv_ans3)
    TextView tvAns3;
    @BindView(R.id.tv_ans4)
    TextView tvAns4;
    @BindView(R.id.tv_ans5)
    TextView tvAns5;
    @BindView(R.id.btn_cat)
    Button btnSubmit;
    private int sum;
    private String answers;
    private ArrayList<Integer> scoreList;
    public static int CAT_SCALE_ANS8 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat8);
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

    private void initData(){
        tvAns0.setOnClickListener((View v) -> {
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 0;
        });

        tvAns1.setOnClickListener((View v) -> {
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 1;
        });

        tvAns2.setOnClickListener((View v) -> {
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 2;
        });

        tvAns3.setOnClickListener((View v) -> {
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 3;
        });

        tvAns4.setOnClickListener((View v) -> {
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 4;
        });

        tvAns5.setOnClickListener((View v) -> {
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare_pressed));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity8.this,R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity8.this,R.color.colorPrimary));
            CAT_SCALE_ANS8 = 5;
        });

        btnSubmit.setOnClickListener((View v) -> {
            getScore();
            sum = 0;
            answers = "";
            int isFinished = 1;
            for (int i=0;i<8;i++){
                if(scoreList.get(i) == -1){
                    GlobalMethod.showToast(CatActivity8.this,"您还有题目未填写",Toast.LENGTH_SHORT);//实际只有第九题可能没填
                    isFinished = 0;
                    break;
                }else {
                    sum = sum + scoreList.get(i);
                    answers = answers + scoreList.get(i) + "";
                    if (i < 7) {
                        answers = answers + ",";
                    }
                }

            }
            if (isFinished ==1 ){
                MaterialDialog.Builder builder = new MaterialDialog.Builder(CatActivity8.this)
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
        scoreList.add(CatActivity1.CAT_SCALE_ANS1);
        scoreList.add(CatActivity2.CAT_SCALE_ANS2);
        scoreList.add(CatActivity3.CAT_SCALE_ANS3);
        scoreList.add(CatActivity4.CAT_SCALE_ANS4);
        scoreList.add(CatActivity5.CAT_SCALE_ANS5);
        scoreList.add(CatActivity6.CAT_SCALE_ANS6);
        scoreList.add(CatActivity7.CAT_SCALE_ANS7);
        scoreList.add(CatActivity8.CAT_SCALE_ANS8);
    }

    private void commitDataToCloud() {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        ScaleTask catTask = new ScaleTask();
        catTask.setId(0);
        catTask.setScore(sum);
        catTask.setDuration(0);
        catTask.setMeasureTime(TimeManager.getStrDateTime());
        catTask.setAnswer(answers);

        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(catTask);

        Log.d("wzyphq", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.CAT);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(CatActivity8.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                        GlobalMethod.updateLatestCatRecord(catTask);
                        int point = GlobalMethod.calculateEvaluationValue();
                        if(point == 0){
                            Intent intent = new Intent(CatActivity8.this, DepressionActivity1.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(CatActivity8.this,EvaluationActivity.class);
                            startActivity(intent);
                        }

                    }
                }
            }
        });
    }
}
