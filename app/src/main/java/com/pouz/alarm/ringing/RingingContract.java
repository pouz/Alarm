package com.pouz.alarm.ringing;

import com.pouz.alarm.BasePresenter;
import com.pouz.alarm.BaseView;
import com.pouz.alarm.data.Alarm;

/**
 * Created by PouZ on 2017-02-22.
 */

public interface RingingContract
{
    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter
    {

    }
}
