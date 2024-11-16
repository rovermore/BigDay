package com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.implementation;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.NotificationAnnouncementDetailPresenter;

import rx.Scheduler;

/**
 * Created by pedro del castillo on 14/11/17
 */
public class NotificationAnnouncementDetailPresenterImpl extends GenericPresenterImpl implements NotificationAnnouncementDetailPresenter {

    private View mView;

    public NotificationAnnouncementDetailPresenterImpl(Scheduler observeOn, View view, GenericActivity activity) {
        super(observeOn, SmallWorldApplication.getApp());
        this.mView = view;
        this.mActivity = activity;
    }

    @Override
    public void create() {
        mView.configureView();
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }
}
