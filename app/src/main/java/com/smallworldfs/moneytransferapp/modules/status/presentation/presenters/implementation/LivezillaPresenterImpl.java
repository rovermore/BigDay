package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import android.app.Activity;
import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.LivezillaPresenter;

import rx.Scheduler;

/**
 * Created by michel on 27/6/18.
 */

public class LivezillaPresenterImpl extends GenericPresenterImpl {

    private static final String TAG = LivezillaPresenter.class.getSimpleName();

    private LivezillaPresenter.View mView;
    private Activity mActivity;

    public LivezillaPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, LivezillaPresenter.View view, Activity activity) {
        super(observeOn, context);
        this.mView = view;
        this.mActivity = activity;
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public void onRetryClick(){
        mView.configureView();
    }
}
