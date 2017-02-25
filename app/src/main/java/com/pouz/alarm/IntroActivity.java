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

public class IntroActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

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
