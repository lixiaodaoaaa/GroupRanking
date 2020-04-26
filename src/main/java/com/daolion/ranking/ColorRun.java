package com.daolion.ranking;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/20
       Time     :  10:35
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

public class ColorRun {

    public static void main(String[] args) {

        System.out.print(Color.RED);
        System.out.println("Black_Bold");
        System.out.print(Color.RESET);

        System.out.print(Color.YELLOW);
        System.out.print(Color.BLUE_BACKGROUND);
        System.out.println("YELLOW & BLUE");
        System.out.print(Color.RESET);

        System.out.print(Color.YELLOW);
        System.out.println("YELLOW");
        System.out.print(Color.RESET);
    }
}
