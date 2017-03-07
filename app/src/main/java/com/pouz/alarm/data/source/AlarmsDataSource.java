package com.pouz.alarm.data.source;

import android.support.annotation.NonNull;

import com.pouz.alarm.data.Alarm;

import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public interface AlarmsDataSource
{
    // for alarms
    interface LoadAlarmsCallBack
    {
        void onAlarmsLoaded(List<Alarm> alarms);
    }

    // Just for one alarm
    interface GetAlarmCallBack
    {
        void onAlarmLoaded(Alarm alarm);
        void onDataNotAvailable();
    }

    void getAlarms(@NonNull LoadAlarmsCallBack callBack);

    void getAlarm(@NonNull GetAlarmCallBack callBack);

    void saveAlarm(@NonNull Alarm alarm);

    //void deleteAlarm(@NonNull String phoneNum);
    void deleteAlarm(@NonNull int id);

    void updateAlarm(@NonNull final Alarm alarm);

    void reordering();
}
