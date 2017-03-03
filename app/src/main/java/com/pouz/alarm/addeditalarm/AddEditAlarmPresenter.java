package com.pouz.alarm.addeditalarm;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AddEditAlarmPresenter implements AddEditAlarmContract.Presenter {
    @Nullable
    private AddEditAlarmContract.View mView;
    @Nullable
    private AlarmsDataSource mRepository;

    public AddEditAlarmPresenter(@Nullable AlarmsDataSource mRepository, @Nullable AddEditAlarmContract.View mView) {
        this.mRepository = mRepository;
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean checkAvailability(Alarm alarm) {
        if (alarm.getName() == "" || alarm.getName() == null ||
                alarm.getPhoneNumber() == "" || alarm.getPhoneNumber() == null ||
                alarm.getStartKeyword() == "" || alarm.getStartKeyword() == null ||
                alarm.getEndKeyword() == "" || alarm.getEndKeyword() == null ||
                alarm.getSetDayOfWeek() == 0)
            return false;
        return true;
    }

    @Override
    public void selectContact() {
        mView.showContact();
    }

    @Override
    public void selectTimePicker(String key) {
        mView.showTimePicker(key);
    }

    @Override
    public void saveAlarm(Alarm alarm) {
        if (checkAvailability(alarm)) {
            if (isNewAlarm())
                mRepository.saveAlarm(alarm);
            else
                updateAlarm(alarm);
            mView.finishAddEdit();
        } else {
            mView.showAvailabilityFailed();
        }
    }

    @Override
    public void updateAlarm(Alarm alarm) {

    }

    public boolean isNewAlarm() {
        return true;
    }
}
