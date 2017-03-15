package com.pouz.alarm.addeditalarm;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;
import com.pouz.alarm.utils.SmsSender;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AddEditAlarmPresenter implements AddEditAlarmContract.Presenter {
    @Nullable
    private AddEditAlarmContract.View mView;
    @Nullable
    private AlarmsDataSource mRepository;

    public AddEditAlarmPresenter(@Nullable final AlarmsDataSource repository, @Nullable final AddEditAlarmContract.View view) {
        this.mRepository = repository;
        this.mView = view;
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
            if (isNewAlarm()) {
                mRepository.saveAlarm(alarm);
                SmsSender.sendAddAlarmSMS(((Fragment)mView).getContext(), alarm, true);
                mView.showToast("Save Done.");
            }
            else {
                mRepository.updateAlarm(alarm);
                SmsSender.sendModifiedAlarmSMS(((Fragment)mView).getContext(), alarm, true);
                mView.showToast("Update Done");
            }


//            mView.finishAddEdit();
        } else {
            mView.showAvailabilityFailed();
        }
    }

    public boolean isNewAlarm() {
        return (mView.getMode() == AddEditAlarmFragment.ADD_MODE) ? true : false;
    }
}
