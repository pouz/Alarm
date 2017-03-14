package com.pouz.alarm.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.pouz.alarm.data.Alarm;

import java.util.ArrayList;

/**
 * Created by white on 2017-03-14.
 */

public class SmsSender {
    private static final String SEND_START_ALARM_SMS = "알람 시작됨";
    private static final String SEND_END_ALARM_SMS = "알람 멈춤";

    public static void sendAddAlarmSMS(Context context, Alarm alarm, boolean askDialog) {
        sendSMSWithDialog(context, alarm.getPhoneNumber().toString(),
                "당신을 알람 송신자로 지정하였습니다.\n" +
                        "문자를 통해 알람을 울릴 수 있습니다.\n" +
                        "설정시간 [" + Utils.intToTime(alarm.getStartTime()) + " ~ " + Utils.intToTime(alarm.getEndTime()) + "]\n" +
                        "시작단어 [" + alarm.getStartKeyword() + "]\n" +
                        "종료단어 [" + alarm.getEndKeyword() + "]\n" + alarm.getDayOfWeek(), askDialog, true);
    }

    public static void sendDeleteAlarmSMS(Context context, Alarm alarm, boolean askDialog) {
        sendSMSWithDialog(context, alarm.getPhoneNumber().toString(),
                "당신을 알람 송신자로부터 삭제하였습니다.\n" +
                        "설정시간 [" + Utils.intToTime(alarm.getStartTime()) + " ~ " + Utils.intToTime(alarm.getEndTime()) + "]\n" +
                        "시작단어 [" + alarm.getStartKeyword() + "]\n" +
                        "종료단어 [" + alarm.getEndKeyword() + "]\n" + alarm.getDayOfWeek(), askDialog, false);
    }

    public static void sendModifiedAlarmSMS(Context context, Alarm alarm, boolean askDialog) {
        sendSMSWithDialog(context, alarm.getPhoneNumber().toString(),
                "당신의 알람 송신자설정을 다음과 같이 수정하였습니다.\n" +
                        "설정시간 [" + Utils.intToTime(alarm.getStartTime()) + " ~ " + Utils.intToTime(alarm.getEndTime()) + "]\n" +
                        "시작단어 [" + alarm.getStartKeyword() + "]\n" +
                        "종료단어 [" + alarm.getEndKeyword() + "]\n" + alarm.getDayOfWeek(), askDialog, true);

    }

    public static void sendStartAlarmSMS(Context context, String mobile) {
        sendSMS(context, mobile, SEND_START_ALARM_SMS);
    }

    public static void sendEndAlarmSMS(Context context, String mobile) {
        sendSMS(context, mobile, SEND_END_ALARM_SMS);
    }

    public static void sendSMSWithDialog(final Context context, final String mobile, final String text, boolean askDialog, final boolean closeActivity) {
        if (askDialog) {
            AlertDialog.Builder alt_db = new AlertDialog.Builder(context);
            alt_db.setTitle("알림 문자")
                    .setMessage("대상에게 확인 문자를 보내시겠습니까?")
                    .setCancelable(true)
                    .setNegativeButton("보내지 않음", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(closeActivity)
                                closeActivity((Activity) context);
                        }
                    })
                    .setPositiveButton("보냄", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendSMS(context, mobile, text);
                            if(closeActivity)
                                closeActivity((Activity) context);
                        }
                    }).show();

        } else {
            sendSMS(context, mobile, text);
            if(closeActivity)
                closeActivity((Activity) context);
        }

    }

    private static void closeActivity(Activity context) {
        (context).setResult(Activity.RESULT_OK);
        (context).finish();
    }

    private static void sendSMS(Context context, String mobile, String text) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        Log.i("sendSMS", mobile + " :: " + text);
//
//        final BroadcastReceiver br_send_result = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        // 전송 성공
//                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        // 전송 실패
//                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        // 서비스 지역 아님
//                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        // 무선 꺼짐
//                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        // PDU 실패
//                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                }
//            }
//        };
//
//        final BroadcastReceiver br_delivered_result = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        // 도착 완료
//                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        // 도착 안됨
//                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
//                        context.unregisterReceiver(this);
//                        break;
//                }
//            }
//        };
//
//        context.registerReceiver(br_send_result, new IntentFilter("SMS_SENT_ACTION"));
//        context.registerReceiver(br_delivered_result, new IntentFilter("SMS_DELIVERED_ACTION"));
//        SmsManager.getDefault().sendTextMessage(mobile, null, text, sentIntent, deliveredIntent);
//        SmsManager.getDefault().sendTextMessage(mobile, null, text, null, null);
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgArray = smsManager.divideMessage(text);
        smsManager.sendMultipartTextMessage(mobile, null, msgArray, null, null);
    }
}
