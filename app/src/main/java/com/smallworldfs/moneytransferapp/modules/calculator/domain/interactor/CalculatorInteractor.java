package com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;

/**
 * Created by luismiguel on 27/6/17.
 */

public interface CalculatorInteractor extends Interactor {

    interface Callback {
        void onBeneficiaryInfoAvailable();

        void onRetrievingBeneficiaryMethodsError();

        void totalSaleCalculated(double totalSaleCalculated, RateValues mCurrentRate, String mCurrencyType, Pair<String, String> mSendingCurrency, Promotion promotion, CalculatorPromotion mandatoryPromotion);

        void onRetrievingRateError();
    }

    interface PassiveCallback {
        void onCalculatorChanges(String amountBeneficiaryReceive, String amountYouPay, String currencyPayout);

        void onCalculatorUpdatedWithBeneficiary(BeneficiaryUIModel beneficiary);

        void onCalculatorError();
    }

}
