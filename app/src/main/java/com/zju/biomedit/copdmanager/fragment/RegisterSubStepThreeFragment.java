package com.zju.biomedit.copdmanager.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSubStepThreeFragment extends Fragment {
    @BindView(R.id.cb_male)
    CheckBox cbMale;
    @BindView(R.id.cb_female)
    CheckBox cbFemale;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;

    private String selectSex;

    public RegisterSubStepThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_three, container, false);
        ButterKnife.bind(this,view);
        initView();
        setListener();

        setButtonStatus();
        return view;
    }

    private void initView() {
        cbMale.setChecked(true);
        cbFemale.setChecked(false);
        selectSex = "男";
    }

    private void setListener() {
        final Activity hostActivity = getActivity();

        btnPreviousStep.setOnClickListener((View v)->{
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_SEX);
            }
        });

        btnNextStep.setOnClickListener((View v) -> {
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onNextStepButtonClick(selectSex, Utils.REGISTER_STEP_SEX);
            }
        });

        cbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbFemale.setChecked(false);
                    selectSex = "男";
                }
                setButtonStatus();
            }
        });

        cbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbMale.setChecked(false);
                    selectSex = "女";
                }
                setButtonStatus();
            }
        });
    }

    private void setButtonStatus() {
        if(!cbMale.isChecked() && !cbFemale.isChecked()){
            btnNextStep.setEnabled(false);
        }
        else {
            btnNextStep.setEnabled(true);
        }
    }
}
