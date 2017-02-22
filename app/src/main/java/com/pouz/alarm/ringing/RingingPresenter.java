package com.pouz.alarm.ringing;

import android.support.annotation.Nullable;

import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

/**
 * Created by PouZ on 2017-02-22.
 */

public class RingingPresenter implements RingingContract.Presenter
{
    private AlarmsLocalDataSource mAlarmsLocalDataSource;
    private RingingContract.View  mView;

    public RingingPresenter(@Nullable AlarmsLocalDataSource alarmsLocalDataSource, @Nullable RingingContract.View view)
    {
        mAlarmsLocalDataSource = alarmsLocalDataSource;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start()
    {

    }
}
