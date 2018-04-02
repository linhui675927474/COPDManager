package com.zju.biomedit.copdmanager.support;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ybj on 2016/5/19.
 */
public class CustomEducationView {

    private TextView txvGradeSchool;
    private TextView txvJuniorMiddleSchool;
    private TextView txvTechnicalSecondarySchool;
    private TextView txvHighMiddleSchool;
    private TextView txvJuniorCollege;
    private TextView txvUndergraduate;
    private TextView txvMaster;
    private TextView txvDoctor;
    private List<TextView> allEducation = new ArrayList<TextView>();

    private  EducationUpdater educationUpdater;

    public CustomEducationView(View view,  EducationUpdater updater){
        txvGradeSchool = (TextView)view.findViewById(R.id.txv_grade_school);
        txvJuniorMiddleSchool = (TextView)view.findViewById(R.id.txv_junior_middle_school);
        txvTechnicalSecondarySchool = (TextView)view.findViewById(R.id.txv_technical_secondary_school);
        txvHighMiddleSchool = (TextView)view.findViewById(R.id.txv_high_middle_school);
        txvJuniorCollege = (TextView)view.findViewById(R.id.txv_junior_college);
        txvUndergraduate = (TextView)view.findViewById(R.id.txv_undergraduate);
        txvMaster = (TextView)view.findViewById(R.id.txv_master);
        txvDoctor = (TextView)view.findViewById(R.id.txv_doctor);
        allEducation.add(txvGradeSchool);
        allEducation.add(txvJuniorMiddleSchool);
        allEducation.add(txvTechnicalSecondarySchool);
        allEducation.add(txvHighMiddleSchool);
        allEducation.add(txvJuniorCollege);
        allEducation.add(txvUndergraduate);
        allEducation.add(txvMaster);
        allEducation.add(txvDoctor);
        educationUpdater = updater;
        setListener();
    }

    private void setListener() {
        for(int i = 0; i < allEducation.size(); i++) {
            final TextView txv = allEducation.get(i);
            txv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    educationUpdater.educationChanged(txv.getText().toString());
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
    public interface EducationUpdater{
        void educationChanged(String education);
    }
}
