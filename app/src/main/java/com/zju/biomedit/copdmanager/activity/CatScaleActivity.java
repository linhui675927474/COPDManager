package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.CATScaleAdapter;
import com.zju.biomedit.copdmanager.model.CatScale;
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

public class CatScaleActivity extends AppCompatActivity {
    @BindView(R.id.btn_cat)
    Button btnSubmit;
    @BindView(R.id.rv_cat)
    RecyclerView catRecyclerView;
    private ArrayList<CatScale> catData;
    private Realm realm;
    private CatScale catScale1;
    private CatScale catScale2;
    private CatScale catScale3;
    private CatScale catScale4;
    private CatScale catScale5;
    private CatScale catScale6;
    private CatScale catScale7;
    private CatScale catScale8;
    private int sum;
    private String answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catscale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        initData();
        submitScore();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_SCALE_CAT);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_SCALE_CAT);
        super.onResume();
    }

    private void initRecyclerView(){
        RecyclerView.LayoutManager catLayoutManager =  new LinearLayoutManager(CatScaleActivity.this);
        catRecyclerView.setLayoutManager(catLayoutManager);
        CATScaleAdapter catScaleAdapter = new CATScaleAdapter(catData);
        catRecyclerView.setAdapter(catScaleAdapter);
    }

    private void submitScore() {
        btnSubmit.setOnClickListener((View v) -> {
            sum = 0;
            answers = "";
            int isFinished = 1;
            for (int i=0;i<8;i++){
                if(catData.get(i).getScore() == -1){
                    Toast.makeText(CatScaleActivity.this,"您还有题目未填写",Toast.LENGTH_SHORT).show();
                    isFinished = 0;
                    break;
                }else {
                    sum = sum + catData.get(i).getScore();
                    answers = answers + catData.get(i).getScore() + "";
                    Log.i("pid"+i,catData.get(i).getScore()+"");
                    if (i < 7) {
                        answers = answers + ",";
                    }
                }
            }
            if (isFinished ==1 ) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(CatScaleActivity.this)
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

    private void initData() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        catData = new ArrayList<>();
        catScale1 = realm.where(CatScale.class).equalTo("problemId",1).findFirst();//需要定义为全局变量
        catScale2 = realm.where(CatScale.class).equalTo("problemId",2).findFirst();
        catScale3 = realm.where(CatScale.class).equalTo("problemId",3).findFirst();
        catScale4 = realm.where(CatScale.class).equalTo("problemId",4).findFirst();
        catScale5 = realm.where(CatScale.class).equalTo("problemId",5).findFirst();
        catScale6 = realm.where(CatScale.class).equalTo("problemId",6).findFirst();
        catScale7 = realm.where(CatScale.class).equalTo("problemId",7).findFirst();
        catScale8 = realm.where(CatScale.class).equalTo("problemId",8).findFirst();
        Log.i("id1",""+catScale1);
        if(catScale1 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale1 = realm.createObject(CatScale.class,1);
                catScale1.setScore(-1);
                catScale1.setZeroPointText("我从不咳嗽");
                catScale1.setFivePointText("我一直在咳嗽");
                catData.add(catScale1);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale1.setScore(-1);
                catData.add(catScale1);
            });
        }
        if (catScale2 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale2 = realm.createObject(CatScale.class,2);
                catScale2.setScore(-1);
                catScale2.setZeroPointText("我胸腔里一点痰也没有");
                catScale2.setFivePointText("我胸腔里有很多很多痰");
                catData.add(catScale2);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale2.setScore(-1);
                catData.add(catScale2);
            });
        }

        if (catScale3 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale3 = realm.createObject(CatScale.class,3);
                catScale3.setScore(-1);
                catScale3.setZeroPointText("我一点也没有胸闷的感觉");
                catScale3.setFivePointText("我胸闷的感觉很严重");
                catData.add(catScale3);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale3.setScore(-1);
                catData.add(catScale3);
            });
        }

        if (catScale4 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale4 = realm.createObject(CatScale.class,4);
                catScale4.setScore(-1);
                catScale4.setZeroPointText("当我在爬坡或爬一层楼梯时，我并不感觉喘不过气来");
                catScale4.setFivePointText("当我在爬坡或爬一层楼梯时，我感觉非常喘不过气来");
                catData.add(catScale4);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale4.setScore(-1);
                catData.add(catScale4);
            });
        }

        if (catScale5 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale5 = realm.createObject(CatScale.class,5);
                catScale5.setScore(-1);
                catScale5.setZeroPointText("我的居家活动不会受到限制");
                catScale5.setFivePointText("我的居家活动受到很大的限制");
                catData.add(catScale5);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale5.setScore(-1);
                catData.add(catScale5);
            });
        }

        if (catScale6 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale6 = realm.createObject(CatScale.class,6);
                catScale6.setScore(-1);
                catScale6.setZeroPointText("尽管我有肺部疾病，我还是有信心外出");
                catScale6.setFivePointText("因为我的肺部疾病，我完全没有信心外出");
                catData.add(catScale6);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale6.setScore(-1);
                catData.add(catScale6);
            });
        }

        if (catScale7 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale7 = realm.createObject(CatScale.class,7);
                catScale7.setScore(-1);
                catScale7.setZeroPointText("我睡得安稳\n");//使对话框底部对齐，需要更好的方法
                catScale7.setFivePointText("因为我的肺部疾病，我睡得不安稳");
                catData.add(catScale7);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale7.setScore(-1);
                catData.add(catScale7);
            });
        }

        if (catScale8 == null){
            realm.executeTransaction((Realm realm) -> {
                catScale8 = realm.createObject(CatScale.class,8);
                catScale8.setScore(-1);
                catScale8.setZeroPointText("我活力旺盛");
                catScale8.setFivePointText("我一点活力都没有");
                catData.add(catScale8);
            });
        }else {
            realm.executeTransaction((Realm realm) -> {
                catScale8.setScore(-1);
                catData.add(catScale8);
            });
        }
        initRecyclerView();
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

        Log.d("wzycat", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.CAT);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;   // "http://120.27.141.50:18909/COPDService.svc//CommitGenericRecord"

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(CatScaleActivity.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("wzycat", "the error is: " + e);
                Log.d("wzycat", "the result is: " + result);

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
                            Intent intent = new Intent(CatScaleActivity.this,MainActivity.class);
                            intent.putExtra("FLAG",1);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(CatScaleActivity.this,EvaluationActivity.class);
                            startActivity(intent);
                        }

                    }
                }

            }
        });



    }

}
