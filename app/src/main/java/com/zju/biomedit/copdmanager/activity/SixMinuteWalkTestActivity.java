package com.zju.biomedit.copdmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.sixMWD.SixMWDActivity;
import com.zju.biomedit.copdmanager.activity.sixMWD.SixMWDActivity1;
import com.zju.biomedit.copdmanager.activity.sixMWD.SixMWDActivity2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//测试说明和开始测试
public class SixMinuteWalkTestActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
    private static final String LICENSE_FILE_NAME = "temp_license_2016-04-05";

    private String mSampleDirPath="..";

    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    private Button start_test;
    private Button history;
    private EditText editInterval;

    //语音播报sd配置
    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        File file = new File(mSampleDirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
    }

    /**
     * 将工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    public void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6mwd1
        );
        initialEnv();
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SixMinuteWalkTestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permissionList.isEmpty()){
            String[] permission=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SixMinuteWalkTestActivity.this, permission, 1);
        }

        start_test=findViewById(R.id.six_mwd_start_test);
        editInterval=findViewById(R.id.edit_interval);
        history=findViewById(R.id.map);
        history.setOnClickListener(this);
        start_test.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SixMinuteWalkTestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.six_mwd_start_test:
                String input=editInterval.getText().toString();
                int interval=Integer.parseInt(input);
                Intent intent_test=new Intent(this, SixMWDActivity1.class);
                intent_test.putExtra("interval",interval);
                startActivity(intent_test);
				break;
            case R.id.map:
                Intent intent_history=new Intent(this, SixMWDActivity2.class);
                startActivity(intent_history);
                break;
            default:
                break;
        }
    }
/**
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result: grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(SixMinuteWalkTestActivity.this, "必须同意所有权限方可使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }else {
                    Toast.makeText(SixMinuteWalkTestActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    */
}
