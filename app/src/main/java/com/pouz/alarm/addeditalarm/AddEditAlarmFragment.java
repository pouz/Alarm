package com.pouz.alarm.addeditalarm;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pouz.alarm.R;
import com.pouz.alarm.utils.Utils;
import com.pouz.alarm.data.Alarm;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AddEditAlarmFragment extends PreferenceFragmentCompat implements AddEditAlarmContract.View, Preference.OnPreferenceChangeListener
        , Preference.OnPreferenceClickListener {
    public static final int REQUEST_FOR_CONTACT = 1;

    public static final int ADD_MODE = 1;
    public static final int EDIT_MODE = 2;

    private AddEditAlarmContract.Presenter mPresenter;

    Alarm mAlarm;

    private int mMode;
    private Alarm mAlarmUsingForEdit;

    CheckBoxPreference mMonPreference, mTuePreference, mWedPreference, mThuPreference, mFriPreference, mSatPreference, mSunPreference;
    Preference mContactPreference, mStartKeywordPreference, mEndKeywordPreference, mStartTimePreference, mEndTimePreference;

    public static AddEditAlarmFragment newInstance() {
        return new AddEditAlarmFragment();
    }

    public void setPreferenceByAlarm(final Alarm alarm) {
        mAlarm = alarm;
        int flag = mAlarm.getSetDayOfWeek();

        mContactPreference.setSummary(mAlarm.getName()+ ", " + mAlarm.getPhoneNumber());
        mStartKeywordPreference.setSummary(mAlarm.getStartKeyword());
        mEndKeywordPreference.setSummary(mAlarm.getEndKeyword());
        mStartTimePreference.setSummary(Utils.intToTime(mAlarm.getStartTime()));
        mEndTimePreference.setSummary(Utils.intToTime(mAlarm.getEndTime()));

        if((flag & 1) != 0)
            mMonPreference.setChecked(true);
        if((flag & 2) != 0)
            mTuePreference.setChecked(true);
        if((flag & 4) != 0)
            mWedPreference.setChecked(true);
        if((flag & 8) != 0)
            mThuPreference.setChecked(true);
        if((flag & 16) != 0)
            mFriPreference.setChecked(true);
        if((flag & 32) != 0)
            mSatPreference.setChecked(true);
        if((flag & 64) != 0)
            mSunPreference.setChecked(true);
    }

    public int getMode() {
        return mMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAlarm = new Alarm();

        mMode = getActivity().getIntent().getIntExtra("mode", 1);
        if(mMode == EDIT_MODE)
            mAlarmUsingForEdit = getActivity().getIntent().getParcelableExtra("alarm");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 상단에 글씨로 된 옵션메뉴 생성하려면 app:showAsAction="always" 해야함
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mMode == EDIT_MODE) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("수정");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_menu, menu);

        if (mMode == AddEditAlarmFragment.EDIT_MODE)
            menu.findItem(R.id.menu_add_alarm).setTitle(R.string.edit_confirm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_alarm:
                /** add logic to check a alarm set is correct */
                mPresenter.saveAlarm(mAlarm);
                return true;
        }
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("AddEditAlarmFragment", "onCreatePreferences");
        addPreferencesFromResource(R.xml.add_edit_alarm_pref);

        mContactPreference = findPreference("contact");
        mStartKeywordPreference = findPreference("start_keyword");
        mEndKeywordPreference = findPreference("end_keyword");
        mStartTimePreference = findPreference("set_start_time");
        mEndTimePreference = findPreference("set_end_time");

        mContactPreference.setOnPreferenceClickListener(this);
        mStartKeywordPreference.setOnPreferenceChangeListener(this);
        mEndKeywordPreference.setOnPreferenceChangeListener(this);
        mStartTimePreference.setOnPreferenceClickListener(this);
        mEndTimePreference.setOnPreferenceClickListener(this);

        mMonPreference = (CheckBoxPreference) findPreference("alarm_monday");
        mMonPreference.setOnPreferenceChangeListener(this);
        mMonPreference.setChecked(false);
        mTuePreference = (CheckBoxPreference) findPreference("alarm_tuesday");
        mTuePreference.setOnPreferenceChangeListener(this);
        mTuePreference.setChecked(false);
        mWedPreference = (CheckBoxPreference) findPreference("alarm_wednesday");
        mWedPreference.setOnPreferenceChangeListener(this);
        mWedPreference.setChecked(false);
        mThuPreference = (CheckBoxPreference) findPreference("alarm_thursday");
        mThuPreference.setOnPreferenceChangeListener(this);
        mThuPreference.setChecked(false);
        mFriPreference = (CheckBoxPreference) findPreference("alarm_friday");
        mFriPreference.setOnPreferenceChangeListener(this);
        mFriPreference.setChecked(false);
        mSatPreference = (CheckBoxPreference) findPreference("alarm_saturday");
        mSatPreference.setOnPreferenceChangeListener(this);
        mSatPreference.setChecked(false);
        mSunPreference = (CheckBoxPreference) findPreference("alarm_sunday");
        mSunPreference.setOnPreferenceChangeListener(this);
        mSunPreference.setChecked(false);

        if(mMode == EDIT_MODE){
            setPreferenceByAlarm(mAlarmUsingForEdit);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_FOR_CONTACT:
                Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                cursor.moveToFirst();

                mAlarm.setName(cursor.getString(0));
                mAlarm.setPhoneNumber(cursor.getString(1));

                cursor.close();
                mContactPreference.setSummary(mAlarm.getName()+ ", " + mAlarm.getPhoneNumber());
                break;
        }
    }

    public AddEditAlarmFragment() {
    }

    @Override
    public void setPresenter(@Nullable AddEditAlarmContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof EditTextPreference) {
            ((EditTextPreference) preference).setText("");

            if (preference.getKey().toString().equals("start_keyword")) {
                if (newValue.toString().equals(mAlarm.getEndKeyword())) {
                    Snackbar.make(getView(), "시작 키워드가 멈춤 키워드와 동일합니다.", Toast.LENGTH_SHORT).setDuration(4000).show();
                    mAlarm.setStartKeyword("");
                } else {
                    mAlarm.setStartKeyword(newValue.toString());
                    preference.setSummary(mAlarm.getStartKeyword());
                }
            }
            if (preference.getKey().toString().equals("end_keyword")) {
                if (newValue.toString().equals(mAlarm.getStartKeyword())) {
                    Snackbar.make(getView(), "멈춤 키워드가 시작 키워드와 동일합니다.", Toast.LENGTH_SHORT).setDuration(4000).show();
                    mAlarm.setEndKeyword("");
                } else {
                    mAlarm.setEndKeyword(newValue.toString());
                    preference.setSummary(mAlarm.getEndKeyword());
                }
            }

        } else if (preference instanceof CheckBoxPreference) {
            int flag = 0;
            // TODO: check here for checkbox preference
            if (!((CheckBoxPreference) preference).isChecked())
                ((CheckBoxPreference) preference).setChecked(true);
            else
                ((CheckBoxPreference) preference).setChecked(false);

            if (mMonPreference.isChecked()) {
                flag += 1;
            }
            if (mTuePreference.isChecked())
                flag += (1 << 1);
            if (mWedPreference.isChecked())
                flag += (1 << 2);
            if (mThuPreference.isChecked())
                flag += (1 << 3);
            if (mFriPreference.isChecked())
                flag += (1 << 4);
            if (mSatPreference.isChecked())
                flag += (1 << 5);
            if (mSunPreference.isChecked())
                flag += (1 << 6);

            mAlarm.setSetDayOfWeek(flag);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
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
    public void showContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                .setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE), REQUEST_FOR_CONTACT);
    }

    @Override
    public void showTimePicker(final String key) {
        Date date = new Date(System.currentTimeMillis());

        if(mMode == EDIT_MODE) {
            switch(key) {
                case "set_start_time":
                    date.setHours(mAlarm.getStartTime() / 60);
                    date.setMinutes(mAlarm.getStartTime() % 60);
                    break;
                case "set_end_time":
                    date.setHours(mAlarm.getEndTime() / 60);
                    date.setMinutes(mAlarm.getEndTime() % 60);
                    break;
            }
        }
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (key) {
                    case "set_start_time":
                        mStartTimePreference.setSummary(Utils.intToTime(Utils.timeToInt(i, i1)));
                        mAlarm.setStartTime(Utils.timeToInt(i, i1));
                        break;
                    case "set_end_time":
                        mEndTimePreference.setSummary(Utils.intToTime(Utils.timeToInt(i, i1)));
                        mAlarm.setEndTime(Utils.timeToInt(i, i1));
                        break;
                }
            }
        }, date.getHours(), date.getMinutes(), false).show();
    }

    @Override
    public void finishAddEdit() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showAvailabilityFailed() {
        Log.i("AddEditAlarmFragment", "showAvaulabilityFailed");
        Snackbar.make(this.getView(), "선택항목이 유효하지 않습니다.\n다시한번 확인해 주세요.", Toast.LENGTH_LONG)
                .setDuration(4000).show();
    }

    @Override
    public void showToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_LONG);
    }
}
