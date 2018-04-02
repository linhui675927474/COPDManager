package com.zju.biomedit.copdmanager.support;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.tencent.bugly.Bugly;
//import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by wangzheyu on 2017/7/8.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bugly.init(this, "deefa6853b", false);
        OkGo.getInstance().init(this);
//        CrashReport.initCrashReport(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

//        // 安装tinker
//        Beta.installTinker();
    }

    public static Context getContext() {
        return context;
    }


}
