package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.support.Utils;
import com.zju.biomedit.copdmanager.support.WifiDetect;

import androidilearn.com.mobileusability.UserAgent;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zju.biomedit.copdmanager.support.Utils.APP_CACHE_DIRNAME;

public class KnoContentActivity extends AppCompatActivity {
    @BindView(R.id.kno_web_view)
    WebView wvKno;
    private  MaterialDialog dialog;
    private int knoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kno_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onPause() {
        UserAgent.OnPause(getApplicationContext(), Utils.P_CLASS_KNOWLEDGE+ " " + knoId);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserAgent.OnResume(Utils.P_CLASS_KNOWLEDGE+" " + knoId);
        super.onResume();
    }

    private void initView() {
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(KnoContentActivity.this)
                        .content(Utils.MESSAGE_GET_KNO_CONTENT)
                        .progress(true, 0);
                dialog = builder.build();
                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        };

        Intent intent = getIntent();
        knoId = intent.getIntExtra("knoId",1);
        final String contentString = "http://172.16.119.155:8080/copd/message/show?knoId=" + knoId;

        wvKno.setWebViewClient(webViewClient);
        WebSettings settings = wvKno.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setDisplayZoomControls(false);
        if(WifiDetect.getCurrentNetType(this)==1){
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            Log.i("wifi","true");
        }else{
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Log.i("wifi","false");
        }
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//因为网页基本不会变，直接使用本地缓存可以提升速度（目前缓存占用空间不大，以后考虑清除缓存功能？）
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启database storage API功能
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        wvKno.loadUrl(contentString);
    }
}
