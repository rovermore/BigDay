package com.smallworldfs.moneytransferapp.modules.calculator.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

/**
 * Created by luismiguel on 11/7/17.
 */

public interface TransactionalCalculatorPresenter extends BasePresenter {
    interface View {
        void configureView();

        void configureCalculator(String payoutCountryKey, String currentBeneficiaryAmount, String currentYouPayAmount, String sendingCurrency, String first, Method currentMethod);

        void openCalculator(boolean disableAnimation);

        void updateYouPayEditText(String amount);

        void updateTheyReceiveEditText(String amount);

        void showYouPayCalculatorLoadingView();

        void hideYouPayCalculatorLoadingView();

        void hideYouPayCalculatorViewError();

        void showYouPayCalculatorViewError();

        void showBeneficiaryLoadingView();

        void hideBeneficiaryLoadingView();

        void showBeneficiaryContainer();

        void hideBeneficiaryContainer();

        void showYouPayInfoContainer();

        void hideYouPayInfoContainer();

        void closeCalculator(boolean recoveryData, boolean disableAnimation);

        void enableDisableDoneButton(boolean enable);

        void enableDoneMode();
    }
}
