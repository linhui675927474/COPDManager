package com.zju.biomedit.copdmanager.activity;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.igexin.sdk.PushManager;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.dialog.AutoUpdateDialog;
import com.zju.biomedit.copdmanager.dialog.DownloadDialog;
import com.zju.biomedit.copdmanager.fragment.ClassFragment;
import com.zju.biomedit.copdmanager.fragment.HomepageFragment;
import com.zju.biomedit.copdmanager.fragment.MineFragment;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.step.LocalStepHelper;
import com.zju.biomedit.copdmanager.step.StepCounterService;
import com.zju.biomedit.copdmanager.support.AppUpdateService;
import com.zju.biomedit.copdmanager.support.CustomInterface;
import com.zju.biomedit.copdmanager.support.DemoIntentService;
import com.zju.biomedit.copdmanager.support.DemoPushService;
import com.zju.biomedit.copdmanager.support.TimeService;
import com.zju.biomedit.copdmanager.support.Utils;

import androidilearn.com.mobileusability.UserAgent;

public class MainActivity extends AppCompatActivity {

    private HomepageFragment homepageFragment;
    private ClassFragment classFragment;
    private MineFragment mineFragment;
    //自动更新对话框
    private AutoUpdateDialog autoUpdateDialog;
    //下载进度对话框
    private DownloadDialog downloadDialog;
    //自动更新服务消息的广播接收者
    private AppUpdateServiceReceiver appUpdateServiceReceiver;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PHONE_STATE = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_PHONE = {Manifest.permission.READ_PHONE_STATE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyPhoneStatePermissions(this);
        }else {
            UserAgent.Init(this, "COPD", PatientUserManager.getInstance().getPatientId());
            UserAgent.UserDevice(this);
        }
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        PushManager.getInstance().bindAlias(this.getApplicationContext(),PatientUserManager.getInstance().getPatientId());
        setDefaultFragment();
        setBottomNavigation();
        Intent intent = getIntent();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle !=null){
            String url = bundle.getString("url");
            Log.i("url",url);
            Intent noti_intent = new Intent(this, MsgContentActivity.class);
            noti_intent.putExtras(bundle);
            startActivity(noti_intent);
            Log.i("url","test");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //开启计步器服务
        if(LocalStepHelper.getLocalPedometerEnable(getApplicationContext())) {
            Intent service = new Intent(this, StepCounterService.class);
            startService(service);
        }

        //开启TimeTick监听服务
        Intent timeService = new Intent(this, TimeService.class);
        startService(timeService);


        //先注册广播
        if (appUpdateServiceReceiver == null) {
            appUpdateServiceReceiver = new AppUpdateServiceReceiver();
            IntentFilter intentFilter = new IntentFilter(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
            registerReceiver(appUpdateServiceReceiver, intentFilter);
        }

        //再启动app自动更新服务（针对android 6.0以上的系统提供运行时权限）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyStoragePermissions(this);//当targetversion小于23时，会默认返回0，不影响程序运行
        } else {
            Intent intent = new Intent(this, AppUpdateService.class);
            intent.putExtra(Utils.INTENT_EXTRA_KEY_PATIENT, PatientUserManager.getInstance().getPatientId());
            startService(intent);
        }
        //发送一个尝试建立关系的广播
        Intent mIntent = new Intent(Utils.APP_UPDATE_ACTIVITY_RECEIVE_ACTION);
        mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_ACTIVITY_TO_SERVICE_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_TRYCHECK_DOWNLOAD);
        sendBroadcast(mIntent);

    }

    private void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(this, AppUpdateService.class);
            intent.putExtra(Utils.INTENT_EXTRA_KEY_PATIENT, PatientUserManager.getInstance().getPatientId());
            startService(intent);
        }
    }

    private void verifyPhoneStatePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONE, REQUEST_PHONE_STATE);
        } else {
            UserAgent.Init(this, "COPD", PatientUserManager.getInstance().getPatientId());
            UserAgent.UserDevice(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, AppUpdateService.class);
                intent.putExtra(Utils.INTENT_EXTRA_KEY_PATIENT, PatientUserManager.getInstance().getPatientId());
                startService(intent);
            }
        }else if(requestCode == REQUEST_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UserAgent.Init(this, "COPD", PatientUserManager.getInstance().getPatientId());
                UserAgent.UserDevice(this);
            }
        }
    }

    @Override
    public void onBackPressed() {// 只显示一次启动页（ App 没被 kill 的情况下）
        // super.onBackPressed(); 	不要调用父类的方法
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homepageFragment = new HomepageFragment();
        transaction.replace(R.id.fragment_container, homepageFragment);
        transaction.commit();
    }

    private void setBottomNavigation() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem itemHomepage = new AHBottomNavigationItem(R.string.title_home, R.mipmap.ic_homepage, R.color.darkGrey);
        AHBottomNavigationItem itemClass = new AHBottomNavigationItem(R.string.title_class, R.mipmap.ic_class, R.color.darkGrey);
        AHBottomNavigationItem itemMine = new AHBottomNavigationItem(R.string.title_mine, R.mipmap.ic_mine, R.color.darkGrey);

        // Add items
        bottomNavigation.addItem(itemHomepage);
        bottomNavigation.addItem(itemClass);
        bottomNavigation.addItem(itemMine);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#03A9F4"));
        bottomNavigation.setInactiveColor(Color.parseColor("#4A4A4A"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                switch (position) {
                    case 0:
                        if (homepageFragment == null) {
                            homepageFragment = new HomepageFragment();
                        }
                        transaction.replace(R.id.fragment_container, homepageFragment);
                        break;
                    case 1:
                        if (classFragment == null) {
                            classFragment = new ClassFragment();
                        }
                        transaction.replace(R.id.fragment_container, classFragment);
                        break;
                    case 2:
                        if (mineFragment == null) {
                            mineFragment = new MineFragment();
                        }
                        transaction.replace(R.id.fragment_container, mineFragment);
                        break;
                }
                transaction.commitAllowingStateLoss();
                return true;
            }
        });
    }

    /**
     * 内部广播类
     */
    private class AppUpdateServiceReceiver extends BroadcastReceiver {

        public AppUpdateServiceReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int msgCode = intent.getIntExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_NO_UPDATE);
            switch (msgCode) {
                case Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_NEW_VERSION:
                    String msgContent = intent.getStringExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_INFO);
                    boolean msgIsForced = intent.getBooleanExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_FORCE, false);
                    autoUpdateDialog = new AutoUpdateDialog();
                    autoUpdateDialog.setContentText(msgContent);
                    if (!msgIsForced) { // 兼容版本，非强制更新
                        autoUpdateDialog.setLeftBtnText(Utils.BTN_TEXT_LATER);
                        autoUpdateDialog.setOnClickBtnUpdateLaterListener(new CustomInterface.UpdateButtonOnClickListener() {
                            @Override
                            public void onClick() {
                                autoUpdateDialog.dismiss();
                            }
                        });
                    } else { //非兼容版本，强制更新
                        autoUpdateDialog.setLeftBtnText(Utils.BTN_TEXT_EXIT);
                        autoUpdateDialog.setOnClickBtnUpdateLaterListener(new CustomInterface.UpdateButtonOnClickListener() {
                            @Override
                            public void onClick() {
                                autoUpdateDialog.dismiss();
                                Intent sIntent = new Intent(MainActivity.this, AppUpdateService.class);
                                stopService(sIntent);
                                MainActivity.this.finish();
                                System.exit(0);
                            }
                        });
                    }
                    autoUpdateDialog.setOnClickBtnUpdateNowListener(new CustomInterface.UpdateButtonOnClickListener() {
                        @Override
                        public void onClick() {
                            //发送请求下载的广播
                            Intent mIntent = new Intent(Utils.APP_UPDATE_ACTIVITY_RECEIVE_ACTION);
                            mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_ACTIVITY_TO_SERVICE_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_UPDATE_NOW);
                            sendBroadcast(mIntent);
                            //关闭自动更新对话框，显示下载进度对话框
                            autoUpdateDialog.dismiss();
                            downloadDialog = new DownloadDialog();
                            downloadDialog.show(MainActivity.this.getFragmentManager(), "Download");
                        }
                    });
                    autoUpdateDialog.show(MainActivity.this.getFragmentManager(), "AutoUpdate");
                    break;
                case Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING:
                    int progressValue = intent.getIntExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS, 0);
                    if (downloadDialog != null && downloadDialog.isVisible()) {
                        downloadDialog.updateProgress(progressValue);
                    }
                    break;
                case Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_FINISH:
                    if (downloadDialog != null && downloadDialog.isVisible()) {
                        downloadDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
