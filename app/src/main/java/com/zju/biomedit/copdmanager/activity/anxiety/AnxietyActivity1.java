package com.zju.biomedit.copdmanager.activity.anxiety;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AnxietyActivity1 extends AppCompatActivity {
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.tv_sometimes)
    TextView tvSometimes;
    @BindView(R.id.tv_half)
    TextView tvHalf;
    @BindView(R.id.tv_everyday)
    TextView tvEveryday;
    public static int ANXIETY_SCALE_ANS1 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anxiety1);
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
    protected void onResume() {
        UserAgent.OnResume(Utils.P_SCALE_ANXIETY);
        super.onResume();
    }

    private void initData() {
        tvNot.setOnClickListener((View v) -> {
            tvNot.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare_pressed));
            tvNot.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            ANXIETY_SCALE_ANS1 = 0;
            Intent intent = new Intent(AnxietyActivity1.this,AnxietyActivity2.class);
            startActivity(intent);
        });

        tvSometimes.setOnClickListener((View v) -> {
            tvSometimes.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare_pressed));
            tvSometimes.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.white));
            tvNot.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            ANXIETY_SCALE_ANS1 = 1;
            Intent intent = new Intent(AnxietyActivity1.this,AnxietyActivity2.class);
            startActivity(intent);

        });

        tvHalf.setOnClickListener((View v) -> {
            tvHalf.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare_pressed));
            tvHalf.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            ANXIETY_SCALE_ANS1 = 2;
            Intent intent = new Intent(AnxietyActivity1.this,AnxietyActivity2.class);
            startActivity(intent);
        });

        tvEveryday.setOnClickListener((View v) -> {
            tvEveryday.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare_pressed));
            tvEveryday.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(AnxietyActivity1.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(AnxietyActivity1.this,R.color.colorPrimary));
            ANXIETY_SCALE_ANS1 = 3;
            Intent intent = new Intent(AnxietyActivity1.this,AnxietyActivity2.class);
            startActivity(intent);
        });
    }

}
