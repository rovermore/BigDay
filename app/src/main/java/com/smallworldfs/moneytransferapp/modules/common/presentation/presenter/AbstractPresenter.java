package com.smallworldfs.moneytransferapp.modules.common.presentation.presenter;

import android.content.Context;
import androidx.core.util.Pair;

import rx.Scheduler;

/**
 * Created by pedro on 13/2/17
 */
public abstract class AbstractPresenter {

    protected Scheduler mObserveOn;
    protected Context mContext;

    public AbstractPresenter(Scheduler observeOn, Context context) {
        mObserveOn = observeOn;
        mContext = context;
    }
}
