package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/21.
 */
public class Message {
    private int knoId;
    private String knoTime;
    private String knoName;
    private String knoLink;

    public int getKnoId() {
        return knoId;
    }

    public void setKnoId(int knoId) {
        this.knoId = knoId;
    }

    public String getKnoTime() {
        return knoTime;
    }

    public void setKnoTime(String knoTime) {
        this.knoTime = knoTime;
    }

    public String getKnoName() {
        return knoName;
    }

    public void setKnoName(String knoName) {
        this.knoName = knoName;
    }

    public String getKnoLink() {
        return knoLink;
    }

    public void setKnoLink(String knoLink) {
        this.knoLink = knoLink;
    }
}
