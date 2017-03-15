package com.pouz.alarm.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.pouz.alarm.utils.SmsSender;
import com.pouz.alarm.utils.Utils;
import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;
import com.pouz.alarm.data.AlarmState;
import com.pouz.alarm.service.AlarmService;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by PouZ on 2017-02-22.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final int AVAILABLE_AND_ALARM_START = 1;
    private static final int AVAILABLE_AND_ALARM_END = 0;
    private static final int NO_AVAILABLE = -1;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private AlarmsLocalDataSource mAlarmsLocalDataSource;
    private Context mContext;

    private StringBuilder mPhoneNumber;
    private StringBuilder mMessageBody;

    private AlarmState mAlarmState;



    @Override
    public void onReceive(Context context, Intent intent) {
        mAlarmsLocalDataSource = AlarmsLocalDataSource.getInstance(context);
        mContext = context;
        mAlarmState = AlarmState.getInstance();

        mPhoneNumber = new StringBuilder();
        mMessageBody = new StringBuilder();

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle == null)
                return;

            Object[] pdusObj = (Object[]) bundle.get("pdus");
            if (pdusObj == null)
                return;

            SmsMessage[] messages = new SmsMessage[pdusObj.length];
            for (int i = 0; i < pdusObj.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

            for (SmsMessage smsMessage : messages) {
                mMessageBody.append(smsMessage.getMessageBody());
                mPhoneNumber.append(smsMessage.getOriginatingAddress());
            }
//            showToast("SMS : " + mMessageBody + " From " + mPhoneNumber);
            play_alarm_if_requested_alarm_is_available();
        }
    }

    private void play_alarm_if_requested_alarm_is_available() {
        mAlarmsLocalDataSource.getAlarms(new AlarmsDataSource.LoadAlarmsCallBack() {
            @Override
            public void onAlarmsLoaded(List<Alarm> alarms) {
                int isAvailable = get_result_requested_alarm_is_in_alarm_list(alarms);
                if (isAvailable == AVAILABLE_AND_ALARM_START)
                    runAlarm();
                else if (isAvailable == AVAILABLE_AND_ALARM_END)
                    stopAlarm();
                else if (isAvailable == NO_AVAILABLE)
                    return;
            }
        });
    }

    private int get_result_requested_alarm_is_in_alarm_list(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            if (alarm.getPhoneNumber().toString().replaceAll("[()\\s-]+", "").equals(mPhoneNumber.toString())) {

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                int currentTime = Utils.timeToInt(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                Log.i("SmsReceiver", "getAlarms - Phone number is matched");

                if (!(mAlarmState.isAlarmActive()) &&
                        alarm.isActivate() &&
                        isAvailableDay(alarm.getSetDayOfWeek()) &&
                        isStartKeyword(alarm.getStartKeyword()) &&
                        isAvailableTime(alarm.getStartTime(), alarm.getEndTime(), currentTime))
                /** activate an alarm service */
                    return AVAILABLE_AND_ALARM_START;
                else if (mAlarmState.getAlarmAuthor().equals(mPhoneNumber.toString()) &&
                        isEndKeyword(alarm.getEndKeyword()))
                    return AVAILABLE_AND_ALARM_END;
            }
        }
        return NO_AVAILABLE;
    }

    private void showToast(String toastString) {
        Toast.makeText(mContext, toastString, Toast.LENGTH_SHORT).show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;

        return false;
    }

    private void stopAlarm() {
        showToast("알람멈춤");
        changeAlarmState(false);
        SmsSender.sendEndAlarmSMS(mPhoneNumber.toString());
        mContext.startService(makeIntent());
        mAlarmState.releaseInstance();
    }

    private void runAlarm() {
        showToast("알람울림");
        changeAlarmState(true);
        SmsSender.sendStartAlarmSMS(mPhoneNumber.toString());
        mContext.startService(makeIntent());
    }

    private Intent makeIntent() {
        Intent serviceIntent = new Intent(mContext, AlarmService.class);
        Log.i("SmsReceiver", "alarm_activation :: " + mAlarmState.isAlarmActive());
        serviceIntent.putExtra("alarm_activation", mAlarmState.isAlarmActive());
        return serviceIntent;
    }

    private void changeAlarmState(boolean isAlarmOccupied) {
        if (isAlarmOccupied) {
            mAlarmState.setIsAlarmActive(true);
            mAlarmState.setAlarmAuthor(mPhoneNumber.toString());
            Log.i("changeAlarmState", mAlarmState.isAlarmActive() + " :: " + mAlarmState.getAlarmAuthor());
        } else {
            mAlarmState.setIsAlarmActive(false);
            mAlarmState.setAlarmAuthor("");
            Log.i("changeAlarmState", mAlarmState.isAlarmActive() + " :: " + mAlarmState.getAlarmAuthor());
        }
    }

    private boolean isStartKeyword(String startKeyword) {
        return startKeyword.equals(mMessageBody.toString());
    }

    private boolean isEndKeyword(String endKeyword) {
        return endKeyword.equals(mMessageBody.toString());
    }

    private boolean isAvailableTime(int startTime, int endTime, int currentTime) {
        if (startTime > endTime) {
            if ((currentTime > startTime && currentTime > endTime) ||
                    (currentTime < startTime && currentTime < endTime) ||
                    (currentTime < startTime && currentTime > endTime))
                return true;
        } else {
            if (startTime <= currentTime && currentTime <= endTime)
                return true;
        }

        return false;
    }

    private boolean isAvailableDay(int setDayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
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
