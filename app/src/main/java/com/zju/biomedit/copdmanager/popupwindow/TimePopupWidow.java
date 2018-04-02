package com.zju.biomedit.copdmanager.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.view.WheelView;

import java.util.ArrayList;

/**
 * Created by ybj on 2016/3/2.
 */
public class TimePopupWidow extends PopupWindow {

    private View contentView;
    private Activity context;
    private RelativeLayout btn_delete, btn_correct;
    private WheelView mWheelView1, mWheelView2;
    private ArrayList<String> dataHour = new ArrayList<>();
    private ArrayList<String> dataMinute = new ArrayList<>();

    public TimePopupWidow(final Activity context){
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_time_wheelview, null);
        btn_delete = (RelativeLayout) contentView.findViewById(R.id.btn_delete);
        btn_correct = (RelativeLayout) contentView.findViewById(R.id.btn_correct);
        mWheelView1 = (WheelView)contentView.findViewById(R.id.wheelview1);
        mWheelView2 = (WheelView)contentView.findViewById(R.id.wheelview2);

        this.initWheelAndInput();
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void onClickDeleteListener(View.OnClickListener onClickListener){
        btn_delete.setOnClickListener(onClickListener);
    }

    public void onClickCorrectListener(View.OnClickListener onClickListener){
        btn_correct.setOnClickListener(onClickListener);
    }

    public void setTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        mWheelView1.setDefault(hour);
        mWheelView2.setDefault(minute);
    }

    public String getTime(){
        String time = mWheelView1.getSelectedText() + ":" + mWheelView2.getSelectedText();
        return time;
    }

    private void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    private void initWheelAndInput(){
        for(int i=0; i<24; i++){
            if(i < 10)
                dataHour.add("0" + i);
            else
                dataHour.add(i + "");
        }
        mWheelView1.setData(dataHour);
        mWheelView1.setDefault(12);

        for(int i=0; i<60; i++){
            if(i < 10)
                dataMinute.add("0" + i);
            else
                dataMinute.add(i + "");
        }
        mWheelView2.setData(dataMinute);
        mWheelView2.setDefault(30);
    }
}
