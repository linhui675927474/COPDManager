package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.popupwindow.TimePopupWidow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzheyu on 2017/7/6.
 */

public class DrugHistoryAdapter extends RecyclerView.Adapter<DrugHistoryAdapter.DrugViewHolder>{
    private List<MedicineTask> mDrugSet;
    private Context context; //设置全局Application会与现有的冲突
    private TimePopupWidow timePopupWheelView;

    //构造器，接受数据集
    public DrugHistoryAdapter(List<MedicineTask> data){
        this.mDrugSet = data;
    }

    @Override
    public DrugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug_history,parent,false);
        return new DrugViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DrugViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String drugName = mDrugSet.get(position).getMedicineName();
        String useTime = mDrugSet.get(position).getMeasureTime().substring(11,16);
        holder.mName.setText(drugName);
        holder.mTime.setText(useTime);
    }

    @Override
    public int getItemCount() {
        return mDrugSet.size();
    }

    class DrugViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_history_drug_name)
        TextView mName;
        @BindView(R.id.tv_history_time)
        TextView mTime;

        public DrugViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
