package com.zju.biomedit.copdmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.Utils;


/**
 * Created by wangzheyu on 2017/7/12.
 */

public class DrugSelectDialog extends Dialog {
    private ImageView ivDrugPic;
    private TextView tvDrugName;
    private TextView tvDrugTime;
    private TextView tvSure;
    private TextView tvClose;
    private EditText etCustomName;
    private RelativeLayout rlCustomName;
    private RelativeLayout rlDrug;
    private String drugName;
    private String drugTime;
    private int imageResId = -1;

    public DrugSelectDialog(Context context){
        super(context, R.style.CustomDialog);
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_drug_add);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        tvSure.setOnClickListener((View v) ->{
            if ( onCLickDrugListener!= null) {
                onCLickDrugListener.onPositiveClick();
            }
        });
        //设置关闭按钮被点击后，向外界提供监听
        tvClose.setOnClickListener((View v) ->{
            if ( onCLickDrugListener!= null) {
                onCLickDrugListener.onNegtiveClick();
            }
        });
        //设置时间设置按钮被点击后，向外界提供监听
        rlDrug.setOnClickListener((View v) -> {
            if ( onCLickDrugListener!= null) {
                onCLickDrugListener.onTimeClick();
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(drugName)) {
            tvDrugName.setText(drugName);
        }
        if (!TextUtils.isEmpty(drugTime)) {
            tvDrugTime.setText(drugTime);
        }
        if(drugName.equals(Utils.CUSTOM_DRUG)){
            rlCustomName.setVisibility(View.VISIBLE);
        }
        if (imageResId!=-1) {
            ivDrugPic.setImageResource(imageResId);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        tvDrugName = (TextView) findViewById(R.id.tv_select_drug_name);
        tvDrugTime = (TextView) findViewById(R.id.tv_time);
        etCustomName = (EditText) findViewById(R.id.et_custom_name);
        rlCustomName = (RelativeLayout) findViewById(R.id.rl_drug_custom);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvClose = (TextView) findViewById(R.id.tv_close);
        ivDrugPic = (ImageView) findViewById(R.id.iv_select_drug_pic);
        rlDrug = (RelativeLayout) findViewById(R.id.rl_drug);
    }

    /**
     * 设置按钮的回调
     */
    private OnCLickDrugListener onCLickDrugListener;
    public DrugSelectDialog setonCLickDrugListener(OnCLickDrugListener onCLickDrugListener) {
        this.onCLickDrugListener = onCLickDrugListener;
        return this;
    }
    public interface OnCLickDrugListener{
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();
        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
        /**
         * 点击时间选择事件
         */
        public void onTimeClick();
    }

    public String getDrugName() {
        return drugName;
    }

    public DrugSelectDialog setDrugName(String name) {
        this.drugName = name;
        return this ;
    }

    public String getDrugTime() {
        return drugTime;
    }

    public DrugSelectDialog setDrugTime(String time) {
        this.drugTime = time;
        return this ;
    }


    public DrugSelectDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this ;
    }

    public String getDrugCustomName() {
        String name = etCustomName.getText().toString();
        if(!TextUtils.isEmpty(name)){
            return name;
        }else{
            return Utils.CUSTOM_DRUG;
        }
    }



}
