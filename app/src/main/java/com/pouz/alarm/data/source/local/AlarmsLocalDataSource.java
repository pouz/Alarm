package com.pouz.alarm.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.data.source.AlarmsDataSource;
import com.pouz.alarm.data.source.local.AlarmsPersistenceContract.AlarmEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsLocalDataSource implements  AlarmsDataSource
{
    private static AlarmsLocalDataSource INSTANCE;
    private AlarmsDbHelper mDbHelper;

    private AlarmsLocalDataSource(@NonNull Context context)
    {
        mDbHelper = new AlarmsDbHelper(context);
    }

    public static AlarmsLocalDataSource getInstance(@NonNull Context context)
    {
        if(INSTANCE == null) {
            INSTANCE = new AlarmsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAlarms(@NonNull LoadAlarmsCallBack callBack)
    {
        List<Alarm> alarms = new ArrayList<Alarm>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection =
                {
                        AlarmEntry.COLUMN_NAME_PHONE_NUMBER,
                        AlarmEntry.COLUMN_NAME_NAME,
                        AlarmEntry.COLUMN_NAME_TIME,
                        AlarmEntry.COLUMN_NAME_START_KEYWORD,
                        AlarmEntry.COLUM_NAME_END_KEYWORD,
                        AlarmEntry.COLUM_NAME_SET_DAY_OF_WEEK,
                        AlarmEntry.COLUM_NAME_IS_ACTIVATE,
                };

        // send query to DB for receiving all tasks
        Cursor c = db.query(AlarmEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0)
        {
            while(c.moveToNext())
            {
                String phoneNumber = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_PHONE_NUMBER));
                String name = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_NAME));
                String time = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_TIME));
                String startKeyword = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_START_KEYWORD));
                String endKeyword = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_END_KEYWORD));
                int setDayOfWeek = c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_SET_DAY_OF_WEEK));
                boolean mIsActivate = c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_IS_ACTIVATE)) == 1;

                Alarm alarm = new Alarm(time, name, phoneNumber, startKeyword, endKeyword, setDayOfWeek, mIsActivate);
                alarms.add(alarm);
                Log.i("DataSource : ", "getAlarms()");
                Log.i("Detail : ", alarm.toString() );
            }
        }
        if(c != null) {
            c.close();
        }

        db.close();

        callBack.onAlarmsLoaded(alarms);
    }

    @Override
    public void getAlarm(@NonNull GetAlarmCallBack callBack)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection =
                {
                        AlarmEntry.COLUMN_NAME_PHONE_NUMBER,
                        AlarmEntry.COLUMN_NAME_NAME,
                        AlarmEntry.COLUMN_NAME_TIME,
                        AlarmEntry.COLUMN_NAME_START_KEYWORD,
                        AlarmEntry.COLUM_NAME_END_KEYWORD,
                        AlarmEntry.COLUM_NAME_SET_DAY_OF_WEEK,
                        AlarmEntry.COLUM_NAME_IS_ACTIVATE
                };

        // send query to DB for receiving all tasks
        Cursor c = db.query(AlarmEntry.TABLE_NAME, projection, null, null, null, null, null);
        Alarm alarm = null;

        if (c != null && c.getCount() > 0)
        {
                String phoneNumber = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_PHONE_NUMBER));
                String name = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_NAME));
                String time = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_TIME));
                String startKeyword = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME_START_KEYWORD));
                String endKeyword = c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_END_KEYWORD));
                int setDayOfWeek = c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_SET_DAY_OF_WEEK));
                boolean mIsActivate = c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUM_NAME_IS_ACTIVATE)) == 1;

                alarm = new Alarm(time, name, phoneNumber, startKeyword, endKeyword, setDayOfWeek, mIsActivate);
        }
        if(c != null) {
            c.close();
        }

        db.close();

        if(alarm != null)
            callBack.onAlarmLoaded(alarm);
        else
            callBack.onDataNotAvailable();
    }

    @Override
    public void saveAlarm(@NonNull Alarm alarm)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AlarmEntry.COLUMN_NAME_PHONE_NUMBER, alarm.getPhoneNumber());
        values.put(AlarmEntry.COLUMN_NAME_NAME, alarm.getName());
        values.put(AlarmEntry.COLUMN_NAME_TIME, alarm.getTime());
        values.put(AlarmEntry.COLUMN_NAME_START_KEYWORD, alarm.getStartKeyword());
        values.put(AlarmEntry.COLUM_NAME_END_KEYWORD, alarm.getEndKeyword());
        values.put(AlarmEntry.COLUM_NAME_SET_DAY_OF_WEEK, alarm.getSetDayOfWeek());
        values.put(AlarmEntry.COLUM_NAME_IS_ACTIVATE, alarm.isActivate());

        db.insert(AlarmEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteAlarm(@NonNull String phoneNum)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = AlarmEntry.COLUMN_NAME_PHONE_NUMBER + " LIKE ?";
        String[] selectionArgs = {phoneNum};

        db.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
