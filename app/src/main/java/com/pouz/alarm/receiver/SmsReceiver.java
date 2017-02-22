package com.pouz.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.pouz.alarm.Utils.Utils;
import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;
import com.pouz.alarm.ringing.RingingActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by PouZ on 2017-02-22.
 */

public class SmsReceiver extends BroadcastReceiver
{
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private AlarmsLocalDataSource mAlarmsLocalDataSource;
    private Context mContext;

    private StringBuilder mPhoneNumber;
    private StringBuilder mMessageBody;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        mAlarmsLocalDataSource = AlarmsLocalDataSource.getInstance(context);
        mContext = context;

        mPhoneNumber = new StringBuilder();
        mMessageBody = new StringBuilder();

        if (intent.getAction().equals(SMS_RECEIVED))
        {
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;

            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null)
                return;

            SmsMessage[] messages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

            for (SmsMessage smsMessage : messages)
            {
                mMessageBody.append(smsMessage.getMessageBody());
                mPhoneNumber.append(smsMessage.getOriginatingAddress());
            }
            Toast.makeText(context, "SMS : " + mMessageBody + " From " + mPhoneNumber, Toast.LENGTH_SHORT).show();
            getAlarms();
        }
    }

    private void getAlarms()
    {
        mAlarmsLocalDataSource.getAlarms(new AlarmsDataSource.LoadAlarmsCallBack()
        {
            @Override
            public void onAlarmsLoaded(List<Alarm> alarms)
            {
                for (Alarm alarm : alarms)
                {
                    if (alarm.getPhoneNumber().toString().replaceAll("[()\\s-]+", "").equals(mPhoneNumber.toString()))
                    {
                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
                        int currentTime = Utils.timeToInt(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                        if (isAvailableDay(alarm.getSetDayOfWeek()) &&
                                isStartKeyword(alarm.getStartKeyword()) &&
                                isAvailableTime(alarm.getStartTime(), alarm.getEndTime(), currentTime))
                        {
                            Toast.makeText(mContext, "알람울림", Toast.LENGTH_SHORT).show();
                            doAlarmRing();
                            return;
                        }
                    }
                }
            }
        });
    }

    private void doAlarmRing()
    {
        Intent intent = new Intent(mContext, RingingActivity.class);
        mContext.startActivity(intent);
    }

    private boolean isStartKeyword(String startKeyword)
    {
        return startKeyword.equals(mMessageBody.toString());
    }

    /**
     * endTime이 startTime보다 클때(일반적 경우)
     * startTime이 endTime보다 클때(설정 갭이 커서 24를 지나는 경우)
     */
    private boolean isAvailableTime(int startTime, int endTime, int currentTime)
    {
        if (startTime > endTime)
        {
            if ((currentTime > startTime && currentTime > endTime) ||
                    (currentTime < startTime && currentTime < endTime) ||
                    (currentTime < startTime && currentTime > endTime))
                return true;
        } else
        {
            if (startTime <= currentTime && currentTime <= endTime)
                return true;
        }

        return false;
    }

    private boolean isAvailableDay(int setDayOfWeek)
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day)
        {
            case Calendar.SUNDAY:
                if ((64 & setDayOfWeek) == 64)
                    return true;
                break;
            case Calendar.MONDAY:
                if ((1 & setDayOfWeek) == 1)
                    return true;
                break;
            case Calendar.TUESDAY:
                if ((2 & setDayOfWeek) == 2)
                    return true;
                break;
            case Calendar.WEDNESDAY:
                if ((4 & setDayOfWeek) == 4)
                    return true;
                break;
            case Calendar.THURSDAY:
                if ((8 & setDayOfWeek) == 8)
                    return true;
                break;
            case Calendar.FRIDAY:
                if ((16 & setDayOfWeek) == 16)
                    return true;
                break;
            case Calendar.SATURDAY:
                if ((32 & setDayOfWeek) == 32)
                    return true;
                break;
        }

        return false;
    }
}
