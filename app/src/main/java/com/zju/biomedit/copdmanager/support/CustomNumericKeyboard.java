package com.zju.biomedit.copdmanager.support;

import android.graphics.Color;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zju.biomedit.copdmanager.R;

import java.util.ArrayList;



/**
 * Created by ybj on 2016/3/1.
 */
public class CustomNumericKeyboard {

    private LinearLayout layoutPoint;
    private Button btnNumPoint;

    private Button btnNumOne;
    private Button btnNumTwo;
    private Button btnNumThree;
    private Button btnNumFour;
    private Button btnNumFive;
    private Button btnNumSix;
    private Button btnNumSeven;
    private Button btnNumEight;
    private Button btnNumNine;
    private Button btnNumZero;
    private Button btnNumDelete;
    private Button btnNumClear;
    private ArrayList<Button> numButtonList;

    private EditText curEditText;

    private int keyTextColorNormal;
    private int keyTextColorPress;

    public CustomNumericKeyboard(View v, EditText editText){


        this.layoutPoint = (LinearLayout)v.findViewById(R.id.lyot_point);
        this.btnNumPoint = (Button)v.findViewById(R.id.btn_num_point);

        this.btnNumOne = (Button)v.findViewById(R.id.btn_num_one);
        this.btnNumTwo = (Button)v.findViewById(R.id.btn_num_two);
        this.btnNumThree = (Button)v.findViewById(R.id.btn_num_three);
        this.btnNumFour = (Button)v.findViewById(R.id.btn_num_four);
        this.btnNumFive = (Button)v.findViewById(R.id.btn_num_five);
        this.btnNumSix = (Button)v.findViewById(R.id.btn_num_six);
        this.btnNumSeven = (Button)v.findViewById(R.id.btn_num_seven);
        this.btnNumEight = (Button)v.findViewById(R.id.btn_num_eight);
        this.btnNumNine = (Button)v.findViewById(R.id.btn_num_nine);
        this.btnNumZero = (Button)v.findViewById(R.id.btn_num_zero);
        this.btnNumDelete = (Button)v.findViewById(R.id.btn_num_del);
        this.btnNumClear = (Button)v.findViewById(R.id.btn_num_clear);
        this.numButtonList = new ArrayList<>();
        numButtonList.add(btnNumZero);
        numButtonList.add(btnNumOne);
        numButtonList.add(btnNumTwo);
        numButtonList.add(btnNumThree);
        numButtonList.add(btnNumFour);
        numButtonList.add(btnNumFive);
        numButtonList.add(btnNumSix);
        numButtonList.add(btnNumSeven);
        numButtonList.add(btnNumEight);
        numButtonList.add(btnNumNine);
        numButtonList.add(btnNumDelete);
        numButtonList.add(btnNumClear);
        numButtonList.add(btnNumPoint);

        this.curEditText = editText;
        this.keyTextColorNormal = Color.BLACK;
        this.keyTextColorPress = Color.WHITE;
        setListener();
    }


    private void setListener() {
        for(int i = 0; i <= 9; i++){
            final Button tempBtn = numButtonList.get(i);
            tempBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertNum(tempBtn.getText().toString());
                }
            });
        }

        btnNumDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = curEditText.getText();
                int nStartIndex = curEditText.getSelectionStart();
                int nEndIndex = curEditText.getSelectionEnd();
                if(nEndIndex > nStartIndex) {
                    editable.delete(nStartIndex, nEndIndex);
                }
                else if(nStartIndex > 0) {
                    editable.delete(nStartIndex - 1, nStartIndex);
                }
            }
        });

        btnNumClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = curEditText.getText();
                editable.clear();
            }
        });

        btnNumPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNum(btnNumPoint.getText().toString());
            }
        });

        for(int i = 0; i < numButtonList.size(); i++){
            final Button tempBtn = numButtonList.get(i);
            tempBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            tempBtn.setTextColor(keyTextColorPress);
                            break;
                        case MotionEvent.ACTION_UP:
                            tempBtn.setTextColor(keyTextColorNormal);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            tempBtn.setTextColor(keyTextColorNormal);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(!tempBtn.isPressed()){
                                tempBtn.setTextColor(keyTextColorNormal);
                            }
                        default:
                            break;
                    }

                    return false;
                }
            });
        }
    }

    private void insertNum(String num){
        Editable editable = curEditText.getText();
        int nStartIndex = curEditText.getSelectionStart();
        int nEndIndex = curEditText.getSelectionEnd();
        if(nEndIndex > nStartIndex) {
            editable.delete(nStartIndex, nEndIndex);
        }

        editable.insert(nStartIndex, num);
    }


    public void hidePoint(){
        layoutPoint.setVisibility(View.GONE);
    }

    public void setPointKeyText(String text){
        btnNumPoint.setText(text);
    }

    public void resetEditText(EditText editText){
        this.curEditText = editText;
    }

    public void setAllNumKeysTextColor(int normalColor, int pressColor){
        this.keyTextColorNormal = normalColor;
        this.keyTextColorPress = pressColor;
    }

    public void setAllNumKeysBackground(int resource){
        for(int i = 0; i < numButtonList.size(); i++){
            Button tempBtn = numButtonList.get(i);
            tempBtn.setBackgroundResource(resource);
        }
    }
}
