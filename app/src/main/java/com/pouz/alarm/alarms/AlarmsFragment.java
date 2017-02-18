package com.pouz.alarm.alarms;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.data.Alarm;

import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsFragment extends Fragment implements AlarmsContract.View
{
    private AlarmsContract.Presenter mPresenter;

    public static AlarmsFragment newInstance()
    {
        return new AlarmsFragment();
    }


    @Override
    public void showAddAlarm()
    {
        Log.e("showAddAlarm() ", "AlarmsFragment");
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(intent, AddEditAlarmActivity.REQUEST_ADD_ALARM);
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    private static class AlarmAdapter extends BaseAdapter
    {
        // 여기서 하는게 실제 리스너들의 구현 방법이다 유념하자
        private List<Alarm> mAlarms;
        private AlarmItemListener mItemListener;

        public AlarmAdapter(List<Alarm> alarms, AlarmItemListener itemListener)
        {
            setList(alarms);
            mItemListener = itemListener;
        }

        private void setList(List<Alarm> alarms)
        {
            mAlarms = alarms;
        }

        public void replaceData(List<Alarm> alarms)
        {
            setList(alarms);
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return mAlarms.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mAlarms.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {

            return null;
        }
    }

    public interface AlarmItemListener
    {
        void onTaskClick(Alarm clickedAlarm);
    }
}
