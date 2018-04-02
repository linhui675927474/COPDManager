package com.zju.biomedit.copdmanager.activity.cat;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CatActivity1 extends AppCompatActivity {
    @BindView(R.id.tv_ans0)
    TextView tvAns0;
    @BindView(R.id.tv_ans1)
    TextView tvAns1;
    @BindView(R.id.tv_ans2)
    TextView tvAns2;
    @BindView(R.id.tv_ans3)
    TextView tvAns3;
    @BindView(R.id.tv_ans4)
    TextView tvAns4;
    @BindView(R.id.tv_ans5)
    TextView tvAns5;
    public static int CAT_SCALE_ANS1 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_SCALE_CAT);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_SCALE_CAT);
        super.onResume();
    }

    private void initData() {
        tvAns0.setOnClickListener((View v) -> {
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 0;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

        tvAns1.setOnClickListener((View v) -> {
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 1;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

        tvAns2.setOnClickListener((View v) -> {
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 2;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

        tvAns3.setOnClickListener((View v) -> {
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 3;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

        tvAns4.setOnClickListener((View v) -> {
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 4;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

        tvAns5.setOnClickListener((View v) -> {
            tvAns5.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare_pressed));
            tvAns5.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.white));
            tvAns1.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns1.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns2.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns2.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns3.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns3.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns4.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns4.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            tvAns0.setBackground(ContextCompat.getDrawable(CatActivity1.this, R.drawable.shape_select_scare));
            tvAns0.setTextColor(ContextCompat.getColor(CatActivity1.this, R.color.colorPrimary));
            CAT_SCALE_ANS1 = 5;
            Intent intent = new Intent(CatActivity1.this, CatActivity2.class);
            startActivity(intent);
        });

    }
}
