package com.pouz.alarm.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by PouZ on 2017-03-16.
 */

public class SettingAlarmFragment extends PreferenceFragmentCompat implements SettingAlarmContract.View {
    private SettingAlarmContract.Presenter mPresenter;

    @Override
    public void setPresenter(SettingAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new SettingAlarmPresenter(null, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
