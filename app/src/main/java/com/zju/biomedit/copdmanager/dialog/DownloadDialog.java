package com.zju.biomedit.copdmanager.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zju.biomedit.copdmanager.R;


/**
 * Created by ybj on 2015/12/29.
 */
public class DownloadDialog extends DialogFragment {

    public DownloadDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_download, container);

        Button btnHandleBackground = (Button)view.findViewById(R.id.btn_handle_background);
        btnHandleBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //设置返回键不取消，点击dialog外部不取消
        setCancelable(false);
        //getDialog().setCanceledOnTouchOutside(false);

        return view;
    }

    public void updateProgress(int progressValue){
        View view = getView();
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressbar_download);
        TextView txvProgress = (TextView)view.findViewById(R.id.txv_progress_text);
        progressBar.setProgress(progressValue);
        txvProgress.setText("下载进度：" + progressValue + "%");
    }
}
