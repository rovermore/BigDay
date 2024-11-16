package com.smallworldfs.moneytransferapp.modules.flinks.presentation.presenter.implementation;

import android.app.Activity;
import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.interactor.FlinksInteractor;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.interactor.implementation.FlinksInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.flinks.presentation.presenter.FlinksPresenter;

import rx.Scheduler;

public class FlinksPresenterImpl extends GenericPresenterImpl implements FlinksPresenter, FlinksInteractor.Callback {

    private FlinksInteractorImpl mInteractor;
    private FlinksPresenter.View mView;
    private Activity mActivity;

    public FlinksPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, FlinksPresenter.View view, Activity activity) {
        super(observeOn, context);

        this.mView = view;
        this.mInteractor = new FlinksInteractorImpl(observeOn, susbscribeOn, this);
        this.mActivity = activity;

    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
        mInteractor.run();
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
        mInteractor.destroy();
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    public void verifyFlinkState(String url){
        mView.showHideValidationView(true);
        mInteractor.verifyFlinksState(url);
    }

    @Override
    public void onFlinksUrlReady(String url) {
        mView.showHideLoadingView(false);
        mView.startWebViewWithdUrl(url);
    }

    @Override
    public void onFlinksUrlError() {
        mView.showHideLoadingView(false);
        mView.showHideGeneralErrorView(true);
    }

    @Override
    public void onFlinksVerificationFinished() {
        mView.showHideValidationView(false);
        mView.closeActivity();
    }

    @Override
    public void onFlinksVerificationError() {
        mView.showHideValidationView(false);
        mView.showHideGeneralErrorView(true);
    }

    public void retryButtonClick() {
        mView.showHideLoadingView(true);
        mView.showHideGeneralErrorView(false);
        mInteractor.run();
    }

}
