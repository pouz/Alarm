package com.pouz.alarm.service;

/**
 * Created by PouZ on 2017-02-28.
 */

public class AlarmAuth
{
    private boolean mIsAlarmActive = false;
    private String  mAlarmAuthor = "";

    private static AlarmAuth mAlarmAuth;

    public static AlarmAuth getInstance() {
        if(mAlarmAuth == null)
        {
            mAlarmAuth = new AlarmAuth();
        }
        return mAlarmAuth;
    }

    public static void delInstance()
    {
        mAlarmAuth = null;
    }

    public String getAlarmAuthor()
    {
        return mAlarmAuthor;
    }

    public void setAlarmAuthor(String mAlarmAuthor)
    {
        this.mAlarmAuthor = mAlarmAuthor;
    }

    public boolean isIsAlarmActive()
    {
        return mIsAlarmActive;
    }

    public void setIsAlarmActive(boolean mIsAlarmActive)
    {
        this.mIsAlarmActive = mIsAlarmActive;
    }
}
