package com.pouz.alarm.settings;

import android.os.Bundle;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pouz.alarm.R;


/**
 * Created by PouZ on 2017-03-16.
 */

public class SettingAlarmFragment extends PreferenceFragmentCompat implements SettingAlarmContract.View, Preference.OnPreferenceChangeListener {
    private SettingAlarmContract.Presenter mPresenter;

    private SwitchPreferenceCompat mSwitchPrefSendText;
    private SwitchPreferenceCompat mSwitchPrefSendTextWhenAdded;
    private SwitchPreferenceCompat mSwitchPrefSendTextWhenDeleted;
    private SwitchPreferenceCompat mSwitchPrefSendTextWhenModified;
    private SwitchPreferenceCompat mSwitchPrefSendTextWhenAlarmControlled;

    private SharedPreferencesCompat mSharedPreferences;

    private boolean mSendText = false;
    private boolean mSendTextWhenAdded = false;
    private boolean mSendTextWhenDeleted = false;
    private boolean mSendTextWhenModified = false;
    private boolean mSendTextWhenAlarmControlled = false;

    /**
     * override super functions
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new SettingAlarmPresenter(null, this);
        setStateBySharedPreference();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_alarm_pref);

        mSwitchPrefSendText = (SwitchPreferenceCompat) findPreference("send_text");
        mSwitchPrefSendTextWhenAdded = (SwitchPreferenceCompat) findPreference("send_text_when_added");
        mSwitchPrefSendTextWhenDeleted = (SwitchPreferenceCompat) findPreference("send_text_when_deleted");
        mSwitchPrefSendTextWhenModified = (SwitchPreferenceCompat) findPreference("send_text_when_modified");
        mSwitchPrefSendTextWhenAlarmControlled = (SwitchPreferenceCompat) findPreference("send_text_when_alarm_controlled");

        // set listener for send_text
        mSwitchPrefSendText.setOnPreferenceChangeListener(this);
        mSwitchPrefSendTextWhenAdded.setOnPreferenceChangeListener(this);
        mSwitchPrefSendTextWhenDeleted.setOnPreferenceChangeListener(this);
        mSwitchPrefSendTextWhenModified.setOnPreferenceChangeListener(this);
        mSwitchPrefSendTextWhenAlarmControlled.setOnPreferenceChangeListener(this);

        // disable when send_text is disabled
        // TODO: Need to restore state to switches from shared preference
        if (mSwitchPrefSendText.isChecked() == false) {
            mSwitchPrefSendTextWhenAdded.setEnabled(false);
            mSwitchPrefSendTextWhenDeleted.setEnabled(false);
            mSwitchPrefSendTextWhenModified.setEnabled(false);
            mSwitchPrefSendTextWhenAlarmControlled.setEnabled(false);
        }
    }

    /**
     * override BaseView
     */

    @Override
    public void setPresenter(SettingAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * implements listeners
     */

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // for SwitchPreferenceCompat
        if (preference instanceof SwitchPreferenceCompat) {
            switch (preference.getKey()) {
                // listener for sending text
                case "send_text":
                    if (((SwitchPreferenceCompat) preference).isChecked() == true) {
                        mSwitchPrefSendTextWhenAdded.setEnabled(false);
                        mSwitchPrefSendTextWhenDeleted.setEnabled(false);
                        mSwitchPrefSendTextWhenModified.setEnabled(false);
                        mSwitchPrefSendTextWhenAlarmControlled.setEnabled(false);
                        // TODO: Save state to shared preference.
                        // TODO: Save additional state to shared preference when turn send_text switch off
                        mSendText = true;
                    } else {
                        mSwitchPrefSendTextWhenAdded.setEnabled(true);
                        mSwitchPrefSendTextWhenDeleted.setEnabled(true);
                        mSwitchPrefSendTextWhenModified.setEnabled(true);
                        mSwitchPrefSendTextWhenAlarmControlled.setEnabled(true);
                        // TODO: Save state to shared preference
                        // TODO: Restore state to switches when turn send_text switch on
                        mSendText = false;
                    }
                    break;

                case "send_text_when_added":
                    // true means switch is off;
                    if (((SwitchPreferenceCompat) preference).isChecked() == true) {
                        mSendTextWhenAdded = false;
                        // TODO: Save state to shared preference
                    } else {
                        mSendTextWhenAdded = true;
                        // TODO: Save state to shared preference
                    }
                    break;

                case "send_text_when_deleted":
                    // true means switch is off
                    if (((SwitchPreferenceCompat) preference).isChecked() == true) {
                        mSendTextWhenDeleted = false;
                        // TODO: Save state to shared preference
                    } else {
                        mSendTextWhenDeleted = true;
                        // TODO: Save state to shared preference
                    }
                    break;

                case "send_text_when_modified":
                    // true means switch is off
                    if (((SwitchPreferenceCompat) preference).isChecked() == true) {
                        mSendTextWhenModified = false;
                        // TODO: Save state to shared preference
                    } else {
                        mSendTextWhenModified = true;
                        // TODO: Save state to shared preference
                    }
                    break;

                case "send_text_when_alarm_controlled":
                    // true means switch is off
                    if (((SwitchPreferenceCompat) preference).isChecked() == true) {
                        mSendTextWhenAlarmControlled = false;
                        // TODO: Save state to shared preference
                    } else {
                        mSendTextWhenAlarmControlled = true;
                        // TODO: Save state to shared preference
                    }
            }
        }
        return true;
    }

    /**
     * local functions
     */

    private void setStateBySharedPreference() {

    }
}
