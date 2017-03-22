package com.pouz.alarm.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

/**
 * Created by PouZ on 2017-03-16.
 */

public class SettingAlarmPresenter implements SettingAlarmContract.Presenter{

    private SettingAlarmContract.View mView;
    private AlarmsLocalDataSource mAlarmsLocalDataSource;

    public SettingAlarmPresenter(@Nullable AlarmsLocalDataSource alarmLocalDataSource, @NonNull SettingAlarmContract.View view) {
        mAlarmsLocalDataSource = alarmLocalDataSource;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
