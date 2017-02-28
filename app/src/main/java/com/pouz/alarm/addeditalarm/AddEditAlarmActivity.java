package com.pouz.alarm.addeditalarm;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.pouz.alarm.R;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

public class AddEditAlarmActivity extends AppCompatActivity
{

    public static final int REQUEST_ADD_ALARM = 1;

    private AddEditAlarmContract.Presenter mPresenter;

    /** Home as Up 은 activity에서 처리해야 한다..... fragment에서 처리할 수 있는 방법은? activity 호출 또는 onbackpressed를 fragment에서 호출? */
    /*
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
   }
   */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_alarm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        AddEditAlarmFragment addEditAlarmFragment =
                (AddEditAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.preferenceAddEditFrame);
        if (addEditAlarmFragment == null)
        {
            // frame layout과 fragment 연결. Fragment 를 singletone으로 생성해 놓았기 때문에 이런식으로
            // 다시 한번 확인해 볼 필요가 있다.
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
