package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import android.app.Activity;
import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.PayNowInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation.PayNowInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentUrl;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.PayNowPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.navigator.TransactionalNavigator;

import java.io.File;

import rx.Scheduler;

/**
 * Created by luismiguel on 9/10/1
 */
public class PayNowPresenterImpl extends GenericPresenterImpl implements PayNowPresenter, PayNowInteractor.Callback {

    private PayNowInteractorImpl mInteractor;
    private PayNowPresenter.View mView;
    private Activity mActivity;
    private String urlToShow;
    private String mTransactionMtn;

    public PayNowPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, View view, String transactionMtn, String urlToShow, Activity activity) {
        super(observeOn, context);

        this.mView = view;
        this.mInteractor = new PayNowInteractorImpl(observeOn, susbscribeOn, transactionMtn, this);
        this.mActivity = activity;
        this.urlToShow = urlToShow;
        this.mTransactionMtn = transactionMtn;
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
        if (urlToShow == null || urlToShow.isEmpty()) {
            mInteractor.run();
        } else {
            PaymentUrl paymentUrl = new PaymentUrl();
            paymentUrl.setUrl(urlToShow);
            paymentUrl.setHttp("GET");
            onGetPaymentUrlSusccessfull(paymentUrl);
        }
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
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onGetPaymentUrlSusccessfull(PaymentUrl paymentUrl) {
        mView.showHideLoadingView(false);
        mView.setPaymentUrl(paymentUrl);
    }

    @Override
    public void onGetPaymentUrlError() {
        mView.showHideLoadingView(false);
        mView.showHideGeneralErrorView(true);
    }

    public void retryButtonClick() {
        mView.showHideLoadingView(true);
        mView.showHideGeneralErrorView(false);
        mInteractor.run();
    }

    public void trackEcomerce(String mtn) {
        mInteractor.requestTransactionData(mtn);
    }

    @Override
    public void onTransactionReceived(Transaction transactionsResponse) {
    }

    /**
     * Show receipt
     */
    public void showReceipt() {
        mInteractor.getReceipt(mTransactionMtn, false, false);
    }

    @Override
    public void onPdfReceiptReceived(File file) {
        TransactionalNavigator.navigateToPdfReceiptDetail(mActivity, file, mContext);
        mView.finish();
    }

    @Override
    public void onPdfReceiptError() {
        mView.finish();
    }
}
