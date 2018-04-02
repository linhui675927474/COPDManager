package com.zju.biomedit.copdmanager.activity.depression;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.DepressionScale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DepressionActivity5 extends AppCompatActivity {
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.tv_sometimes)
    TextView tvSometimes;
    @BindView(R.id.tv_half)
    TextView tvHalf;
    @BindView(R.id.tv_everyday)
    TextView tvEveryday;
    public static int DEPRESSION_SCALE_ANS5 = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depression5);
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

    private void initData() {
        tvNot.setOnClickListener((View v) -> {
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare_pressed));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS5 = 0;
            Intent intent = new Intent(DepressionActivity5.this,DepressionActivity6.class);
            startActivity(intent);
        });

        tvSometimes.setOnClickListener((View v) -> {
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare_pressed));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.white));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS5 = 1;
            Intent intent = new Intent(DepressionActivity5.this,DepressionActivity6.class);
            startActivity(intent);

        });

        tvHalf.setOnClickListener((View v) -> {
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare_pressed));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS5 = 2;
            Intent intent = new Intent(DepressionActivity5.this,DepressionActivity6.class);
            startActivity(intent);
        });

        tvEveryday.setOnClickListener((View v) -> {
            tvEveryday.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare_pressed));
            tvEveryday.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.white));
            tvSometimes.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvSometimes.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvHalf.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvHalf.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            tvNot.setBackground(ContextCompat.getDrawable(DepressionActivity5.this,R.drawable.shape_select_scare));
            tvNot.setTextColor(ContextCompat.getColor(DepressionActivity5.this,R.color.colorPrimary));
            DEPRESSION_SCALE_ANS5 = 3;
            Intent intent = new Intent(DepressionActivity5.this,DepressionActivity6.class);
            startActivity(intent);
        });
    }

}
