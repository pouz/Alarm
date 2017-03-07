package com.pouz.alarm.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pouz.alarm.utils.Utils;

/**
 * Created by PouZ on 2017-02-17.
 */

public final class Alarm implements Parcelable {
    public static final int MON = 1;
    public static final int TUE = 2;
    public static final int WED = 4;
    public static final int THU = 8;
    public static final int FRI = 16;
    public static final int SAT = 32;
    public static final int SUN = 64;

    @NonNull
    private int mId;
    @NonNull
    private int mStartTime;
    @NonNull
    private int mEndTime;
    @NonNull
    private String mName;
    @NonNull
    private String mPhoneNumber;
    @NonNull
    private String mStartKeyword;
    @NonNull
    private String mEndKeyword;
    @NonNull
    private int mSetDayOfWeek;
    @NonNull
    private boolean mIsActivate;

    public void setSetDayOfWeek(@NonNull int mSetDayOfWeek) {
        this.mSetDayOfWeek = mSetDayOfWeek;
    }

    public void setStartTime(@NonNull int mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setEndTime(@NonNull int mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    public void setPhoneNumber(@NonNull String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setStartKeyword(@NonNull String mStartKeyword) {
        this.mStartKeyword = mStartKeyword;
    }

    public void setEndKeyword(@NonNull String mEndKeyword) {
        this.mEndKeyword = mEndKeyword;
    }

    public void setIsActivate(@NonNull boolean mIsActivate) {
        this.mIsActivate = mIsActivate;
    }

    @NonNull
    public int getID() {
        return mId;
    }

    @NonNull
    public boolean isActivate() {
        return mIsActivate;
    }

    @NonNull
    public int getSetDayOfWeek() {
        return mSetDayOfWeek;
    }


    @NonNull
    public int getStartTime() {
        return mStartTime;
    }

    @NonNull
    public int getEndTime() {
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

    public Alarm() {
        this.mId = 0;
        this.mStartTime = 0;
        this.mEndTime = 0;
        this.mName = "";
        this.mPhoneNumber = "";
        this.mStartKeyword = "";
        this.mEndKeyword = "";
        this.mSetDayOfWeek = 0;
        this.mIsActivate = false;
    }

    public Alarm(@NonNull int mStartTime, @NonNull int mEndTime, @NonNull String mName, @NonNull String mPhoneNumber, @NonNull String mStartKeyword,
                 @NonNull String mEndKeyword, @NonNull int mSetDayOfWeek, @NonNull boolean mIsActivate) {
        this.mId = 0;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mStartKeyword = mStartKeyword;
        this.mEndKeyword = mEndKeyword;
        this.mSetDayOfWeek = mSetDayOfWeek;
        this.mIsActivate = mIsActivate;
    }

    public Alarm(@NonNull int mId, @NonNull int mStartTime, @NonNull int mEndTime, @NonNull String mName, @NonNull String mPhoneNumber, @NonNull String mStartKeyword,
                 @NonNull String mEndKeyword, @NonNull int mSetDayOfWeek, @NonNull boolean mIsActivate) {
        this.mId = mId;
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
    public int describeContents() {
        return 0;
    }

    // implement Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mStartTime);
        dest.writeInt(mEndTime);
        dest.writeString(mName);
        dest.writeString(mPhoneNumber);
        dest.writeString(mStartKeyword);
        dest.writeString(mEndKeyword);
        dest.writeInt(mSetDayOfWeek);
        dest.writeByte((byte) (mIsActivate ? 1 : 0));
    }

    public static final Parcelable.Creator<Alarm> CREATOR =
            new Creator<Alarm>() {
                @Override
                public Alarm createFromParcel(Parcel source) {
                    return new Alarm(source.readInt(), source.readInt(), source.readInt(), source.readString(), source.readString(),
                            source.readString(), source.readString(), source.readInt(), source.readByte() != 0);
                }

                @Override
                public Alarm[] newArray(int size) {
                    return new Alarm[0];
                }
            };

    @Override
    public String toString() {
        return "[" + mName + "]"
                + ", " + "[" + Utils.intToTime(mStartTime)
                + " ~ " + Utils.intToTime(mEndTime) + "]";
    }
}
