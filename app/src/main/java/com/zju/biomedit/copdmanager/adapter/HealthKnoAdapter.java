package com.zju.biomedit.copdmanager.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.KnoContentActivity;
import com.zju.biomedit.copdmanager.activity.MsgContentActivity;
import com.zju.biomedit.copdmanager.model.HealthKno;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzheyu on 2017/7/6.
 */

public class HealthKnoAdapter extends RecyclerView.Adapter<HealthKnoAdapter.KnoViewHolder>{
    private List<HealthKno> mKnoSet;
    private Activity context; //设置全局Application会与现有的冲突

    //构造器，接受数据集
    public HealthKnoAdapter(Activity activity,List<HealthKno> data){
        context = activity;
        this.mKnoSet = data;
    }

    @Override
    public KnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kno,parent,false);
        return new KnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final KnoViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String strKnoName = mKnoSet.get(position).getKnoName();
        String strKnoTime = mKnoSet.get(position).getKnoTime();
        Boolean ifKnoRead = mKnoSet.get(position).getKnoIfRead();
        holder.knoName.setText(strKnoName);
        holder.knoTime.setText(strKnoTime.substring(0,10));
        if (ifKnoRead) {
            holder.knoRead.setText(context.getResources().getText(R.string.read));
            holder.knoRead.setTextColor(ContextCompat.getColor(context,R.color.red));
        } else {
            holder.knoRead.setText(context.getResources().getText(R.string.not_read));
            holder.knoRead.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        Glide.with(context).load(mKnoSet.get(position).getKnoLink()).into(holder.knoIv);
        holder.knoCard.setOnClickListener((View v) ->{
            Intent intent = new Intent(context, MsgContentActivity.class);
            intent.putExtra("knoId", mKnoSet.get(position).getKnoId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mKnoSet.size();
    }

    class KnoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.kno_name)
        TextView knoName;
        @BindView(R.id.kno_time)
        TextView knoTime;
        @BindView(R.id.kno_read)
        TextView knoRead;
        @BindView(R.id.kno_iv)
        ImageView knoIv;
        @BindView(R.id.kno_card)
        CardView knoCard;

        public KnoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
