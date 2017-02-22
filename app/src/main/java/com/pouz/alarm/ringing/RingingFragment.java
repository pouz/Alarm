package com.pouz.alarm.ringing;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pouz.alarm.R;
import com.pouz.alarm.data.Alarm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RingingFragment extends Fragment implements RingingContract.View
{
    private RingingContract.Presenter mPresenter;

    @Override
    public void setPresenter(RingingContract.Presenter presenter)
    {
       mPresenter = presenter;
    }

    public static RingingFragment newInstance()
    {
        return new RingingFragment();
    }

    public RingingFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ringing, container, false);
    }

}
