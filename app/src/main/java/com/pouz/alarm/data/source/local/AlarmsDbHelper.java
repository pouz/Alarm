package com.pouz.alarm.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PouzAlarm.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmsPersistenceContract.AlarmEntry.TABLE_NAME + " (" +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_START_TIME + INTEGER_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_END_TIME + INTEGER_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_START_KEYWORD + TEXT_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_END_KEYWORD + TEXT_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_SET_DAY_OF_WEEK + BOOLEAN_TYPE + COMMA_SEP +
                    AlarmsPersistenceContract.AlarmEntry.COLUMN_NAME_IS_ACTIVATE + BOOLEAN_TYPE +
            " )";

    public AlarmsDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
