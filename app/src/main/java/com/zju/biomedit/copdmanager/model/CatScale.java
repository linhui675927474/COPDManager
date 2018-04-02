package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzheyu on 2017/7/5.
 */

public class CatScale extends RealmObject {
    @PrimaryKey
    private int problemId;
    private int score;
    private String zeroPointText;
    private String fivePointText;

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getZeroPointText() {
        return zeroPointText;
    }

    public void setZeroPointText(String zeroPointText) {
        this.zeroPointText = zeroPointText;
    }

    public String getFivePointText() {
        return fivePointText;
    }

    public void setFivePointText(String fivePointText) {
        this.fivePointText = fivePointText;
    }





}
