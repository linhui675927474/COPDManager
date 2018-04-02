package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.NewKnoAdapter;
import com.zju.biomedit.copdmanager.fragment.HealthKnoFragment;
import com.zju.biomedit.copdmanager.model.HealthKno;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView_collect_kno)
    RecyclerView rvKno;
    private List<HealthKno> healthKnoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        getKnoData();
    }

    private void initRecyclerView() {
        rvKno.setLayoutManager(new LinearLayoutManager(this));
        rvKno.setHasFixedSize(true);
        NewKnoAdapter newKnoAdapter = new NewKnoAdapter(R.layout.item_kno, healthKnoList);
        newKnoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CollectActivity.this, MsgContentActivity.class);
                intent.putExtra("knoId", healthKnoList.get(position).getKnoId());
                CollectActivity.this.startActivity(intent);
            }
        });
        rvKno.setAdapter(newKnoAdapter);
    }

    private void getKnoData() {
        healthKnoList = new ArrayList<>();
//        String url = "http://120.27.141.50:8080/health-knowledge/COPDKnoService.jsp";
        String url = "http://120.27.141.50:8080/copd/message/getKnoListPaging?type=1&items_per_page=" + 10000 + "&page_index=" + 1 + "&patientId=" + PatientUserManager.getInstance().getPatientId();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(Utils.MESSAGE_GET_COLLECT_KNO_LIST)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();
        Ion.with(this).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();
                Log.d("wzykno", result + "");
                if (e != null) {
                    Log.d("wzykno", "load kno error");
                } else {
                    Gson gson = new Gson();
                    List<HealthKno> knoList = new ArrayList<>();
                    JsonArray jsonKno = null;
                    try {
                        jsonKno = result.get("result").getAsJsonObject().get("items").getAsJsonArray();
                        knoList = gson.fromJson(jsonKno, new TypeToken<List<HealthKno>>() {
                        }.getType());
                        Log.d("wzyknonew", knoList + "");
                        if (knoList != null && knoList.size() > 0) {
                            for (int i = 0;i<knoList.size();i++){
                                if(knoList.get(i).getKnoIfFavorite()){
                                    healthKnoList.add(knoList.get(i));
                                }
                            }
                            initRecyclerView();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }


}
