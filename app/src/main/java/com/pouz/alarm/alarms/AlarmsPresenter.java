package com.pouz.alarm.alarms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

import java.util.ArrayList;
import java.util.List;

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
        loadAlarms(true);
    }

    @Override
    public void loadAlarms(boolean forcedLoad) {
        Log.i("AlarmsPresenter : ", "loadAlarms()");
        mAlarmsLocalDataSource.getAlarms(new AlarmsLocalDataSource.LoadAlarmsCallBack()
        {
            @Override
            public void onAlarmsLoaded(List<Alarm> alarms) {
                List<Alarm> alarmsToShow = new ArrayList<>();
                for(Alarm alarm : alarms)
                {
                    alarmsToShow.add(alarm);
                }

                mAlarmsView.showAlarms(alarmsToShow);
            }
        });

    }

    @Override
    public void addAlarm()
    {
        mAlarmsView.showAddAlarm();
    }
}
