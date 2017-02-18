package com.pouz.alarm.alarms;

import android.content.Context;
import android.support.annotation.Nullable;

import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsPresenter implements AlarmsContract.Presenter
{
    private final AlarmsLocalDataSource mAlarmsLocalDataSource;
    private final AlarmsContract.View mAlarmsView;
    private boolean mFirstLoad = true;

    public AlarmsPresenter(@Nullable  AlarmsLocalDataSource alarmsLocalDataSource, @Nullable AlarmsContract.View alarmsView)
    {
        this.mAlarmsLocalDataSource = alarmsLocalDataSource;
        this.mAlarmsView = alarmsView;

        mAlarmsView.setPresenter(this);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void addAlarm()
    {
        mAlarmsView.showAddAlarm();
    }
}
