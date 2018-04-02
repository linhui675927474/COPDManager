package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.DisEvaluationAdapter;
import com.zju.biomedit.copdmanager.model.DisEvaluation;
import com.zju.biomedit.copdmanager.support.GlobalMethod;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscomfortResultActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_discomfort)
    RecyclerView rvDis;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    private String strDiscomfort;
    private DisEvaluationAdapter disEvaluationAdapter;
    private List<DisEvaluation> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discomfort_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        strDiscomfort = intent.getStringExtra("dis");
        onCLick();
        initRecyclerView();

    }

    private void onCLick() {
        tvFinish.setOnClickListener((View v) ->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {
        rvDis.setLayoutManager(new LinearLayoutManager(DiscomfortResultActivity.this));
        rvDis.setHasFixedSize(true);
        dataList = GlobalMethod.getDisEvaluation(strDiscomfort);
        disEvaluationAdapter = new DisEvaluationAdapter(dataList);
        rvDis.setAdapter(disEvaluationAdapter);
    }

}
