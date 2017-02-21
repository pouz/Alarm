package com.pouz.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;

/**
 * Created by PouZ on 2017-02-17.
 */

public class TextMessageReceiver extends BroadcastReceiver
{
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    AlarmsLocalDataSource mAlarmsData;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //mAlarmsData = AlarmsLocalDataSource.getInstance(context);
        Log.e("TextMessageReceiver : ", "onReceive()");

        if (intent.getAction().equals(SMS_RECEIVED))
        {
            StringBuilder sms = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;

            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null)
                return;

            SmsMessage[] messages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

            for(SmsMessage smsMessage : messages)
                sms.append(smsMessage.getMessageBody());

            Log.e("SMS Receiver : " , sms.toString());
        }
    }
}
