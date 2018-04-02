package com.zju.biomedit.copdmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ybj on 2016/11/3.
 */
public class HistoryPefRecord {

    /**
     * 私有构造方法，无法被外部类实例化
     */
    private HistoryPefRecord(){}

    /**
     * 单例模式，全局唯一实例
     */
    private static HistoryPefRecord instance = new HistoryPefRecord();
    public static synchronized HistoryPefRecord getInstance(){
        return instance;
    }

    /**
     * 清空
     */
    public void clear(){
        instance = new HistoryPefRecord();
    }

    /**
     * 是否没有数据
     */
    private boolean isEmpty = true;

    /**
     * 已加载的历史PEF记录
     */
    private HashMap<String, List<PefTask>>  historyPefRecordMap = new HashMap<>();


    public boolean getIsEmpty(){
        return isEmpty;
    }


    public HashMap<String, List<PefTask>> gethistoryPefRecordMap() {
        return historyPefRecordMap;
    }

    /**
     * 存入历史PEF记录
     */
    public void updatePefRecordMap(List<PefTask> pefRecordList, String yearMonth) {
        historyPefRecordMap.put(yearMonth, pefRecordList);
        isEmpty = false;
    }


}
