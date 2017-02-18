package com.pouz.alarm.addeditalarm;

import android.support.annotation.Nullable;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AddEditAlarmPresenter implements AddEditAlarmContract.Presenter
{
    @Nullable
    private AddEditAlarmContract.View mView;
    @Nullable
    private AlarmsDataSource mRepository;

    public AddEditAlarmPresenter(@Nullable AlarmsDataSource mRepository, @Nullable AddEditAlarmContract.View mView)
    {
        this.mRepository = mRepository;
        this.mView = mView;
    }

    @Override
    public void start()
    {

    }

    @Override
    public void selectContact()
    {
        mView.showContact();
    }

    @Override
    public void selectTimePicker(String key)
    {
        mView.showTimePicker(key);
    }

    @Override
    public void saveAlarm(Alarm alarm)
    {
        if (isNewAlarm())
            mRepository.saveAlarm(alarm);
        else
            updateAlarm(alarm);
        mView.finishAddEdit();
    }

    @Override
    public void updateAlarm(Alarm alarm)
    {

    }

    public boolean isNewAlarm()
    {
        return true;
    }
}
