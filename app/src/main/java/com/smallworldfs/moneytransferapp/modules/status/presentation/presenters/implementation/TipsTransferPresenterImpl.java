package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.TransferTipsDetailsInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.TransferTipsDetailInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TipInfo;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.TipsTransferPresenter;

import java.util.ArrayList;

import rx.Scheduler;

/**
 * Created by luismiguel on 27/10/17
 */
public class TipsTransferPresenterImpl extends GenericPresenterImpl implements TipsTransferPresenter, TransferTipsDetailsInteractor.Callback {

    private TipsTransferPresenter.View mView;
    private TransferTipsDetailInteractorImpl mInteractor;


    public TipsTransferPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, TipsTransferPresenter.View view, String paymentMethod) {
        super(observeOn, context);
        this.mContext = context;
        this.mView = view;
        this.mInteractor = new TransferTipsDetailInteractorImpl(observeOn, susbscribeOn, paymentMethod, this);

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
        mInteractor.destroy();
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onTipsReceivedSusccessfull(ArrayList<TipInfo> data) {
        mView.showHideLoadingView(false);
        if(data != null && data.size() > 0){
            mView.drawAdditionalTips(data);
        }
    }

    @Override
    public void onTipsError() {
        mView.showHideLoadingView(false);
    }
}
