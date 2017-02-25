package com.pouz.alarm.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.pouz.alarm.service.DummyService;

public class BootReceiver extends BroadcastReceiver
{
    //Intent mIntent;
    //Context mContext;
    //AlarmManager mAlarmManager;
    //PendingIntent mPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("BootReceiver", "onReceive() -> Make PendingIntent for calling DummyService");
        //Toast.makeText(context, "IntroActivity : onReceive() -> Make PendingIntent for calling DummyService", Toast.LENGTH_SHORT);
        //mContext = context;
        //mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        //mIntent = new Intent(mContext, DummyCallReceiver.class);
        //intent.setAction(DummyCallReceiver.START_DUMMY_SERVICE);

        //mPendingIntent.getBroadcast(mContext, 0, mIntent, 0);
        /** 10초에 한번씩 DummyCallReceiver 호출 */
        //mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 10000, mPendingIntent);
    }
}
