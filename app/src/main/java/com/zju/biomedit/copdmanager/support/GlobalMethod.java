package com.zju.biomedit.copdmanager.support;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.zju.biomedit.copdmanager.R;
import com.zju.biomedit.copdmanager.fragment.HomepageFragment;
import com.zju.biomedit.copdmanager.model.DisEvaluation;
import com.zju.biomedit.copdmanager.model.DiscomfortTask;
import com.zju.biomedit.copdmanager.model.LoginUserInfo;
import com.zju.biomedit.copdmanager.model.MedicineTask;
import com.zju.biomedit.copdmanager.model.PatientUserManager;
import com.zju.biomedit.copdmanager.model.PefTask;
import com.zju.biomedit.copdmanager.model.ScaleTask;
import com.zju.biomedit.copdmanager.model.SixMWDTask;
import com.zju.biomedit.copdmanager.model.WeatherAir;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by ybj on 2015/10/23.
 */
public class GlobalMethod {
    public static String IP_URL;

    /**
     * 防止Toast重复显示
     */
    private static Toast toast;
    public static void showToast(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 120);
        }
        toast.setText(text);
        toast.show();
    }

    /**
     * 检查网络状态是否可用
     */
    private static boolean networkConnection = false;
    public static boolean getNetworkConnection(){
        return networkConnection;
    }
    public static void setNetworkConnection(boolean connection){ networkConnection = connection; }
    public static boolean isNetworkAvailable(Context context){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean res = false;
        if (connectivityManager == null){
            res = false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++){
                    // 判断当前网络状态是否为连接状态且可用
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED && networkInfo[i].isAvailable()){
                        res = true;
                        break;
                    }
                }
            }
        }

        if(!res) {
            GlobalMethod.showToast(context, Utils.NETWORK_DISABLED, Toast.LENGTH_LONG);
        }

        networkConnection = res;
        return res;
    }

    /**
     * 获取App版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            PackageManager manager = context.getPackageManager();
            packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return packageInfo;
        }
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private static int statusBarHeight = -1;
    public static int getStatusBarHeight(Activity activity){
        if(statusBarHeight < 0) {
            statusBarHeight = systemStatusBarHeight(activity);
        }
        return statusBarHeight;
    }
    private static int systemStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 计算PEF标准值
     */
    public static void calculatePefStandard() {
        float height = PatientUserManager.getInstance().getLoginUserInfo().getNewestHeight();
        String birthDate = PatientUserManager.getInstance().getLoginUserInfo().getBirthDate();
        String sexCode = PatientUserManager.getInstance().getLoginUserInfo().getSexCode();
        int pefStandard;
        int age = TimeManager.getAgeByDateString(birthDate);
        Log.i("pefStandard",birthDate + " " + age+" "+ sexCode + " "+ height);
        switch (sexCode){
            case("M"):
                pefStandard = (int) (75.6 + 20.4 * age - 0.41 * Math.pow(age,2) + 0.002 * Math.pow(age,3) + 1.19 * height);
                break;
            case("F"):
                pefStandard = (int) (292.0 + 1.79 * age - 0.046 * Math.pow(age,2) + 0.68 * height);
                break;
            default:
                pefStandard = (int) (75.6 + 20.4 * age - 0.41 * Math.pow(age,2) + 0.002 * Math.pow(age,3) + 1.19 * height);
                break;
        }
        Log.i("pefStandard",pefStandard+"");
        PatientUserManager.getInstance().setPefStandard(pefStandard);
    }

    /**
     * 计算个人综合评价值
     */
    public static int calculateEvaluationValue() {
        PefTask pefTask = PatientUserManager.getInstance().getLatestRecord().getPefTask();
        ScaleTask catScaleTask = PatientUserManager.getInstance().getLatestRecord().getCatScaleTask();
        int pefStandard = PatientUserManager.getInstance().getPefStandard();
        int evaluationValue;
        if(pefTask == null || catScaleTask == null){
            evaluationValue = 0;
        }else {
            int pefValue = pefTask.getValue();
            int catScore = catScaleTask.getScore();
            if((pefValue >= pefStandard * 0.8) && (catScore >= 0 && catScore <= 10)){
                evaluationValue = 80;
            }else if((pefValue >= pefStandard * 0.8) && (catScore >= 11 && catScore <= 20)){
                evaluationValue = 60;
            }else if((pefValue >= pefStandard * 0.8) && (catScore >= 21 && catScore <= 30)){
                evaluationValue = 40;
            }else if((pefValue >= pefStandard * 0.8) && (catScore >= 31 && catScore <= 40)){
                evaluationValue = 20;
            }else if((pefValue >= pefStandard * 0.6 && pefValue <= pefStandard * 0.8) && (catScore >= 0 && catScore <= 20)){
                evaluationValue = 60;
            }else if((pefValue >= pefStandard * 0.6 && pefValue <= pefStandard * 0.8) && (catScore >= 21 && catScore <= 30)){
                evaluationValue = 40;
            }else if((pefValue >= pefStandard * 0.6 && pefValue <= pefStandard * 0.8) && (catScore >= 31 && catScore <= 40)){
                evaluationValue =  20;
            }else if(pefValue <= pefStandard * 0.6 ){
                evaluationValue = 20;
            }else{
                return 0;
            }
        }
        PatientUserManager.getInstance().setEvaluationValue(evaluationValue);
        return evaluationValue;
    }

    public static String getLatestDiscomfort(DiscomfortTask discomfortTask){
        int inflammation = discomfortTask.getInflammation();
        int lung = discomfortTask.getLung();
        int heart = discomfortTask.getHeart();
        int breath = discomfortTask.getBreath();
        String strDiscomfort = "";
        if(inflammation ==0 && lung ==0 && heart ==0 && breath==0 ){
            strDiscomfort = "无";
        }
        if((inflammation & 8) !=0){
            strDiscomfort = strDiscomfort+ "血痰 ";
        }
        if((inflammation & 4) !=0){
            strDiscomfort = strDiscomfort+ "咳嗽 ";
        }
        if((inflammation & 2) !=0){
            strDiscomfort = strDiscomfort+ "黄痰 ";
        }
        if((inflammation & 1) !=0){
            strDiscomfort = strDiscomfort+ "发烧 ";
        }
        if((lung & 4) !=0){
            strDiscomfort = strDiscomfort+ "气短 ";
        }
        if((lung & 2) !=0){
            strDiscomfort = strDiscomfort+ "喘息 ";
        }
        if((lung & 1) !=0){
            strDiscomfort = strDiscomfort+ "活动后气短加重 ";
        }
        if((heart & 8) !=0){
            strDiscomfort = strDiscomfort+ "消瘦 ";
        }
        if((heart & 4) !=0){
            strDiscomfort = strDiscomfort+ "腿肿 ";
        }
        if((heart & 2) !=0){
            strDiscomfort = strDiscomfort+ "腹胀 ";
        }
        if((heart & 1) !=0){
            strDiscomfort = strDiscomfort+ "反酸水 ";
        }
        if((breath & 2) !=0){
            strDiscomfort = strDiscomfort+ "迷糊 ";
        }
        if((breath & 1) !=0){
            strDiscomfort = strDiscomfort+ "嗜睡 ";
        }
        return strDiscomfort;

    }

    public static List<DisEvaluation> getDisEvaluation(String strDis) {
        List<DisEvaluation> disEvaluationList = new ArrayList<>();
        String[] strArray;
        strArray = strDis.split(" ");
        DisEvaluation disEvaluationLung = new DisEvaluation();
        DisEvaluation disEvaluationBreath = new DisEvaluation();
        for(String s:strArray) {
            DisEvaluation disEvaluation = new DisEvaluation();
            switch (s){
                case Utils.INFLAMMATION1:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.INFLAMMATION1_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.INFLAMMATION2:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.INFLAMMATION2_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.INFLAMMATION3:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.INFLAMMATION3_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.INFLAMMATION4:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.INFLAMMATION4_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.HEART1:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.HEART1_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.HEART2:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.HEART2_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.HEART3:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.HEART3_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.HEART4:
                    disEvaluation.setDisName(s+"：");
                    disEvaluation.setDisEvaluation(Utils.HEART4_HINT);
                    disEvaluationList.add(disEvaluation);
                    break;
                case Utils.LUNG1:
                    if(!TextUtils.isEmpty(disEvaluationLung.getDisName())){
                        disEvaluationLung.setDisName(disEvaluationLung.getDisName()+ "、"+ s);
                    }
                    else{
                        disEvaluationLung.setDisName(s);
                    }
                    disEvaluationLung.setDisEvaluation(Utils.LUNG_HINT);
                    break;
                case Utils.LUNG2:
                    if(!TextUtils.isEmpty(disEvaluationLung.getDisName())){
                        disEvaluationLung.setDisName(disEvaluationLung.getDisName()+ "、"+ s);
                    }
                    else{
                        disEvaluationLung.setDisName(s);
                    }
                    disEvaluationLung.setDisEvaluation(Utils.LUNG_HINT);
                    break;
                case Utils.LUNG3:
                    if(!TextUtils.isEmpty(disEvaluationLung.getDisName())){
                        disEvaluationLung.setDisName(disEvaluationLung.getDisName()+ "、"+ s);
                    }
                    else{
                        disEvaluationLung.setDisName(s);
                    }
                    disEvaluationLung.setDisEvaluation(Utils.LUNG_HINT);
                    break;
                case Utils.BREATH1:
                    if(!TextUtils.isEmpty(disEvaluationBreath.getDisName())){
                        disEvaluationBreath.setDisName(disEvaluationBreath.getDisName()+ "、"+ s);
                    }
                    else{
                        disEvaluationBreath.setDisName(s);
                    }
                    disEvaluationBreath.setDisEvaluation(Utils.BREATH_HINT);
                    break;
                case Utils.BREATH2:
                    if(!TextUtils.isEmpty(disEvaluationBreath.getDisName())){
                        disEvaluationBreath.setDisName(disEvaluationBreath.getDisName()+ "、"+ s);
                    }
                    else{
                        disEvaluationBreath.setDisName(s);
                    }
                    disEvaluationBreath.setDisEvaluation(Utils.BREATH_HINT);
                    break;
                default:
                    break;
            }
        }
        if(!TextUtils.isEmpty(disEvaluationLung.getDisName())){
            disEvaluationLung.setDisName(disEvaluationLung.getDisName()+"：");
            disEvaluationList.add(disEvaluationLung);
        }
        if(!TextUtils.isEmpty(disEvaluationBreath.getDisName())){
            disEvaluationBreath.setDisName(disEvaluationBreath.getDisName()+"：");
            disEvaluationList.add(disEvaluationBreath);
        }
        return disEvaluationList;
    }

    /**
     * 判断本地保存的最新一次cat记录是否覆盖
     */
    public static void updateLatestCatRecord(ScaleTask catTask) {
        ScaleTask mCatTask = PatientUserManager.getInstance().getLatestRecord().getCatScaleTask();
        if(mCatTask == null){
            PatientUserManager.getInstance().getLatestRecord().setCatScaleTask(catTask);
        }else{
            String beforeDate = mCatTask.getMeasureTime();
            String afterDate = catTask.getMeasureTime();
            if(TimeManager.equalsBefore(beforeDate,afterDate)){
                PatientUserManager.getInstance().getLatestRecord().setCatScaleTask(catTask);
            }
        }

    }

    /**
     * 判断本地保存的最新一次pef记录是否覆盖
     */
    public static void updateLatestPefRecord(PefTask pefTask) {
        PefTask mPefTask = PatientUserManager.getInstance().getLatestRecord().getPefTask();
        if(mPefTask == null){
            PatientUserManager.getInstance().getLatestRecord().setPefTask(pefTask);
        }else{
            String beforeDate = mPefTask.getMeasureTime();
            String afterDate = pefTask.getMeasureTime();
            if(TimeManager.equalsBefore(beforeDate,afterDate)){
                PatientUserManager.getInstance().getLatestRecord().setPefTask(pefTask);
            }
        }

    }

    /**
     * 判断本地保存的最新一次6MWD记录是否覆盖
     */
    public static void updateLatest6MWDRecord(SixMWDTask sixMWDTask) {
        SixMWDTask m6MWDTask = PatientUserManager.getInstance().getLatestRecord().getSixMWDTask();
        if(m6MWDTask == null){
            PatientUserManager.getInstance().getLatestRecord().setSixMWDTask(m6MWDTask);
        }else{
            String beforeDate = m6MWDTask.getMeasureTime();
            String afterDate = sixMWDTask.getMeasureTime();
            if(TimeManager.equalsBefore(beforeDate,afterDate)){
                PatientUserManager.getInstance().getLatestRecord().setSixMWDTask(sixMWDTask);
            }
        }

    }

    /**
     * 判断本地保存的最新一次服药记录是否覆盖
     */
    public static void updateLatestMedicineRecord(MedicineTask medicineTask) {
        MedicineTask mMedicineTask = PatientUserManager.getInstance().getLatestRecord().getMedicineTask();
        if(mMedicineTask == null){
            PatientUserManager.getInstance().getLatestRecord().setMedicineTask(medicineTask);
        }else{
            String beforeDate = mMedicineTask.getMeasureTime();
            String afterDate = medicineTask.getMeasureTime();
            if(TimeManager.equalsBefore(beforeDate,afterDate)){
                PatientUserManager.getInstance().getLatestRecord().setMedicineTask(medicineTask);
            }
        }

    }

    /**
     * 判断本地保存的最新一次不适记录是否覆盖
     */
    public static void updateLatestDiscomfortRecord(DiscomfortTask discomfortTask) {
        DiscomfortTask mDiscomfortTask = PatientUserManager.getInstance().getLatestRecord().getDiscomfortTask();
        if(mDiscomfortTask == null){
            PatientUserManager.getInstance().getLatestRecord().setDiscomfortTask(discomfortTask);
        }else{
            String beforeDate = mDiscomfortTask.getMeasureTime();
            String afterDate = discomfortTask.getMeasureTime();
            if(TimeManager.equalsBefore(beforeDate,afterDate)){
                PatientUserManager.getInstance().getLatestRecord().setDiscomfortTask(discomfortTask);
            }
        }

    }


    /**
     * 登录成功后调用
     */
    public static void afterLoginSucceed(JsonObject result, Context context){
        Gson gson = new Gson();

        //更新用户信息
        JsonObject jsonInfo = result.get("loginUserInfo").getAsJsonObject();
        Log.i("wzy0718",jsonInfo+"");
        LoginUserInfo tempInfo = gson.fromJson(jsonInfo, new TypeToken<LoginUserInfo>() {
        }.getType());
        PatientUserManager.getInstance().updateLoginUserInfo(tempInfo);

        calculatePefStandard();//计算pef标准值



    }

    /**
     * 发送通知
     */
    public static void sendNotification(Context context, int notificationId, int iconId, String title, String content, boolean isSound){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setTicker("COPD管家");
        notificationBuilder.setSmallIcon(iconId);
        Notification notification = notificationBuilder.setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(content).build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;//点击自动消失，不跳转至app
        if(isSound){
            notification.defaults = Notification.DEFAULT_SOUND; //开启声音
        }
        notificationManager.notify(notificationId, notification);

    }

    /**
     * 取消通知
     */
    public static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }


    /**
     * 隐藏系统键盘
     */
    public static void hideSoftInputMethod(EditText ed, Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取ip地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * 获取外网IP地址
     *
     * @return
     */
    public static void getNetIp() {
        IP_URL = "0.0.0.0";
        String url = Utils.IP_URL;
        Log.d("ip", url);
        Ion.with(MyApplication.getContext()).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                Log.d("ip", e+" "+result+"");
                if (e != null) {
                    Log.d("ip", "load weather error");
                }else {
                    try {
                        int start = result.indexOf("{");
                        int end = result.indexOf("}");
                        String json = result.substring(start, end + 1);
                        JSONObject jsonObject = new JSONObject(json);
                        IP_URL = jsonObject.optString("cip");
                        Log.d("ip", IP_URL);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        Log.d("ipreturn", IP_URL);
    }

}