package com.zju.biomedit.copdmanager.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.HealthKno;
import com.zju.biomedit.copdmanager.support.MyApplication;

import java.util.List;


/**
 * A new adapter use BRVAH.
 * Created by wangzheyu on 2018/1/30.
 */

public class NewKnoAdapter extends BaseQuickAdapter<HealthKno,BaseViewHolder>{
    public NewKnoAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HealthKno kno) {
        helper.setText(R.id.kno_name, kno.getKnoName());
        helper.setText(R.id.kno_time, kno.getKnoTime().substring(0,10));
        Glide.with(mContext).load(kno.getKnoLink()).into((ImageView) helper.getView(R.id.kno_iv));
        if (kno.getKnoIfRead()) {
            helper.setText(R.id.kno_read, MyApplication.getContext().getResources().getText(R.string.read));
            helper.setTextColor(R.id.kno_read, ContextCompat.getColor(MyApplication.getContext(),R.color.red));
        } else {
            helper.setText(R.id.kno_read, MyApplication.getContext().getResources().getText(R.string.not_read));
            helper.setTextColor(R.id.kno_read, ContextCompat.getColor(MyApplication.getContext(),R.color.black));
        }
        helper.addOnClickListener(R.id.kno_card);
    }
}
