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
public class RegisterSubStepTenFragment extends Fragment {
    @BindView(R.id.cb_smoke)
    CheckBox cbSmoke;
    @BindView(R.id.cb_not_smoke)
    CheckBox cbNoSmoke;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;

    private String selectSmoke;

    public RegisterSubStepTenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_ten, container, false);
        ButterKnife.bind(this,view);
        initView();
        setListener();

        setButtonStatus();
        return view;
    }

    private void initView() {
        cbSmoke.setChecked(true);
        cbNoSmoke.setChecked(false);
        selectSmoke = "吸烟";
    }

    private void setListener() {
        final Activity hostActivity = getActivity();

        btnPreviousStep.setOnClickListener((View v)->{
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_SMOKE);
            }
        });

        btnNextStep.setOnClickListener((View v) -> {
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onNextStepButtonClick(selectSmoke, Utils.REGISTER_STEP_SMOKE);
            }
        });

        cbSmoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbNoSmoke.setChecked(false);
                    selectSmoke = "吸烟";
                }
                setButtonStatus();
            }
        });

        cbNoSmoke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbSmoke.setChecked(false);
                    selectSmoke = "不吸烟";
                }
                setButtonStatus();
            }
        });
    }

    private void setButtonStatus() {
        if(!cbSmoke.isChecked() && !cbNoSmoke.isChecked()){
            btnNextStep.setEnabled(false);
        }
        else {
            btnNextStep.setEnabled(true);
        }
    }
}
