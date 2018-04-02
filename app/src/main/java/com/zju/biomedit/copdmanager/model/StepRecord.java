package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/17.
 */

public class StepRecord {
    private long id;
    private int value = -1;
    private String measureTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }
}
