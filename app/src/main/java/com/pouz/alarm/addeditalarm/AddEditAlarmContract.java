package com.pouz.alarm.addeditalarm;

import com.pouz.alarm.BasePresenter;
import com.pouz.alarm.BaseView;
import com.pouz.alarm.data.Alarm;

/**
 * Created by PouZ on 2017-02-17.
 */

public interface AddEditAlarmContract
{
    interface View extends BaseView<Presenter>
    {
        void showContact();
        void showTimePicker(String Key);
        /** called by saveAlarm() */
        void finishAddEdit();
    }

    interface Presenter extends BasePresenter
    {
        void selectContact();
        void selectTimePicker(String key);
        void saveAlarm(Alarm alarm);
        void updateAlarm(Alarm alarm);
    }
}
