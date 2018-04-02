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
import android.widget.Toast;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.CustomNumericKeyboard;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.TimeManager;
import com.zju.biomedit.copdmanager.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSubStepTwoFragment extends Fragment {
    public static final String ARGUMENT_IDENTITY_CARD_NUMBER = "IDENTITY_CARD_NUMBER";

    @BindView(R.id.edt_input)
    EditText edtInput;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.layout_numeric_keyboard)
    LinearLayout layoutKeyboard;
    private CustomNumericKeyboard customNumericKeyboard;

    public RegisterSubStepTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_two, container, false);
        ButterKnife.bind(this,view);
        initView();
        setListener();
        setButtonStatus();

        return view;
    }

    private void initView() {
        customNumericKeyboard = new CustomNumericKeyboard(layoutKeyboard, edtInput);
        customNumericKeyboard.setPointKeyText("-");
        customNumericKeyboard.setAllNumKeysBackground(R.drawable.selector_num_key_white_to_blue);
        GlobalMethod.hideSoftInputMethod(edtInput, getActivity());
        edtInput.requestFocus();
        Bundle bundle = getArguments();
        if(bundle != null) {
            String birthDate = getBirthDateFromIdentityCardNumber(bundle.getString(ARGUMENT_IDENTITY_CARD_NUMBER));
            edtInput.setText(birthDate);
        }
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

        btnPreviousStep.setOnClickListener((View v) ->{
            if(hostActivity instanceof FragmentRegister){
                ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_BIRTHDAY);
            }
        });

        btnNextStep.setOnClickListener((View v) ->{
            if(hostActivity instanceof FragmentRegister){
                String info = edtInput.getText().toString();
                if(!inputValidation(info)){
                    GlobalMethod.showToast(hostActivity, "出生日期不合理或输入格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                ((FragmentRegister)hostActivity).onNextStepButtonClick(info, Utils.REGISTER_STEP_BIRTHDAY);
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

    /**
     * 输入验证
     */
    private boolean inputValidation(String info){
        if(info.length() != 10)
            return false;

        String firstSpilt = info.substring(4, 5);
        String secondSpilt = info.substring(7, 8);
        if(!firstSpilt.equals("-") || !secondSpilt.equals("-"))
            return false;

        //当前日期
        String nowDate = TimeManager.getStrDate();
        String nowYearStr = nowDate.substring(0, 4);
        String nowMonthStr = nowDate.substring(5, 7);
        String nowDayStr = nowDate.substring(8, 10);

        //输入日期
        String inputYearStr = info.substring(0, 4);
        String inputMonthStr = info.substring(5, 7);
        String inputDayStr = info.substring(8, 10);

        try {
            int nowYear = Integer.parseInt(nowYearStr);
            int nowMonth= Integer.parseInt(nowMonthStr);
            int nowDay = Integer.parseInt(nowDayStr);
            int inputYear = Integer.parseInt(inputYearStr);
            int inputMonth = Integer.parseInt(inputMonthStr);
            int inputDay = Integer.parseInt(inputDayStr);
            if(inputYear < 1920 || inputYear > nowYear)
                return false;
            if(inputMonth < 1 || inputMonth > 12 || (inputYear == nowYear && inputMonth > nowMonth))
                return false;
            if(inputDay < 1 || inputDay > 31 || (inputYear == nowYear && inputMonth == nowMonth && inputDay > nowDay))
                return false;

            // 日期是否存在的判断（如：2000-02-30是不存在）
            if((inputMonth == 4 || inputMonth == 6 || inputMonth == 9 || inputMonth == 11) && inputDay > 30)
                return false;
            if(inputMonth == 2 && inputDay > 29)
                return false;
            if(((inputYear % 100 == 0 && inputYear % 400 != 0) || inputYear % 4 != 0) && inputMonth == 2 && inputDay > 28)
                return false;

            return true;
        }
        catch (Exception e){
            return false;
        }
    }


    /**
     * 从身份证中获取出生日期
     */
    private String getBirthDateFromIdentityCardNumber(String identityCardNumber){
        String birthDate = "";
        if(identityCardNumber.length() == 15){
            birthDate = "19" + identityCardNumber.substring(6, 8) + "-"
                    + identityCardNumber.substring(8, 10) + "-" + identityCardNumber.substring(10, 12);
        }
        else if(identityCardNumber.length() == 18){
            birthDate = identityCardNumber.substring(6, 10) + "-"
                    + identityCardNumber.substring(10, 12) + "-" + identityCardNumber.substring(12, 14);
        }
        return birthDate;
    }

    /**
     * 设置传入Fragment的参数
     */
    public void setCustomArguments(String identityCardNumber){
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_IDENTITY_CARD_NUMBER, identityCardNumber);
        this.setArguments(bundle);
    }
}
