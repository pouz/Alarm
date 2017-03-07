package com.pouz.alarm.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by PouZ on 2017-02-24.
 */

public class AlarmService extends Service {
    // TODO: need to find another way to communicate between Service and Receiver(SmsReceiver <-> AlarmService, SmsReceiver <-> AlarmStopService)
    private AlarmAuth mAlarmAuth;

    private final static int VIBRATE_DELAY_TIME = 2000;
    private final static int DURATION_OF_VIBRATION = 1000;
    private final static int VOLUME_INCREASE_DELAY = 600;
    private final static float MAX_VOLUME = 1.0f;

    private static int sCurrentPosition = 0;

    private MediaPlayer mPlayer;
    private Vibrator mVibrator;

    private Handler mHandler = new Handler();
    private Runnable mVibrationRunnable = new Runnable() {
        @Override
        public void run() {
            mVibrator.vibrate(DURATION_OF_VIBRATION);
            // Provide loop for vibration
            mHandler.postDelayed(mVibrationRunnable, DURATION_OF_VIBRATION + VIBRATE_DELAY_TIME);
        }
    };

    private Runnable mVolumeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null) {
                AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);

                mPlayer.setVolume(MAX_VOLUME, MAX_VOLUME);
                am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

                mHandler.postDelayed(mVolumeRunnable, VOLUME_INCREASE_DELAY);
            }
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener()
    {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra)
        {
            mp.stop();
            mp.release();
            mHandler.removeCallbacksAndMessages(null);
            AlarmService.this.stopSelf(); // Class.this.do() ??
            return false;
        }
    };

    @Override
    public void onCreate()
    {
        HandlerThread ht = new HandlerThread("alarm_service");
        ht.start();
        mHandler = new Handler(ht.getLooper());

        mAlarmAuth = AlarmAuth.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("AlarmService", "Start Service");
        startPlayer();
        // Start the activity where toy can top alarm.. we don't need it
        return Service.START_NOT_STICKY;
        //return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        // TODO: 종료 시점에 쓰레드를 해지하지 않아도 서비스가 종료되면 알아서 터지나 확인
        Log.e("AlarmService", "Exit Service");
        if (mAlarmAuth.isIsAlarmActive())
        {
//            if(mPlayer != null)
//                sCurrentPosition = mPlayer.getCurrentPosition();

            Intent broadcastIntent = new Intent("com.pouz.alarm.receiver.AlarmStopReceiver");
            sendBroadcast(broadcastIntent);
            return;
        }

        if (mPlayer.isPlaying())
        {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null; // for GC

        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void startPlayer()
    {
        //mPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mPlayer = new MediaPlayer();
        mPlayer.setOnErrorListener(mErrorListener);

        try
        {
            // add vibration to alarm alert if it is set
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            // Player setup is here
            String ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();

            mPlayer.setDataSource(this, Uri.parse(ringtone));
            mPlayer.setLooping(true);
            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mPlayer.setVolume(MAX_VOLUME, MAX_VOLUME);
            //mPlayer.seekTo(sCurrentPosition);
            mPlayer.prepare();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mPlayer.start();
                    mHandler.post(mVibrationRunnable);
                    mHandler.postDelayed(mVolumeRunnable, VOLUME_INCREASE_DELAY);
                }
            });
        } catch (Exception e)
        {
            if (mPlayer.isPlaying())
            {
                mPlayer.stop();
            }
            stopSelf();
        }
    }
}
