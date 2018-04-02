package com.zju.biomedit.copdmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.adapter.DrugListAdapter;
import com.zju.biomedit.copdmanager.model.DrugData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugListActivity extends AppCompatActivity {
    private DrugListAdapter drugListAdapter;
    private List<DrugData> drugDataList = new ArrayList<>();
    @BindView(R.id.recyclerView_drug)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_list);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    public void back(View v){
        finish();
    }

    private void initRecyclerView() {
        setDataListItems();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(DrugListActivity.this));
        mRecyclerView.setHasFixedSize(true);
        drugListAdapter = new DrugListAdapter(DrugListActivity.this,drugDataList);
        mRecyclerView.setAdapter(drugListAdapter);
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


    }

    private void setDataListItems() {
        DrugData drugData1 = new DrugData();
        drugData1.setDrugName(getResources().getString(R.string.tv_sldie));
        drugData1.setDrugDis(getResources().getString(R.string.tv_sldie_dis));
        drugData1.setDrugPicId(R.mipmap.pic_sldie);
        DrugData drugData2 = new DrugData();
        drugData2.setDrugName(getResources().getString(R.string.tv_stxf));
        drugData2.setDrugDis("");
        drugData2.setDrugPicId(R.mipmap.pic_stxaf);
        DrugData drugData3 = new DrugData();
        drugData3.setDrugName(getResources().getString(R.string.tv_xbkdb));
        drugData3.setDrugDis(getResources().getString(R.string.tv_xbkdb_dis));
        drugData3.setDrugPicId(R.mipmap.pic_xbkdb);
        DrugData drugData4 = new DrugData();
        drugData4.setDrugName(getResources().getString(R.string.tv_srl));
        drugData4.setDrugDis(getResources().getString(R.string.tv_srl_dis));
        drugData4.setDrugPicId(R.mipmap.pic_srl);
        DrugData drugData5 = new DrugData();
        drugData5.setDrugName(getResources().getString(R.string.tv_acjp));
        drugData5.setDrugDis("");
        drugData5.setDrugPicId(R.mipmap.pic_acjp);
        DrugData drugData6 = new DrugData();
        drugData6.setDrugName("自定义用药");
        drugData6.setDrugDis("请在这里添加自定义药物");
        drugData6.setDrugPicId(R.mipmap.pic_amidi);
        drugDataList.add(drugData1);
        drugDataList.add(drugData2);
        drugDataList.add(drugData3);
        drugDataList.add(drugData4);
        drugDataList.add(drugData5);
        drugDataList.add(drugData6);
    }
}
