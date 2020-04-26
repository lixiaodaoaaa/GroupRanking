package com.daolion.ranking;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/19
       Time     :  18:51
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

import java.util.ArrayList;
import java.util.List;

public class RankUserLoader {


    public static String LC = "赵良辰";
    public static String XL = "张新路";
    public static String XY = "小跃";
    public static String MM = "明明";
    public static String CHARLES = "charles";
    public static String DG = "李小道";


    public static List<String> loadAllRankUsers() {
        List<String> listRankUser = new ArrayList<>();
        listRankUser.add(LC);
        listRankUser.add(XL);
        listRankUser.add(XY);
        listRankUser.add(MM);
        listRankUser.add(CHARLES);
        listRankUser.add(DG);
        return listRankUser;
    }
}
