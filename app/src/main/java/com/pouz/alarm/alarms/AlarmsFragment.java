package com.pouz.alarm.alarms;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pouz.alarm.R;
import com.pouz.alarm.Utils.Utils;
import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.data.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsFragment extends Fragment implements AlarmsContract.View
{
    private AlarmsContract.Presenter mPresenter;
    private AlarmAdapter mListAdapter;
    private AlarmItemListener mAlarmItemListener = new AlarmItemListener()
    {
        @Override
        public void onAlarmClick(Alarm clickedAlarm)
        {

        }

        @Override
        public void onAlarmLongClick(Alarm longClickedAlarm)
        {
           mPresenter.deleteAlarm(longClickedAlarm.getID());
        }
    };

    public static AlarmsFragment newInstance()
    {
        return new AlarmsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mListAdapter = new AlarmAdapter(new ArrayList<Alarm>(0), mAlarmItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_alarms, container, false);

        ListView listView = (ListView) root.findViewById(R.id.alarms_list);
        listView.setAdapter(mListAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.canChildScrollUp();  // it makes to be able to scroll up
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mPresenter.loadAlarms(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i("AlarmsFragment : ", "onResume()");
        mPresenter.start();
    }

    @Override
    public void showAddAlarm()
    {
        Log.e("showAddAlarm() ", "AlarmsFragment");
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(intent, AddEditAlarmActivity.REQUEST_ADD_ALARM);
    }

    @Override
    public void showAlarms(List<Alarm> alarms)
    {
        Log.i("AlarmsFragment : ", "showAlarms()");
        mListAdapter.replaceData(alarms);
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    private static class AlarmAdapter extends BaseAdapter
    {
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
            Log.i("AlarmsFragment : ", "replaceData()");
            setList(alarms);
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return mAlarms.size();
        }

        @Override
        public Alarm getItem(int i)
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
            View rawView = view;

            if (rawView == null)
            {
                // TODO: Need to study about Inflater
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rawView = inflater.inflate(R.layout.alarm_item, viewGroup, false);
            }

            final Alarm alarm = getItem(i);

            TextView titleAV = (TextView) rawView.findViewById(R.id.list_item_title);
            titleAV.setText(alarm.getName() + "/" + Utils.intToTime(alarm.getStartTime()) + "~" + Utils.intToTime(alarm.getEndTime()));

            rawView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mItemListener.onAlarmClick(alarm);
                }
            });

            rawView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mItemListener.onAlarmLongClick(alarm);
                    return true;
                }
            });

            return rawView;
        }
    }

    public interface AlarmItemListener
    {
        void onAlarmClick(Alarm clickedAlarm);

        void onAlarmLongClick(Alarm longClickedAlarm);
    }
}
