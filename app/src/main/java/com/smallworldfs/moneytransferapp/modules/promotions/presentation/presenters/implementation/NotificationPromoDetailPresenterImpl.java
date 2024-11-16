package com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.implementation;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.promotions.presentation.presenters.NotificationPromoDetailPresenter;

import rx.Scheduler;

/**
 * Created by pedro del castillo on 14/11/17
 */
public class NotificationPromoDetailPresenterImpl extends GenericPresenterImpl implements NotificationPromoDetailPresenter {

    private View mView;

    public NotificationPromoDetailPresenterImpl(Scheduler observeOn, Context context, View view, GenericActivity activity) {
        super(observeOn, context);
        this.mContext = context;
        this.mView = view;
        this.mActivity = activity;
    }

    @Override
    public void create() {

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
