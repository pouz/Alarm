package com.pouz.alarm.addeditalarm;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.pouz.alarm.R;
import com.pouz.alarm.data.Alarm;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AddEditAlarmFragment extends PreferenceFragmentCompat implements AddEditAlarmContract.View, Preference.OnPreferenceChangeListener
        , Preference.OnPreferenceClickListener
{
    public static final int REQUEST_FOR_CONTACT = 1;

    private AddEditAlarmContract.Presenter mPresenter;

    Alarm mAlarm;

    private String mReceiveName;
    private String mReceivePhoneNumber;
    private String mStartKeyword;
    private String mEndKeyword;
    private String mStartTime;
    private String mEndTime;
    private int mSetDayOfWeek;

    CheckBoxPreference mon, tue, wed, thu, fri, sat, sun;

    public static AddEditAlarmFragment newInstance()
    {
        return new AddEditAlarmFragment();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.add_edit_alarm_preference);

        findPreference("contact").setOnPreferenceClickListener(this);
        findPreference("start_keyword").setOnPreferenceChangeListener(this);
        findPreference("end_keyword").setOnPreferenceChangeListener(this);
        findPreference("set_start_time").setOnPreferenceClickListener(this);
        findPreference("set_end_time").setOnPreferenceClickListener(this);

        mon = (CheckBoxPreference) findPreference("alarm_monday");
        mon.setOnPreferenceChangeListener(this);
        tue = (CheckBoxPreference) findPreference("alarm_tuesday");
        tue.setOnPreferenceChangeListener(this);
        wed = (CheckBoxPreference) findPreference("alarm_wednesday");
        wed.setOnPreferenceChangeListener(this);
        thu = (CheckBoxPreference) findPreference("alarm_thursday");
        thu.setOnPreferenceChangeListener(this);
        fri = (CheckBoxPreference) findPreference("alarm_friday");
        fri.setOnPreferenceChangeListener(this);
        sat = (CheckBoxPreference) findPreference("alarm_saturday");
        sat.setOnPreferenceChangeListener(this);
        sun = (CheckBoxPreference) findPreference("alarm_sunday");
        sun.setOnPreferenceChangeListener(this);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAlarm = new Alarm(mStartTime + "/" + mEndTime, mReceiveName, mReceivePhoneNumber, mStartKeyword, mEndKeyword, mSetDayOfWeek);
                mPresenter.saveAlarm(mAlarm);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode)
        {
            case REQUEST_FOR_CONTACT:
                Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                cursor.moveToFirst();

                this.mReceiveName = cursor.getString(0);
                this.mReceivePhoneNumber = cursor.getString(1);

                cursor.close();
                findPreference("contact").setSummary(mReceiveName + ", " + mReceivePhoneNumber);
                break;
        }
    }

    public AddEditAlarmFragment()
    {

    }

    @Override
    public void setPresenter(@Nullable AddEditAlarmContract.Presenter presenter)
    {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference instanceof EditTextPreference)
        {
            preference.setSummary(newValue.toString());
            if (preference.getKey() == "start_keyword")
                mStartKeyword = findPreference("start_keyword").getSummary().toString();
            else if (preference.getKey() == "end_keyword")
                mEndKeyword = findPreference("end_keyword").getSummary().toString();
        } else if (preference instanceof CheckBoxPreference)
        {
            int flag = 0;
            // 왜 반응이 한박자 느릴까? 누르고 boolean check가 바뀌기 전에 호출되는건가...
            // 그렇다면 어떻게 해결해야 하는가?
            // 이것말고 리스트로 해결해보자
            if (!((CheckBoxPreference) preference).isChecked())
                ((CheckBoxPreference) preference).setChecked(true);
            else
                ((CheckBoxPreference) preference).setChecked(false);

            if (mon.isChecked())
            {
                flag += 1;
            }
            if (tue.isChecked())
                flag += (1 << 1);
            if (wed.isChecked())
                flag += (1 << 2);
            if (thu.isChecked())
                flag += (1 << 3);
            if (fri.isChecked())
                flag += (1 << 4);
            if (sat.isChecked())
                flag += (1 << 5);
            if (sun.isChecked())
                flag += (1 << 6);

            mSetDayOfWeek = flag;
            Log.i("Set Day of Week", "" + flag);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        switch (preference.getKey())
        {
            case "contact":
                mPresenter.selectContact();
                break;
            case "set_start_time":
                mPresenter.selectTimePicker(preference.getKey());
                break;
            case "set_end_time":
                mPresenter.selectTimePicker(preference.getKey());
                break;
        }

        return false;
    }

    @Override
    public void showContact()
    {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                .setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE), REQUEST_FOR_CONTACT);
    }

    @Override
    public void showTimePicker(String key)
    {
        // TODO: Need to change to implements style
        final String k = key;
        Date date = new Date(System.currentTimeMillis());
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1)
            {
                switch (k)
                {
                    case "set_start_time":
                        findPreference(k).setSummary(i + ":" + i1);
                        mStartTime = i + ":" + i1;
                        break;
                    case "set_end_time":
                        findPreference(k).setSummary(i + ":" + i1);
                        mEndTime = i + ":" + i1;
                        break;
                }
            }
        }, date.getHours(), date.getMinutes(), false).show();
    }

    @Override
    public void finishAddEdit()
    {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
