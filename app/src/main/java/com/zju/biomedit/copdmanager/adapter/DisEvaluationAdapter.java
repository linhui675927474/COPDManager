package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.DisEvaluation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzheyu on 2017/10/27.
 */

public class DisEvaluationAdapter extends RecyclerView.Adapter<DisEvaluationAdapter.DisViewHolder>{
    private List<DisEvaluation> mDisSet;
    private Context context;
    private int code;

    //构造器，接受数据集
    public DisEvaluationAdapter(List<DisEvaluation> data){
        this.mDisSet = data;
    }

    @Override
    public DisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_history,parent,false);
        return new DisViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DisViewHolder holder, final int position) {
        //将数据填充到具体的view中
        String disName = mDisSet.get(position).getDisName();
        String disEvaluation = mDisSet.get(position).getDisEvaluation();
        holder.tvName.setText(disName);
        holder.tvEvaulation.setText(disEvaluation);


    }

    @Override
    public int getItemCount() {
        return mDisSet.size();
    }

    class DisViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_dis_name)
        TextView tvName;
        @BindView(R.id.tv_dis_evaluation)
        TextView tvEvaulation;

        public DisViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}
