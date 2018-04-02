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
import android.widget.TextView;
import android.widget.Toast;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.PefHistoryActivity;
import com.zju.biomedit.copdmanager.support.AddressPickTask;
import com.zju.biomedit.copdmanager.support.FragmentRegister;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.Utils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterSubStepNineFragment extends Fragment {
    @BindView(R.id.edt_input)
    EditText edtInput;
    @BindView(R.id.btn_previous_step)
    Button btnPreviousStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    public RegisterSubStepNineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regist_sub_step_nine, container, false);
        ButterKnife.bind(this,view);
        initView();
        setListener();
        //清空文本
        //edtInput.setText("");
        setButtonStatus();

        return view;
    }


    private void initView() {
        edtInput.setFocusable(false);
        edtInput.setFocusableInTouchMode(false);
        edtInput.setText("");///不写也默认为空
        tvAddress.setOnClickListener((View v) -> {
            AddressPickTask task = new AddressPickTask(RegisterSubStepNineFragment.this.getActivity());
            task.setHideProvince(false);
            task.setHideCounty(false);
            task.setCallback(new AddressPickTask.Callback() {
                @Override
                public void onAddressInitFailed() {
                    GlobalMethod.showToast(RegisterSubStepNineFragment.this.getActivity(),"数据初始化失败", Toast.LENGTH_LONG);
                }

                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    edtInput.setFocusable(true);
                    edtInput.setFocusableInTouchMode(true);
                    edtInput.requestFocus();
                    if (county == null) {
                        String address = province.getAreaName() + "，"+ city.getAreaName()+"，";
                        edtInput.setText(address);
                        edtInput.setSelection(address.length());
                    } else {
                        String address = province.getAreaName() + "，"+city.getAreaName() +"，"+ county.getAreaName();
                        edtInput.setText(address);
                        edtInput.setSelection(address.length());
                    }
                }
            });
            task.execute("宁夏", "银川", "兴庆");
        });
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
                ((FragmentRegister)hostActivity).onPreviousStepButtonClick(Utils.REGISTER_STEP_ADDRESS);
            }
        });

        btnNextStep.setOnClickListener((View v) -> {
            if(hostActivity instanceof FragmentRegister){
                String info = edtInput.getText().toString();
                ((FragmentRegister)hostActivity).onNextStepButtonClick(info, Utils.REGISTER_STEP_ADDRESS);
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
