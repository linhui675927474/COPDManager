package com.zju.biomedit.copdmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.DiscomfortTask;
import com.zju.biomedit.copdmanager.model.LatestRecord;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.PefTask;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class SplashActivity extends Activity {
    private final int CODE_ENTER_APP = 112;
    private static boolean isLoadingOver = false;
    private static boolean isLoginOver = false;
    private static boolean isLoginSucceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waitLoading();
        boolean isNetworkConnection = GlobalMethod.isNetworkAvailable(getApplicationContext());
        if(isNetworkConnection){
            tryLogin();
            GlobalMethod.getNetIp();
        }
        else{
            isLoginSucceed = false;
            isLoginOver = true;
        }
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_ENTER_APP:
                    tryEnterApp();
                    break;
                default:
                    break;
            }
        }
    };
    
    /**
     *等待载入的线程
     */
    private void waitLoading(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2200);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                isLoadingOver = true;
                myHandler.sendEmptyMessage(CODE_ENTER_APP);
            }
        }).start();
    }

    /**
     * 尝试进入App
     */
    private void tryEnterApp(){
        //未等待完成或未登录结束
        if(!isLoadingOver || !isLoginOver ) return;

        // 等待加载完毕
        isLoadingOver = false; //重置
        isLoginOver = false; //重置

        Intent intent;
        if (isLoginSucceed) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            if(getIntent().getBundleExtra("bundle") != null){
                intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
            }
        } else {
            intent = new Intent(SplashActivity.this, UserLoginActivity.class);
        }

        startActivity(intent);
        finish();//回退时不会再进入这个activity
    }

    /**
     * 尝试登录
     */
    private void tryLogin(){
        // 得到指定名称的Preference文件对象， 如果没有则新建
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREFERENCES_SAVE_LOGIN_INFO, MODE_PRIVATE);
        boolean isSaved = sharedPreferences.getBoolean(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_SAVESTATE, false);
        if(!isSaved){//判断是否有保存账号信息
            isLoginSucceed = false;
            isLoginOver = true;
            tryEnterApp();
        }
        else {
            String account = sharedPreferences.getString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_ACCOUNT, "");
            String password = sharedPreferences.getString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_PASSWORD, "");
            JsonObject jsonArgs = new JsonObject();
            jsonArgs.addProperty("account", account);
            jsonArgs.addProperty("password", password);
            Log.d("loginwzy", "the json is: " + jsonArgs);

            String url = Utils.BASE_URL + Utils.WAP_LOGIN;
            Ion.with(SplashActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null) {
                        Log.d("loginwzy", "the error is: " + e);
                        GlobalMethod.showToast(getApplicationContext(), Utils.ERROR_MESSAGE, Toast.LENGTH_SHORT);
                        isLoginSucceed = false;
                        isLoginOver = true;
                        tryEnterApp();
                    }else{
                        int flag = result.get("flag").getAsInt();
                        // 登录失败,可能在别处更改了密码或者别的原因
                        if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                            isLoginSucceed = false;
                            isLoginOver = true;
                            tryEnterApp();
                        }else{
                            //登录成功
                            Log.d("loginwzy", "the result is: " + result);
                            GlobalMethod.afterLoginSucceed(result, getApplicationContext());
                            getNewestRecord();
                        }
                    }

                }
            });
        }
    }

    /**
     * 获取最新一次的记录（目前嵌套过多，待修改）
     */
    private void getNewestRecord() {
        LatestRecord latestRecord = new LatestRecord();
        PatientUserManager.getInstance().updateLatestRecord(latestRecord);
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        jsonArgs.addProperty("recordType", Utils.CAT);
        jsonArgs.addProperty("num", 1);
        String url = Utils.BASE_URL + Utils.GET_LAST_GENERIC_RECORDS;
        Ion.with(SplashActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d("wzy0718", "load cat error");
                }
                else {
                    int flag = result.get("flag").getAsInt();
                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                        Gson gson = new Gson();
                        List<ScaleTask> scaleTasks = new ArrayList<>();
                        JsonArray jsonCat = result.get("recordList").getAsJsonArray();
                        Log.d("wzy0718", jsonCat+"");
                        scaleTasks = gson.fromJson(jsonCat, new TypeToken<List<ScaleTask>>() {
                        }.getType());
                        if (scaleTasks != null && scaleTasks.size() > 0) {
                            PatientUserManager.getInstance().getLatestRecord().setCatScaleTask(scaleTasks.get(0));
                            Log.d("wzy0718scale", scaleTasks.get(0).getMeasureTime()+"");
                        }

                    }
                }

                JsonObject jsonArgs = new JsonObject();
                jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
                jsonArgs.addProperty("recordType", Utils.PEF);
                jsonArgs.addProperty("num", 1);
                String url = Utils.BASE_URL + Utils.GET_LAST_GENERIC_RECORDS;
                Ion.with(SplashActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.d("wzy0718", "load pef error");
                        }
                        else {
                            int flag = result.get("flag").getAsInt();
                            if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                                Gson gson = new Gson();
                                List<PefTask> pefTasks = new ArrayList<>();
                                JsonArray jsonPef = result.get("recordList").getAsJsonArray();
                                pefTasks = gson.fromJson(jsonPef, new TypeToken<List<PefTask>>() {
                                }.getType());
                                if (pefTasks != null && pefTasks.size() > 0) {
                                    PatientUserManager.getInstance().getLatestRecord().setPefTask(pefTasks.get(0));
                                    Log.d("wzy0718pef", pefTasks.get(0).getValue()+"");
                                }
                            }
                        }

                        JsonObject jsonArgs = new JsonObject();
                        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
                        jsonArgs.addProperty("recordType", Utils.MEDICATION);
                        jsonArgs.addProperty("num", 1);
                        String url = Utils.BASE_URL + Utils.GET_LAST_GENERIC_RECORDS;
                        Ion.with(SplashActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (e != null) {
                                    Log.d("wzy0718", "load pef error");
                                }
                                else {
                                    int flag = result.get("flag").getAsInt();
                                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                                        Gson gson = new Gson();
                                        List<MedicineTask> medicineTasks = new ArrayList<>();
                                        JsonArray jsonMedicine = result.get("recordList").getAsJsonArray();
                                        medicineTasks = gson.fromJson(jsonMedicine, new TypeToken<List<MedicineTask>>() {
                                        }.getType());
                                        if (medicineTasks != null && medicineTasks.size() > 0) {
                                            PatientUserManager.getInstance().getLatestRecord().setMedicineTask(medicineTasks.get(0));
                                            Log.d("wzy0718medicine", medicineTasks.get(0).getMedicineName());
                                        }
                                    }
                                }

                                JsonObject jsonArgs = new JsonObject();
                                jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
                                jsonArgs.addProperty("recordType", Utils.DISCOMFORT);
                                jsonArgs.addProperty("num", 1);
                                String url = Utils.BASE_URL + Utils.GET_LAST_GENERIC_RECORDS;
                                Ion.with(SplashActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if (e != null) {
                                            Log.d("wzy0718", "load pef error");
                                        }
                                        else {
                                            int flag = result.get("flag").getAsInt();
                                            if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                                                Gson gson = new Gson();
                                                List<DiscomfortTask> discomfortTasks = new ArrayList<>();
                                                JsonArray jsonDiscomfort = result.get("recordList").getAsJsonArray();
                                                discomfortTasks = gson.fromJson(jsonDiscomfort, new TypeToken<List<DiscomfortTask>>() {
                                                }.getType());
                                                if (discomfortTasks != null && discomfortTasks.size() > 0) {
                                                    PatientUserManager.getInstance().getLatestRecord().setDiscomfortTask(discomfortTasks.get(0));
                                                    Log.d("wzy0718discomfort", discomfortTasks.get(0).getInflammation()+"");
                                                }
                                            }
                                            isLoginSucceed = true;
                                            isLoginOver = true;
                                            tryEnterApp();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }




}
