package com.zju.biomedit.copdmanager.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.MainActivity;
import com.zju.biomedit.copdmanager.dialog.DrugSelectDialog;
import com.zju.biomedit.copdmanager.model.DrugData;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.JsonUtil;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzheyu on 2017/7/6.
 */

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder>{
    private List<DrugData> mDrugSet;
    private Activity context;
    private TimePopupWidow timePopupWheelView;
    private String medicineName;
    private String measureTime;

    //构造器，接受数据集
    public DrugListAdapter(Activity activity,List<DrugData> data){
        context = activity;
        this.mDrugSet = data;
    }

    @Override
    public DrugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        //context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug,parent,false);
        return new DrugViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DrugViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String drugName = mDrugSet.get(position).getDrugName();
        String drugDis = mDrugSet.get(position).getDrugDis();
        int picId = mDrugSet.get(position).getDrugPicId();
        holder.mName.setText(drugName);
        if(!drugDis.isEmpty()){
            holder.mDis.setText(drugDis);
            holder.mDis.setVisibility(View.VISIBLE);
        }else{
            holder.mDis.setVisibility(View.GONE);
        }
        holder.mDrug.setImageDrawable(ContextCompat.getDrawable(context,picId));
        holder.mAddDrug.setOnClickListener((View v) -> {
            initDialog(drugName,picId);
        });
    }

    @Override
    public int getItemCount() {
        return mDrugSet.size();
    }

    class DrugViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_drug)
        ImageView mDrug;
        @BindView(R.id.tv_drug_name)
        TextView mName;
        @BindView(R.id.tv_drug_dis)
        TextView mDis;
        @BindView(R.id.tv_add_drug)
        TextView mAddDrug;

        public DrugViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void initDialog(String drugName,int picId) {
        measureTime = TimeManager.getStrDateTime();
        medicineName = drugName;
        final DrugSelectDialog dialog = new DrugSelectDialog(context);
        dialog.setDrugName(drugName)
                .setDrugTime(TimeManager.getStrTime())
                .setImageResId(picId)
                .setonCLickDrugListener(new DrugSelectDialog.OnCLickDrugListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                        commitDataToCloud(dialog);
                    }

                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onTimeClick() {
                        initTimePicker(dialog);
                    }
                }).show();
    }

    private void initTimePicker (DrugSelectDialog drugSelectDialog) {
        timePopupWheelView = new TimePopupWidow(context);
        timePopupWheelView.setTime(drugSelectDialog.getDrugTime());
        timePopupWheelView.onClickCorrectListener((View v) -> {
            String changeTime = timePopupWheelView.getTime();
            measureTime = TimeManager.getStrDate()+" "+changeTime+":00";
            drugSelectDialog.setDrugTime(changeTime).show();
            timePopupWheelView.dismiss();
        });
        timePopupWheelView.onClickDeleteListener((View v) -> {
            timePopupWheelView.dismiss();
        });
        timePopupWheelView.showAtLocation(drugSelectDialog.findViewById(R.id.rl_drug), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void commitDataToCloud(DrugSelectDialog drugSelectDialog) {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(context, Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        if(medicineName.equals(Utils.CUSTOM_DRUG)){
            medicineName = drugSelectDialog.getDrugCustomName();
        }

        MedicineTask medicineTask = new MedicineTask();
        medicineTask.setId(0);
        medicineTask.setMedicineName(medicineName);
        medicineTask.setMeasureTime(measureTime);

        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(medicineTask);

        Log.d("wzymedicine", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.MEDICATION);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .content(Utils.MESSAGE_UPLOADING)
                .progress(true, 0);
        MaterialDialog dialog = builder.build();
        dialog.show();

        Ion.with(context).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                dialog.dismiss();

                Log.d("wzymedicien", "the error is: " + e);
                Log.d("wzymedicine", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(context, Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    if (flag != Utils.SERVICE_RETURN_SUCCEED) {
                        GlobalMethod.showToast(context, Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                    }else{
                        GlobalMethod.showToast(context, Utils.MESSAGE_UPLOAD_SUCCESS, Toast.LENGTH_SHORT);
                        GlobalMethod.updateLatestMedicineRecord(medicineTask);
                        Intent intent = new Intent(context,MainActivity.class);
                        intent.putExtra("FLAG",1);
                        context.startActivity(intent);
                    }
                }
            }
        });



    }


}
