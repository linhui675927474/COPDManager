package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/17.
 */

public class PatientUserManager {
    /**
     * 私有构造方法，无法被外部类实例化
     */
    private PatientUserManager(){}

    /**
     * 单例模式，全局唯一实例
     */
    private static PatientUserManager instance = new PatientUserManager();
    public static synchronized  PatientUserManager getInstance(){
        return instance;
    }


    /**
     * 非空标识
     */
    private boolean emptyFlag = true;
    public boolean isEmpty(){
        return emptyFlag;
    }


    /**
     * 重置
     */
    public void resetAll(){
        loginUserInfo = null;
        latestRecord = null;
        emptyFlag = true;
    }

    /**
     * 当前用户信息
     */
    private LoginUserInfo loginUserInfo;
    public LoginUserInfo getLoginUserInfo(){
        return loginUserInfo;
    }

    public void updateLoginUserInfo(LoginUserInfo info) {
        loginUserInfo = info;
        emptyFlag = false;
    }

    /**
     * 最新记录
     */
    private LatestRecord latestRecord;
    public LatestRecord getLatestRecord(){
        LatestRecord record = new LatestRecord();
        if (latestRecord != null){
            return latestRecord;
        }else{
            return record;
        }

    }

    public void updateLatestRecord(LatestRecord record) {
        latestRecord = record;
        emptyFlag = false;
    }

    /**
     * 常用信息
     */
    public String getPatientId(){
        String id = "noid";
        if (loginUserInfo != null){
            return loginUserInfo.getPatientId();
        }else {
            return id;
        }

    }


    /**
     * 个人标准PEF值
     */
    private int pefStandard;
    public int getPefStandard() {
        return pefStandard;
    }

    public void setPefStandard(int pefStandard) {
        this.pefStandard = pefStandard;
    }

    /**
     * 个人综合评价
     */
    private int evaluationValue;

    public void setEvaluationValue(int evaluationValue) {
        this.evaluationValue = evaluationValue;
    }
    public int getEvaluationValue() {
        return evaluationValue;
    }

}
