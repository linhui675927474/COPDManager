package com.zju.biomedit.copdmanager.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzheyu on 2017/7/10.
 */

public class AnxietyScale extends RealmObject {
    @PrimaryKey
    private int problemId;
    private int score;

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
}
