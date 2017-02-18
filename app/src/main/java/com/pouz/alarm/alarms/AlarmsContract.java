package com.pouz.alarm.alarms;

import com.pouz.alarm.BasePresenter;
import com.pouz.alarm.BaseView;
import com.pouz.alarm.data.source.AlarmsDataSource;

/**
 * Created by PouZ on 2017-02-17.
 */

public interface AlarmsContract
{
    interface View extends BaseView<Presenter>
    {
        public void showAddAlarm();
    }

    interface Presenter extends BasePresenter
    {
        public void addAlarm();
    }
}
