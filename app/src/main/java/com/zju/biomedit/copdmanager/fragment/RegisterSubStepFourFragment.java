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
import com.zju.biomedit.copdmanager.support.CustomNumericKeyboard;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSubStepFourFragment extends Fragment {
    @BindView(R.id.edt_input)
    EditText edtInput;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.layout_numeric_keyboard)
    LinearLayout layoutKeyboard;
    private CustomNumericKeyboard customNumericKeyboard;

    public RegisterSubStepFourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_four, container, false);
        ButterKnife.bind(this,view);
        initView();
        setListener();
        //清空文本
        edtInput.setText("");
        setButtonStatus();

        return view;
    }


    private void initView() {
        customNumericKeyboard = new CustomNumericKeyboard(layoutKeyboard, edtInput);
        customNumericKeyboard.hidePoint();
        customNumericKeyboard.setAllNumKeysBackground(R.drawable.selector_num_key_white_to_blue);
        GlobalMethod.hideSoftInputMethod(edtInput, getActivity());
        edtInput.requestFocus();
    }

    private void setListener() {
        final Activity hostActivity = getActivity();

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

        btnPreviousStep.setOnClickListener((View v) -> {
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_HEIGHT);
            }
        });

        btnNextStep.setOnClickListener((View v) -> {
            if(hostActivity instanceof FragmentRegister){
                int height = Integer.parseInt(edtInput.getText().toString());
                String info = String.valueOf(height);
                ((FragmentRegister)hostActivity).onNextStepButtonClick(info, Utils.REGISTER_STEP_HEIGHT);
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
}
