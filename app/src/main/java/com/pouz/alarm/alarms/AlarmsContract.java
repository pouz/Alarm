package com.pouz.alarm.alarms;

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

        void showAlarms(List<Alarm> alarms);
    }

    interface Presenter extends BasePresenter
    {
        void addAlarm();
        void loadAlarms(boolean forcedLoad);

    }
}
