package com.pouz.alarm.data;

/**
 * Created by PouZ on 2017-02-28.
 */

public class AlarmState {
    private boolean mIsAlarmActive;
    private String mAlarmAuthor;

    private static AlarmState INSTANCE;

    public static AlarmState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlarmState();
        }
        return INSTANCE;
    }

    private AlarmState() {
        mIsAlarmActive = false;
        mAlarmAuthor = "";

        return;
    }

    public static void releaseInstance() {
        INSTANCE = null;
    }

    public String getAlarmAuthor() {
        return mAlarmAuthor;
    }

    public void setAlarmAuthor(String mAlarmAuthor) {
        this.mAlarmAuthor = mAlarmAuthor;
    }

    public boolean isAlarmActive() {
        return mIsAlarmActive;
    }

    public void setIsAlarmActive(boolean mIsAlarmActive) {
        this.mIsAlarmActive = mIsAlarmActive;
    }

    public boolean isAlarmAuthor(Alarm alarm) {
        return AlarmState.getInstance().getAlarmAuthor().equals(alarm.getPhoneNumber().toString().replaceAll("[()\\s-]+", ""));
    }
}
