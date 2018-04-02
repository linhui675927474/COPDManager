package com.zju.biomedit.copdmanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.presenter.TimePresenter;
import com.zju.biomedit.copdmanager.support.GlobalMethod;
import com.zju.biomedit.copdmanager.support.MyApplication;
import com.zju.biomedit.copdmanager.support.Utils;
import com.zju.biomedit.copdmanager.support.WifiDetect;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zju.biomedit.copdmanager.support.Utils.APP_CACHE_DIRNAME;

public class MsgContentActivity extends AppCompatActivity {

    @BindView(R.id.noti_con_web)
    WebView wvNoti;
    @BindView(R.id.read_count)
    TextView tvRead;
    @BindView(R.id.collect_count)
    TextView tvCollect;
    @BindView(R.id.tv_collect)
    TextView ifCollect;
    @BindView(R.id.btn_collect)
    ImageView ivCollect;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.btn_share)
    ImageView ivShare;
    private MaterialDialog dialog;
    private String url;
    private int knoId;
    private int value;
    private boolean knoIfFavorite;
    private TimePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            finish();
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        initView();
        getKnoInfo();
    }

    @Override
    protected void onPause() {
        //记录时间 上传
        presenter.stopTimer();
        if (value > 2000) {
            recordIfKnoRead();
            uploadKnoReadTime();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        //开始计时
        presenter = new TimePresenter(this);
        presenter.startTimerFrom0();
        super.onResume();
    }

    private void initView() {
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(MsgContentActivity.this)
                        .content(Utils.MESSAGE_GET_CONTENT)
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
        knoId = intent.getIntExtra("knoId", 0);
        if (knoId != 0) {
            url = "http://120.27.141.50:8080/copd/message/show?knoId=" + knoId;
            Log.i("url", url.split("knoId=")[1]);
        } else {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                url = bundle.getString("url");
                if (url != null) {
                    String id = url.split("knoId=")[1];
                    knoId = Integer.parseInt(id);
                }
                if (TextUtils.isEmpty(url)) {
                    url = "www.baidu.com";
                }
                Log.i("url", url);
            } else {
                Log.i("url", url);
            }
        }

        getIfKnoCollected();

        ifCollect.setOnClickListener((View v) -> {
            if (knoIfFavorite) {
                ifCollect.setText(this.getResources().getText(R.string.not_collect));
                ivCollect.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_collect));
                recordIfKnoCollect(0);
            } else {
                ifCollect.setText(this.getResources().getText(R.string.collect));
                ivCollect.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_collected));
                recordIfKnoCollect(1);
            }
        });

        ivCollect.setOnClickListener((View v) -> {
            if (knoIfFavorite) {
                ifCollect.setText(this.getResources().getText(R.string.not_collect));
                ivCollect.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_collect));
                recordIfKnoCollect(0);
            } else {
                ifCollect.setText(this.getResources().getText(R.string.collect));
                ivCollect.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_collected));
                recordIfKnoCollect(1);
            }
        });

        tvShare.setOnClickListener((View v) -> {
            GlobalMethod.showToast(this,"分享功能暂未开放", Toast.LENGTH_SHORT);
        });

        ivShare.setOnClickListener((View v) -> {
            GlobalMethod.showToast(this,"分享功能暂未开放", Toast.LENGTH_SHORT);
        });



        wvNoti.setWebViewClient(webViewClient);
        WebSettings settings = wvNoti.getSettings();
        if (WifiDetect.getCurrentNetType(this) == 1) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            Log.i("wifi", "true");
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Log.i("wifi", "false");
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
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
        wvNoti.loadUrl(url);
    }

    private void recordIfKnoRead() {
        String knoUrl = Utils.IF_KNO_READ_URL + "&patientId=" + PatientUserManager.getInstance().getPatientId() + "&knoId=" + knoId;
        Ion.with(this).load(knoUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("knoRead", result + "");
                if (e != null) {
                    Log.d("knoRead", "kno read error");
                }
            }
        });
    }

    private void recordIfKnoCollect(int collect) {
        String collectUrl = Utils.KNO_COLLECT_URL + PatientUserManager.getInstance().getPatientId() + "&knoId=" + knoId + "&value=" + collect;
        Ion.with(this).load(collectUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("knoCollect", result + "");
                if (e != null) {
                    Log.d("knoCollect", "kno collect error");
                }
            }
        });
    }

    private void getKnoInfo() {
        String knoUrl = Utils.GET_KNO_INFO_URL + knoId;
        Ion.with(this).load(knoUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("knoInfo", result + "");
                if (e != null) {
                    Log.d("knoInfo", "get kno info error");
                } else {
                    try {
                        String strRead = result.getAsJsonObject("result").get("readTimesNumber").getAsString();
                        String strCollect = result.getAsJsonObject("result").get("favoriteTimeNumber").getAsString();
                        tvRead.setText(strRead);
                        tvCollect.setText(strCollect);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void getIfKnoCollected() {
        String knoUrl = Utils.IF_KNO_COLLECT_URL + PatientUserManager.getInstance().getPatientId() + "&knoId=" + knoId;
        Ion.with(this).load(knoUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("ifKnoCollect", result + "");
                if (e != null) {
                    Log.d("ifKnoCollect", "get kno collect error");
                } else {
                    try {
                        knoIfFavorite = result.getAsJsonObject("result").get("knoIfFavorite").getAsBoolean();
                        if (knoIfFavorite) {
                            ifCollect.setText(MsgContentActivity.this.getResources().getText(R.string.collect));
                            ivCollect.setImageDrawable(ContextCompat.getDrawable(MsgContentActivity.this, R.mipmap.ic_collected));
                        } else {
                            ifCollect.setText(MsgContentActivity.this.getResources().getText(R.string.not_collect));
                            ivCollect.setImageDrawable(ContextCompat.getDrawable(MsgContentActivity.this, R.mipmap.ic_collect));
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void uploadKnoReadTime() {
        String timeUrl = Utils.KNO_TIME_URL + PatientUserManager.getInstance().getPatientId() + "&knoId=" + knoId + "&value=" + value;
        //Log.i("readtime",value+"");
        Ion.with(MyApplication.getContext()).load(timeUrl).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("knoTime", result + "");//用当前Activity的context会无法显示log
                if (e != null) {
                    Log.d("knoTime", "kno read error");
                }
            }
        });
    }

    public void getTime(int time) {
        value = time;
    }

}
