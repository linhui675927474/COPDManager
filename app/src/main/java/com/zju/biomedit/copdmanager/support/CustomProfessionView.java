package com.zju.biomedit.copdmanager.support;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ybj on 2016/5/20.
 */
public class CustomProfessionView {

    private TextView txvWorker;
    private TextView txvFarmer;
    private TextView txvScience;
    private TextView txvAdministrative;
    private TextView txvTeacher;
    private TextView txvFinance;
    private TextView txvBusiness;
    private TextView txvMedical;
    private TextView txvStudent;
    private TextView txvSoldier;
    private TextView txvHousework;
    private TextView txvSelf;
    private TextView txvOther;
    private List<TextView> allProfession = new ArrayList<TextView>();

    private ProfessionUpdater professionUpdater;

    public CustomProfessionView(View view, ProfessionUpdater updater){
        txvWorker = (TextView)view.findViewById(R.id.txv_worker);
        txvFarmer = (TextView)view.findViewById(R.id.txv_farmer);
        txvScience = (TextView)view.findViewById(R.id.txv_science);
        txvAdministrative = (TextView)view.findViewById(R.id.txv_administrative);
        txvTeacher = (TextView)view.findViewById(R.id.txv_teacher);
        txvFinance = (TextView)view.findViewById(R.id.txv_finance);
        txvBusiness = (TextView)view.findViewById(R.id.txv_business);
        txvMedical = (TextView)view.findViewById(R.id.txv_medical);
        txvStudent = (TextView)view.findViewById(R.id.txv_student);
        txvSoldier = (TextView)view.findViewById(R.id.txv_soldier);
        txvHousework = (TextView)view.findViewById(R.id.txv_housework);
        txvSelf = (TextView)view.findViewById(R.id.txv_self);
        txvOther = (TextView)view.findViewById(R.id.txv_other);
        allProfession.add(txvWorker);
        allProfession.add(txvFarmer);
        allProfession.add(txvScience);
        allProfession.add(txvAdministrative);
        allProfession.add(txvTeacher);
        allProfession.add(txvFinance);
        allProfession.add(txvBusiness);
        allProfession.add(txvMedical);
        allProfession.add(txvStudent);
        allProfession.add(txvSoldier);
        allProfession.add(txvHousework);
        allProfession.add(txvSelf);
        allProfession.add(txvOther);
        professionUpdater = updater;
        setListener();
    }

    private void setListener() {
        for(int i = 0; i < allProfession.size(); i++) {
            final TextView txv = allProfession.get(i);
            txv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    professionUpdater.professionChanged(txv.getText().toString());
                }
            });

            final int normalColor = Color.argb(255, 3, 169, 244);
            txv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            txv.setTextColor(Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            txv.setTextColor(normalColor);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            txv.setTextColor(normalColor);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(!txv.isPressed()){
                                txv.setTextColor(normalColor);
                            }
                        default:
                            break;
                    }

                    return false;
                }
            });
        }
    }

    /**
     * 用于回调的接口
     */
    public interface ProfessionUpdater{
        void professionChanged(String profession);
    }
}
