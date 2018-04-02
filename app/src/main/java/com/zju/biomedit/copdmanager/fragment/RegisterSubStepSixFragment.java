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
import android.widget.LinearLayout;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.CustomEducationView;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSubStepSixFragment extends Fragment implements CustomEducationView.EducationUpdater {

    @BindView(R.id.edt_input)
    EditText edtInput;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.layout_education)
    LinearLayout layoutEducation;
    private CustomEducationView customEducationView;

    public RegisterSubStepSixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_six, container, false);
        ButterKnife.bind(this,view);

        initView();
        setListener();
        //清空文本
        edtInput.setText("");
        setButtonStatus();

        return view;
    }

    private void initView() {
        customEducationView = new CustomEducationView(layoutEducation, this);
    }

    private void setListener() {
        final Activity hostActivity = getActivity();

        btnPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hostActivity instanceof FragmentRegister){
                    ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_EDUCATION);
                }
            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hostActivity instanceof FragmentRegister){
                    String info = edtInput.getText().toString();
                    ((FragmentRegister)hostActivity).onNextStepButtonClick(info, Utils.REGISTER_STEP_EDUCATION);
                }
            }
        });

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setButtonStatus() {
        if(edtInput.getText().toString().trim().equals("")){
            btnNextStep.setEnabled(false);
        }
        else {
            btnNextStep.setEnabled(true);
        }
    }


    @Override
    public void educationChanged(String education) {
        edtInput.setText(education);
    }
}
