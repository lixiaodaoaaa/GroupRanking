package com.daolion.ranking.utils;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/27
       Time     :  10:26
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

import com.daolion.ranking.RedisEngnie;

import org.junit.Test;

public class DateTest {


    @Test
    public void testDate() {

        RedisEngnie engnie = RedisEngnie.getInstance();




        boolean result = WeekDayUtils.isTodayIsMonday();
        System.out.println(" result is "+result);
    }
}
