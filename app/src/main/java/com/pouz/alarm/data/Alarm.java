package com.pouz.alarm.data;

import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by PouZ on 2017-02-17.
 */

public final class Alarm {
    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 4;
    public static final int THU = 8;
    public static final int FRI = 16;
    public static final int SAT = 32;
    public static final int SUN = 64;

    @NonNull
    private final int mStartTime;
    @NonNull
    private final int mEndTime;
    @NonNull
    private final String mName;
    @NonNull
    private final String mPhoneNumber;
    @NonNull
    private final String mStartKeyword;
    @NonNull
    private final String mEndKeyword;
    @NonNull
    private final boolean mIsActivate;

    @NonNull
    public boolean isActivate() {
        return mIsActivate;
    }

    @NonNull
    public int getSetDayOfWeek() {
        return mSetDayOfWeek;
    }

    @NonNull
    private final int mSetDayOfWeek;

    @NonNull
    public int getStartTime()
    {
        return mStartTime;
    }

    @NonNull
    public int getEndTime()
    {
        return mEndTime;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @NonNull
    public String getStartKeyword() {
        return mStartKeyword;
    }

    @NonNull
    public String getEndKeyword() {
        return mEndKeyword;
    }

    public Alarm(@Nullable int mStartTime, @NonNull int mEndTime, @NonNull String mName, @NonNull String mPhoneNumber, @NonNull String mStartKeyword,
                 @NonNull String mEndKeyword, @NonNull int mSetDayOfWeek, @NonNull boolean mIsActivate) {
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mStartKeyword = mStartKeyword;
        this.mEndKeyword = mEndKeyword;
        this.mSetDayOfWeek = mSetDayOfWeek;
        this.mIsActivate = mIsActivate;
    }

    @Override
    public String toString()
    {
        return mName + ":" + mPhoneNumber;
    }
}
