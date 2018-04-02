package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/10/25.
 */

public class DoctorAppointment {
    private int num;//顺序号
    private String time;//时间段
    private Boolean available; //是否可以预约

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
