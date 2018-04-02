package com.zju.biomedit.copdmanager.model;


/**
 * Created by wangzheyu on 2017/7/17.
 */

public class DiscomfortTask {
    private long id;
    private int inflammation;
    private int lung;
    private int heart;
    private int breath;
    private String measureTime;
    private String memo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getInflammation() {
        return inflammation;
    }

    public void setInflammation(int inflammation) {
        this.inflammation = inflammation;
    }

    public int getLung() {
        return lung;
    }

    public void setLung(int lung) {
        this.lung = lung;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
