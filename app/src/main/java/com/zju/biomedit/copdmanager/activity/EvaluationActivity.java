package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.cat.CatActivity1;
import com.zju.biomedit.copdmanager.activity.depression.DepressionActivity1;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EvaluationActivity extends AppCompatActivity {
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.btn_retest)
    Button btnRetest;
    @BindView(R.id.btn_continue)
    Button btnContinue;
    @BindView(R.id.rl_evaluation)
    RelativeLayout rlEvaluation;
    @BindView(R.id.iv_evaluation)
    ImageView ivEvaluation;
    @BindView(R.id.tv_evaluation_point)
    TextView tvPoint;
    @BindView(R.id.tv_evaluation)
    TextView tvEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        ButterKnife.bind(this);
        setClick();
        setEvaluation();
    }

    @Override
    public void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_EVALUATION);
        super.onPause();
    }

    @Override
    public void onResume() {
        UserAgent.OnResume(Utils.P_EVALUATION);
        super.onResume();
    }

    private void setClick() {
        tvFinish.setOnClickListener((View v) -> {
            Intent intent = new Intent(EvaluationActivity.this,ScaleListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("FLAG",1);
            startActivity(intent);
            finish();
        });

        btnContinue.setOnClickListener((View v) -> {
            Intent intent = new Intent(EvaluationActivity.this,DepressionActivity1.class);
            startActivity(intent);
        });

        btnRetest.setOnClickListener((View v) -> {
            Intent intent = new Intent(EvaluationActivity.this,CatActivity1.class);
            startActivity(intent);
        });
    }

    private void setEvaluation() {
        int point = GlobalMethod.calculateEvaluationValue();
        if(point == 20){
            rlEvaluation.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_background_evaluation_bad));
            ivEvaluation.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_bad));
            String strPoint = point+"分";
            tvPoint.setText(strPoint);
            tvEvaluation.setText(getResources().getString(R.string.tv_bad));
        }else if(point == 40){
            rlEvaluation.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_background_evaluation_poor));
            ivEvaluation.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_poor));
            String strPoint = point+"分";
            tvPoint.setText(strPoint);
            tvEvaluation.setText(getResources().getString(R.string.tv_poor));
        }else if(point == 60){
            rlEvaluation.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_background_evaluation_mid));
            ivEvaluation.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_medium));
            String strPoint = point+"分";
            tvPoint.setText(strPoint);
            tvEvaluation.setText(getResources().getString(R.string.tv_mid));
        }else if(point == 80){
            rlEvaluation.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_background_evaluation_great));
            ivEvaluation.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_great));
            String strPoint = point+"分";
            tvPoint.setText(strPoint);
            tvEvaluation.setText(getResources().getString(R.string.tv_great));
        }
    }
}
