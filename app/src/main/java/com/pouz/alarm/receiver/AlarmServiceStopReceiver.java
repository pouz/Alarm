package com.pouz.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pouz.alarm.service.AlarmService;

/**
 * Created by PouZ on 2017-02-24.
 */

public class AlarmServiceStopReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(AlarmServiceStopReceiver.class.getSimpleName(), "Service Stops!");
        context.startService(new Intent(context, AlarmService.class));
    }
}
