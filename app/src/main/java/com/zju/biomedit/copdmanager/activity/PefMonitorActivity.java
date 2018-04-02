package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.PefDailyRecordAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PefMonitorActivity extends AppCompatActivity {
    @BindView(R.id.btn_pef)
    Button btnPefRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pef_monitor);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        btnPefRecord.setOnClickListener((View v) -> {
            Intent intentPefRecord = new Intent(PefMonitorActivity.this, PefRecordActivity.class);
            startActivity(intentPefRecord);
        });
    }

    public void back(View v){
        finish();
    }

}
