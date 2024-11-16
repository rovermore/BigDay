package com.smallworldfs.moneytransferapp.modules.tracking.presentation.implementation;

import android.app.Activity;
import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.AbstractPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.tracking.presentation.TrackerPresenter;
import com.smallworldfs.moneytransferapp.modules.tracking.ui.navigator.TrackingNavigator;

import rx.android.schedulers.AndroidSchedulers;

public class TrackerPresenterImpl extends AbstractPresenter implements TrackerPresenter.Presenter {

    private Activity activity;

    public TrackerPresenterImpl(Context context, Activity activity) {
        super(AndroidSchedulers.mainThread(), context);
        this.activity = activity;
    }


    @Override
    public void openQrTracker() {
        TrackingNavigator.navigateToQrTrackingActivity(activity);
    }

    @Override
    public void openMtnTracker() {
        TrackingNavigator.navigateToMtnTrackingActivity(activity);
    }
}
