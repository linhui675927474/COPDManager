package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzheyu on 2017/7/23.
 */

public class Reminder extends RealmObject {
    @PrimaryKey
    private int type;
    private String time;
    private int freq;//0: 每天 1: 每周 2: 每月
    private boolean useSound;
    private boolean isDefault;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public boolean isUseSound() {
        return useSound;
    }

    public void setUseSound(boolean useSound) {
        this.useSound = useSound;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
