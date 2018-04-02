package com.zju.biomedit.copdmanager.support;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeManager {
    private static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    private static Calendar c;
    private static final int invalidAge = -1;//非法的年龄，用于处理异常。

    public TimeManager() {
        // TODO Auto-generated constructor stub
    }

    public static boolean compareTimeWithCunrrent(String time) {
        boolean isTime = false;
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
        int hour, minute;
        if (time.length() == 5) {
            hour = Integer.parseInt(time.substring(0, 2));
            minute = Integer.parseInt(time.substring(3, 5));
        } else {
            hour = Integer.parseInt(time.substring(0, 1));
            minute = Integer.parseInt(time.substring(2, 4));
        }

        if (currentHour > hour) {
            isTime = true;
        } else if (currentHour == hour) {
            if (currentMinute >= minute) {
                isTime = true;
            }
        }

        return isTime;
    }

    public static int getStrTimeInMinute(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        return (hour * 60 + minute);
    }

    public static String turnValueToStrTime(int value) {
        String hour, minute;
        int a = value / 60;
        int b = value % 60;
        if (b < 10) {
            minute = ":0" + b + ":00";
        } else {
            minute = ":" + b + ":00";
        }
        if (a < 10) {
            hour = "0" + a;
        } else {
            hour = a + "";
        }

        return (hour + minute);
    }

    public static String getStrDate() {
        c = Calendar.getInstance();
        String strDate = DateTimeFormat.format(c.getTime());
        strDate = strDate.substring(0, 10);
        return strDate;
    }

    public static String getStrDate(Calendar time) {
        if (time == null)
            return "";
        String strDate = DateTimeFormat.format(time.getTime());
        strDate = strDate.substring(0, 10);
        return strDate;
    }

    public static String getStrTime() {
        c = Calendar.getInstance();
        String strDate = DateTimeFormat.format(c.getTime());
        strDate = strDate.substring(11, 16);
        return strDate;
    }

    public static String getStrTime(Calendar time) {
        if (time == null)
            return "";
        String strDate = DateTimeFormat.format(time.getTime());
        strDate = strDate.substring(11, 16);
        return strDate;
    }

    public static String getStrDateTime() {
        c = Calendar.getInstance();
        String strDate = DateTimeFormat.format(c.getTime());
        return strDate;
    }

    public static String getStrDateTime(Calendar time) {
        if (time == null)
            return "";
        String strDate = DateTimeFormat.format(time.getTime());
        return strDate;
    }

    public static Calendar parseStrDateTime(String strDate) {
        if (strDate == null)
            return null;
        Date date = null;
        Calendar time = Calendar.getInstance();
        try {
            date = DateTimeFormat.parse(strDate);
            time.setTime(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    public static Calendar parseStrDate(String strDate) {
        if (strDate == null)
            return null;
        Date date = null;
        Calendar time = Calendar.getInstance();
        try {
            date = DateTimeFormat2.parse(strDate);
            time.setTime(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    public static boolean isThisWeek(String time)
    {
        c = Calendar.getInstance();
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        Calendar calendar = parseStrDateTime(time);
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        return (paramWeek==currentWeek);
    }

    /**
     * 获取所给日期是星期几
     */
    public static int getDayOfWeek(String time)
    {
        Calendar calendar = parseStrDateTime(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    public static int getMonthInterval(Calendar start, Calendar end) {
        long interval = end.getTimeInMillis() - start.getTimeInMillis();
        int month = (int) (interval / 30 / 24 / 3600 / 1000);
        return month;
    }

    public static String getStrDayHourMin(long min) {
        // TODO Auto-generated method stub
        String strTime = "";
        int hour = 0, day = 0;
        if (min > 60) {
            hour = (int) (min / 60);
            min = min % 60;
            if (hour > 24) {
                day = hour / 24;
                hour = hour % 24;
            }
        }

        if (day > 0)
            strTime += Integer.toString(day) + "天";
        if (hour > 0)
            strTime += Integer.toString(hour) + "小时";
        if (min > 0)
            strTime += Integer.toString((int) min) + "分钟";
        return strTime;
    }


    public static int getMonthNow() {
        Date today = new Date();
        int month = today.getMonth() + 1;
        return month;
    }

    public static int getCurrentYear() {
        Date today = new Date();
        int year = today.getYear() + 1900;
        return year;
    }

    public static int getCurrentDate() {
        Date today = new Date();
        int date = today.getDate();
        return date;
    }

    public static String getDateFormat(String date) {
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        String dateFormatNew = month + "/" + day;
        return dateFormatNew;
    }

    public static String[] getWeekDate() {
        String[] weekDate = new String[7];
        return weekDate;
    }

    public static int getWeekDay() {
        Date today = new Date();
        int weekDay = today.getDay();
        return weekDay;
    }

    /**
     * 获取指定日期往前推n天的日期
     */
    public static String getBeforeDate(String assignDate, int beforeDay) {
        int year = Integer.parseInt(assignDate.substring(0, 4));
        int month = Integer.parseInt(assignDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(assignDate.substring(8, 10));

        Calendar calender = Calendar.getInstance();
        calender.set(year, month, day);
        int tempDay = calender.get(Calendar.DAY_OF_MONTH) - beforeDay;
        calender.set(Calendar.DAY_OF_MONTH, tempDay);

        String beforeDate = getStrDate(calender);
        Log.d("ybj", beforeDate);
        return beforeDate;
    }

    /**
     * 两个日期先后的比较
     */
    public static boolean equalsBefore(String beforeDate, String afterDate) {
        int bYear = Integer.parseInt(beforeDate.substring(0, 4));
        int bMonth = Integer.parseInt(beforeDate.substring(5, 7));
        int bDay = Integer.parseInt(beforeDate.substring(8, 10));
        int bHour = Integer.parseInt(beforeDate.substring(11, 13));
        int bMinute = Integer.parseInt(beforeDate.substring(14, 16));
        int bSecond = Integer.parseInt(beforeDate.substring(17, 19));
        int aYear = Integer.parseInt(afterDate.substring(0, 4));
        int aMonth = Integer.parseInt(afterDate.substring(5, 7));
        int aDay = Integer.parseInt(afterDate.substring(8, 10));
        int aHour = Integer.parseInt(afterDate.substring(11, 13));
        int aMinute = Integer.parseInt(afterDate.substring(14, 16));
        int aSecond = Integer.parseInt(afterDate.substring(17, 19));

        if (bYear != aYear)
            return bYear < aYear;
        else if (bMonth != aMonth)
            return bMonth < aMonth;
        else if (bDay != aDay)
            return bDay < aDay;
        else if (bHour != aHour)
            return bHour < aHour;
        else if (bMinute != aMinute)
            return bMinute < aMinute;
        else if (bSecond != aSecond)
            return bSecond < aSecond;

        return false;
    }


    /**
     * 获取两个日期之间的间隔天数
     */
    public static int getGapCount(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = new Date();
        Date eDate = new Date();
        try {
            sDate = sdf.parse(startDate);
            eDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 1;
        }
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(sDate);
        sCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sCalendar.set(Calendar.MINUTE, 0);
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);

        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setTime(eDate);
        eCalendar.set(Calendar.HOUR_OF_DAY, 0);
        eCalendar.set(Calendar.MINUTE, 0);
        eCalendar.set(Calendar.SECOND, 0);
        eCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((eCalendar.getTime().getTime() - sCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取两个时间的时间间隔（毫秒级）
     */
    public static long getDateTimeSpanMS(String sDateTime, String eDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sDate = new Date();
        Date eDate = new Date();
        try {
            sDate = sdf.parse(sDateTime);
            eDate = sdf.parse(eDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        long diff = eDate.getTime() - sDate.getTime();//这样得到的差值是毫秒级别
        return diff;
    }


    /**
     * 获取某个月份的天数
     */
    public static int getDayCountFromMonth(int year, int month) {
        int dayCount = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayCount = 30;
                break;
            case 2:
                if(year % 4 != 0)
                    dayCount = 28;
                else if(year % 100 != 0)
                    dayCount = 29;
                else if(year % 400 != 0)
                    dayCount = 28;
                else
                    dayCount = 29;
                break;
        }
        return dayCount;

    }

    /**
     *根据生日计算年龄
     * @param dateStr 这样格式的生日 1990/6/14
     */

    public static int getAgeByDateString(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return getAgeByDate(birthday);
        } catch (ParseException e) {
            return -1;
        }
    }


    public static int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();

        //calendar.before()有的点bug
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return invalidAge;
        }


        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);


        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;


        if (monthNow <= monthBirthday && monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday || monthNow < monthBirthday) {
            age--;
        }

        return age;
    }

    // 获得当天0点时间
    public static Date getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();


    }
    // 获得昨天0点时间
    public static Date getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
        return cal.getTime();
    }
    // 获得当天近7天时间
    public static Date getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得距离当前日期最近的周五时间
    public static String getLatestFriday() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);//预约当日号必须是收费状态
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Calendar calNow = Calendar.getInstance();
        String now = DateTimeFormat.format(calNow.getTime());
        String friday = DateTimeFormat.format(cal.getTime());
        if(equalsBefore(now,friday)){
            return friday.substring(0, 10);
        }else{
            cal.add(Calendar.DATE, 7);
            return DateTimeFormat.format(cal.getTime()).substring(0,10);
        }

    }

    // 获得本周日0点时间A(以周日为一周的开始）
    public static String getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return DateTimeFormat.format(cal.getTime());
    }

    //  获得指定日期所在星期的周日0点时间
    public static String getGeneralTimesWeekmorning(String date) {
        Calendar cal = parseStrDate(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return DateTimeFormat.format(cal.getTime());
    }

    public static Date getTimesWeekmorningDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    // 获得本周六24点时间
    public static String getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorningDate());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return DateTimeFormat.format(cal.getTime());
    }

    // 获得指定日期所在星期的周六24点时间
    public static String getGeneralTimesWeeknight(String date) {
        Calendar cal = parseStrDate(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return DateTimeFormat.format(cal.getTime());
    }

    // 获得本月第一天0点时间
    public static String getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return DateTimeFormat.format(cal.getTime());

    }

    // 获得本月最后一天24点时间
    public static String getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return DateTimeFormat.format(cal.getTime());

    }

    // 将日期转换为指定格式
    public static String tansferDataString(String date, int mode) {
        int bYear = Integer.parseInt(date.substring(0, 4));
        int bMonth = Integer.parseInt(date.substring(5, 7));
        int bDay = Integer.parseInt(date.substring(8, 10));
        String transferedDate1 = bYear+"年"+bMonth+"月"+bDay+"日";
        String transferedDate2 = bMonth+"月"+bDay+"日";
        if(mode == 0){
            return transferedDate1;//mode=0返回包含年份的字符串
        }else{
            return transferedDate2;
        }

    }


    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }


    public static Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }

    public static Date getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    public static Date getLastYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    /**
     * 判断今日是否为周一
     * @return
     */
    public static boolean isMonday() {
        return (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
    }

    /**
     * 判断今日是否为月初
     * @return
     */
    public static boolean isMonthFirst() {
        return ((c.get(Calendar.DAY_OF_MONTH)) == c.getActualMinimum(Calendar.DAY_OF_MONTH));
    }


}
