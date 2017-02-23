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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("AlarmService", "Exit Service");
        Intent broadcastIntent = new Intent("com.pouz.alarm.receiver.AlarmServiceReceiver");
        sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
