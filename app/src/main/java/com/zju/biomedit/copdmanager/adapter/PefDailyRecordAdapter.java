package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;

import java.util.ArrayList;
import java.util.HashMap;


public class PefDailyRecordAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;

    //构造函数，传入数据
    public PefDailyRecordAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }


    //定义Viewholder
    class Viewholder extends RecyclerView.ViewHolder  {
        private TextView pefValue;

        public Viewholder(View root) {
            super(root);
            pefValue = (TextView) root.findViewById(R.id.pef_value);

        }

        public TextView getPefValue() {
            return pefValue;
        }



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(inflater.inflate(R.layout.list_cell, null));
    }//在这里把ViewHolder绑定Item的布局

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Viewholder vh = (Viewholder) holder;
        // 绑定数据到ViewHolder里面
        vh.pefValue.setText((String) listItem.get(position).get("pefValue"));
    }

    //返回Item数目
    @Override
    public int getItemCount() {
        return listItem.size();
    }


}
