package com.zju.biomedit.copdmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.LoginUserInfo;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInformationActivity extends AppCompatActivity {
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.rl_birth)
    RelativeLayout rlBirth;
    @BindView(R.id.rl_height)
    RelativeLayout rlHeight;
    @BindView(R.id.rl_weight)
    RelativeLayout rlWeight;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.tv_user_birth)
    TextView tvUserBirth;
    @BindView(R.id.tv_user_age)
    TextView tvUserAge;
    @BindView(R.id.tv_user_height)
    TextView tvUserHeight;
    @BindView(R.id.tv_user_weight)
    TextView tvUserWeight;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        ButterKnife.bind(this);
        setUserinfo();
        clickEvent();
    }

    @Override
    public void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_USER_INFO);
        super.onPause();
    }

    @Override
    public void onResume() {
        UserAgent.OnResume(Utils.P_USER_INFO);
        super.onResume();
    }

    public void back(View v){
        finish();
    }

    private void clickEvent() {
        rlName.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
        rlSex.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
        rlBirth.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
        rlHeight.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
        rlWeight.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
        rlPhone.setOnClickListener((View v) ->{
            Toast.makeText(this,"信息修改功能暂时未开放",Toast.LENGTH_SHORT).show();
        });
    }

    private void setUserinfo(){
        LoginUserInfo loginUserInfo = PatientUserManager.getInstance().getLoginUserInfo();
        String name = loginUserInfo.getPatientName();
        String sexCode = loginUserInfo.getSexCode();
        String birth = loginUserInfo.getBirthDate();
        float height = loginUserInfo.getNewestHeight();
        float weight = loginUserInfo.getNewestWeight();
        String phone = loginUserInfo.getPhoneNumber();
        String age = TimeManager.getAgeByDateString(birth)+"岁";
        String strHeight = (int) height + "cm";
        String strWeight = (int) weight + "kg";
        String strBirth = birth.substring(0,birth.length()-8);
        String sex;
        switch (sexCode) {
            case ("M"):
                sex = "男";
                break;
            case ("F"):
                sex = "女";
                break;
            default:
                sex = "性别不明";
                break;
        }

        tvUserName.setText(name);
        tvUserSex.setText(sex);
        tvUserBirth.setText(strBirth);
        tvUserAge.setText(age);
        tvUserHeight.setText(strHeight);
        tvUserWeight.setText(strWeight);
        tvUserPhone.setText(phone);

    }
}
