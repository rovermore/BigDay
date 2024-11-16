package com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.US_COUNTRY_VALUE;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionCheckOutDialogSummary;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.CheckoutDialogPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import rx.Scheduler;

/**
 * Created by luismiguel on 28/9/17
 */
@Singleton
public class CheckoutDialogPresenterImpl extends GenericPresenterImpl implements CheckoutDialogPresenter {

    private String mStyle;
    private CheckoutDialogPresenter.View mView;
    private ArrayList<TransactionErrors> mListBlockErrors;
    private Transaction mTransactionData;
    private Activity mActivity;
    private TransactionCheckOutDialogSummary mSummary;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        LoginRepository provideLoginRepository();
    }

    public LoginRepository loginRepository;

    @Inject
    public CheckoutDialogPresenterImpl(Scheduler observeOn, Context context, String style, View view, ArrayList<TransactionErrors> listBlockErrors, Transaction transactionData, Activity activity, TransactionCheckOutDialogSummary summary) {
        super(observeOn, context);
        this.mStyle = style;
        this.mView = view;
        this.mListBlockErrors = listBlockErrors;
        this.mTransactionData = transactionData;
        this.mActivity = activity;
        this.mSummary = summary;

        CheckoutDialogPresenterImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CheckoutDialogPresenterImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
    }

    @Override
    public void create() {
        mView.configureView(mStyle, mTransactionData);

        // If the transaction was successfully show dialogs with warnings or summary (USA case), if not: show errors
        if (mTransactionData != null && !TextUtils.isEmpty(mTransactionData.getMtn())) {
            if (mListBlockErrors == null) {
                mListBlockErrors = new ArrayList<>();
            }
            mListBlockErrors.add(0, new TransactionErrors(SmallWorldApplication.getStr(R.string.order_placed_successfully), String.format(SmallWorldApplication.getStr(R.string.your_mtn_is), mTransactionData.getMtn())));

            // If the user country is USA show the summary only and not show errors
            User user = loginRepository.getUser();
            if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
                ArrayList<TransactionErrors> header = new ArrayList<>();
                header.add(0, new TransactionErrors(SmallWorldApplication.getStr(R.string.order_placed_successfully_usa), String.format(SmallWorldApplication.getStr(R.string.your_mtn_is), mTransactionData.getMtn())));
                mView.fillSummary(header, mSummary);
            } else {
                mView.fillListBlockErrors(mListBlockErrors);
            }
        } else {
            mView.fillListBlockErrors(mListBlockErrors);
        }


        // Check bottom buttons
        if (mTransactionData != null) {
            if (mTransactionData.getSecondaryActionType(mContext) == Transaction.SecondaryAction.AWAITING_PAYMENT) {
                // PAY NOW
                mView.setupPayNowButton(mTransactionData.getSecondaryActionButtonString(mContext),
                        mTransactionData.getDrawablePopupActionButton(mContext));
            } else {
                // TRANSFER DETAILS
                mView.setupTransferDetailsButton(mTransactionData.getSecondaryActionButtonString(mContext),
                        mTransactionData.getDrawablePopupActionButton(mContext));
            }
        }
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
        mTransactionData = null;
        mListBlockErrors = null;
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public void helpButtonClick() {
        mView.helpRequested();
    }

    public void checkNow() {
        mView.checkErrorsNow();
    }

    public void actionButtonClick() {
        String actionType = "";
        if (mTransactionData.getSecondaryActionType(mContext) == Transaction.SecondaryAction.AWAITING_PAYMENT) {
            // Pay now
            mView.closeDialogAndPayNow(mTransactionData.getMtn());
        } else {
            // Show transfer details
            mView.closeDialogAndShowTransactionDetails(mTransactionData);
        }
    }
}
