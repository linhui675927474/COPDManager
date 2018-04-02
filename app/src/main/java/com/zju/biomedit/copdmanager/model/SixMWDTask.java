package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by linhui on 2018/3/20.
 */

public class SixMWDTask extends RealmObject {
    private long id;
    private double distance = -1;
    private String measureTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

}
