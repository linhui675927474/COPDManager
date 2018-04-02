package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.TimeLineAdapter;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.tv_today_empty)
    TextView tvEmpty;
    private List<MedicineTask> medicineTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);
        ButterKnife.bind(this);
        setClick();
        setDataListItems();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_MEDICINE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_MEDICINE);
        super.onResume();
    }

    public void back(View v){
        finish();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(DrugActivity.this));
        mRecyclerView.setHasFixedSize(true);
        TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(medicineTaskList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems() {
        String startDate = TimeManager.getStrDate()+" 00:00:00";
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        jsonArgs.addProperty("recordType", Utils.MEDICATION);
        jsonArgs.addProperty("start", startDate);
        jsonArgs.addProperty("end", TimeManager.getStrDateTime());
        Log.i("wzydrug",jsonArgs+"");
        String url = Utils.BASE_URL + Utils.GET_GENERIC_RECORDS;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_GET_MEDICINE_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        Ion.with(DrugActivity.this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();
                Log.d("wzydrug", result+"");
                if (e != null) {
                    Log.d("wzydrug", "load drug error");
                }else {
                    int flag = result.get("flag").getAsInt();
                    if (flag == Utils.SERVICE_RETURN_SUCCEED) {
                        Gson gson = new Gson();
                        List<MedicineTask> medicineTasks = new ArrayList<>();
                        JsonArray jsonMedicine = result.get("recordList").getAsJsonArray();
                        medicineTasks = gson.fromJson(jsonMedicine, new TypeToken<List<MedicineTask>>() {
                        }.getType());
                        if (medicineTasks != null && medicineTasks.size() > 0) {
                            medicineTaskList = medicineTasks;
                            initRecyclerView();
                        }else{
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void setClick() {
        tvAdd.setOnClickListener((View v) ->{
            Intent intent = new Intent(DrugActivity.this,DrugListActivity.class);
            startActivity(intent);
        });
        tvHistory.setOnClickListener((View v) ->{
            Intent intent = new Intent(DrugActivity.this,DrugHistoryActivity.class);
            startActivity(intent);
        });
    }


}
