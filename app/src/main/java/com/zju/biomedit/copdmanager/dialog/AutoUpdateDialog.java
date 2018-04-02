package com.zju.biomedit.copdmanager.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.CustomInterface;


/**
 * Created by ybj on 2015/12/25.
 */
public class AutoUpdateDialog extends DialogFragment {

    private String content;
    private String leftBtnText;
    private CustomInterface.UpdateButtonOnClickListener btnUpdateLaterOnClickListener;
    private CustomInterface.UpdateButtonOnClickListener btnUpdateNowOnClickListener;

    private TextView txvUpdateContent;
    private Button btnUpdateLater;
    private Button btnUpdateNow;

    public AutoUpdateDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_auto_update, container);

        txvUpdateContent = (TextView)view.findViewById(R.id.txv_update_content);
        btnUpdateLater = (Button)view.findViewById(R.id.btn_update_later);
        btnUpdateNow = (Button)view.findViewById(R.id.btn_update_now);
        txvUpdateContent.setText(content);
        btnUpdateLater.setText(leftBtnText);
        setListener();
        //设置点击dialog外部不取消
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false); //返回键无效

        return view;
    }

    private void setListener() {

        btnUpdateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnUpdateLaterOnClickListener != null) {
                    btnUpdateLaterOnClickListener.onClick();
                }
            }
        });

        btnUpdateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnUpdateNowOnClickListener != null) {
                    btnUpdateNowOnClickListener.onClick();
                }
            }
        });
    }

    public void setContentText(String text) {
        this.content = text;
    }

    public void setLeftBtnText(String text) { this.leftBtnText = text; }

    public void setOnClickBtnUpdateLaterListener(final CustomInterface.UpdateButtonOnClickListener l) {
        btnUpdateLaterOnClickListener = l;
    }

    public void setOnClickBtnUpdateNowListener(final CustomInterface.UpdateButtonOnClickListener l) {
        btnUpdateNowOnClickListener = l;
    }
}
