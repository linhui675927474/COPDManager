package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/5.
 */

public class DrugData{
    private int drugPicId;
    private String drugName;

    public String getDrugDis() {
        return drugDis;
    }

    public void setDrugDis(String drugDis) {
        this.drugDis = drugDis;
    }

    private String drugDis;

    public int getDrugPicId() {
        return drugPicId;
    }

    public void setDrugPicId(int drugPicId) {
        this.drugPicId = drugPicId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
