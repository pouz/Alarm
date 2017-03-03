package com.pouz.alarm.utils;

/**
 * Created by PouZ on 2017-02-20.
 */

public class Utils
{
    public static int timeToInt(int hour, int min)
    {
        return hour * 60 + min;
    }

    public static String intToTime(int integer)
    {
        return integer / 60 + ":" + integer % 60;
    }
}
