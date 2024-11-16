package com.smallworldfs.moneytransferapp.modules.status.domain.interactor;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentUrl;

import java.io.File;

/**
 * Created by luismiguel on 9/10/17.
 */

public interface PayNowInteractor extends Interactor {
    interface Callback {
        void onGetPaymentUrlSusccessfull(PaymentUrl url);
        void onGetPaymentUrlError();
        void onTransactionReceived(Transaction transaction);
        void onPdfReceiptReceived(File file);
        void onPdfReceiptError();
    }
}
