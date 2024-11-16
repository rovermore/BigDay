package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.DEFAULT_AMOUNT;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.CalculatorInteractor;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Taxes;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.home.domain.interactors.SendToInteractor;
import com.smallworldfs.moneytransferapp.modules.home.domain.interactors.implementation.SendToInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.interactors.PromotionsCodeInteractor;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.interactors.implementation.PromotionsCodeInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.SendToPresenter;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.util.ArrayList;
import java.util.TreeMap;

import rx.Scheduler;

public class SendToPresenterImpl extends GenericPresenterImpl implements SendToPresenter, SendToInteractor.Callback, CalculatorInteractor.Callback, PromotionsCodeInteractor.Callback, CalculatorInteractor.PassiveCallback {

    private static final String TAG = "SendToPresenter";

    private final SendToPresenter.View mView;
    private final Activity mActivity;
    private final SendToInteractorImpl mInteractor;


    // Temp values
    private Pair<String, String> mUserCountry;
    private Pair<String, String> mBeneficiaryCountry;

    private boolean mBeneficiaryReceiveBlocked = false;
    private boolean mYouPayBlocked = false;
    private boolean mIsInPassiveMode = false;

    // Track current amount
    private String mCurrentAmount = "";
    private String mCurrentCurrencyCoin = "";

    // Track current beneficiary selected
    private BeneficiaryUIModel mBeneficiarySelected = null;
    private String mYouPay;

    private Taxes taxes;

    private double totalSale = -1;
    private String sendingCurrency = "";
    private boolean updateCalculator = false;

    public SendToPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, SendToPresenter.View view, Activity activity) {
        super(observeOn, context);
        this.mView = view;
        this.mActivity = activity;
        this.mInteractor = new SendToInteractorImpl(observeOn, susbscribeOn, this);
    }

    @Override
    public void create() {
        CalculatorInteractorImpl.getInstance().addCallback(this);
        PromotionsCodeInteractorImpl.getInstance().addCallback(this);

        mView.configureView();
    }

    @Override
    public void resume() {
        // Remove passive callbacks
        exitFromPassiveMode();
        requestInfo();
        if (updateCalculator) {
            updateCalculatorView(totalSale, sendingCurrency);
            updateCalculator = false;
        }
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.RESET_VALUE) {
            mView.showYouPayCalculatorLoadingView();
            mView.showBeneficiaryCalculatorLoadingView();
            if (mBeneficiarySelected != null) {
                CalculatorInteractorImpl.getInstance().updateCalculatorWithBeneficiary(null, mBeneficiarySelected);
            } else {
                CalculatorInteractorImpl.getInstance().refreshCalculator();
            }
            mView.resetPromotionsView();
            mYouPayBlocked = true;
            String amountBeneficiaryReceive = AmountFormatter.normalizeDoubleString(CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount());
            mView.updateTheyReceiveEditText(amountBeneficiaryReceive);
            String amountYouPay = AmountFormatter.normalizeDoubleString(CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount());
            mView.updateYouPayEditText(amountYouPay);

            String amount;
            if (CalculatorInteractorImpl.getInstance().getCurrencyType().equals(TOTALSALE)) {
                amount = amountYouPay;
            } else {
                amount = amountBeneficiaryReceive;
            }
            onAmmountReadyToCalculate(amount, CalculatorInteractorImpl.getInstance().getCurrencyType());

            mYouPayBlocked = false;
            CalculatorInteractorImpl.getInstance().setCalculatorStatus(CalculatorInteractorImpl.CalculatorStatus.OK);
        }
        // Recovery status
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.OK) {
            // Recovery Calculator changes
            onBeneficiaryInfoAvailable();
            onYouPayInfoAvailable();
        }
    }

    private void onYouPayInfoAvailable() {
        final CalculatorData calculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();
        if (calculatorData != null) {
            final RateValues rateValues = calculatorData.getCurrentCalculator();
            if (rateValues != null) {
                mView.updateBottomRateIndicators(rateValues.getRate(), rateValues.getTotalFee(),
                        rateValues.getTaxes(), CalculatorInteractorImpl.getInstance().getSendingCurrency());
            }
        }
    }

    @Override
    public void pause() {
        // Add pasive callback to track calculator events
        enterInPassiveMode();
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
        CalculatorInteractorImpl.getInstance().removeCallback(this);
        CalculatorInteractorImpl.getInstance().removePassiveCallback(this);
        PromotionsCodeInteractorImpl.getInstance().removeCallback(this);

        if (mInteractor != null) {
            mInteractor.destroy();
        }
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }


    private void requestInfo() {
        // Get origin countries
        mInteractor.requestBeneficiaries(null);
    }


    public void onSelectCountryPopupClick() {
        if (mUserCountry != null) {
            mView.showCountryBeneficiaryPopup(mUserCountry);
        }
    }


    public void onSelectPromoCode(int requestCode, String payoutCountry) {
        HomeNavigator.showSelectPromoCodeActivity(mActivity, requestCode, payoutCountry);
    }

    public void onSelectCountryPayOutFromPopupDialog(Pair<String, String> country) {

        mView.hideErrorView();
        mView.configureCountrySelectorHeader(country);
        mView.hideYouPayCalculatorViewError();

        // Save to the next session
        mInteractor.setPayoutCountrySelected(country);

        requestPromotions(country.first);

        // Request Rate depends on new payout countries, if diferent that previous payout countries
        if (CalculatorInteractorImpl.getInstance().getCurrentPayoutCountry() != null &&
                !CalculatorInteractorImpl.getInstance().getCurrentPayoutCountry().first.equals(country.first)) {
            mView.showTotalCalculatorLoadingView();
            CalculatorInteractorImpl.getInstance().updateCalculatorWithPayoutCountry(country, mCurrentAmount);
        }
    }

    public void onSelectBeneficiaryInList(BeneficiaryUIModel beneficiary) {
        if (beneficiary != null && !beneficiary.getPayoutCountry().getIso3().isEmpty()) {

            mBeneficiarySelected = beneficiary;
            mView.changeReceivesLabel(mBeneficiarySelected.getName());

            requestPromotions(beneficiary.getPayoutCountry().getIso3());

            mView.showTotalCalculatorLoadingView();
            mView.hideYouPayCalculatorViewError();
            mView.disableYouPayReceiveTextWatcher();
            CalculatorInteractorImpl.getInstance().setYouPayAmount(DEFAULT_AMOUNT);
            mView.updateYouPayEditText(AmountFormatter.normalizeDoubleString(CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount()));
            CalculatorInteractorImpl.getInstance().updateCalculatorWithBeneficiary(null, mBeneficiarySelected);
            mView.enableYouPayReceiveTextWatcher();
        }
    }

    private void requestPromotions(String payoutCountry) {
        // Clean Promotions
        if (mInteractor.getUserPromotion() == null) {
            mInteractor.cleanAllPromotions();
            mInteractor.requestPromotions(payoutCountry);
            mView.resetPromotionsView();
        }
    }

    public void onSelectCurrencyInCalculator(Pair<String, String> currency) {
        // Request new Rate depends on new currency
        mView.showYouPayCalculatorLoadingView();
        mView.showBeneficiaryCalculatorLoadingView();
        CalculatorInteractorImpl.getInstance().updateCalculatorWithCurrency(currency);
        mView.hideYouPayCalculatorViewError();
    }

    public void onNewBeneficiaryActionSelected() {
        HomeNavigator.navigateToCreateBeneficiaryActivity(mActivity);
    }

    public void setBlockedBeneficiaryEditText(boolean blocked) {
        this.mBeneficiaryReceiveBlocked = blocked;
    }

    public void setBlockedYouPayEditText(boolean blocked) {
        this.mYouPayBlocked = blocked;
    }

    public boolean showWelcome() {
        return mInteractor.showWelcome();
    }

    public void setWelcomeShown(boolean shown) {
        mInteractor.setWelcomeShown(shown);
    }

    private void enterInPassiveMode() {
        mIsInPassiveMode = true;
        mBeneficiaryReceiveBlocked = true;
        mYouPayBlocked = true;
        CalculatorInteractorImpl.getInstance().addPassiveCallback(this);
    }

    private void exitFromPassiveMode() {
        mIsInPassiveMode = false;
        mBeneficiaryReceiveBlocked = false;
        mYouPayBlocked = false;
        CalculatorInteractorImpl.getInstance().removePassiveCallback(this);
    }


    /**
     * Set amount and operation to operate in local
     */
    public void onAmmountReadyToCalculate(String amount, String currencyType) {
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.OK || CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.RESET_VALUE) {
            if (currencyType.equals(Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL)) {
                // Updating track amount to keep value changing countries, beneficiary, ...
                mCurrentAmount = amount;
                double youPay = CalculatorInteractorImpl.getInstance().localCalculate(mCurrentAmount, currencyType);
                if (taxes != null && taxes.getPercentage() != null && !taxes.getPercentage().isEmpty() && taxes.getTaxCode().equalsIgnoreCase("IOF")) {
                    double iofAmount = (youPay - CalculatorInteractorImpl.getInstance().getCalculatorData()
                            .getCurrentCalculator().getTotalFee() + CalculatorInteractorImpl.getInstance().getTotalDiscount()) * Double.parseDouble(taxes.getPercentage()) / 100;
                    if (iofAmount < 0) {
                        iofAmount = 0;
                    }
                    updateIofAmount(iofAmount);
                }
                updateYouPayValue(youPay);
                mYouPay = null;
            } else {
                mYouPay = amount;
                double totalSale = CalculatorInteractorImpl.getInstance().localCalculate(amount, currencyType);
                if (taxes != null && taxes.getPercentage() != null && !taxes.getPercentage().isEmpty() && taxes.getTaxCode().equalsIgnoreCase("IOF")) {
                    double iofAmount = ((totalSale / CalculatorInteractorImpl.getInstance().getCalculatorData().getCurrentCalculator().getRate())
                            + CalculatorInteractorImpl.getInstance().getTotalDiscount()) * Double.parseDouble(taxes.getPercentage()) / 100;
                    if (iofAmount < 0) {
                        iofAmount = 0;
                    }
                    updateIofAmount(iofAmount);
                }
                updateBeneficiaryValue(totalSale);
                mCurrentAmount = AmountFormatter.formatDoubleAmountNumber(totalSale);
            }
            CalculatorInteractorImpl.getInstance().setCurrencyType(currencyType);
            Log.d(TAG, "Calculate ->" + mCurrentAmount + " operation: " + currencyType);
            Log.d(TAG, "new value: " + CalculatorInteractorImpl.getInstance().localCalculate(amount, currencyType));
            Log.d(TAG, "-------------------------");
        }
    }

    public String getCurrentAmountInScreen() {
        return CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount();
    }

    public boolean isBlockedBeneficiaryReceive() {
        return this.mBeneficiaryReceiveBlocked;
    }

    public boolean isBlockedYouPay() {
        return this.mYouPayBlocked;
    }

    private void updateYouPayValue(double totalSale) {
        mYouPayBlocked = true;
        mView.updateYouPayEditText(AmountFormatter.formatDoubleAmountNumber(totalSale));
        mYouPayBlocked = false;
    }

    private void updateBeneficiaryValue(double totalSale) {
        mBeneficiaryReceiveBlocked = true;
        mView.updateTheyReceiveEditText(AmountFormatter.formatDoubleAmountNumber(totalSale));
        mBeneficiaryReceiveBlocked = false;
    }

    private void updateIofAmount(double iofAmount) {
        mView.updateIofAmount(AmountFormatter.formatDoubleAmountNumber(iofAmount), mCurrentCurrencyCoin, taxes);
    }

    public void retryGeneralClick() {
        mView.hideGlobalErrorEmptyView();
        mView.showGeneralLoadingView();
    }


    public void retryCalculatorClick() {
        mView.hideLocalCalculatorError();
    }

    public String getCurrentCurrencyCoin() {
        return this.mCurrentCurrencyCoin;
    }

    public void continueButtonClick() {
        if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.OK) {
            if (mInteractor.synchronizeCalculatorBeneficiaryMethod(mBeneficiarySelected)) {
                if (mBeneficiarySelected != null) {
                    HomeNavigator.showTransactionalActivity(mActivity, mBeneficiarySelected, mYouPay, mBeneficiarySelected.getBeneficiaryType());
                } else {
                    if (mBeneficiaryCountry != null && mBeneficiaryCountry.first != null && !TextUtils.isEmpty(mBeneficiaryCountry.first) && mBeneficiaryCountry.second != null && !TextUtils.isEmpty(mBeneficiaryCountry.second)) {
                        HomeNavigator.navigateToC2BActivity(mActivity, mBeneficiaryCountry, true);
                    } else {
                        HomeNavigator.navigateToC2BActivity(mActivity, mUserCountry, true);
                    }
                }
            } else {
                mView.showErrorView(SmallWorldApplication.getStr(R.string.no_calculate_message_error_text), SmallWorldApplication.getStr(R.string.no_calculate_message_error_subtitle_text));
            }
        } else {
            mView.showErrorView(SmallWorldApplication.getStr(R.string.no_calculate_message_error_text), SmallWorldApplication.getStr(R.string.no_calculate_message_error_subtitle_text));
        }
    }

    public void resetBehavior() {
        mView.showGeneralLoadingView();
        mView.showTotalCalculatorLoadingView();
        mView.resetBeneficiaryAdapterValues();
        CalculatorInteractorImpl.getInstance().setCalculatorStatus(CalculatorInteractorImpl.CalculatorStatus.LOADING_DATA);

        requestInfo();
    }


    /**
     * Interactor Callbacks
     */

    @Override
    public void onHeaderInfoReady(Pair<String, String> payoutCountry, Pair<String, String> baseCountryOrigin, Pair<String, String> firstCountryInSelector) {

        mView.configureCountrySelectorHeader(firstCountryInSelector);

        mUserCountry = baseCountryOrigin;

        if (mInteractor.getListBeneficiaries().size() > 0) {
            // show beneficiary list
            mView.showListBeneficiaries();
            BeneficiaryUIModel newBeneficiary = null;
            for (BeneficiaryUIModel beneficiary : mInteractor.getListBeneficiaries()) {
                if (beneficiary.isNew()) {
                    newBeneficiary = beneficiary;
                    break;
                }
            }

            if (newBeneficiary != null) {
                mBeneficiarySelected = newBeneficiary;
                Pair<String, String> payout = new Pair<>(newBeneficiary.getPayoutCountry().getIso3(), newBeneficiary.getPayoutCountry().getName());
                mView.configureCountrySelectorHeader(payout);
            } else if (mBeneficiarySelected != null) {
                Pair<String, String> payout = new Pair<>(mBeneficiarySelected.getPayoutCountry().getIso3(), mBeneficiarySelected.getPayoutCountry().getName());
                mView.configureCountrySelectorHeader(payout);
            } else {
                mBeneficiarySelected = mInteractor.getListBeneficiaries().get(0);
            }

            mView.configureBeneficiariesListHeader(mInteractor.getListBeneficiaries(), mBeneficiarySelected);
            mView.changeReceivesLabel(mBeneficiarySelected.getName());

            // So, we can request calculator info
            CalculatorInteractorImpl.getInstance().updateCalculatorWithBeneficiary(baseCountryOrigin, mBeneficiarySelected);
        } else {
            // show countries header
            mView.showCountrySelector();

            // So, we can request calculator info
            CalculatorInteractorImpl.getInstance().updateCalculatorWithOriginAndPayout(baseCountryOrigin, payoutCountry);
        }

        mView.hideGeneralLoadingView();

    }


    @Override
    public void onRetrievingHeaderInfoError() {
        mView.hideGeneralLoadingView();
        mView.showGlobalErrorEmptyView();
    }

    /**
     * Calculator Callbacks
     */

    //-------------
    // CALCULATOR
    //-------------
    @Override
    public void onBeneficiaryInfoAvailable() {
        if (!mIsInPassiveMode) {
            Method selectedPaymentMethod = CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod();
            if (selectedPaymentMethod != null && selectedPaymentMethod.getCurrencies() != null && selectedPaymentMethod.getCurrencies().size() >= 0) {
                ArrayList<Pair<String, String>> currencies = new ArrayList<>();

                for (TreeMap<String, String> currency : selectedPaymentMethod.getCurrencies()) {
                    currencies.add(new Pair<>(currency.firstEntry().getKey(),
                            currency.firstEntry().getValue()));
                }
                mView.updateBeneficiaryReceiveInfo(currencies,
                        CalculatorInteractorImpl.getInstance().getPayoutCurrency());
            }
        }
    }

    @Override
    public void onRetrievingBeneficiaryMethodsError() {
        if (!mIsInPassiveMode) {
            mView.showLocalCalculatorError();
            CalculatorInteractorImpl.getInstance().refreshCalculator();
        }
    }

    @Override
    public void totalSaleCalculated(double totalSale, RateValues currentRate, String operationType, Pair<String, String> sendingCurrency, Promotion promotion, CalculatorPromotion mandatoryPromotion) {
        mView.updateBottomRateIndicators(currentRate.getRate(), currentRate.getTotalFee(),
                currentRate.getTaxes(), sendingCurrency.first);
        if (!mIsInPassiveMode) {
            updateCalculator = false;
            updateCalculatorView(totalSale, sendingCurrency.first);
        } else {
            this.totalSale = totalSale;
            this.sendingCurrency = sendingCurrency.first;
            updateCalculator = true;
        }
        checkPromotions(mandatoryPromotion, promotion, true);
    }

    private void updateCalculatorView(double totalSale, String sendingCurrency) {
        if (totalSale != -1 && !sendingCurrency.isEmpty()) {
            if (CalculatorInteractorImpl.getInstance().getCurrencyType().equals(Constants.CALCULATOR.TOTALSALE)) {
                updateBeneficiaryValue(totalSale);
            } else {
                updateYouPayValue(totalSale);
            }
            // update current currency
            mCurrentCurrencyCoin = sendingCurrency;
        } else {
            mView.showYouPayCalculatorViewError();
            checkPromotions(null, null, false);
        }
        mView.hideYouPayCalculatorLoadingView();
        mView.hideBeneficiaryCalculatorLoadingView();
    }

    @Override
    public void onRetrievingRateError() {
        if (!mIsInPassiveMode) {
            updateCalculator = false;
            mView.showYouPayCalculatorViewError();
            mView.hideYouPayCalculatorLoadingView();
            mView.hideBeneficiaryCalculatorLoadingView();
            checkPromotions(null, null, false);
        } else {
            totalSale = -1;
            sendingCurrency = "";
            updateCalculator = true;
        }
    }

    private void checkPromotions(CalculatorPromotion calculatorPromotion, Promotion promotion, boolean checkLocal) {
        if (!checkLocal) {
            calculatorPromotion = mInteractor.getCurrentCalculatorPromotion();
            promotion = mInteractor.getUserPromotion();
        }

        // Check Promotions
        if (calculatorPromotion != null) {
            // Exist server calculator promotion
            mView.drawCalculatorPromotion(calculatorPromotion);
        } else {
            if (promotion != null) {
                // User has promotion selected from list
                mView.drawUserPromotion(promotion);
            } else {
                mView.drawPlaceHolderPromotions(mInteractor.havePromotionsForContext());
            }
        }
    }

    //---------------------
    // PASIVE CALCULATOR
    //---------------------

    @Override
    public void onCalculatorChanges(String amountBeneficiaryReceive, String amountYouPay, String currencyPayout) {
        mView.updateAmountsFromPassiveMode(amountBeneficiaryReceive, amountYouPay, currencyPayout);
    }

    @Override
    public void onCalculatorUpdatedWithBeneficiary(BeneficiaryUIModel beneficiary) {
        mBeneficiarySelected = beneficiary;
        mView.changeReceivesLabel(beneficiary.getName());
        mView.updateSelectedBeneficiary(beneficiary);
    }

    /**
     * Promotions Callback
     */
    @Override
    public void onUserChangeSelectedPromotion() {
        // Listener to update calculator if user select a promotion
        CalculatorInteractorImpl.getInstance().refreshCalculator();
        mView.resetPromotionsView();
        mView.hideYouPayCalculatorViewError();
        mView.showYouPayCalculatorLoadingView();
        mView.showBeneficiaryCalculatorLoadingView();
    }

    @Override
    public void onPromotionInvalid() {
        // not aplicate
    }

    @Override
    public void onPromotionCheckedSusccessfull() {
        CalculatorInteractorImpl.getInstance().refreshCalculator();
        mView.resetPromotionsView();
        mView.showYouPayCalculatorLoadingView();
        mView.showBeneficiaryCalculatorLoadingView();
    }


    public void normalizeValue(int viewId, String value) {
        if (viewId == R.id.they_receive_edittext) {
            mBeneficiaryReceiveBlocked = true;
            mView.updateTheyReceiveEditText(AmountFormatter.normalizeDoubleString(value));
            mBeneficiaryReceiveBlocked = false;

        } else if (viewId == R.id.you_pay_edittext) {
            mYouPayBlocked = true;
            mView.updateYouPayEditText(AmountFormatter.normalizeDoubleString(value));
            mYouPayBlocked = false;
        }

    }

    public BeneficiaryUIModel getBeneficiarySelected() {
        return mBeneficiarySelected;
    }

    public void setmBeneficiaryCountry(Pair<String, String> country) {
        mBeneficiaryCountry = country;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @Override
    public void onCalculatorError() {
    }

    public void navigateToCountrySelector() {
        HomeNavigator.navigateToCountrySelector(mActivity);
    }
}
