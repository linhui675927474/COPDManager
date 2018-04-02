package com.zju.biomedit.copdmanager.support;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.activity.MainActivity;
import com.zju.biomedit.copdmanager.activity.PefRecordActivity;
import com.zju.biomedit.copdmanager.activity.SplashActivity;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.StepRecord;
import com.zju.biomedit.copdmanager.model.Reminder;
import com.zju.biomedit.copdmanager.step.LocalStepHelper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TimeService extends Service {

    private TimeTickReceiver timeTickReceiver;
    private Realm realm;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timeTickReceiver = new TimeTickReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);//每分钟发送一次
        registerReceiver(timeTickReceiver, filter);
        updateNotification();

    }

    private void updateNotification(){
        int localStep = LocalStepHelper.getLocalStepCount(this);
        boolean pedometerEnable = LocalStepHelper.getLocalPedometerEnable(this);
        String notificationTitle = "COPD管家 - 计步器";
        if(!pedometerEnable){
            notificationTitle = "COPD管家 - 计步器（已关闭）";
        }
        Notification notification = new Notification.Builder(this).
                setTicker("COPD管家").
                setSmallIcon(R.mipmap.ic_launcher_new).
                setContentTitle(notificationTitle).
                setContentText("今日步数：" + localStep + "步").build();
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        startForeground(Utils.NOTIFICATION_ID_STEP_CHANGED, notification);
    }

    private void commitStepToCloud() {
        // 访问网络前先检查网络状态
        if(!GlobalMethod.getNetworkConnection()){
            GlobalMethod.showToast(getApplicationContext(), Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
            return;
        }

        int step = LocalStepHelper.getLocalStepCount(MyApplication.getContext());
        StepRecord stepRecord = new StepRecord();
        stepRecord.setId(0);
        stepRecord.setMeasureTime(TimeManager.getStrDateTime());
        stepRecord.setValue(step);


        JsonObject json = new JsonObject();
        String data = JsonUtil.toJson(stepRecord);

        Log.d("wzypef", "the data is: " + data);
        json.addProperty("data", data);
        json.addProperty("patientId", PatientUserManager.getInstance().getPatientId());
        json.addProperty("recordType", Utils.STEP);
        String url = Utils.BASE_URL + Utils.COMMIT_GENERIC_RECORD;

        Ion.with(MyApplication.getContext()).load(url).setJsonObjectBody(json).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                Log.d("wzystep", "the error is: " + e);
                Log.d("wzystep", "the result is: " + result);

                if (e != null) {
                    GlobalMethod.showToast(getApplicationContext(), Utils.MESSAGE_UPLOAD_FAIL, Toast.LENGTH_SHORT);
                }else{
                    int flag = result.get("flag").getAsInt();
                    Log.d("wzystep", "the flag is: " + flag);

                }
            }
        });



    }

    /**
     * 内部广播
     */
    private class TimeTickReceiver extends BroadcastReceiver {

        public TimeTickReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {

                String time_HMS = TimeManager.getStrDateTime().substring(11, 19); //DateFormat.format("HH:mm:ss", new Date()).toString();
                String time_MS = time_HMS.substring(3, 8); //DateFormat.format("mm:ss", new Date()).toString();
                int hour = Integer.parseInt(time_HMS.substring(0, 2));
                int minute = Integer.parseInt(time_HMS.substring(3, 5));
                Realm.init(context);
                realm = Realm.getDefaultInstance();
                if(realm.where(Reminder.class).count() !=0 ){
                    RealmResults<Reminder> reminderList = realm.where(Reminder.class).findAll();
                    for (int i = 0; i < reminderList.size(); i++) {
                        Reminder r = reminderList.get(i);
                        int reminderHour = Integer.parseInt(r.getTime().substring(0,2));
                        int reminderminute = Integer.parseInt(r.getTime().substring(3,5));
                        //Log.d("ybj", r.getHour() + ":" + r.getMinute() + " ; " + r.getEnable() + "; " + r.getCategory());
                        switch (r.getType()) {
                            case 0:
                                if (r.isDefault() && (reminderHour == hour && reminderminute == minute) && TimeManager.isMonday()){
                                    GlobalMethod.sendNotification(context, 0, R.mipmap.ic_launcher, "COPD管家提醒你：", "请进行每周的CAT量表填写", r.isUseSound());
                                }
                                break;
                            case 1:
                                if (r.isDefault() && (reminderHour == hour && reminderminute == minute) && TimeManager.isMonthFirst()){
                                    GlobalMethod.sendNotification(context, 1, R.mipmap.ic_launcher, "COPD管家提醒你：", "请进行每月的抑郁量表填写", r.isUseSound());
                                }
                                break;
                            case 2:
                                if (r.isDefault() && (reminderHour == hour && reminderminute == minute) && TimeManager.isMonthFirst()){
                                    GlobalMethod.sendNotification(context, 2, R.mipmap.ic_launcher, "COPD管家提醒你：", "请进行每月的焦虑量表填写", r.isUseSound());
                                }
                                break;
                            case 3:
                                if (r.isDefault() && (reminderHour == hour && reminderminute == minute)){
                                    GlobalMethod.sendNotification(context, 3, R.mipmap.ic_launcher, "COPD管家提醒你：", "请进行每天的用药记录", r.isUseSound());
                                    //sendNotification(3,"COPD管家提醒你：", "请进行每天的用药记录",r.isUseSound());
                                }
                                break;
                            case 4:
                                if (r.isDefault() && (reminderHour == hour && reminderminute == minute) && TimeManager.isMonday()){
                                    GlobalMethod.sendNotification(context, 4, R.mipmap.ic_launcher, "COPD管家提醒你：", "请进行每周的峰流速记录", r.isUseSound());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                if(time_HMS.equals("23:59:00")) { // 作为一次保险上传
                    commitStepToCloud();
                }
                else if(time_HMS.equals("00:00:00")) { //重置本地计步信息
                    LocalStepHelper.updateLocalStepCount(context, 0);
                    LocalStepHelper.updateLocalStepDate(context, TimeManager.getStrDate());
                }
                else if(time_MS.equals("00:00") || time_MS.equals("30:00")) { //每30分钟上传一次“走步”记录，但在零点不进行上传数据（避免短时间内多次操作引起问题）
                   commitStepToCloud();
                }

            }
        }

    }
    
    private void sendNotification(int id,String title,String content,boolean isSound) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon( R.mipmap.ic_launcher_new)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentText(content);
        Notification notification = mBuilder.build();
        if(isSound){
            notification.defaults = Notification.DEFAULT_SOUND; //开启声音
        }

        Intent resultIntent = new Intent(this, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification);
    }


}
