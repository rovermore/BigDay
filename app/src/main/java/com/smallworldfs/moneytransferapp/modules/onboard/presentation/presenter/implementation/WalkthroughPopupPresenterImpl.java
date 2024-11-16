package com.smallworldfs.moneytransferapp.modules.onboard.presentation.presenter.implementation;

import android.content.Context;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.AbstractPresenter;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.onboard.presentation.presenter.WalkthroughPopupPresenter;
import com.smallworldfs.moneytransferapp.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

/**
 * Created by luismiguel on 13/11/17
 */
@Singleton
public class WalkthroughPopupPresenterImpl extends AbstractPresenter implements WalkthroughPopupPresenter {

    private WalkthroughPopupPresenter.View mView;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public WalkthroughPopupPresenterImpl(Scheduler observeOn, Context context, WalkthroughPopupPresenter.View view) {
        super(observeOn, context);
        this.mView = view;

        WalkthroughPopupPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        WalkthroughPopupPresenterImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void create() {
        User user = userDataRepository.retrieveUser();
        String userName = "";
        if (user != null && !user.getStatus().equalsIgnoreCase(Constants.UserType.LIMITED)) {
            userName = user.getName();
        }
        mView.configureView(userName);
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
