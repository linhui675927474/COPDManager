package com.zju.biomedit.copdmanager.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.fragment.RegisterFirstStepFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSecondStepFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepEightFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepFiveFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepFourFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepNineFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepOneFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepSevenFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepSixFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepTenFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepThreeFragment;
import com.zju.biomedit.copdmanager.fragment.RegisterSubStepTwoFragment;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.WapRegisterInfo;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements FragmentRegister {
    public static final String RETURN_ARGUMENT_PATIENT_ID = "PATIENT_ID";

    private RegisterFirstStepFragment firstStepFragment;
    private RegisterSecondStepFragment secondStepFragment;
    private RegisterSubStepOneFragment subStepOneFragment;
    private RegisterSubStepTwoFragment subStepTwoFragment;
    private RegisterSubStepThreeFragment subStepThreeFragment;
    private RegisterSubStepFourFragment subStepFourFragment;
    private RegisterSubStepFiveFragment subStepFiveFragment;
    private RegisterSubStepSixFragment subStepSixFragment;
    private RegisterSubStepSevenFragment subStepSevenFragment;
    private RegisterSubStepEightFragment subStepEightFragment;
    private RegisterSubStepNineFragment subStepNineFragment;
    private RegisterSubStepTenFragment subStepTenFragment;

    @BindView(R.id.tv_valid_patient_id)
    TextView tvValidPatientId;
    @BindView(R.id.tv_valid_patient_name)
    TextView tvValidPatientName;
    @BindView(R.id.layout_basic_info)
    LinearLayout layoutBasicInfo;
    @BindView(R.id.layout_progress)
    RelativeLayout layoutProgress;
    @BindView(R.id.progressbar_register)
    ProgressBar progressStep;
    @BindView(R.id.tv_current_progress)
    TextView tvProgress;

    private int currentProgress;

    //手机端注册信息
    private String commonPatientId;
    private String commonPatientName;
    private WapRegisterInfo wapRegisterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();

        // 防止待机、Home键等退出再进入时重建fragment
        if(savedInstanceState == null) {
            setDefaultFragment();
        }
    }

    public void back(View v){
        finish();
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        firstStepFragment = new RegisterFirstStepFragment();
        transaction.replace(R.id.content_fragment, firstStepFragment, Utils.REGISTER_STEP_VALIDATE);
        transaction.commit();
    }

    private void initView() {
        hideBasicInfo();
        setProgressStep(0);
        wapRegisterInfo = new WapRegisterInfo();
    }

    private void hideBasicInfo(){
        tvValidPatientId.setText("");
        tvValidPatientName.setText("");
        layoutBasicInfo.setVisibility(View.GONE);
    }

    private void setProgressStep(int step){
        currentProgress = step;
        progressStep.setProgress(step);
        tvProgress.setText(String.valueOf(step));
        if(step == 0){
            layoutProgress.setVisibility(View.GONE);
        }
        else if(step == 1) {
            layoutProgress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回键处理
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (currentProgress){
            case 0:
            case 1:
                hideBasicInfo();
                setProgressStep(0);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                setProgressStep(currentProgress - 1);
                break;
        }
    }

    @Override
    public void onPreviousStepButtonClick(String stepName) {
        super.onBackPressed();
        switch (stepName){
            case Utils.REGISTER_STEP_IDENTITY_CARD:
                hideBasicInfo();
                setProgressStep(0);
                break;
            case Utils.REGISTER_STEP_BIRTHDAY:
                setProgressStep(1);
                break;
            case Utils.REGISTER_STEP_SEX:
                setProgressStep(2);
                break;
            case Utils.REGISTER_STEP_HEIGHT:
                setProgressStep(3);
                break;
            case Utils.REGISTER_STEP_WEIGHT:
                setProgressStep(4);
                break;
            case Utils.REGISTER_STEP_EDUCATION:
                setProgressStep(5);
                break;
            case Utils.REGISTER_STEP_PROFESSION:
                setProgressStep(6);
                break;
            case Utils.REGISTER_STEP_CONTACT:
                setProgressStep(7);
                break;
            case Utils.REGISTER_STEP_ADDRESS:
                setProgressStep(8);
                break;
            case Utils.REGISTER_STEP_SMOKE:
                setProgressStep(9);
                break;
            case Utils.REGISTER_STEP_FINISH:
                setProgressStep(10);
                break;
        }
    }

    @Override
    public void onNextStepButtonClick(String info, String stepName) {
        switch (stepName){
            case Utils.REGISTER_STEP_IDENTITY_CARD:
                subStepTwoFragment = new RegisterSubStepTwoFragment();
                subStepTwoFragment.setCustomArguments(info);
                replaceFragment(subStepTwoFragment, Utils.REGISTER_STEP_BIRTHDAY);
                setProgressStep(2);
                wapRegisterInfo.setIdentityCardNumber(info);
                break;
            case Utils.REGISTER_STEP_BIRTHDAY:
                subStepThreeFragment = new RegisterSubStepThreeFragment();
                replaceFragment(subStepThreeFragment, Utils.REGISTER_STEP_SEX);
                setProgressStep(3);
                wapRegisterInfo.setBirthDate(info);
                break;
            case Utils.REGISTER_STEP_SEX:
                subStepFourFragment = new RegisterSubStepFourFragment();
                replaceFragment(subStepFourFragment, Utils.REGISTER_STEP_HEIGHT);
                setProgressStep(4);
                wapRegisterInfo.setSex(info);
                break;
            case Utils.REGISTER_STEP_HEIGHT:
                subStepFiveFragment = new RegisterSubStepFiveFragment();
                replaceFragment(subStepFiveFragment, Utils.REGISTER_STEP_WEIGHT);
                setProgressStep(5);
                wapRegisterInfo.setHeight(info);
                break;
            case Utils.REGISTER_STEP_WEIGHT:
                subStepSixFragment = new RegisterSubStepSixFragment();
                replaceFragment(subStepSixFragment, Utils.REGISTER_STEP_EDUCATION);
                setProgressStep(6);
                wapRegisterInfo.setWeight(info);
                break;
            case Utils.REGISTER_STEP_EDUCATION:
                subStepSevenFragment = new RegisterSubStepSevenFragment();
                replaceFragment(subStepSevenFragment, Utils.REGISTER_STEP_PROFESSION);
                setProgressStep(7);
                wapRegisterInfo.setEducation(info);
                break;
            case Utils.REGISTER_STEP_PROFESSION:
                subStepEightFragment = new RegisterSubStepEightFragment();
                replaceFragment(subStepEightFragment, Utils.REGISTER_STEP_CONTACT);
                setProgressStep(8);
                wapRegisterInfo.setProfession(info);
                break;
            case Utils.REGISTER_STEP_CONTACT:
                subStepNineFragment = new RegisterSubStepNineFragment();
                replaceFragment(subStepNineFragment, Utils.REGISTER_STEP_ADDRESS);
                setProgressStep(9);
                wapRegisterInfo.setPhoneNumber(info);
                break;
            case Utils.REGISTER_STEP_ADDRESS:
                subStepTenFragment = new RegisterSubStepTenFragment();
                replaceFragment(subStepTenFragment, Utils.REGISTER_STEP_SMOKE);
                setProgressStep(10);
                wapRegisterInfo.setAddress(info);
                break;
            case Utils.REGISTER_STEP_SMOKE:
                secondStepFragment = new RegisterSecondStepFragment();
                secondStepFragment.setCustomArguments(commonPatientId, commonPatientName);
                replaceFragment(secondStepFragment, Utils.REGISTER_STEP_FINISH);
                setProgressStep(11);
                Boolean isSmoke = (info.equals("吸烟"));
                wapRegisterInfo.setSmoke(isSmoke);
                break;
        }
    }

    private void replaceFragment(Fragment fragment, String tag){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_fragment, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onValidateStepButtonClick(String patientId, String patientName){
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_VALIDATE)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        final String id = patientId;
        final String name = patientName;

        String url = Utils.BASE_URL + Utils.VALIDATE_REGISTER;
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", id);
        jsonArgs.addProperty("patientName", name);

        Ion.with(RegisterActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.i("wzyregist",e +" ");
                Log.i("wzyregist",result +" ");
                dialog.dismiss();
                if(e != null){
                    GlobalMethod.showToast(getApplicationContext(), Utils.ERROR_MESSAGE, Toast.LENGTH_SHORT);
                }else {
                    int flag = result.get("flag").getAsInt();
                    if(flag == Utils.SERVICE_RETURN_SUCCEED){ //注册新用户
                        subStepOneFragment = new RegisterSubStepOneFragment();
                        replaceFragment(subStepOneFragment, Utils.REGISTER_STEP_IDENTITY_CARD);
                        //显示注册进度
                        setProgressStep(1);
                        setCommonPatientInfo(id, name);
                        showBasicInfo(id, name);
                    }
                    else {
                        String messageTips = "异常消息";
                        switch(flag)
                        {
                            case Utils.SERVICE_RETURN_FAILED:
                                messageTips = "此用户已注册";
                                break;
                            case Utils.SERVICE_RETURN_ERROR:
                                messageTips = "异常消息";
                                break;
                            default:
                                break;
                        }
                        GlobalMethod.showToast(getApplicationContext(), messageTips, Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }


    @Override
    public void onFinishButtonClick(Bundle bundle){
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_REGISTER)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        // 得到参数
        String password = bundle.getString(RegisterSecondStepFragment.ARGUMENT_PASSWORD);
        wapRegisterInfo.setPatientId(commonPatientId);
        wapRegisterInfo.setPatientName(commonPatientName);
        wapRegisterInfo.setPassword(password);
        String info = JsonUtil.toJson(wapRegisterInfo);

        String url = Utils.BASE_URL + Utils.WAP_REGIST_WITH_PATIENT_INFO;
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", commonPatientId);
        jsonArgs.addProperty("info", info);

        Ion.with(RegisterActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();
                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.ERROR_MESSAGE, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                        // 注册成功
                        String messageTips = "注册成功";
                        GlobalMethod.showToast(getApplicationContext(), messageTips, Toast.LENGTH_SHORT);
                        addKnoListToNewUser();
                        Intent intent = new Intent();
                        intent.putExtra(RETURN_ARGUMENT_PATIENT_ID, commonPatientId);
                        RegisterActivity.this.setResult(Utils.RESULTCODE_FROM_REGIST_ACTIVITY, intent);
                        RegisterActivity.this.finish();
                    }else{
                        String messageTips = "异常消息";
                        switch (flag) {
                            case Utils.SERVICE_RETURN_FAILED:
                                messageTips = "注册失败";
                                break;
                            case Utils.SERVICE_RETURN_ERROR:
                                messageTips = "异常消息";
                                break;
                            default:
                                break;
                        }
                        GlobalMethod.showToast(getApplicationContext(), messageTips, Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }


    private void setCommonPatientInfo(String patientId, String patientName){
        this.commonPatientId = patientId;
        this.commonPatientName = patientName;
    }

    private void showBasicInfo(String patientId, String patientName){
        tvValidPatientId.setText(patientId);
        tvValidPatientName.setText(patientName);
        layoutBasicInfo.setVisibility(View.VISIBLE);
    }

    private void addKnoListToNewUser() {
        String knoUrl = Utils.NEW_USER_URL + commonPatientId;
        Ion.with(this).load(knoUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("addKno", result + "");
                if (e != null) {
                    Log.d("addKno", "kno read error");
                }
            }
        });
    }
}
