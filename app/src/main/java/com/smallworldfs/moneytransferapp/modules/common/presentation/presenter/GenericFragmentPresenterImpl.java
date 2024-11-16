package com.smallworldfs.moneytransferapp.modules.common.presentation.presenter;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragmentActivity;

import rx.Scheduler;

public class GenericFragmentPresenterImpl extends AbstractPresenter implements BasePresenter {

    public GenericFragmentActivity mActivity;


    public GenericFragmentPresenterImpl(Scheduler observeOn, Context context) {
        super(observeOn, context);
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
