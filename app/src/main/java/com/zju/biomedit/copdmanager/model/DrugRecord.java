package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;

/**
 * Created by wangzheyu on 2017/7/5.
 */

public class DrugRecord extends RealmObject {
    private String useTime;
    private String drugName;

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
