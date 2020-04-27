package com.daolion.ranking;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/19
       Time     :  17:32
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

import com.daolion.ranking.utils.WeekDayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class RedisEngnie {

    public static String KEY_ALL_RANKING = "ALL_RANKING";

    public static final String IS_ADDED_TO_ALL_RANKING_FLAG = "_IS_ADDED_TO_ALL_RANKING_FLAG";

    public static final String YES = "YES";
    public static final String NO = "NO";

    private Jedis jedis;

    private static RedisEngnie instance;

    private RedisEngnie() {
        jedis = new Jedis("127.0.0.1", 6379);
    }


    public static RedisEngnie getInstance() {
        if (instance == null) {
            instance = new RedisEngnie();
        }
        return instance;
    }


    public void setAllRankUserScore(String rankerUser, double score) {
        jedis.zadd(KEY_ALL_RANKING, score, rankerUser);
    }


    public void addTodayScore(String rankerUser, int score) {
        String todayTableName = WeekDayUtils.getTodayRankTableName();
        jedis.zadd(todayTableName, score, rankerUser);
    }


    public void addSomeDayScore(String someDayTableName, String rankerUser, int score) {
        jedis.zadd(someDayTableName, score, rankerUser);
    }

    public void addSomeDayRankUser(String someDayTableName, HashMap<String,Integer> userDataMap) {
        Set<String> rankUserSet = userDataMap.keySet();
        for (String userRank : rankUserSet) {
            jedis.zadd(someDayTableName, userDataMap.get(userRank), userRank);
        }
        String someDayIsAddedFlagKey = someDayTableName + IS_ADDED_TO_ALL_RANKING_FLAG;
        if (!jedis.exists(someDayIsAddedFlagKey)) {
            jedis.set(getKeyTodayIsAdded(), NO);
        }
    }

    public void addTodayRankUser(HashMap<String,Integer> userDataMap) {

        Set<String> fileLoadUsers = userDataMap.keySet();

        if (isTodayDataIsYesterdayData(userDataMap)) {
            System.out.println("has added not needed add!");
            return;
        }


        for (String userRank : fileLoadUsers) {
            addTodayScore(userRank, userDataMap.get(userRank));
        }

        if (!jedis.exists(getKeyTodayIsAdded())) {
            jedis.set(getKeyTodayIsAdded(), NO);
        }
    }

    public boolean isTodayDataIsYesterdayData(HashMap<String,Integer> userDataMap) {

        String lastTradeDateTableName = WeekDayUtils.getYesterdayRankTableName();
        if(WeekDayUtils.isTodayIsMonday()){

            lastTradeDateTableName =   WeekDayUtils.getLastFridayTableName();
        }
        if (!jedis.exists(lastTradeDateTableName)) {
            return true;
        }


        Set<String> fileLoadUsers = userDataMap.keySet();
        //判断今天的数据和昨天的数据是否相同，相同 就不要加进去 重复添加
        int sameCount = 0;
        for (String fileUserRank : fileLoadUsers) {
            int fileUserScore = userDataMap.get(fileUserRank);

            Double dbUserScoreDoubule = jedis.zscore(lastTradeDateTableName, fileUserRank);
            int userRankScoreDb = new Long(Math.round(dbUserScoreDoubule)).intValue();
            if (fileUserScore == userRankScoreDb) {
                sameCount++;
            }
            if (sameCount > 1) {
                return true;
            }
        }
        return false;
    }


    public void addTotalScore(String rankUser, int scoreValue) {
        jedis.zincrby(KEY_ALL_RANKING, scoreValue, rankUser);
    }


    public void initAllRankingUserScore() {
        jedis.zadd(KEY_ALL_RANKING, new Double(11470), RankUserLoader.LC);
        jedis.zadd(KEY_ALL_RANKING, new Double(10075), RankUserLoader.XL);
        jedis.zadd(KEY_ALL_RANKING, new Double(1055), RankUserLoader.XY);
        jedis.zadd(KEY_ALL_RANKING, new Double(2675), RankUserLoader.MM);
        jedis.zadd(KEY_ALL_RANKING, new Double(120), RankUserLoader.CHARLES);
        jedis.zadd(KEY_ALL_RANKING, new Double(-23995), RankUserLoader.DG);
    }


    //查看当日排行榜
    public void lookSomeDayRanking(String someDay) {

        System.out.println("✨✨✨✨✨✨" + someDay + "排行榜✨✨✨✨✨✨");

        if (jedis == null) {
            jedis = new Jedis("127.0.0.1", 6379);
        }
        Set<String> rankUserSets = jedis.zrevrange(someDay, 0l, -1l);
        int count = 0;
        for (String rankUser : rankUserSets) {
            count++;
            Double userScore = jedis.zscore(someDay, rankUser);
            int userRankScore = new Long(Math.round(userScore)).intValue();
            if (userRankScore < 0) {
                System.out.print(Color.BLUE);
                System.out.printf("%-1d\t\t %-8s\t\t\t\t\t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");
                System.out.print(Color.RESET);

            } else {
                System.out.print(Color.RED_BOLD_BRIGHT);
                System.out.printf("%-1d\t\t %-8s\t\t\t\t\t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");
                System.out.print(Color.RESET);

            }


        }
        System.out.println("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨");
    }


    //查看当日排行榜
    public void lookToadyRanking() {

        System.out.println("✨✨✨✨✨✨" + WeekDayUtils.getTodayRankTableName() + "排行榜✨✨✨✨✨✨");

        if (jedis == null) {
            jedis = new Jedis("127.0.0.1", 6379);
        }

        Set<String> rankUserSets = jedis.zrevrange(WeekDayUtils.getTodayRankTableName(), 0l, -1l);
        int count = 0;
        for (String rankUser : rankUserSets) {
            count++;
            Double userScore = jedis.zscore(WeekDayUtils.getTodayRankTableName(), rankUser);
            int userRankScore = new Long(Math.round(userScore)).intValue();

            if (userRankScore < 0) {
                System.out.print(Color.BLUE);
                System.out.printf("%-1d\t\t %-8s\t\t\t\t\t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");
                System.out.print(Color.RESET);

            } else {
                System.out.print(Color.RED_BOLD_BRIGHT);
                System.out.printf("%-1d\t\t %-8s\t\t\t\t\t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");
                System.out.print(Color.RESET);

            }


        }
        System.out.println("✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨✨");
    }


    /**
     * 将今日排行榜数据 插入到总排行榜总
     * <p>
     * 每天只能添加一次。如果今天添加过了 ，每次运行后不能再次添加了
     * </p>
     */
    public void addTodayDataToAllRanking(HashMap<String,Integer> userTodayData) {
        if (isTodayHasAdded()) {
            return;
        }

        if (isTodayDataIsYesterdayData(userTodayData)) {
            System.out.println("has added not needed add to allRanking!");
            return;
        }


        Set<String> rankUserSet = userTodayData.keySet();
        for (String rankUser : rankUserSet) {
            double userScore = Double.valueOf(userTodayData.get(rankUser).toString());
            jedis.zincrby(KEY_ALL_RANKING, userScore, rankUser);
        }
        jedis.set(getKeyTodayIsAdded(), YES);
    }


    /**
     * 将今日排行榜数据 插入到总排行榜总
     * <p>
     * 每天只能添加一次。如果今天添加过了 ，每次运行后不能再次添加了
     * </p>
     */
    public void addSomeDayDataToAllRanking(String someDayTableName, HashMap<String,Integer> userSomeDayData) {
        if (isTodayHasAdded()) {
            return;
        }

        Set<String> rankUserSet = userSomeDayData.keySet();
        for (String rankUser : rankUserSet) {
            double userScore = Double.valueOf(userSomeDayData.get(rankUser).toString());
            jedis.zincrby(KEY_ALL_RANKING, userScore, rankUser);
        }
        jedis.set(getKeySomedDayIsAdded(someDayTableName), YES);
    }


    public void setTodayIsNotAddToAllRanking() {
        String keyTodayIsAdded = getKeyTodayIsAdded();
        jedis.set(keyTodayIsAdded, NO);
    }


    public boolean isTodayHasAdded() {
        if (!jedis.exists(getKeyTodayIsAdded())) {
            return false;
        }

        String result = jedis.get(getKeyTodayIsAdded());
        if (result.trim().equals(YES)) {
            System.out.println(" is added  not need to add again");
            return true;
        }
        return false;
    }

    public String getKeyTodayIsAdded() {
        return WeekDayUtils.getTodayRankTableName() + IS_ADDED_TO_ALL_RANKING_FLAG;
    }


    public String getKeySomedDayIsAdded(String someDayTableName) {
        return someDayTableName + IS_ADDED_TO_ALL_RANKING_FLAG;
    }


    public void lookAllRanking() {
        if (jedis == null) {
            jedis = new Jedis("127.0.0.1", 6379);
        }
        Set<String> rankUserSets = jedis.zrevrange(KEY_ALL_RANKING, 0l, -1l);

        int count = 0;

        System.out.println("✨✨✨✨✨✨✨✨✨累计排行榜✨✨✨✨✨✨✨✨✨✨");

        for (String rankUser : rankUserSets) {
            count++;

            Double userScore = jedis.zscore(KEY_ALL_RANKING, rankUser);
            int userRankScore = new Long(Math.round(userScore)).intValue();

            if (userRankScore < 0) {
                System.out.print(Color.BLUE);

                System.out.printf("%-1d  \t %-8s \t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");

                System.out.print(Color.RESET);

            } else {
                System.out.print(Color.RED_BOLD_BRIGHT);

                System.out.printf("%-1d  \t %-8s \t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");

                System.out.print(Color.RESET);

            }
        }
    }

    public void getLastDayRankName(String someDay) {
        //rank_2020-04-21

        String date = someDay.replace("rank_", "");

        String rankNameFront = someDay.substring(0, someDay.lastIndexOf("-"));

        int lastDay = Integer.valueOf(date.substring(date.lastIndexOf("-")));
        if (lastDay > 1) {
            String lastDate = new StringBuilder().append(rankNameFront).append(lastDay).toString();
        }
    }

    public void countLastWeekRanking() {
        String lastWeekTableName = WeekDayUtils.getLastWeekTableName();
        List<String> lastWeekDayTableNames = WeekDayUtils.getLastWeekDayTableNames();
        for (String everyDataTableName : lastWeekDayTableNames) {
            Set<String> rankUserSets = jedis.zrevrange(everyDataTableName, 0l, -1l);
            for (String rankUser : rankUserSets) {
                Double userScore = jedis.zscore(everyDataTableName, rankUser);
                int userRankScore = new Long(Math.round(userScore)).intValue();
                jedis.zincrby(lastWeekTableName, userRankScore, rankUser);
            }
        }
        lookSomeTableRanking(lastWeekTableName);
    }

    public void lookSomeTableRanking(String tableName) {
        if (jedis == null) {
            jedis = new Jedis("127.0.0.1", 6379);
        }
        Set<String> rankUserSets = jedis.zrevrange(tableName, 0l, -1l);

        int count = 0;

        System.out.printf("✨✨✨✨✨✨✨✨✨%s✨✨✨✨✨✨✨✨✨✨", tableName);
        System.out.println("\n");

        for (String rankUser : rankUserSets) {
            count++;

            Double userScore = jedis.zscore(tableName, rankUser);
            int userRankScore = new Long(Math.round(userScore)).intValue();

            if (userRankScore < 0) {
                System.out.print(Color.BLUE);

                System.out.printf("%-1d  \t %-8s \t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");

                System.out.print(Color.RESET);

            } else {
                System.out.print(Color.RED_BOLD_BRIGHT);

                System.out.printf("%-1d  \t %-8s \t %d", count, rankUser, userRankScore);
                System.out.println();
                System.out.println("-------------------------------------------");

                System.out.print(Color.RESET);

            }
        }

    }


}
