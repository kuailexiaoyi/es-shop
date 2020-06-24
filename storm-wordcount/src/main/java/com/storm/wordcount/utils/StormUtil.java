package com.storm.wordcount.utils;

/**
 * @Desc:
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class StormUtil {

    /**
     * @Desc: 睡眠指定时间
     * @Param times
     * @Return void
     * @Date: 2020/6/6
     */
    public static void sleep(int times) {
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
