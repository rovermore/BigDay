package com.smallworldfs.moneytransferapp.modules.calculator.presentation.presenter.impl;

import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.presenter.TransactionalCalculatorPresenter;
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.ui.fragment.TransactionalCalculatorFragment;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;

import java.text.NumberFormat;
import java.util.Locale;

import rx.Scheduler;

/**
 * Created by luismiguel on 11/7/17
 */
public class TransactionalCalculatorPresenterImpl extends GenericPresenterImpl implements TransactionalCalculatorPresenter, CalculatorInteractorImpl.Callback {

    private static final String TAG = "TransactionalCalculator";

    private TransactionalCalculatorPresenter.View mView;

    private boolean mBeneficiaryReceiveBlocked = false;
    private boolean mYouPayBlocked = false;
    private String mCurrentAmountDefault = "";
    private CalculatorData mInitCalculatorData;
    private String mYouPayAmount;

    public TransactionalCalculatorPresenterImpl(Scheduler observeOn, Context context, TransactionalCalculatorPresenter.View view) {
        super(observeOn, context);
        this.mView = view;
    }

    @Override
    public void create() {
        CalculatorInteractorImpl.getInstance().addCallback(this);
        CalculatorInteractorImpl.getInstance().saveLastCalculatorData();

        mView.configureView();
        // Calculator header info
        mInitCalculatorData = new CalculatorData(CalculatorInteractorImpl.getInstance().getCalculatorData());
        mView.configureCalculator(CalculatorInteractorImpl.getInstance().getPayoutCountryKey(),
                CalculatorInteractorImpl.getInstance().getBeneficiaryReceives(),
                CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount(),
                CalculatorInteractorImpl.getInstance().getSendingCurrency(),
                CalculatorInteractorImpl.getInstance().getPayoutCurrency(),
                CalculatorInteractorImpl.getInstance().getCurrentDeliveryMethod());
        mView.openCalculator(Utils.isLowerThan21SDK());
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
        CalculatorInteractorImpl.getInstance().removeCallback(this);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public boolean isBlockedBeneficiaryReceive() {
        return this.mBeneficiaryReceiveBlocked;
    }

    public boolean isBlockedYouPay() {
        return this.mYouPayBlocked;
    }

    /**
     * Set amount and operation to operate in local
     *
     */
    public void onAmmountReadyToCalculate(String amount, String currencyType) {
        CalculatorInteractorImpl.getInstance().setCurrencyType(currencyType);
        if (currencyType.equals(CURRENCY_TYPE_PAYOUT_PRINCIPAL)) {
            // Updating track amount to keep value changing countries, beneficiary, ...
            mYouPayAmount = null;
            mCurrentAmountDefault = amount;
            updateYouPayValue(CalculatorInteractorImpl.getInstance().localCalculate(AmountFormatter.normalizeDoubleString(mCurrentAmountDefault), currencyType));
        } else {
            mYouPayAmount = amount;
            double totalSale = CalculatorInteractorImpl.getInstance().localCalculate(AmountFormatter.normalizeDoubleString(amount), currencyType);
            updateBeneficiaryValue(totalSale);
            mCurrentAmountDefault = AmountFormatter.formatDoubleAmountNumber(totalSale);
        }
        Log.d(TAG, "Calculate ->" + mCurrentAmountDefault + " operation: " + currencyType);
        Log.d(TAG, "new value: " + CalculatorInteractorImpl.getInstance().localCalculate(amount, currencyType));
        Log.d(TAG, "-------------------------");
    }

    public void onDoneClicked() {
        mInitCalculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();
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

    public void onSelectCurrencyInCalculator(Pair<String, String> currency) {
        // Request new Rate depends on new currency

        mView.enableDisableDoneButton(false);
        if (CalculatorInteractorImpl.getInstance().getCurrencyType().equals(TOTALSALE)){
            mView.showBeneficiaryLoadingView();
            mView.hideBeneficiaryContainer();
        }else {
            mView.showYouPayCalculatorLoadingView();
            mView.hideYouPayInfoContainer();
        }
        mView.hideYouPayCalculatorViewError();

        CalculatorInteractorImpl.getInstance().updateCalculatorWithCurrency(currency);
}

    public void onYouPayLostFocus(String amount) {
        //Log.d(TAG, "Lost Focus You Pay, amount: " + amount);
        if (!TextUtils.isEmpty(amount)) {
            String formattedAmount = AmountFormatter.formatStringNumber(amount);
            mYouPayBlocked = true;
            mView.updateYouPayEditText(formattedAmount);
            mYouPayBlocked = false;
        }
    }

    public void onTheyReceivesLostFocus(String amount) {
        //Log.d(TAG, "They Receives, amount: " + amount);
        if (!TextUtils.isEmpty(amount)) {
            String formattedAmount = AmountFormatter.formatStringNumber(amount);
            mBeneficiaryReceiveBlocked = true;
            mView.updateTheyReceiveEditText(formattedAmount);
            mBeneficiaryReceiveBlocked = false;
        }
    }

    public void onRecalculateClicked(@NonNull final TransactionalCalculatorFragment.CHANGED_FIELD changedField) {
        mView.enableDisableDoneButton(false);
        switch (changedField) {
            case YOU_PAY:
                mView.showBeneficiaryLoadingView();
                mView.hideBeneficiaryContainer();
                mView.hideYouPayCalculatorViewError();
                break;
            case BENEFICIARY:
                mView.showYouPayCalculatorLoadingView();
                mView.hideYouPayInfoContainer();
                mView.hideYouPayCalculatorViewError();
                break;
        }
        // Calculate
        String currencyType = CalculatorInteractorImpl.getInstance().getCurrencyType();
        if (currencyType.equals(Constants.CALCULATOR.TOTALSALE)) {
            CalculatorInteractorImpl.getInstance().calculateTransactionalAmount(mYouPayAmount);
        } else {
            CalculatorInteractorImpl.getInstance().calculateTransactionalAmount(CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount());
        }
    }

    public void recoveryData() {
        CalculatorInteractorImpl.getInstance().setCalculatorData(mInitCalculatorData);
    }

    /**
     * Calculator Callbacks
     */

    @Override
    public void onBeneficiaryInfoAvailable() {
        // Nothing
    }

    @Override
    public void onRetrievingBeneficiaryMethodsError() {
        // Nothing
    }

    @Override
    public void totalSaleCalculated(double totalSale, RateValues currentRate, String operationType, Pair<String, String> sendingCurrency, Promotion promotion, CalculatorPromotion mandatoryPromotion) {
        mView.enableDisableDoneButton(true);
        updateCalculatorData();
        mView.enableDoneMode();
        mView.hideYouPayCalculatorLoadingView();
        mView.hideBeneficiaryLoadingView();
        mView.showBeneficiaryContainer();
        mView.showYouPayInfoContainer();
    }

    private void updateCalculatorData() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        NumberFormat format = NumberFormat.getInstance(locale);
        try {
            updateYouPayValue(format.parse(CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount()).doubleValue());
            updateBeneficiaryValue(format.parse(CalculatorInteractorImpl.getInstance().getBeneficiaryReceives()).doubleValue());//Double.parseDouble());
        }
        catch(Exception e) {
            //
        }
    }

    @Override
    public void onRetrievingRateError() {
        mView.enableDisableDoneButton(false);
        mView.hideYouPayCalculatorLoadingView();
        mView.showYouPayCalculatorViewError();
    }


}



