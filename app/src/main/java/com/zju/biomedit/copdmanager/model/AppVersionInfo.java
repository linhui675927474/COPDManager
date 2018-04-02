package com.zju.biomedit.copdmanager.model;

/**
 * Created by ybj on 2015/12/14.
 */
public class AppVersionInfo {

    // 版本号
    private int versionCode;
    // 版本名
    private String versionName;
    // 更新日期
    private String updateDate;
    // 更新内容
    private String updateContent;
    // 是否需要强制更新
    private int isForced;

    public int getVersionCode() { return versionCode; }
    public void setVersionCode(int versionCode) { this.versionCode = versionCode; }

    public String getVersionName() { return versionName; }
    public void setVersionName(String versionName) { this.versionName = versionName; }

    public String getUpdateDate() { return updateDate; }
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }

    public String getUpdateContent() { return updateContent; }
    public void setUpdateContent(String updateContent) { this.updateContent = updateContent; }

    public boolean getForced(){ return isForced == 1; }
    public void setForced(boolean isForced) { this.isForced = isForced ? 1 : 0; }
}
