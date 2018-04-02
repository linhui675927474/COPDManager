package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;

/**
 * Created by wangzheyu on 2017/7/5.
 */

public class PefRecord extends RealmObject {
    private String measureTime;
    private int pefValue;

    public int getPefValue() {
        return pefValue;
    }

    public void setPefValue(int pefValue) {
        this.pefValue = pefValue;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }


}
