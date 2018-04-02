package com.zju.biomedit.copdmanager.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.FragmentRegister;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFirstStepFragment extends Fragment {
    @BindView(R.id.edt_patient_id)
    EditText edtPatientId;
    @BindView(R.id.edt_patient_name)
    EditText edtPatientName;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;


    public RegisterFirstStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_first_step, container, false);
        ButterKnife.bind(this,view);
        setListener();
        // 清空文本
        edtPatientId.setText("");
        edtPatientName.setText("");
        setBtnNextStepStatus();
        return view;
    }

    private void setListener(){
        btnNextStep.setOnClickListener((View v) -> {
            Activity hostActivity = getActivity();
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister) hostActivity).onValidateStepButtonClick(edtPatientId.getText().toString(), edtPatientName.getText().toString());
            }
        });

        edtPatientId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnNextStepStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnNextStepStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setBtnNextStepStatus(){
        if(edtPatientId.getText().toString().equals("") || edtPatientName.getText().toString().equals("")){
            btnNextStep.setEnabled(false);
        }
        else {
            btnNextStep.setEnabled(true);
        }
    }
}