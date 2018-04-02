package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/17.
 */

public class LatestRecord {
    private ScaleTask catScaleTask;
    private ScaleTask phqScaleTask;
    private ScaleTask gadScaleTask;
    private MedicineTask medicineTask;
    private DiscomfortTask discomfortTask;
    private PefTask pefTask;
    private SixMWDTask sixMWDTask;

    public SixMWDTask getSixMWDTask(){ return sixMWDTask; }

    public void setSixMWDTask(SixMWDTask sixMWDTask){ this.sixMWDTask=sixMWDTask; }

    public ScaleTask getCatScaleTask() {
        return catScaleTask;
    }

    public void setCatScaleTask(ScaleTask catScaleTask) {
        this.catScaleTask = catScaleTask;
    }

    public ScaleTask getPhqScaleTask() {
        return phqScaleTask;
    }

    public void setPhqScaleTask(ScaleTask phqScaleTask) {
        this.phqScaleTask = phqScaleTask;
    }

    public ScaleTask getGadScaleTask() {
        return gadScaleTask;
    }

    public void setGadScaleTask(ScaleTask gadScaleTask) {
        this.gadScaleTask = gadScaleTask;
    }

    public MedicineTask getMedicineTask() {
        return medicineTask;
    }

    public void setMedicineTask(MedicineTask medicineTask) {
        this.medicineTask = medicineTask;
    }

    public DiscomfortTask getDiscomfortTask() {
        return discomfortTask;
    }

    public void setDiscomfortTask(DiscomfortTask discomfortTask) {
        this.discomfortTask = discomfortTask;
    }

    public PefTask getPefTask() {
        return pefTask;
    }

    public void setPefTask(PefTask pefTask) {
        this.pefTask = pefTask;
    }
}
