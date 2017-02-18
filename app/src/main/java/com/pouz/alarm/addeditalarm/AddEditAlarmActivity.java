package com.pouz.alarm.addeditalarm;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pouz.alarm.R;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

public class AddEditAlarmActivity extends AppCompatActivity
{

    public static final int REQUEST_ADD_ALARM = 1;

    private AddEditAlarmContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_alarm);

        AddEditAlarmFragment addEditAlarmFragment =
                (AddEditAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.preferenceAddEditFrame);
        if (addEditAlarmFragment == null)
        {
            addEditAlarmFragment = AddEditAlarmFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.preferenceAddEditFrame, addEditAlarmFragment);
            transaction.commit();
        }

        mPresenter =
               new AddEditAlarmPresenter(AlarmsLocalDataSource.getInstance(getApplicationContext()), addEditAlarmFragment);
        addEditAlarmFragment.setPresenter(mPresenter);
    }
}
