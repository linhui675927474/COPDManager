package com.zju.biomedit.copdmanager.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSecondStepFragment extends Fragment {

    public static final String ARGUMENT_PATIENT_ID = "PATIENT_ID";
    public static final String ARGUMENT_PATIENT_NAME = "PATIENT_NAME";
    public static final String ARGUMENT_PASSWORD = "PATIENT_PASSWORD";

    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_finish_regist)
    Button btnFinishRegist;
    @BindView(R.id.btn_show_password)
    ImageView btnShowPassword;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_password_repeat)
    EditText edtPasswordRepeat;

    private boolean isShowPassword;

    public RegisterSecondStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_second_step, container, false);
        ButterKnife.bind(this,view);
        isShowPassword = false;
        setListener();
        edtPassword.setText(""); //清空之前设置的密码
        edtPasswordRepeat.setText(""); //清空之前设置的密码
        //setBtnFinishRegistStatus();
        return view;
    }

    private void setListener(){
        final Activity hostActivity = getActivity();

        btnPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hostActivity instanceof FragmentRegister){
                    ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_FINISH);
                }
            }
        });

        btnFinishRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString();
                String passwordRepeat = edtPasswordRepeat.getText().toString();
                if(password.equals(passwordRepeat)){
                    if(hostActivity instanceof FragmentRegister){
                        Bundle bundle = new Bundle();
                        bundle.putString(ARGUMENT_PASSWORD, edtPassword.getText().toString());
                        ((FragmentRegister)hostActivity).onFinishButtonClick(bundle);
                    }
                }else {
                    GlobalMethod.showToast(getActivity(),"两次密码输入不一致", Toast.LENGTH_LONG);
                }

            }
        });

        btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cursorPosition = edtPassword.getSelectionStart(); //获取光标位置
                isShowPassword = !isShowPassword;
                if(isShowPassword){
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnShowPassword.setImageResource(R.mipmap.ic_eye_pressed);
                }
                else{
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnShowPassword.setImageResource(R.mipmap.ic_eye);
                }
                edtPassword.setSelection(cursorPosition); //设置光标回原来的位置
            }
        });

        /*edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnFinishRegistStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void setBtnFinishRegistStatus(){//密码允许为空
        String password = edtPassword.getText().toString();
        String passwordRepeat = edtPasswordRepeat.getText().toString();
        if(password.equals("") || passwordRepeat.equals("")){
            btnFinishRegist.setEnabled(true);
        }else {
            btnFinishRegist.setEnabled(false);
        }
    }


    /**
     * 设置传入Fragment的参数
     */
    public void setCustomArguments(String patientId, String patientName){
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_PATIENT_ID, patientId);
        bundle.putString(ARGUMENT_PATIENT_NAME, patientName);
        this.setArguments(bundle);
    }
}