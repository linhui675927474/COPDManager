package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/17.
 */

public class LoginUserInfo {
    private String patientID;
    private String identityCardNumber;
    private String patientName;
    private String phoneNumber;
    private String sexCode;
    private String birthDate;
    private String registDate;
    private float newestHeight;
    private float newestWeight;

    public String getPatientId() {
        return patientID;
    }

    public void setPatientId(String patientID) {
        this.patientID = patientID;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public float getNewestHeight() {
        return newestHeight;
    }

    public void setNewestHeight(float newestHeight) {
        this.newestHeight = newestHeight;
    }

    public float getNewestWeight() {
        return newestWeight;
    }

    public void setNewestWeight(float newestWeight) {
        this.newestWeight = newestWeight;
    }
}
