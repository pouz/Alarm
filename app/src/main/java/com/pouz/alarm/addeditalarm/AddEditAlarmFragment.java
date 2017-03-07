package com.pouz.alarm.addeditalarm;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.pouz.alarm.alarms.AlarmsActivity;
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

    CheckBoxPreference mon, tue, wed, thu, fri, sat, sun;

    public static AddEditAlarmFragment newInstance() {
        return new AddEditAlarmFragment();
    }

    public void setPreferenceByAlarm(final Alarm alarm) {
        Log.i("AddEditAlarmFragment", "setPreferenceByAlarm -> " + alarm.toString());
        mAlarm = alarm;
        int flag = mAlarm.getSetDayOfWeek();

        findPreference("contact").setSummary(mAlarm.getName()+ ", " + mAlarm.getPhoneNumber());
        findPreference("start_keyword").setSummary(mAlarm.getStartKeyword());
        findPreference("end_keyword").setSummary(mAlarm.getEndKeyword());
        findPreference("set_start_time").setSummary(Utils.intToTime(mAlarm.getStartTime()));
        findPreference("set_end_time").setSummary(Utils.intToTime(mAlarm.getEndTime()));

        if((flag & 1) != 0)
            mon.setChecked(true);
        if((flag & 2) != 0)
            tue.setChecked(true);
        if((flag & 4) != 0)
            wed.setChecked(true);
        if((flag & 8) != 0)
            thu.setChecked(true);
        if((flag & 16) != 0)
            fri.setChecked(true);
        if((flag & 32) != 0)
            sat.setChecked(true);
        if((flag & 64) != 0)
            sun.setChecked(true);
    }

    public int getMode() {
        return mMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("AddEditAlarmFragment", "onCreate");
        // routine to get extras from activity.
        //super.onCreate가 여기있으면 호출과 동시에 onCreatePreferences를 호출하기 때문에 꼬였음
        //super.onCreate(savedInstanceState);

        mAlarm = new Alarm();

        mMode = getActivity().getIntent().getIntExtra("mode", 1);
        Log.i("AddEditAlarmFragment", "onCreate() -> mode is " + ((mMode == 1) ? "ADD_MODE" : "EDIT_MODE"));
        if(mMode == EDIT_MODE) {
            mAlarmUsingForEdit = getActivity().getIntent().getParcelableExtra("alarm");
            Log.i("AddEditAlarmFragment", "onCreate() mAlarmUsingForEdit -> " + mAlarmUsingForEdit.toString());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** 상단에 글씨로 된 옵션메뉴 생성하려면 app:showAsAction="always" 해야함 */
        Log.i("AddEditAlarmFragment", "onCreateView");
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_add_edit_menu, menu);
        //mMode = getActivity().getIntent().getIntExtra("mode", 1);

        if (mMode == AddEditAlarmFragment.EDIT_MODE) {
            // menu.getItem(index i) 는 해당 인덱스의 아이템 반환
            // menu.findItem(int id) 로 찾는게 합당
            menu.findItem(R.id.menu_add_alarm).setTitle(R.string.edit_confirm);
            // 메뉴 아이템 텍스트를 바꿀 때 받는 extras 처리할 때 alarm정보 extra도 함께 처리하려고 여기서 진행
            // 이렇게 하면 너무 중구남방 onCreate()에서 extras를 처리하자.
            //mAlarmUsingForEdit = getActivity().getIntent().getParcelableExtra("alarm");
        }
    }


    // routine to do when user select the option button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_alarm:
                /** add logic to check a alarm set is correct */
                mPresenter.saveAlarm(mAlarm);
                return true;
            case android.R.id.home:
                Intent intent = new Intent(getContext(), AlarmsActivity.class);
                startActivity(intent);

                return true;
        }
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("AddEditAlarmFragment", "onCreatePreferences");
        addPreferencesFromResource(R.xml.add_edit_alarm_preference);

        findPreference("contact").setOnPreferenceClickListener(this);
        findPreference("start_keyword").setOnPreferenceChangeListener(this);
        findPreference("end_keyword").setOnPreferenceChangeListener(this);
        findPreference("end_keyword").setDefaultValue("");
        findPreference("set_start_time").setOnPreferenceClickListener(this);
        findPreference("set_end_time").setOnPreferenceClickListener(this);

        mon = (CheckBoxPreference) findPreference("alarm_monday");
        mon.setOnPreferenceChangeListener(this);
        mon.setChecked(false);
        tue = (CheckBoxPreference) findPreference("alarm_tuesday");
        tue.setOnPreferenceChangeListener(this);
        tue.setChecked(false);
        wed = (CheckBoxPreference) findPreference("alarm_wednesday");
        wed.setOnPreferenceChangeListener(this);
        wed.setChecked(false);
        thu = (CheckBoxPreference) findPreference("alarm_thursday");
        thu.setOnPreferenceChangeListener(this);
        thu.setChecked(false);
        fri = (CheckBoxPreference) findPreference("alarm_friday");
        fri.setOnPreferenceChangeListener(this);
        fri.setChecked(false);
        sat = (CheckBoxPreference) findPreference("alarm_saturday");
        sat.setOnPreferenceChangeListener(this);
        sat.setChecked(false);
        sun = (CheckBoxPreference) findPreference("alarm_sunday");
        sun.setOnPreferenceChangeListener(this);
        sun.setChecked(false);

        if(mMode == EDIT_MODE){
            setPreferenceByAlarm(mAlarmUsingForEdit);
        }

        // TODO: change for alarm activation
        //mIsActivate = true;
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
                findPreference("contact").setSummary(mAlarm.getName()+ ", " + mAlarm.getPhoneNumber());
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
            // 왜 반응이 한박자 느릴까? 누르고 boolean check가 바뀌기 전에 호출되는건가...
            // 그렇다면 어떻게 해결해야 하는가?
            // 이것말고 리스트로 해결해보자
            // TODO: check here for checkbox preference
            if (!((CheckBoxPreference) preference).isChecked())
                ((CheckBoxPreference) preference).setChecked(true);
            else
                ((CheckBoxPreference) preference).setChecked(false);

            if (mon.isChecked()) {
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

            mAlarm.setSetDayOfWeek(flag);
            Log.i("Set Day of Week", "" + flag);
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
    public void showTimePicker(String key) {
        // TODO: Need to change implements style
        final String k = key;
        // TODO: set selected time on edit mode
        Date date = new Date(System.currentTimeMillis());
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                switch (k) {
                    case "set_start_time":
                        findPreference(k).setSummary(Utils.intToTime(Utils.timeToInt(i, i1)));
                        mAlarm.setStartTime(Utils.timeToInt(i, i1));
                        break;
                    case "set_end_time":
                        findPreference(k).setSummary(Utils.intToTime(Utils.timeToInt(i, i1)));
                        mAlarm.setEndTime(Utils.timeToInt(i, i1));
                        break;
                }
            }
        }, date.getHours(), date.getMinutes(), false).show();
    }

    @Override
    public void finishAddEdit() {
        getActivity().setResult(Activity.RESULT_OK); // Intent에 추가되나?
        // TODO: Need to check right here
        getActivity().finish(); // 현재 액티비티 닫음. 전 액티비티로 회귀? or something?
    }

    @Override
    public void showAvailabilityFailed() {
        Log.i("AddEditAlarmFragment", "showAvaulabilityFailed");
        Snackbar.make(this.getView(), "선택항목이 유효하지 않습니다.\n다시한번 확인해 주세요.", Toast.LENGTH_LONG).setDuration(4000).show();
    }

    @Override
    public void showToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_LONG);
    }
}
