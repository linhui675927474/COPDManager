package com.zju.biomedit.copdmanager.model;

import java.util.ArrayList;

/**
 * Created by wangzheyu on 2017/10/25.
 */

public class Doctor {
    private int id;
    private String name;
    private String date; //出诊日期
    private String timeflag;
    private ArrayList<DoctorAppointment> doctorAppointmentArrayList;//号源分时段信息
    private String reginfoid;//号源id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeflag() {
        return timeflag;
    }

    public void setTimeflag(String timeflag) {
        this.timeflag = timeflag;
    }

    public ArrayList<DoctorAppointment> getDoctorAppointmentArrayList() {
        return doctorAppointmentArrayList;
    }

    public void setDoctorAppointmentArrayList(ArrayList<DoctorAppointment> doctorAppointmentArrayList) {
        this.doctorAppointmentArrayList = doctorAppointmentArrayList;
    }

    public String getReginfoid() {
        return reginfoid;
    }

    public void setReginfoid(String reginfoid) {
        this.reginfoid = reginfoid;
    }
}
