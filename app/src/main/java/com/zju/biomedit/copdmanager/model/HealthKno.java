package com.zju.biomedit.copdmanager.model;

/**
 * Created by wangzheyu on 2017/7/21.
 */
public class HealthKno {
    private int knoId;
    private String knoName;
    private String knoDis;
    private String knoTime;
    private String knoLink;
    private Boolean knoIfRead;
    private Boolean knoIfFavorite; //未收藏为null

    public int getKnoId() {
        return knoId;
    }

    public void setKnoId(int knoId) {
        this.knoId = knoId;
    }

    public String getKnoName() {
        return knoName;
    }

    public void setKnoName(String knoName) {
        this.knoName = knoName;
    }

    public String getKnoDis() {
        return knoDis;
    }

    public void setKnoDis(String knoDis) {
        this.knoDis = knoDis;
    }

    public String getKnoTime() {
        return knoTime;
    }

    public void setKnoTime(String knoTime) {
        this.knoTime = knoTime;
    }

    public String getKnoLink() {
        return knoLink;
    }

    public void setKnoLink(String knoLink) {
        this.knoLink = knoLink;
    }

    public Boolean getKnoIfRead() {
        return knoIfRead;
    }

    public void setKnoIfRead(Boolean knoIfRead) {
        this.knoIfRead = knoIfRead;
    }

    public Boolean getKnoIfFavorite() {
        return knoIfFavorite;
    }

    public void setKnoIfFavorite(Boolean knoIfFavorite) {
        this.knoIfFavorite = knoIfFavorite;
    }
}
