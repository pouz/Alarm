package com.pouz.alarm.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.pouz.alarm.utils.Utils;

/**
 * Created by PouZ on 2017-02-17.
 */

public final class Alarm implements Parcelable {
    private int mId;
    private int mStartTime;
    private int mEndTime;
    private String mName;
    private String mPhoneNumber;
    private String mStartKeyword;
    private String mEndKeyword;
    private int mSetDayOfWeek;
    private boolean mIsActivate;

    public void setSetDayOfWeek(int mSetDayOfWeek) {
        this.mSetDayOfWeek = mSetDayOfWeek;
    }

    public void setStartTime(int mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setEndTime(int mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setStartKeyword(String mStartKeyword) {
        this.mStartKeyword = mStartKeyword;
    }

    public void setEndKeyword(String mEndKeyword) {
        this.mEndKeyword = mEndKeyword;
    }

    public void setIsActivate(boolean mIsActivate) {
        this.mIsActivate = mIsActivate;
    }

    public int getID() {
        return mId;
    }

    public boolean isActivate() {
        return mIsActivate;
    }

    public int getSetDayOfWeek() {
        return mSetDayOfWeek;
    }

    public int getStartTime() {
        return mStartTime;
    }

    public int getEndTime() {
        return mEndTime;
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getStartKeyword() {
        return mStartKeyword;
    }

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
        this.mIsActivate = true;
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

    public String getDayOfWeek() {
        StringBuilder sb = new StringBuilder();
        sb.append("알람 요일 \n[");

        if((mSetDayOfWeek & 1) != 0)
            sb.append(" 월 ");
        if((mSetDayOfWeek & 2) != 0)
            sb.append(" 화 ");
        if((mSetDayOfWeek & 4) != 0)
            sb.append(" 수 ");
        if((mSetDayOfWeek & 8) != 0)
            sb.append(" 목 ");
        if((mSetDayOfWeek & 16) != 0)
            sb.append(" 금 ");
        if((mSetDayOfWeek & 32) != 0)
            sb.append(" 토 ");
        if((mSetDayOfWeek & 64) != 0)
            sb.append(" 일 ");

        sb.append("]");
        return sb.toString();
    }
}
