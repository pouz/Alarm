package com.pouz.alarm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by PouZ on 2017-02-24.
 */

public class AlarmService extends Service
{
    // TODO: need to find another way to communicate between Service and Receiver(SmsReceiver <-> AlarmService, SmsReceiver <-> AlarmStopService)
    public static boolean ALARM_ACTIVITY;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("AlarmService", "Start Service");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("AlarmService", "Exit Service");
        if(ALARM_ACTIVITY)
        {
            Intent broadcastIntent = new Intent("com.pouz.alarm.receiver.AlarmStopReceiver");
            sendBroadcast(broadcastIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
