package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Taxes;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;

import java.util.ArrayList;

public interface SendToPresenter extends BasePresenter {

    interface View {

        void configureView();

        void showCountryBeneficiaryPopup(Pair<String, String> mOriginCountry);

        void configureBeneficiariesListHeader(ArrayList<BeneficiaryUIModel> listBeneficiary, BeneficiaryUIModel beneficiarySelected);

        void configureCountrySelectorHeader(Pair<String, String> country);

        void showListBeneficiaries();

        void showCountrySelector();

        void showGeneralLoadingView();

        void hideGeneralLoadingView();

        void showErrorView(String errorMessage, String errorSubtitleMessage);

        void hideErrorView();

        void showTotalCalculatorLoadingView();

        void showBeneficiaryCalculatorLoadingView();

        void hideBeneficiaryCalculatorLoadingView();

        void showYouPayCalculatorLoadingView();

        void hideYouPayCalculatorLoadingView();

        void updateTheyReceiveEditText(String formattedAmount);

        void updateYouPayEditText(String formattedAmount);

        void updateIofAmount(String formattedIofAmount, String sendingCurrency, Taxes taxes);

        void updateBeneficiaryReceiveInfo(ArrayList<Pair<String, String>> currencies, String currencySelected);

        void updateBottomRateIndicators(double rate, double totalFee, Taxes taxes, String sendingCurrency);

        void hideGlobalErrorEmptyView();

        void showGlobalErrorEmptyView();

        void showLocalCalculatorError();

        void hideLocalCalculatorError();

        void showYouPayCalculatorViewError();

        void hideYouPayCalculatorViewError();

        void drawCalculatorPromotion(CalculatorPromotion calculatorPromotion);

        void drawUserPromotion(Promotion promotion);

        void drawPlaceHolderPromotions(boolean notifyAvailablesPromotions);

        void resetPromotionsView();

        void updateAmountsFromPassiveMode(String beneficiaryReceiveAmount, String youPayAmount, String currencyPayout);

        void resetBeneficiaryAdapterValues();

        void changeReceivesLabel(String name);

        void updateSelectedBeneficiary(BeneficiaryUIModel position);

        void showProgressDialog(boolean show, String title, String content, boolean showTitle);

        void enableYouPayReceiveTextWatcher();

        void disableYouPayReceiveTextWatcher();
    }
}
