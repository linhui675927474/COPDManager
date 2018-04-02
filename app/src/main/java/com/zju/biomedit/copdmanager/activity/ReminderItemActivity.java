package com.zju.biomedit.copdmanager.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.Switch;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.Reminder;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ReminderItemActivity extends AppCompatActivity {
    @BindView(R.id.tv_remind_item)
    TextView tvItem;
    @BindView(R.id.tv_reminder_time)
    TextView tvTime;
    @BindView(R.id.tv_reminder_freq)
    TextView tvFreq;
    @BindView(R.id.rl_reminder_fq)
    RelativeLayout rlFreq;
    @BindView(R.id.rl_reminder_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_reminder_content_time)
    TextView tvContentTime;
    @BindView(R.id.tv_reminder_content_fq)
    TextView tvContentFreq;
    @BindView(R.id.sw_reminder)
    Switch swReminder;
    @BindView(R.id.cb_sound)
    CheckBox cbSound;
    private int reminderType;
    private TimePopupWidow timePopupWheelView;
    private Realm realm;
    private Reminder catReminder;
    private Reminder phqReminder;
    private Reminder gadReminder;
    private Reminder drugReminder;
    private Reminder pefReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_item);
        ButterKnife.bind(this);
        reminderType = getIntent().getIntExtra("type",0);
        initView();
        initTimePicker();
        onClick();
    }

    @Override
    public void onPause() {
        switch (reminderType) {
            case 0:
                UserAgent.OnPause(getApplicationContext(), Utils.P_REMIND_CAT);
                break;
            case 1:
                UserAgent.OnPause(getApplicationContext(), Utils.P_REMIND_DEPRESS);
                break;
            case 2:
                UserAgent.OnPause(getApplicationContext(), Utils.P_REMIND_ANXIETY);
                break;
            case 3:
                UserAgent.OnPause(getApplicationContext(), Utils.P_REMIND_MEDICINE);
                break;
            case 4:
                UserAgent.OnPause(getApplicationContext(), Utils.P_REMIND_PEF);
                break;
            default:
                break;

        }
        super.onPause();
    }

    @Override
    public void onResume() {
        switch (reminderType) {
            case 0:
                UserAgent.OnResume(Utils.P_REMIND_CAT);
                break;
            case 1:
                UserAgent.OnResume(Utils.P_REMIND_DEPRESS);
                break;
            case 2:
                UserAgent.OnResume(Utils.P_REMIND_ANXIETY);
                break;
            case 3:
                UserAgent.OnResume(Utils.P_REMIND_MEDICINE);
                break;
            case 4:
                UserAgent.OnResume(Utils.P_REMIND_PEF);
                break;
            default:
                break;
        }
        super.onResume();
    }

    private void initView(){
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        switch(reminderType){
            case 0:
                tvItem.setText(getResources().getText(R.string.tv_cat_reminder));
                tvFreq.setText("每周");
                tvContentFreq.setText("每周");
                catReminder = realm.where(Reminder.class).equalTo("type",0).findFirst();
                if(catReminder == null){
                    realm.executeTransaction((Realm realm) -> {
                        catReminder = realm.createObject(Reminder.class,0);
                        catReminder.setFreq(1);
                        catReminder.setDefault(false);
                        catReminder.setUseSound(false);
                        catReminder.setTime(TimeManager.getStrTime());
                    });
                }
                tvTime.setText(catReminder.getTime());
                tvContentTime.setText(catReminder.getTime());
                swReminder.setChecked(catReminder.isDefault());
                cbSound.setChecked(catReminder.isUseSound());
                break;
            case 1:
                tvItem.setText(getResources().getText(R.string.tv_phq_reminder));
                tvFreq.setText("每月");
                tvContentFreq.setText("每月");
                phqReminder = realm.where(Reminder.class).equalTo("type",1).findFirst();
                if(phqReminder == null){
                    realm.executeTransaction((Realm realm) -> {
                        phqReminder = realm.createObject(Reminder.class,1);
                        phqReminder.setFreq(2);
                        phqReminder.setDefault(false);
                        phqReminder.setUseSound(false);
                        phqReminder.setTime(TimeManager.getStrTime());
                    });
                }
                tvTime.setText(phqReminder.getTime());
                tvContentTime.setText(phqReminder.getTime());
                swReminder.setChecked(phqReminder.isDefault());
                cbSound.setChecked(phqReminder.isUseSound());
                break;
            case 2:
                tvItem.setText(getResources().getText(R.string.tv_gad_reminder));
                tvFreq.setText("每月");
                tvContentFreq.setText("每月");
                gadReminder = realm.where(Reminder.class).equalTo("type",2).findFirst();
                if(gadReminder == null){
                    realm.executeTransaction((Realm realm) -> {
                        gadReminder = realm.createObject(Reminder.class,2);
                        gadReminder.setFreq(2);
                        gadReminder.setDefault(false);
                        gadReminder.setUseSound(false);
                        gadReminder.setTime(TimeManager.getStrTime());
                    });
                }
                tvTime.setText(gadReminder.getTime());
                tvContentTime.setText(gadReminder.getTime());
                swReminder.setChecked(gadReminder.isDefault());
                cbSound.setChecked(gadReminder.isUseSound());
                break;
            case 3:
                tvItem.setText(getResources().getText(R.string.tv_drug_reminder));
                tvFreq.setText("每天");
                tvContentFreq.setText("每天");
                drugReminder = realm.where(Reminder.class).equalTo("type",3).findFirst();
                if(drugReminder == null){
                    realm.executeTransaction((Realm realm) -> {
                        drugReminder = realm.createObject(Reminder.class,3);
                        drugReminder.setFreq(0);
                        drugReminder.setDefault(false);
                        drugReminder.setUseSound(false);
                        drugReminder.setTime(TimeManager.getStrTime());
                    });
                }
                tvTime.setText(drugReminder.getTime());
                tvContentTime.setText(drugReminder.getTime());
                swReminder.setChecked(drugReminder.isDefault());
                cbSound.setChecked(drugReminder.isUseSound());
                break;
            case 4:
                tvItem.setText(getResources().getText(R.string.tv_pef_reminder));
                tvFreq.setText("每周");
                tvContentFreq.setText("每周");
                pefReminder = realm.where(Reminder.class).equalTo("type",4).findFirst();
                if(pefReminder == null){
                    realm.executeTransaction((Realm realm) -> {
                        pefReminder = realm.createObject(Reminder.class,4);
                        pefReminder.setFreq(1);
                        pefReminder.setDefault(false);
                        pefReminder.setUseSound(false);
                        pefReminder.setTime(TimeManager.getStrTime());
                    });
                }
                tvTime.setText(pefReminder.getTime());
                tvContentTime.setText(pefReminder.getTime());
                swReminder.setChecked(pefReminder.isDefault());
                cbSound.setChecked(pefReminder.isUseSound());
                break;
            default:
                break;
        }

    }

    public void back(View v){
        finish();
    }

    public void save(View v){
        switch (reminderType){
            case 0:
                realm.executeTransaction((Realm realm) -> {
                    catReminder.setDefault(swReminder.isChecked());
                    catReminder.setUseSound(cbSound.isChecked());
                    catReminder.setTime(tvTime.getText().toString());
                });
                UserAgent.OnEvent(ReminderItemActivity.this, Utils.E_REMIND_CAT);
                break;
            case 1:
                realm.executeTransaction((Realm realm) -> {
                    phqReminder.setDefault(swReminder.isChecked());
                    phqReminder.setUseSound(cbSound.isChecked());
                    phqReminder.setTime(tvTime.getText().toString());
                });
                UserAgent.OnEvent(ReminderItemActivity.this, Utils.E_REMIND_DEPRESS);
                break;
            case 2:
                realm.executeTransaction((Realm realm) -> {
                    gadReminder.setDefault(swReminder.isChecked());
                    gadReminder.setUseSound(cbSound.isChecked());
                    gadReminder.setTime(tvTime.getText().toString());
                });
                UserAgent.OnEvent(ReminderItemActivity.this, Utils.E_REMIND_ANXIETY);
                break;
            case 3:
                realm.executeTransaction((Realm realm) -> {
                    drugReminder.setDefault(swReminder.isChecked());
                    drugReminder.setUseSound(cbSound.isChecked());
                    drugReminder.setTime(tvTime.getText().toString());
                });
                UserAgent.OnEvent(ReminderItemActivity.this, Utils.E_REMIND_MEDICINE);
                break;
            case 4:
                realm.executeTransaction((Realm realm) -> {
                    pefReminder.setDefault(swReminder.isChecked());
                    pefReminder.setUseSound(cbSound.isChecked());
                    pefReminder.setTime(tvTime.getText().toString());
                });
                UserAgent.OnEvent(ReminderItemActivity.this, Utils.E_REMIND_PEF);
                break;
            default:
                break;

        }
        GlobalMethod.showToast(this,"设置成功！", Toast.LENGTH_SHORT);
        finish();
    }

    private void onClick() {
        rlFreq.setOnClickListener((View v) ->{
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(R.string.tv_notice)
                    .content(R.string.tv_freq_hint)
                    .positiveText(R.string.tv_know);
            MaterialDialog dialog = builder.build();
            dialog.show();
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    // TODO
                    dialog.dismiss();

                }
            });
        });
    }

    private void initTimePicker () {
        timePopupWheelView = new TimePopupWidow(ReminderItemActivity.this);
        rlTime.setOnClickListener((View v) -> {
            timePopupWheelView.showAtLocation(ReminderItemActivity.this.findViewById(R.id.main), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            backgroundAlpha(0.3f);
            timePopupWheelView.setTime(tvTime.getText().toString());
        });
        timePopupWheelView.onClickCorrectListener((View v) ->{
            timePopupWheelView.dismiss();
            String changeTime = timePopupWheelView.getTime();
            tvTime.setText(changeTime);
            tvContentTime.setText(changeTime);
            //submitTime = TimeManager.getStrDate()+" "+changeTime+":00";

        });
        timePopupWheelView.onClickDeleteListener((View v) -> {
            timePopupWheelView.dismiss();
        });
    }

    // support models
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
