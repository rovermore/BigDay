package com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter;

import android.graphics.drawable.Drawable;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionCheckOutDialogSummary;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by luismiguel on 28/9/17.
 */

public interface CheckoutDialogPresenter extends BasePresenter {
    interface View {
        void configureView(String mStyle, Transaction mTransactionData);
        void checkErrorsNow();
        void fillListBlockErrors(ArrayList<TransactionErrors> mListBlockErrors);
        void fillSummary(ArrayList<TransactionErrors> header, TransactionCheckOutDialogSummary summary);
        void setupPayNowButton(String transactionsStringStatus, Drawable drawableSecondaryActionButton);
        void setupTransferDetailsButton(String transactionsStringStatus, Drawable drawableSecondaryActionButton);
        void closeDialogAndShowTransactionDetails(Transaction transaction);
        void helpRequested();
        void closeDialogAndPayNow(String mtn);
    }
}
