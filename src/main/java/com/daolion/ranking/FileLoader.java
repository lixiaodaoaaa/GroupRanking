package com.daolion.ranking;
/*
    ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ 
       Author   :  lixiaodaoaaa
       Date     :  2020/4/19
       Time     :  23:53
    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FileLoader {


    /*
              道哥：5205
              赵良辰：4930
              小跃总：2665
              张新路：335
              明明：250
              charles:-390
       */
    public static HashMap<String,Integer> loadFileUserData() {
        File readFile = new File("rankUserScore.txt");
        if (!readFile.isFile() || !readFile.exists()) {
            System.out.println("用户文件不存在，请检查用户数据");
        }
        HashMap<String,Integer> userScoreMap = new HashMap<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(readFile), "UTF-8");
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                String[] content = line.split(":");
                if (content.length ==2) {
                    userScoreMap.put(content[0], Integer.valueOf(content[1].trim()));
                }
            }
            bufferReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userScoreMap;
    }
}
