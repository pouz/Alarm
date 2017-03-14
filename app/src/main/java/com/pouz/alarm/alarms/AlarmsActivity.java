package com.pouz.alarm.alarms;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pouz.alarm.R;
import com.pouz.alarm.data.source.local.AlarmsLocalDataSource;
import com.pouz.alarm.receiver.DeviceAdminReceiver;

public class AlarmsActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_FOR_SMS = 1;
    public static final int REQUEST_CODE_FOR_BOOT_COMPLETED = 2;
    public static final int REQUEST_CODE_FOR_INTERNET = 3;
    public static final int REQUEST_CODE_FOR_ACCESS_NETWORK_STATE = 4;
    public static final int REQUEST_CODE_FOR_SEND_SMS = 5;
    private AlarmsPresenter mAlarmPresenter;

    /**
     * 기기 관리자 설정 변수
     */
    public static final int ACTIVATION_REQUEST = 551;

    DevicePolicyManager mDevicePolicyManager;
    ComponentName mDeviceName;

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_FOR_SMS);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, REQUEST_CODE_FOR_BOOT_COMPLETED);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_FOR_INTERNET);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_FOR_ACCESS_NETWORK_STATE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_FOR_SEND_SMS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_FOR_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("RequestPermissionResult", "Granted SMS");
            } else {
                Log.e("RequestPermissionResult", "Failed SMS");
            }
            return;
        } else if (requestCode == REQUEST_CODE_FOR_BOOT_COMPLETED) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("RequestPermissionResult", "Granted Boot Completed");
            } else {
                Log.e("RequestPermissionResult", "Failed Boot Completed");
            }
            return;
        }
    }

    private void checkDeviceAdmin() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_DEVICE_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDeviceName = new ComponentName(this, DeviceAdminReceiver.class);
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "I need permission!");
            startActivityForResult(intent, ACTIVATION_REQUEST);
        }
        else {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.alarms_toolbar);
        setSupportActionBar(toolbar);

        AlarmsFragment alarmsFragment = (AlarmsFragment) getSupportFragmentManager().findFragmentById(R.id.alarms_fragment);
        if (alarmsFragment == null) {
            // add Fragment to Activity
            alarmsFragment = AlarmsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.alarms_fragment, alarmsFragment);
            transaction.commit();
        }

        mAlarmPresenter = new AlarmsPresenter(AlarmsLocalDataSource.getInstance(getApplicationContext()), alarmsFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                } else {

                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
