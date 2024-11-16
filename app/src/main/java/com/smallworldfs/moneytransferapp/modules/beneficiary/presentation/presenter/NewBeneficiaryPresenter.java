package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter;


import android.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.TransactionalPresenter;

/**
 * Created by pedro del castillo on 9/9/17.
 */

public interface NewBeneficiaryPresenter extends BasePresenter {
    interface View extends TransactionalPresenter.View {
        void configureCountryView(Pair<String, String> country);

        String getBeneficiaryCountry();

        void finishWithSuccess(String beneficiaryName);

        void trackBrazeError(NewGenericError.ErrorType error, String beneficiaryName);
    }
}
