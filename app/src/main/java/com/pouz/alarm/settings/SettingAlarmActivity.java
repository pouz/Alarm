package com.pouz.alarm.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.pouz.alarm.R;

/**
 * Created by PouZ on 2017-03-16.
 */

public class SettingAlarmActivity extends AppCompatActivity{
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm_act);
        SettingAlarmFragment settingAlarmFragment =
                (SettingAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.setting_alarm_pref);

        if (settingAlarmFragment == null) {
            settingAlarmFragment = new SettingAlarmFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_alarm_pref, settingAlarmFragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_alarm_toolbar);
        toolbar.setTitle(R.string.setting_alarm_toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
    }
}
