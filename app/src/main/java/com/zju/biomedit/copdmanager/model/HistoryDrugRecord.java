package com.zju.biomedit.copdmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ybj on 2016/11/3.
 */
public class HistoryDrugRecord {

    /**
     * 私有构造方法，无法被外部类实例化
     */
    private HistoryDrugRecord(){}

    /**
     * 单例模式，全局唯一实例
     */
    private static HistoryDrugRecord instance = new HistoryDrugRecord();
    public static synchronized  HistoryDrugRecord getInstance(){
        return instance;
    }

    /**
     * 清空
     */
    public void clear(){
        instance = new HistoryDrugRecord();
    }

    /**
     * 是否没有数据
     */
    private boolean isEmpty = true;

    /**
     * 已加载的历史服药记录
     */
    private HashMap<String, List<MedicineTask>>  historyDrugRecordMap = new HashMap<>();


    public boolean getIsEmpty(){
        return isEmpty;
    }


    public HashMap<String, List<MedicineTask>> getHistoryDrugRecordMap() {
        return historyDrugRecordMap;
    }

    /**
     * 存入历史服药记录
     */
    public void updateDrugRecordMap(List<MedicineTask> drugRecordList, String yearMonth) {
        historyDrugRecordMap.put(yearMonth, drugRecordList);//如果已存在则进行覆盖
        isEmpty = false;
    }



    /**
     * 获取日服药记录
     */
    public List<MedicineTask> getDayDrugRecords(String dateFormat) {
        List<MedicineTask> dayDrugRecords = new ArrayList<>();
        if(!historyDrugRecordMap.containsKey(dateFormat.substring(0, 7)))
            return dayDrugRecords;//一般不会出现这种情况（因为list为空时也会存储）

        String yearMonth = dateFormat.substring(0, 7);
        List<MedicineTask> drugList = historyDrugRecordMap.get(yearMonth);
        for(MedicineTask MedicineTask: drugList) {
            if (dateFormat.equals(MedicineTask.getMeasureTime().substring(0, 10)))
                dayDrugRecords.add(MedicineTask);
        }
        return dayDrugRecords;
    }
}
