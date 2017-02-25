package com.pouz.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pouz.alarm.alarms.AlarmsActivity;
import com.pouz.alarm.receiver.DummyCallReceiver;

public class IntroActivity extends AppCompatActivity
{
    Intent mIntent;
    Context mContext;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Log.i("IntroActivity", "onCreate() -> Make PendingIntent for calling DummyService");
        Toast.makeText(this, "IntroActivity : onCreate() -> Make PendingIntent for calling DummyService", Toast.LENGTH_SHORT);

        mContext = this;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        mIntent = new Intent(mContext, DummyCallReceiver.class);
        mIntent.setAction(DummyCallReceiver.START_DUMMY_SERVICE);

        mPendingIntent.getBroadcast(mContext, 0, mIntent, 0);
        /** 10초에 한번씩 DummyCallReceiver 호출 */
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 10000, mPendingIntent);

        (new Handler()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(IntroActivity.this, AlarmsActivity.class);
                startActivity(intent);

                finish();
            }
        }, 2000);

    }
}
