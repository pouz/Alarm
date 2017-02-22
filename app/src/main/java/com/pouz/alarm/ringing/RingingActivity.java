package com.pouz.alarm.ringing;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.pouz.alarm.R;

public class RingingActivity extends FragmentActivity
{
    RingingContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /** requetWindowFeature는 View속성을 다루기 전에 선언되어야 한다. */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ringing);

        RingingFragment ringingFragment = (RingingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame_ringing);
        if(ringingFragment == null)
        {
            ringingFragment = RingingFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame_ringing, ringingFragment);
            transaction.commit();
        }

        mPresenter = new RingingPresenter(null, ringingFragment);
    }
}
