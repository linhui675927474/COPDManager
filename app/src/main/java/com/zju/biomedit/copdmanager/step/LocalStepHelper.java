package com.zju.biomedit.copdmanager.step;

import android.content.Context;
import android.content.SharedPreferences;

import com.zju.biomedit.copdmanager.support.Utils;


/**
 * Created by ybj on 2016/3/17.
 */
public class LocalStepHelper {

    // 清除之前保存计步信息的SharedPreferences
    public static void clearOutdatedStepSharedPreferences(Context context){
        SharedPreferences sp_step = context.getSharedPreferences("step", Context.MODE_PRIVATE);
        SharedPreferences sp_time = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences sp_date =  context.getSharedPreferences("date", Context.MODE_PRIVATE);
        if(!sp_step.getAll().isEmpty()){
            sp_step.edit().clear().commit();
        }

        if(!sp_time.getAll().isEmpty()){
            sp_time.edit().clear().commit();
        }
        if(!sp_date.getAll().isEmpty()){
            sp_date.edit().clear().commit();
        }
    }

    /**
     * 重置本地计步信息
     */
    public static void resetLocalStepInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_TOTAL_STEP, 0);
        editor.putString(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_SAVED_DATE, "");
        //editor.putBoolean(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_PEDOMETER_ENABLE, false);
        editor.commit();
    }

    /**
     * 获取本地计步器开关的状态
     */
    public static boolean getLocalPedometerEnable(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        boolean enable = sharedPreferences.getBoolean(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_PEDOMETER_ENABLE, true);
        return enable;
    }

    /**
     * 获取当前本地的计步数
     */
    public static int getLocalStepCount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        int totalStep = sharedPreferences.getInt(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_TOTAL_STEP, 0);
        return totalStep;
    }

    /**
     * 获取当前本地的计步日期
     */
    public static String getLocalStepDate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        String savedDate = sharedPreferences.getString(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_SAVED_DATE, "");
        return savedDate;
    }

    /**
     * 更新本地计步器开关的状态
     */
    public static void updatePedometerEnable(Context context, boolean enable){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_PEDOMETER_ENABLE, enable);
        editor.commit();
    }

    /**
     * 更新当前本地的计步数
     */
    public static void updateLocalStepCount(Context context, int step){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_TOTAL_STEP, step);
        editor.commit();
    }

    /**
     * 更新当前本地的计步日期
     */
    public static void updateLocalStepDate(Context context, String date){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCES_SAVE_STEP_INFO, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Utils.PREFERENCES_SAVE_STEP_INFO_KEY_SAVED_DATE, date);
        editor.commit();
    }

}
