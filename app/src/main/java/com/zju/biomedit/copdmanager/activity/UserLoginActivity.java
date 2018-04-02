package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserLoginActivity extends AppCompatActivity {
    @BindView(R.id.btn_log_in)
    Button tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.user)
    EditText user;
    @BindView(R.id.password)
    EditText password;
    private SharedPreferences sharedPreferences;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        initView();
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case Utils.RESULTCODE_FROM_REGIST_ACTIVITY:
                String patientId = data.getStringExtra(RegisterActivity.RETURN_ARGUMENT_PATIENT_ID);
                Log.i("wzylogin07189",patientId);
                user.setText(patientId);
                user.setSelection(patientId.length());
                password.setText("");
                break;
            default:
                break;
        }
    }

    private void initView() {
        sharedPreferences  = getSharedPreferences(Utils.PREFERENCES_SAVE_LOGIN_INFO, MODE_PRIVATE);
        String savedAccount = sharedPreferences.getString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_ACCOUNT, "");
        String savedPassword = sharedPreferences.getString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_PASSWORD, "");
        Log.i("wzylogin07189",savedAccount);
        user.setText(savedAccount);
        user.setSelection(savedAccount.length());
        password.setText(savedPassword);
    }

    private void setListener() {
        tvLogin.setOnClickListener((View v)->{
            wapLogin();
        });
        tvRegister.setOnClickListener((View v)->{
            Intent intent = new Intent(UserLoginActivity.this,RegisterActivity.class);
            startActivityForResult(intent, Utils.REQUESTCODE_FOR_REGIST_ACTIVITY);
        });
    }

    private void wapLogin() {
        //访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_LOGIN)
                .progress(true, 0);
        dialog = builder.build();
        dialog.show();
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("account", user.getText().toString());
        jsonArgs.addProperty("password", password.getText().toString());

        String url = Utils.BASE_URL + Utils.WAP_LOGIN;
        Ion.with(UserLoginActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("wzy717", "the result is: " + result);
                Log.d("wzy717", "the error is: " + e);
                if (e != null) {
                    dialog.dismiss();
                    GlobalMethod.showToast(getApplicationContext(), Utils.ERROR_MESSAGE, Toast.LENGTH_SHORT);
                    Log.d("wzy717","the error is:"+e);
                }else{
                    String messageTips = "异常消息";
                    int flag = result.get("flag").getAsInt();
                    switch (flag) {
                        case Utils.SERVICE_RETURN_FAILED:
                            messageTips = "用户账号或密码错误";
                            break;
                        case Utils.SERVICE_RETURN_ERROR:
                            messageTips = "登录发生异常";
                            break;
                        case Utils.SERVICE_RETURN_ACCOUNT_DISABLED:
                            messageTips = "该用户已被禁用";
                            break;
                        default:
                            break;
                    }
                    // 登录失败
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        dialog.dismiss();
                        GlobalMethod.showToast(getApplicationContext(), messageTips, Toast.LENGTH_SHORT);
                    }else{
                        //登录成功
                        Log.d("wzy717", "the result is: " + result);
                        GlobalMethod.afterLoginSucceed(result, getApplicationContext());
                        //保存用户登录信息
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_ACCOUNT, user.getText().toString());
                        editor.putString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_PASSWORD, password.getText().toString());
                        editor.putBoolean(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_SAVESTATE, true);
                        editor.apply();
                        getNewestRecord();
                    }
                }

            }
        });
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
        Ion.with(UserLoginActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                Ion.with(UserLoginActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                        Ion.with(UserLoginActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                                Ion.with(UserLoginActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
                                            // 进入主界面
                                            dialog.dismiss();
                                            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
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
