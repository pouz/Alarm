package com.pouz.alarm.alarms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pouz.alarm.R;
import com.pouz.alarm.data.Alarm;
import com.pouz.alarm.utils.SmsSender;
import com.pouz.alarm.utils.Utils;
import com.pouz.alarm.data.AlarmState;
import com.pouz.alarm.addeditalarm.AddEditAlarmActivity;
import com.pouz.alarm.addeditalarm.AddEditAlarmFragment;

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
                        .setMessage("이름 [" + longClickedAlarm.getName() + "]\n" +
                                "전화번호 [" + longClickedAlarm.getPhoneNumber() + "]\n" +
                                "설정시간 [" + Utils.intToTime(longClickedAlarm.getStartTime()) + " ~ " + Utils.intToTime(longClickedAlarm.getEndTime()) + "]\n" +
                                "시작단어 [" + longClickedAlarm.getStartKeyword() + "]\n" +
                                "종료단어 [" + longClickedAlarm.getEndKeyword() + "]\n" + longClickedAlarm.getDayOfWeek())
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
                                        SmsSender.sendDeleteAlarmSMS(getContext(), longClickedAlarm, true);
                                    }
                                }).setNegativeButton("아니오", null).show();
                    }
                }).show();
            }
        }, mPresenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_alarms, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_alarms_add:
                mPresenter.addAlarm();
                return true;
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarms, container, false);

        final ListView listView = (ListView) root.findViewById(R.id.alarms_list);
        listView.setAdapter(mListAdapter);

        setHasOptionsMenu(true);

        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-4268007252677003/9029086373");
        final AdView adView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();

                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
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

            View rawView = getViewFromInflaterIfViewIsNull(viewGroup, view);

            final Alarm alarm = getItem(i);

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
                    if (!AlarmState.getInstance().isAlarmAuthor(alarm)) {
                        if (alarm.isActivate()) {
                            alarm.setIsActivate(false);
                            activationIV.setAlpha(MIN_ALPHA);

                        } else {
                            alarm.setIsActivate(true);
                            activationIV.setAlpha(MAX_ALPHA);
                        }

                        mPresenter.updateAlarm(alarm);
                    } else {
                        mPresenter.notifyToUser("알람을 울린 사용자의 알람은 울림 도중에 수정 및 삭제를 할 수 없습니다!");
                    }
                }
            });

            rawView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!AlarmState.getInstance().isAlarmAuthor(alarm)) {
                        mItemListener.onAlarmLongClick(alarm, v);
                    } else {
                        mPresenter.notifyToUser("알람을 울린 사용자의 알람은 울림 도중에 수정 및 삭제를 할 수 없습니다!");
                    }
                    return true;
                }
            });
            return rawView;
        }

        private View getViewFromInflaterIfViewIsNull(ViewGroup viewGroup, View view) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                view = inflater.inflate(R.layout.alarm_item, viewGroup, false);
            }
            return view;
        }
    }

    interface AlarmItemListener {
        void onAlarmLongClick(Alarm longClickedAlarm, View view);
    }

    @Override
    public void showSnackbar(String string) {
        Snackbar.make(getView(), string, Snackbar.LENGTH_LONG).setDuration(2000).show();
    }
}
