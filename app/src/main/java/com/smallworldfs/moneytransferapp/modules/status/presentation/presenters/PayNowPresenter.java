package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentUrl;

/**
 * Created by luismiguel on 9/10/17.
 */

public interface PayNowPresenter extends BasePresenter {
    interface View {
        void configureView();
        void showHideLoadingView(boolean show);
        void setPaymentUrl(PaymentUrl url);
        void showHideGeneralErrorView(boolean show);
        void finish();
    }
}
