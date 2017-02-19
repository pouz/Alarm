package com.pouz.alarm.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsPersistenceContract
{
    private AlarmsPersistenceContract() {}

    public static abstract class AlarmEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_START_KEYWORD = "start_keyword";
        public static final String COLUM_NAME_END_KEYWORD = "end_keyword";
        public static final String COLUM_NAME_SET_DAY_OF_WEEK = "set_day_of_week";
        public static final String COLUM_NAME_IS_ACTIVAT = "is_active";
    }
}
