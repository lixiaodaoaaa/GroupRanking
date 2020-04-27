package com.daolion.ranking.utils;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/27
       Time     :  10:23
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekDayUtils {

    public static final String RANK_HEADER = "rank_";
    public static final String WEEK = "week_";

    public static final SimpleDateFormat rankDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String getLastMondayTableName() {
        return new StringBuilder().append(RANK_HEADER).append(getLastMonday()).toString();
    }

    public static String getLastFridayTableName() {
        return new StringBuilder().append(RANK_HEADER).append(getLastFriday()).toString();
    }


    public static String getLastWeekTableName() {
        return new StringBuilder().append(RANK_HEADER).append(WEEK).append(getLastMonday()).toString();
    }


    public static List<String> getLastWeekDayTableNames() {

        int totalWeekDays = 5;

        List<String> result = new ArrayList<>();
        String lastMondayTableName = getLastMondayTableName();
        result.add(lastMondayTableName);

        String someDay = getLastMonday();

        while (result.size() < totalWeekDays) {
            someDay = getNextDay(someDay);
            result.add(new StringBuilder().append(RANK_HEADER).append(someDay).toString());
        }
        return result;
    }


    public static String getTodayRankTableName() {
        String rankHeader = "rank_";
        String todayDate = DateFormatterUtils.formatterDateToRankDate(new Date());
        return new StringBuilder().append(rankHeader).append(todayDate).toString();
    }


    public static String getYesterdayRankTableName() {
        String rankHeader = "rank_";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String yesterdayDate = DateFormatterUtils.formatterDateToRankDate(calendar.getTime());
        return new StringBuilder().append(rankHeader).append(yesterdayDate).toString();
    }


    /**
     * 获取上周五时间
     */
    public static String getLastFriday() {
        //作用防止周日得到本周日期
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 7 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 9);
        return getFirstDayOfWeek(calendar.getTime(), 6);//这是从上周日开始数的到本周五为6
    }


    /**
     * 获取上周一时间
     */
    public static String getLastMonday() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 1 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 7);
        return getFirstDayOfWeek(calendar.getTime(), 2);
    }


    public static String getNextDay(String someDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date someDayDate = rankDateFormatter.parse(someDay);
            calendar.setTime(someDayDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateFormatterUtils.formatterDateToRankDate(calendar.getTime());
    }

    /**
     * 得到某一天的该星期的第一日 00:00:00
     *
     * @param date
     * @param firstDayOfWeek 一个星期的第一天为星期几
     * @return
     */
    public static String getFirstDayOfWeek(Date date, int firstDayOfWeek) {

        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);
        cal.setFirstDayOfWeek(firstDayOfWeek);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY, 0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        return rankDateFormatter.format(cal.getTime());
    }

}
