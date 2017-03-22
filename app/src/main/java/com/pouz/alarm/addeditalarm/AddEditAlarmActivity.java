package com.pouz.alarm.addeditalarm;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pouz.alarm.R;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

public class AddEditAlarmActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_ALARM = 1;

    private AddEditAlarmContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_alarm_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("새로 등록");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);

        AddEditAlarmFragment addEditAlarmFragment =
                (AddEditAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.preferenceAddEditFrame);
        if (addEditAlarmFragment == null) {
            addEditAlarmFragment = AddEditAlarmFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.preferenceAddEditFrame, addEditAlarmFragment);
            transaction.commit();
        }

        mPresenter =
                new AddEditAlarmPresenter(AlarmsLocalDataSource.getInstance(getApplicationContext()), addEditAlarmFragment);
        addEditAlarmFragment.setPresenter(mPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
