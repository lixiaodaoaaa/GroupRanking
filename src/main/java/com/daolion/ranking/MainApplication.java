package com.daolion.ranking;

import java.util.HashMap;

/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2018/7/6
       Time     :  12:03
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */
public class MainApplication {

    public static void main(String[] args) {
        RedisEngnie redisEngnie = RedisEngnie.getInstance();

        redisEngnie.lookAllRanking();

        HashMap<String,Integer> fileUserData = FileLoader.loadFileUserData();

        redisEngnie.addTodayRankUser(fileUserData);

        redisEngnie.lookToadyRanking();

        redisEngnie.addTodayDataToAllRanking(fileUserData);

        redisEngnie.lookAllRanking();
    }
}
