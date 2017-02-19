package com.pouz.alarm.alarms;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pouz.alarm.R;
import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.data.Alarm;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsFragment extends Fragment implements AlarmsContract.View {
    private AlarmsContract.Presenter mPresenter;

    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }


    @Override
    public void showAddAlarm() {
        Log.e("showAddAlarm() ", "AlarmsFragment");
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(intent, AddEditAlarmActivity.REQUEST_ADD_ALARM);
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private static class AlarmAdapter extends BaseAdapter {
        // 여기서 하는게 실제 리스너들의 구현 방법이다 유념하자
        private List<Alarm> mAlarms;
        private AlarmItemListener mItemListener;

        public AlarmAdapter(List<Alarm> alarms, AlarmItemListener itemListener) {
            setList(alarms);
            mItemListener = itemListener;
        }

        private void setList(List<Alarm> alarms) {
            mAlarms = alarms;
        }

        public void replaceData(List<Alarm> alarms) {
            setList(alarms);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mAlarms.size();
        }

        @Override
        public Alarm getItem(int i) {
            return mAlarms.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rawView = view;

            if (rawView == null) {
                // TODO: Need to study about Inflater
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rawView = inflater.inflate(R.layout.alarm_item, viewGroup, false);
            }

            final Alarm alarm = getItem(i);

            TextView titleAV = (TextView) rawView.findViewById(R.id.list_item_title);
            titleAV.setText(alarm.getName() + "/" + alarm.getTime());

            CheckBox activateCB = (CheckBox) rawView.findViewById(R.id.list_item_activation);
            activateCB.setChecked(alarm.isActivate());

            if (alarm.isActivate()) {
                rawView.setBackgroundColor(Color.GRAY);
            } else {
                rawView.setBackgroundColor(Color.WHITE);
            }

            activateCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!alarm.isActivate())
                    {
                        mItemListener.onActivateAlarm(alarm);
                    }
                    else
                    {
                        mItemListener.onDeactivateAlarm(alarm);
                    }
                }
            });

            rawView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onTaskClick(alarm);
                }
            });

            return null;
        }
    }

    public interface AlarmItemListener {
        void onTaskClick(Alarm clickedAlarm);

        void onActivateAlarm(Alarm activatedAlarm);

        void onDeactivateAlarm(Alarm deactivatedAlarm);
    }
}
