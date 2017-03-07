package com.pouz.alarm.alarms;

import android.view.View;

import com.pouz.alarm.data.Alarm;

/**
 * Created by white on 2017-03-03.
 */
interface AlarmItemListener {

    void onAlarmLongClick(Alarm longClickedAlarm, View view);
}
