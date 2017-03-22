package com.pouz.alarm.alarms;

import android.content.Context;

import com.pouz.alarm.BasePresenter;
import com.pouz.alarm.BaseView;
import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;

import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public interface AlarmsContract
{
    interface View extends BaseView<Presenter>
    {
        void showAddAlarm();

        void showEditAlarm(final int mode, final Alarm alarm);

        void showAlarms(List<Alarm> alarms);

        void showSnackbar(final String string);

        void showSetting();
    }

    interface Presenter extends BasePresenter
    {
        void addAlarm();

        void showSetting();

        void editAlarm(final int mode, final Alarm alarm);

        void loadAlarms(boolean forcedLoad);

        void deleteAlarm(int id);

        void updateAlarm(final Alarm alarm);

        void notifyToUser(final String string);
    }
}
