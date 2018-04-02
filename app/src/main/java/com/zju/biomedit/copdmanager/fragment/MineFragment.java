package com.zju.biomedit.copdmanager.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.CollectActivity;
import com.zju.biomedit.copdmanager.activity.ReminderActivity;
import com.zju.biomedit.copdmanager.activity.UserInformationActivity;
import com.zju.biomedit.copdmanager.activity.UserLoginActivity;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.step.LocalStepHelper;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.MyApplication;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.TimeService;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment {
    @BindView(R.id.rl_user_information)
    RelativeLayout rlInformation;
    @BindView(R.id.rl_user_remind)
    RelativeLayout rlRemind;
    @BindView(R.id.rl_user_collection)
    RelativeLayout rlCollection;
    @BindView(R.id.rl_user_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.btn_log_out)
    Button tvLogout;
    @BindView(R.id.tv_step)
    TextView tvStep;
    private int total_step = 0;//走的总步数
    private LocalStepChangedReceiver stepChangedReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        onCLick();
        //setUserInfo();
        Log.i("mine", "oncreate");
        initData();
        return view;

    }

    @Override
    public void onResume() {
        // 注册计步变化广播
        stepChangedReceiver = new LocalStepChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(Utils.LOCAL_STEP_CHANGED_ACTION);
        getActivity().registerReceiver(stepChangedReceiver, intentFilter);
        Log.i("mine", "onresume");
        super.onResume();
    }

    @Override
    public void onPause() {
        // 取消注册计步变化广播
        getActivity().unregisterReceiver(stepChangedReceiver);
        Log.i("mine", "onpause");
        super.onPause();
    }


    private void initData() {
        total_step = LocalStepHelper.getLocalStepCount(MyApplication.getContext());
        String strStep = Integer.toString(total_step);
        tvStep.setText(strStep);
    }

    private void onCLick() {
        rlInformation.setOnClickListener((View v) -> {
            Intent intent = new Intent(this.getActivity(), UserInformationActivity.class);
            startActivity(intent);
        });
        rlCollection.setOnClickListener((View v) -> {
//            GlobalMethod.showToast(this.getActivity(), "用户收藏功能暂时未开放", Toast.LENGTH_SHORT);
            Intent intent = new Intent(this.getActivity(), CollectActivity.class);
            startActivity(intent);
        });
        rlFeedback.setOnClickListener((View v) -> {
            GlobalMethod.showToast(this.getActivity(), "用户反馈功能暂时未开放", Toast.LENGTH_SHORT);
        });
        rlRemind.setOnClickListener((View v) -> {
            Intent intent = new Intent(this.getActivity(), ReminderActivity.class);
            startActivity(intent);
        });
        tvLogout.setOnClickListener((View v) -> {
            //清除本地保存的登录信息
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Utils.PREFERENCES_SAVE_LOGIN_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_SAVESTATE, false);
            editor.putString(Utils.PREFERENCES_SAVE_LOGIN_INFO_KEY_PASSWORD, "");
            editor.apply();
            PatientUserManager.getInstance().resetAll(); //清空暂存的所有信息
            //停止TimeService
            Intent timeIntent = new Intent(this.getActivity().getApplicationContext(), TimeService.class);
            this.getActivity().stopService(timeIntent);

            // 注销后进入登录界面
            Intent intent = new Intent(this.getActivity(), UserLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            ;//清空activity栈
            startActivity(intent);
        });
    }

    private void setUserInfo() {
        String name = PatientUserManager.getInstance().getLoginUserInfo().getPatientName();
        String sexCode = PatientUserManager.getInstance().getLoginUserInfo().getSexCode();
        String birthDate = PatientUserManager.getInstance().getLoginUserInfo().getBirthDate();
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
        int age = TimeManager.getAgeByDateString(birthDate);

    }

    /**
     * 本地计步变化的广播接收者
     */
    private class LocalStepChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            String action = intent.getAction();
            if (action.equals(Utils.LOCAL_STEP_CHANGED_ACTION)) {
                total_step = intent.getIntExtra(Utils.INTENT_EXTRA_KEY_LOCAL_STEP, 0);
                String strStep = Integer.toString(total_step);
                tvStep.setText(strStep);
            }
        }
    }


}
