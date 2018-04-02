package com.zju.biomedit.copdmanager.presenter;

import com.zju.biomedit.copdmanager.activity.MsgContentActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangzheyu on 2018/2/1.
 */

public class TimePresenter {
    private MsgContentActivity act;

    public TimePresenter(MsgContentActivity act) {
        this.act = act;
    }

    /**
     * 计时
     */
    private TimerTask timerTask;
    private Timer timer = new Timer();
    private int cntIng = 0;

    //从0开始记时
    public void startTimerFrom0() {
        timerTask = new TimerTask() {
            int cntStart = 0;

            @Override
            public void run() {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cntStart++;
                        //String time = getStringTime(cntStart++);
                        cntIng = cntStart;
                        act.getTime(cntIng);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1);//每隔一秒执行一次task中的任务。
    }

    //暂停后开始记时
    public void startTimerFromPause() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String time = getStringTime(cntIng++);
                        //act.showTime(time);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    //停止记时
    public void stopTimer() {
        if (!timerTask.cancel()) {
            timerTask.cancel();
            timer.cancel();
        }
    }

    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }
}
