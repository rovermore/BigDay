package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter;


import android.util.Pair;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.TransactionalPresenter;

/**
 * Created by pedro del castillo on 9/9/17
 */
public interface EditBeneficiaryPresenter extends BasePresenter {
    interface View extends TransactionalPresenter.View {

        void configureCountryView(Pair<String, String> country);
        void showSaveButton();
        void setSelectedDeliveryMethod(Method deliveryMethod);
        void showDeliveryMethodChangeWarning();
        void backToPreviousScreenWithResultOK(Beneficiary beneficiary, int v);
    }
}
