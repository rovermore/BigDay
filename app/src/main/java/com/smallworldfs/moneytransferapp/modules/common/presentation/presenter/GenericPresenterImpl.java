package com.smallworldfs.moneytransferapp.modules.common.presentation.presenter;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.preferences.UserPreferences;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;

import rx.Scheduler;

public class GenericPresenterImpl extends AbstractPresenter implements BasePresenter {

    public GenericActivity mActivity;

    public GenericPresenterImpl(Scheduler observeOn, Context context) {
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

    public boolean checkIfShouldShowTransactionWalkthrough() {
        UserPreferences prefs = new UserPreferences();
        return !prefs.isFirstTransactionWalkThroughShowed();
    }
}
