package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity {
    @BindView(R.id.rl_cat_reminder)
    RelativeLayout rlCat;
    @BindView(R.id.rl_phq_reminder)
    RelativeLayout rlPhq;
    @BindView(R.id.rl_gad_reminder)
    RelativeLayout rlGad;
    @BindView(R.id.rl_drug_reminder)
    RelativeLayout rlDrug;
    @BindView(R.id.rl_pef_reminder)
    RelativeLayout rlPef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);
        onClick();
    }

    @Override
    public void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_USER_REMIND);
        super.onPause();
    }

    @Override
    public void onResume() {
        UserAgent.OnResume(Utils.P_USER_REMIND);
        super.onResume();
    }

    private void onClick() {
        rlCat.setOnClickListener((View v) -> {
            Intent intent = new Intent(ReminderActivity.this, ReminderItemActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        });
        rlPhq.setOnClickListener((View v) -> {
            Intent intent = new Intent(ReminderActivity.this, ReminderItemActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        });
        rlGad.setOnClickListener((View v) -> {
            Intent intent = new Intent(ReminderActivity.this, ReminderItemActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        });
        rlDrug.setOnClickListener((View v) -> {
            Intent intent = new Intent(ReminderActivity.this, ReminderItemActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        });
        rlPef.setOnClickListener((View v) -> {
            Intent intent = new Intent(ReminderActivity.this, ReminderItemActivity.class);
            intent.putExtra("type", 4);
            startActivity(intent);
        });
    }

    public void back(View v) {
        finish();
    }
}
