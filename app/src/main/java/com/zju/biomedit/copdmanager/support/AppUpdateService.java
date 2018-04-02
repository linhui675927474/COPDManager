package com.zju.biomedit.copdmanager.support;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.BuildConfig;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.model.AppVersionInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class AppUpdateService extends Service {

    private static final int APP_UPDATE_FAILED = 0;
    private static final int APP_UPDATE_START = 1;
    private static final int APP_UPDATE_DOING = 2;
    private static final int APP_UPDATE_SUCCEED = 3;

    /**
     * 是否已检查过app更新
     */
    private static boolean isChecked = false;
    /**
     * 是否下载完成
     */
    private static boolean isFinishDownload = false;

    //通知栏
    private NotificationManager updateNotificationManager;
    private Notification updateNotification;
    private Notification.Builder notificationBuilder;
    private PendingIntent updatePendingIntent;

    // 目标app
    private File destFile;

    //广播接收者
    private AppUpdateActivityReceiver appUpdateActivityReceiver;

    public AppUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.isChecked = false;
        this.isFinishDownload = false;
        appUpdateActivityReceiver = new AppUpdateActivityReceiver();
        IntentFilter intentFilter = new IntentFilter(Utils.APP_UPDATE_ACTIVITY_RECEIVE_ACTION);
        this.registerReceiver(appUpdateActivityReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //获取当前版本信息
        PackageInfo packageInfo = GlobalMethod.getPackageInfo(getApplicationContext());
        //已检查过app的更新则不再进行检查
        if(isChecked || packageInfo == null || intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        final int currentVersionCode = packageInfo.versionCode;
        final String currentVersionName = packageInfo.versionName;
        //获取病人号
        final String patientId = intent.getStringExtra(Utils.INTENT_EXTRA_KEY_PATIENT);

        //设置成已经检查过更新的状态
        this.isChecked = true;
        //检查版本
        JsonObject jsonArgs = new JsonObject();
        jsonArgs.addProperty("patientId", patientId);
        jsonArgs.addProperty("versionCode", currentVersionCode);
        Log.d("ybj", jsonArgs+"");
        String url = Utils.BASE_URL + Utils.CHECK_APPUPDATE_METHOD;
        Ion.with(this).load(url).setJsonObjectBody(jsonArgs).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Log.d("ybj", "the error is:" + e);
                    return;
                }

                int flag = result.get("flag").getAsInt();
                switch (flag) {
                    case Utils.SERVICE_RETURN_FAILED:
                        Log.d("ybj", "no update");
                        break;
                    case Utils.SERVICE_RETURN_SUCCEED:
                        Gson gson = new Gson();
                        JsonObject jsonVersion = result.get("newestVersion").getAsJsonObject();
                        AppVersionInfo newestVersionInfo = gson.fromJson(jsonVersion, new TypeToken<AppVersionInfo>() {
                        }.getType());
                        String newestVersionName = newestVersionInfo.getVersionName();
                        String updateContent = newestVersionInfo.getUpdateContent();

                        //发现新版本，提示用户更新
                        String msg = String.format("【当前版本】：%s%n【最新版本】：%s%n【更新内容】：%n%s", currentVersionName, newestVersionName,updateContent);
                        Log.d("ybj", msg);
                        //发送广播
                        Intent mIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
                        mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_NEW_VERSION);
                        mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_INFO, msg);
                        mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_FORCE, newestVersionInfo.getForced());
                        sendBroadcast(mIntent);
                        break;
                    default:
                        break;
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //取消广播
        this.unregisterReceiver(appUpdateActivityReceiver);
        appUpdateActivityReceiver = null;
        super.onDestroy();
    }

    /**
     * 获取目标app文件
     */
    private File getFile() throws Exception {
        File path = new File(getApplicationContext().getCacheDir(), Utils.SHARE_DIR_DOWNLOAD);
        //判断sdCard是否可以读写
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path = new File(Environment.getExternalStorageDirectory(), Utils.SHARE_DIR_DOWNLOAD);
        }
        File file = new File(path, Utils.SHARE_FILE_DOWNLOAD_APK);

        if(!path.exists()){
            path.mkdirs();
        }
        if(file.exists()){
            file.delete();
        }

        //创建文件
        file.createNewFile();
        return file;
    }

    /**
     * 创建下载通知
     */
    private void createDownloandNotification() {
        //创建通知
        updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        updatePendingIntent = PendingIntent.getActivity(getApplicationContext(), Utils.PENDINGINTENT_REQUESTCODE_NOTIFICATION_DOWNLOADAPP, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder = new Notification.Builder(getApplicationContext());
        notificationBuilder.setTicker("COPD管家");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        appUpdateHandler.sendEmptyMessage(APP_UPDATE_START);

        // 开始下载app
        new Thread(new AppDownloadRunnable()).start();
    }

    /**
     * 下载app
     * @throws Exception
     */
    private void downloadApp() throws Exception {
        int downloadSize = 0; //已下载的大小（上限2048M)
        int downloadPercent = 0; //已下载的百分比

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(Utils.APP_DOWNLOAD_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            int fileSize = httpURLConnection.getContentLength();
            inputStream = httpURLConnection.getInputStream();
            destFile = getFile();
            fileOutputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024 * 4];
            int readSize = 0;
            while ((readSize = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, readSize);
                downloadSize += readSize;
                int newPrecent = (int) ((downloadSize / (float) fileSize ) * 100 ) ;//乘小数转化成double相除会出现精度问题，卡在99.999999999
                Log.i("wzydown",downloadSize+ " " + " " +fileSize + " " + downloadPercent + " "+ newPrecent);
                // 每下载超过10%更新一次进度的显示
                if (newPrecent - 10 >= downloadPercent || newPrecent >= 100) {
                    downloadPercent = newPrecent;
                    if(downloadPercent < 100) {
                        Message message = new Message();
                        message.what = APP_UPDATE_DOING;
                        message.arg1 = downloadPercent;
                        appUpdateHandler.sendMessage(message);
                    }
                    else {
                        appUpdateHandler.sendEmptyMessage(APP_UPDATE_SUCCEED);
                    }
                }
            }
        }
        finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
            if(fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }


    /**
     * 内部下载线程类
     */
    private class AppDownloadRunnable implements Runnable{
        public AppDownloadRunnable() {}

        @Override
        public void run() {
            try{
                downloadApp();
                //下载成功
            }
            catch(Exception e){
                // 下载失败
                appUpdateHandler.sendEmptyMessage(APP_UPDATE_FAILED);
                e.printStackTrace();
            }
        }
    }

    /**
     * 内部Handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<AppUpdateService> mAppUpdateService;

        public MyHandler(AppUpdateService appUpdateService) {
            mAppUpdateService = new WeakReference<AppUpdateService>(appUpdateService);
        }

        @Override
        public void handleMessage(Message msg) {
            AppUpdateService appUpdateService = mAppUpdateService.get();
            if (appUpdateService != null) {
                switch (msg.what){
                    case APP_UPDATE_START:
                        appUpdateService.updateNotification = appUpdateService.notificationBuilder.setContentIntent(appUpdateService.updatePendingIntent)
                                .setContentTitle("开始下载")
                                .setContentText("0%").build();
                        appUpdateService.updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; //正在运行 & 不允许用户清除
                        //updateNotification.defaults = Notification.DEFAULT_SOUND; //默认声音
                        //发送通知
                        appUpdateService.updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, appUpdateService.updateNotification);
                        break;
                    case APP_UPDATE_DOING:
                        appUpdateService.updateNotification = appUpdateService.notificationBuilder.setContentIntent(appUpdateService.updatePendingIntent)
                                .setContentTitle("正在下载...")
                                .setContentText(msg.arg1 + "%").build();
                        appUpdateService.updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
                        appUpdateService.updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, appUpdateService.updateNotification);

                        //发送广播
                        Intent doingIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
                        doingIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING);
                        doingIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS, msg.arg1);
                        appUpdateService.sendBroadcast(doingIntent);
                        break;
                    case APP_UPDATE_SUCCEED:
                        //下载完成，更新标识位
                        isFinishDownload = true;
                        //下载完成，提示音
                        appUpdateService.updateNotification = appUpdateService.notificationBuilder.setContentIntent(appUpdateService.updatePendingIntent)
                                .setContentTitle("下载完成")
                                .setContentText("100%").build();
                        appUpdateService.updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; //正在运行 & 不允许用户清除
                        appUpdateService.updateNotification.defaults = Notification.DEFAULT_SOUND; //默认声音
                        appUpdateService.updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, appUpdateService.updateNotification);

                        //发送广播
                        Intent doneIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
                        doneIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING);
                        doneIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS, 100);
                        appUpdateService.sendBroadcast(doneIntent);

                        //等待1.6秒，再次发送广播，取消通知（这样才能听到提示音和看到进度条显示为100%）
                        try {
                            Thread.sleep(1600);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //发送广播
                        Intent finishIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
                        finishIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_FINISH);
                        appUpdateService.sendBroadcast(finishIntent);
                        appUpdateService.updateNotificationManager.cancel(Utils.NOTIFICATION_ID_DOWNLOADAPP);
                        //安装app

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        //判断是否是AndroidN以及更高的版本
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(MyApplication.getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", appUpdateService.destFile);
                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        } else {
                            intent.setDataAndType(Uri.fromFile(appUpdateService.destFile), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Uri uri = Uri.fromFile(destFile);
//                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        appUpdateService.startActivity(intent);
                        break;
                    case APP_UPDATE_FAILED:
                        appUpdateService.updateNotification = appUpdateService.notificationBuilder.setContentIntent(appUpdateService.updatePendingIntent)
                                .setContentTitle("下载失败")
                                .setContentText("").build();
                        appUpdateService.updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        appUpdateService.updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, appUpdateService.updateNotification);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private final MyHandler appUpdateHandler = new MyHandler(this);
//    private Handler appUpdateHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case APP_UPDATE_START:
//                    updateNotification = notificationBuilder.setContentIntent(updatePendingIntent)
//                            .setContentTitle("开始下载")
//                            .setContentText("0%").build();
//                    updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; //正在运行 & 不允许用户清除
//                    //updateNotification.defaults = Notification.DEFAULT_SOUND; //默认声音
//                    //发送通知
//                    updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, updateNotification);
//                    break;
//                case APP_UPDATE_DOING:
//                    updateNotification = notificationBuilder.setContentIntent(updatePendingIntent)
//                            .setContentTitle("正在下载...")
//                            .setContentText(msg.arg1 + "%").build();
//                    updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
//                    updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, updateNotification);
//
//                    //发送广播
//                    Intent doingIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
//                    doingIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING);
//                    doingIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS, msg.arg1);
//                    sendBroadcast(doingIntent);
//                    break;
//                case APP_UPDATE_SUCCEED:
//                    //下载完成，更新标识位
//                    isFinishDownload = true;
//                    //下载完成，提示音
//                    updateNotification = notificationBuilder.setContentIntent(updatePendingIntent)
//                            .setContentTitle("下载完成")
//                            .setContentText("100%").build();
//                    updateNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR; //正在运行 & 不允许用户清除
//                    updateNotification.defaults = Notification.DEFAULT_SOUND; //默认声音
//                    updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, updateNotification);
//
//                    //发送广播
//                    Intent doneIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
//                    doneIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING);
//                    doneIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS, 100);
//                    sendBroadcast(doneIntent);
//
//                    //等待1.6秒，再次发送广播，取消通知（这样才能听到提示音和看到进度条显示为100%）
//                    try {
//                        Thread.sleep(1600);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    //发送广播
//                    Intent finishIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
//                    finishIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_FINISH);
//                    sendBroadcast(finishIntent);
//                    updateNotificationManager.cancel(Utils.NOTIFICATION_ID_DOWNLOADAPP);
//                    //安装app
//
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    //判断是否是AndroidN以及更高的版本
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        Uri contentUri = FileProvider.getUriForFile(AppUpdateService.this, BuildConfig.APPLICATION_ID + ".fileProvider", destFile);
//                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//                    } else {
//                        intent.setDataAndType(Uri.fromFile(destFile), "application/vnd.android.package-archive");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    }
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    Uri uri = Uri.fromFile(destFile);
////                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                    startActivity(intent);
//                    break;
//                case APP_UPDATE_FAILED:
//                    updateNotification = notificationBuilder.setContentIntent(updatePendingIntent)
//                            .setContentTitle("下载失败")
//                            .setContentText("").build();
//                    updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
//                    updateNotificationManager.notify(Utils.NOTIFICATION_ID_DOWNLOADAPP, updateNotification);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


    /**
     * 内部广播类
     */
    private class AppUpdateActivityReceiver extends BroadcastReceiver {

        public AppUpdateActivityReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int msgCode = intent.getIntExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_ACTIVITY_TO_SERVICE_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_UPDATE_LATER);
            switch (msgCode){
                case Utils.MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_UPDATE_NOW:
                    createDownloandNotification();
                    break;
                case Utils.MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_TRYCHECK_DOWNLOAD:
                    if(isFinishDownload){
                        Intent mIntent = new Intent(Utils.APP_UPDATE_SERVICE_RECEIVE_ACTION);
                        mIntent.putExtra(Utils.INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE, Utils.MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_FINISH);
                        sendBroadcast(mIntent);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
