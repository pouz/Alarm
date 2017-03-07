package com.pouz.alarm.alarms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pouz.alarm.R;
import com.pouz.alarm.addeditalarm.AddEditAlarmFragment;
import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PouZ on 2017-02-17.
 */

public class AlarmsFragment extends Fragment implements AlarmsContract.View {

    private AlarmsContract.Presenter mPresenter;
    private AlarmAdapter mListAdapter;

    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }

    /**
     * class override
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new AlarmAdapter(new ArrayList<Alarm>(0), new AlarmItemListener() {
            @Override
            public void onAlarmLongClick(final Alarm longClickedAlarm, View view) {
                AlertDialog.Builder alt_db = new AlertDialog.Builder(getContext());
                alt_db.setTitle("알람 정보")
                        .setMessage("Name [" + longClickedAlarm.getName() + "]\n" +
                                "Mobile [" + longClickedAlarm.getPhoneNumber() + "]\n" +
                                "Setting Time [" + longClickedAlarm.getStartTime() + " ~ " + longClickedAlarm.getEndTime() + "]\n" +
                                "Start Keyword [" + longClickedAlarm.getStartKeyword() + "]\n" +
                                "End Keyword [" + longClickedAlarm.getEndKeyword() + "]\n" + longClickedAlarm.getDayOfWeek())
                        .setCancelable(true)
                        .setNegativeButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.editAlarm(AddEditAlarmFragment.EDIT_MODE, longClickedAlarm);
                            }
                        }).setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alt_db = new AlertDialog.Builder(getContext());
                        alt_db.setTitle("삭제")
                                .setMessage("정말로 삭제 하시겠습니까?")
                                .setCancelable(true)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPresenter.deleteAlarm(longClickedAlarm.getID());
                                    }
                                }).setNegativeButton("아니오", null).show();
                    }
                }).show();
            }
        }, mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarms, container, false);

        ListView listView = (ListView) root.findViewById(R.id.alarms_list);
        listView.setAdapter(mListAdapter);
        //listView.setSelector(R.drawable.selector);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.alarms_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addAlarm();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.canChildScrollUp();  // it makes to be able to scroll up
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadAlarms(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("AlarmsFragment : ", "onResume()");
        mPresenter.start();
    }

    /**
     * Implement AlarmsContract.View
     */
    @Override
    public void showAddAlarm() {
        Log.e("showAddAlarm() ", "AlarmsFragment");
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        intent.putExtra("mode", AddEditAlarmFragment.ADD_MODE);
        startActivity(intent);
    }

    @Override
    public void showEditAlarm(int mode, Alarm alarm) {
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        intent.putExtra("mode", AddEditAlarmFragment.EDIT_MODE);
        intent.putExtra("alarm", alarm);
        startActivity(intent);
    }

    @Override
    public void showAlarms(List<Alarm> alarms) {
        Log.i("AlarmsFragment : ", "showAlarms()");
        mListAdapter.replaceData(alarms);
    }

    @Override
    public void setPresenter(AlarmsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * AlarmAdapter section
     */
    private static class AlarmAdapter extends BaseAdapter //implements
    {
        private List<Alarm> mAlarms;
        private AlarmItemListener mItemListener;
        private AlarmsContract.Presenter mPresenter;

        private static final int MAX_ALPHA = 200;
        private static final int MIN_ALPHA = 30;

        /**
         * local functions
         */
        public AlarmAdapter(final List<Alarm> alarms, final AlarmItemListener itemListener, final AlarmsContract.Presenter presenter) {
            setList(alarms);
            mItemListener = itemListener;
            mPresenter = presenter;
        }

        private void setList(List<Alarm> alarms) {
            mAlarms = alarms;
        }

        public void replaceData(List<Alarm> alarms) {
            Log.i("AlarmsFragment : ", "replaceData()");
            setList(alarms);
            notifyDataSetChanged();
        }

        /**
         * override BaseAdapter functions
         */
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

            // 전에는 mAlarm에 다이렉트로했는데 안됨. 멤버변수말고 지역변수로 하니 됨
            // 중간에 mAlarm을 뭔가 건드리는 가봄...
            final Alarm alarm = getItem(i);

            //mAlarm = getItem(i);

            final TextView timeAV = (TextView) rawView.findViewById(R.id.list_item_time);
            final TextView nameAV = (TextView) rawView.findViewById(R.id.list_item_name);
            final ImageView activationIV = (ImageView) rawView.findViewById(R.id.list_activation_check);

            timeAV.setText(Utils.intToTime(alarm.getStartTime()) + " - " + Utils.intToTime(alarm.getEndTime()));
            nameAV.setText(alarm.getName() + " [" + alarm.getPhoneNumber() + "]");

            if (alarm.isActivate()) {
                activationIV.setAlpha(MAX_ALPHA);
            } else {
                activationIV.setAlpha(MIN_ALPHA);
            }
            activationIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alarm.isActivate()) {
                        alarm.setIsActivate(false);
                        activationIV.setAlpha(MIN_ALPHA);

                    } else {
                        alarm.setIsActivate(true);
                        activationIV.setAlpha(MAX_ALPHA);
                    }
                    mPresenter.updateAlarm(alarm);
                }
            });

            rawView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemListener.onAlarmLongClick(alarm, v);
                    return true;
                }
            });

            return rawView;
        }
    }

    interface AlarmItemListener {
        void onAlarmLongClick(Alarm longClickedAlarm, View view);
    }
}
