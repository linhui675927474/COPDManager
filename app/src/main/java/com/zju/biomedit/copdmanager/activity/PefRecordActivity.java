package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aigestudio.wheelpicker.WheelPicker;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.PefTask;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;
import com.zju.biomedit.copdmanager.support.CustomNumericKeyboard;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PefRecordActivity extends AppCompatActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_pef_select)
    TextView tvPef;
    @BindView(R.id.tv_his)
    TextView tvHis;
    @BindView(R.id.pef_wheel)
    WheelPicker pefWheelPicker;
    @BindView(R.id.rl_pef_time)
    RelativeLayout rlPefTime;
    @BindView(R.id.edt_input)
    EditText edtInput;
    @BindView(R.id.layout_numeric_keyboard)
    LinearLayout layoutKeyboard;
    @BindView(R.id.btn_pef_submit)
    Button btnSubmit;
    private CustomNumericKeyboard customNumericKeyboard;
    private TimePopupWidow timePopupWheelView;
    private int pefValue;
    private String submitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pef_record);
        ButterKnife.bind(this);
        initData();
//        initPicker();
        initView();
        initTimePicker();
        submit();

    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_PEF);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_PEF);
        super.onResume();
    }

    public void back(View v){
        finish();
    }

    private void initData() {
        tvTime.setText(TimeManager.getStrTime());
        submitTime = TimeManager.getStrDateTime();
        pefValue = PatientUserManager.getInstance().getPefStandard();
        String text = pefValue+"";
        edtInput.setText(text);
//        String textSource = "您选择的峰流速为: <font color='#ff5722'><big>"+pefValue+"L/min</big></font>";
//        tvPef.setText(Html.fromHtml(textSource));
    }

    private void initView() {
        customNumericKeyboard = new CustomNumericKeyboard(layoutKeyboard, edtInput);
        customNumericKeyboard.hidePoint();
        customNumericKeyboard.setAllNumKeysBackground(R.drawable.selector_num_key_white_to_blue);
        GlobalMethod.hideSoftInputMethod(edtInput, this);
        edtInput.requestFocus();
        tvHis.setOnClickListener((View v) -> {
            Intent intent = new Intent(this,PefHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void initPicker() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 851; i++)
            data.add(i);
        pefWheelPicker.setData(data);
        pefWheelPicker.setVisibleItemCount(11);
        pefWheelPicker.setSelectedItemPosition(PatientUserManager.getInstance().getPefStandard());
        pefWheelPicker.setIndicator(true);
        pefWheelPicker.setIndicatorColor(Color.parseColor("#FF5722"));
        pefWheelPicker.setItemTextColor(Color.parseColor("#81D4FA"));
        pefWheelPicker.setItemTextSize(80);
        pefWheelPicker.setItemSpace(8);
        pefWheelPicker.setIndicatorSize(6);
        //pefWheelPicker.setAtmospheric(true);
        pefWheelPicker.setCurved(true);
        pefWheelPicker.setSelectedItemTextColor(Color.parseColor("#03A9F4"));
        pefWheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                String textSource = "您选择的峰流速为: <font color='#ff5722'><big>"+position+"L/min</big></font>";
                tvPef.setText(Html.fromHtml(textSource));
                pefValue = position;

            }
        });
    }

    private void initTimePicker () {
        timePopupWheelView = new TimePopupWidow(PefRecordActivity.this);
        rlPefTime.setOnClickListener((View v) -> {
            timePopupWheelView.showAtLocation(PefRecordActivity.this.findViewById(R.id.main), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            backgroundAlpha(0.3f);
            timePopupWheelView.setTime(tvTime.getText().toString());
        });
        timePopupWheelView.onClickCorrectListener((View v) ->{
            timePopupWheelView.dismiss();
            String changeTime = timePopupWheelView.getTime();
            tvTime.setText(changeTime);
            submitTime = TimeManager.getStrDate()+" "+changeTime+":00";

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

    private void submit() {
        btnSubmit.setOnClickListener((View v) -> {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(R.string.tv_notice)
                    .content(R.string.tv_notice_text)
                    .positiveText(R.string.tv_sure)
                    .negativeText(R.string.tv_cancel);
            MaterialDialog dialog = builder.build();
            dialog.show();
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                            commitDataToCloud();

                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            dialog.dismiss();
                        }
                    });
        });
    }

    private void commitDataToCloud() {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        pefValue = Integer.parseInt(edtInput.getText().toString());
        PefTask pefTask = new PefTask();
        pefTask.setId(0);
        pefTask.setMeasureTime(submitTime);
        pefTask.setValue(pefValue);


        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(pefTask);

        Log.d("wzypef", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.PEF);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(PefRecordActivity.this).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("wzycpef", "the error is: " + e);
                Log.d("wzypef", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                    }else{
                        GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
                        GlobalMethod.updateLatestPefRecord(pefTask);
                        Intent intent = new Intent(PefRecordActivity.this,MainActivity.class);
                        intent.putExtra("FLAG",1);
                        startActivity(intent);
                    }
                }
            }
        });



    }

}
