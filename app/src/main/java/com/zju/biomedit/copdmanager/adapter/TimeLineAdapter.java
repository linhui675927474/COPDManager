package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.MedicineTask;

import java.util.List;


/**
 * Created by wangzheyu on 2017/7/12.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {
    private List<MedicineTask> mMedicineTaskList;//与参数区分命名
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<MedicineTask> medicineTaskList) {
        mMedicineTaskList = medicineTaskList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;
        view = mLayoutInflater.inflate(R.layout.item_timeline,parent,false);
        return new TimeLineViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder,int position) {
        MedicineTask medicineTask = mMedicineTaskList.get(position);
        holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext,R.drawable.ic_marker));
        holder.mDate.setText(medicineTask.getMeasureTime().substring(11,16));
        holder.mMessage.setText(medicineTask.getMedicineName());
    }

    @Override
    public int getItemCount() {
        return (mMedicineTaskList!=null? mMedicineTaskList.size():0);
    }
}
