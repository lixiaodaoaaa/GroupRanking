package com.daolion.ranking;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/21
       Time     :  09:22
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */


import org.junit.Test;

public class TestEngnieer {

    @Test
    public void testDate() {


        RedisEngnie engnie = RedisEngnie.getInstance();


        engnie.addSomeDayScore(engnie.getYesterdayRankTableName(), RankUserLoader.XL, 5767);

        engnie.lookSomeDayRanking(engnie.getYesterdayRankTableName());



    }
}
