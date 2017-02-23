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
        // TODO: 알람 리시버의 전역변수를 확인해서 죽은 서비스를 살릴 것인가 죽일 것인가를 판단한다. 좀 더 효율적인 방법은 없는가?
        /** we decide which we will revival a dead service from a static variable in SmsReceiver Class.
         *  Any solution more efficient?
         * */
        if (SmsReceiver.ALARM_ACTIVITY == true)
        {
            Log.i(AlarmServiceStopReceiver.class.getSimpleName(), "Service Stops!");
            context.startService(new Intent(context, AlarmService.class));
        }
    }
}
