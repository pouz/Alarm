package com.pouz.alarm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 *  It's just dummy service for AlarmService
 *  SMS 브로드 캐스트를 리시버가 받으면서 어플리케이션이 작동하는데
 *  성능이 좋지 못하거나 제조사별로 어플리캐이션의 SMS 브로드캐스트에 작동하는 리시버를
 *  억제하는 경우가 있다.
 *  그래서 어플리캐이션 실행 후 또는 디바이스가 부팅했을 때 DummyService를 작동시켜(AlarmManager를 통해서 주기적으로 살림)
 *  com.pouz.alarm 내에 모든 service와 receiver가 작동할 수 있게 함.
 */

// TODO: 이게 어떤 원리로 구동이 되는지, Service 자체가 package에 종속되는지 확인이 필요
public class DummyService extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("DummyService", "onStartCommand(). Doing nothing. Just Dummy");
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Log.i("DummyService", "onDestroy(). Just checking what onDestroy() called");
    }

    public DummyService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        return null;
    }
}

