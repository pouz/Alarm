package com.pouz.alarm.alarms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;
import com.pouz.alarm.utils.SmsSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsPresenter implements AlarmsContract.Presenter {
    private final AlarmsLocalDataSource mAlarmsLocalDataSource;
    private final AlarmsContract.View mAlarmsView;

    public AlarmsPresenter(@Nullable AlarmsLocalDataSource alarmsLocalDataSource, @Nullable AlarmsContract.View alarmsView) {
        this.mAlarmsLocalDataSource = alarmsLocalDataSource;
        this.mAlarmsView = alarmsView;

        mAlarmsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadAlarms(true);
    }

    @Override
    public void loadAlarms(boolean forcedLoad) {
        Log.i("AlarmsPresenter : ", "loadAlarms()");
        mAlarmsLocalDataSource.getAlarms(new AlarmsLocalDataSource.LoadAlarmsCallBack() {
            @Override
            public void onAlarmsLoaded(List<Alarm> alarms) {
                List<Alarm> alarmsToShow = new ArrayList<>();
                for (Alarm alarm : alarms) {
                    alarmsToShow.add(alarm);
                }

                mAlarmsView.showAlarms(alarmsToShow);
            }
        });

    }

    @Override
    public void addAlarm() {
        mAlarmsView.showAddAlarm();
    }

    @Override
    public void showSetting() {
        mAlarmsView.showSetting();
    }

    @Override
    public void editAlarm(final int mode, final Alarm alarm) {
        mAlarmsView.showEditAlarm(mode, alarm);
    }

    @Override
    public void deleteAlarm(int id) {
        mAlarmsLocalDataSource.deleteAlarm(id);
        loadAlarms(true);
    }

    @Override
    public void updateAlarm(Alarm alarm) {
        // TODO: 최적화 해야함. 예를 들어 activation만 변한다면 데이터베이스 업데이트를 해당하는 것만 하게 만들어야 할듯
        mAlarmsLocalDataSource.updateAlarm(alarm);
    }

    @Override
    public void notifyToUser(final String string) {
        mAlarmsView.showSnackbar(string);
    }
}
