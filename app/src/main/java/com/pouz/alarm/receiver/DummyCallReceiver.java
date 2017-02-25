package com.pouz.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.pouz.alarm.service.DummyService;

public class DummyCallReceiver extends BroadcastReceiver
{
    public static final String START_DUMMY_SERVICE = "com.pouz.alarm.service.DummyCallReceiver";

    public DummyCallReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(START_DUMMY_SERVICE))
        {
            Log.i("DummyCallReceiver", "onReceive -> START_DUMMY_SERVICE");
            Intent i = new Intent(context, DummyService.class);
            context.startService(i);
        }
    }
}
