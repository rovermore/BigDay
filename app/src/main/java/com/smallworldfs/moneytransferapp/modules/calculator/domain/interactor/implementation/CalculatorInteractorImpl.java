package com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.data.base.Cache;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.CalculatorInteractor;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCalculateRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by luismiguel on 27/6/17
 */
@Singleton
public class CalculatorInteractorImpl extends AbstractInteractor implements CalculatorInteractor {

    private static final String TAG = CalculatorInteractorImpl.class.getSimpleName();
    private static CalculatorInteractorImpl sInstance = null;
    private final Cache cache;
    public UserDataRepository userDataRepository;
    private ArrayList<CalculatorInteractor.Callback> mCallbacks;
    private ArrayList<CalculatorInteractor.PassiveCallback> mPasiveCallbacks;
    private Handler mHandler;
    private String mCurrencyType;
    // Status
    private CalculatorStatus mStatus = CalculatorStatus.LOADING_DATA;
    // Current calculator data value
    private CalculatorData mCurrentCalculatorData;
    // Last calculator data value status
    private CalculatorData mLastCalculatorData;
    // Composite Observable
    private CompositeSubscription mCompositeSubscription;
    private Double totalDiscount;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
        Cache providesCache();
    }

    @Inject
    public CalculatorInteractorImpl(Scheduler observeOn, Scheduler subscribeOn) {
        super(observeOn, subscribeOn);

        CalculatorInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CalculatorInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
        cache = hiltEntryPoint.providesCache();
    }

    public static CalculatorInteractorImpl getInstance() {
        if (sInstance == null) {
            sInstance = new CalculatorInteractorImpl(AndroidSchedulers.mainThread(), Schedulers.io());
            sInstance.init();
        }
        return sInstance;
    }

    private void init() {
        mCallbacks = new ArrayList<>();
        mPasiveCallbacks = new ArrayList<>();
        mCompositeSubscription = new CompositeSubscription();
        mHandler = new Handler(Looper.getMainLooper());
        mCurrentCalculatorData = new CalculatorData();
        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
        if (mHandler != null) {
            mHandler = null;
        }

        // Remove callbacks
        removeAllCallbacks();
        removeAllPassiveCallbacks();

        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }

        this.mCurrentCalculatorData = null;
        cache.setCalculatorData(null);
        this.mStatus = null;
        this.mLastCalculatorData = null;
        sInstance = null;
    }

    /**
     * Clear prev observable request
     */
    public void clearPrevRequest() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }

    /**
     * Update calculator with origin and payout countries
     */
    public void updateCalculatorWithOriginAndPayout(Pair<String, String> originCountry, Pair<String, String> payoutCountry) {
        updateCalculator(originCountry, payoutCountry, null, null, null, null, null, null);
    }

    /**
     * Update calculator with representative code
     */
    public void updateCalculatorWithRepresentativeCode(String representativeCode) {
        updateCalculator(null, null, null, null, null, null, null, representativeCode);
    }

    /**
     * Update calculator with delivery method based on Beneficiary
     */
    public void updateCalculatorWithBeneficiary(Pair<String, String> originCountry, BeneficiaryUIModel beneficiary) {
        if (beneficiary != null) {
            Pair<String, String> payoutCountry = new Pair<>(beneficiary.getPayoutCountry().getIso3(), beneficiary.getPayoutCountry().getName());
            String deliveryMethod = beneficiary.getDeliveryMethod().getType();
            mCurrentCalculatorData.setBeneficiaryType(beneficiary.getBeneficiaryType());

            propagateCalculatorUpdatedWithBeneficiary(beneficiary);

            // Clear methods
            saveLastCalculatorData();
            mCurrentCalculatorData.setListMethods(null);
            updateCalculator(originCountry, payoutCountry, beneficiary.getId(), null, null, null, deliveryMethod, beneficiary.getCurrentRepresentativeCode());
        } else {
            Log.e(TAG, "updateCalculatorWithBeneficiary: beneficiary == NULL");
        }
    }

    /**
     * Update calculator updating payout countries
     */
    public void updateCalculatorWithPayoutCountry(Pair<String, String> payoutCountry, String amount) {
        // Clear methods
        saveLastCalculatorData();
        mCurrentCalculatorData.setListMethods(null);
        updateCalculator(null, payoutCountry, null, amount, null, null, null, null);
    }

    /**
     * Update calculator updating currency
     */
    public void updateCalculatorWithCurrency(Pair<String, String> currencyPayout) {
        saveLastCalculatorData();
        if (getCurrencyType().equals(TOTALSALE)) {
            updateCalculator(null, null, null, getCurrentYouPayAmount(), null, currencyPayout, null, null);
        } else {
            updateCalculator(null, null, null, getCurrentBeneficiaryAmount(), null, currencyPayout, null, null);
        }
    }

    /**
     * Update calculator updating delivery method
     */
    public void updateCalculatorWithDeliveryMethod(Method method) {
        saveLastCalculatorData();
        mCurrentCalculatorData.setPayoutMethod(method);
        mCurrentCalculatorData.setDeliveryMethod(new Pair<>(Objects.requireNonNull(method.getMethod().firstEntry()).getKey(), Objects.requireNonNull(method.getMethod().firstEntry()).getValue()));

        // Get currency of selected method
        if (method.getCurrencies() != null && !method.getCurrencies().isEmpty()) {
            Pair<String, String> payoutCurrency = null;
            for (TreeMap<String, String> currency : method.getCurrencies()) {
                if (currency.containsKey(mCurrentCalculatorData.getPayoutCurrency().first)) {
                    payoutCurrency = new Pair<>(mCurrentCalculatorData.getPayoutCurrency().first, currency.firstEntry().getValue());
                }
            }
            updateCalculator(null, null, null, null, null, payoutCurrency, null, null);
        } else {
            propagateBeneficiaryMethodsError();
        }
    }

    public void refreshCalculator() {
        if (getCurrencyType().equals(TOTALSALE)) {
            updateCalculator(null, null, null, getCurrentYouPayAmount(), null, null, null, null);
        } else {
            updateCalculator(null, null, null, getCurrentBeneficiaryAmount(), null, null, null, null);
        }
    }

    private void updateCalculator(final Pair<String, String> originCountry, final Pair<String, String> payoutCountry, final String beneficiaryId, final String amount, final String operationType, final Pair<String, String> payoutCurrency, final String methodKeyUsedByBeneficiary, final String representativeCode) {
        mStatus = CalculatorStatus.LOADING_DATA;

        if (mCurrentCalculatorData.getOriginCountry() == null) {
            mCurrentCalculatorData.setOriginCountry(originCountry);
        }

        if (payoutCountry != null) {
            mCurrentCalculatorData.setPayoutCountry(payoutCountry);
        }

        if (beneficiaryId != null && !beneficiaryId.isEmpty()) {
            mCurrentCalculatorData.setBeneficiaryId(beneficiaryId);
        }

        if (CountryRepository.getInstance().getCountryUserInfo() != null) {
            mCurrentCalculatorData.setSendingCurrency(new Pair<>(CountryRepository.getInstance().getCountryUserInfo().getCurrencies().firstEntry().getKey(), CountryRepository.getInstance().getCountryUserInfo().getCurrencies().firstEntry().getValue()));
        }

        if (representativeCode != null) {
            mCurrentCalculatorData.setRepresentativeCode(representativeCode);
        }

        if (mCurrentCalculatorData.getListMethods() == null || mCurrentCalculatorData.getListMethods().isEmpty()) {

            clearPrevRequest();

            String requestOriginCountry = mCurrentCalculatorData.getOriginCountry() == null || TextUtils.isEmpty(mCurrentCalculatorData.getOriginCountry().first) ?
                    "" : mCurrentCalculatorData.getOriginCountry().first;

            String requestPayoutCountry = mCurrentCalculatorData.getPayoutCountry() == null || TextUtils.isEmpty(mCurrentCalculatorData.getPayoutCountry().first) ?
                    "" : mCurrentCalculatorData.getPayoutCountry().first;

            String beneficiaryType = mCurrentCalculatorData.getBeneficiaryType() != null ? mCurrentCalculatorData.getBeneficiaryType() : "";

            // Get currency
            ServerCurrencieRequest request = new ServerCurrencieRequest(requestOriginCountry, requestPayoutCountry, beneficiaryType);
            mCompositeSubscription.add(CalculatorRepositoryLegacy.getInstance()
                    .getCurrencies(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<CurrenciesResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(retrofit2.Response<CurrenciesResponse> response) {
                            if (response != null && response.body() != null && response.body().getMethods() != null) {
                                // filter methods
                                updateMethodsValues(response.body().getMethods(), payoutCurrency, methodKeyUsedByBeneficiary);
                                updateCalculator(originCountry, payoutCountry, beneficiaryId, amount, operationType, payoutCurrency, methodKeyUsedByBeneficiary, representativeCode);
                            } else {
                                propagateBeneficiaryMethodsError();
                            }
                        }
                    }));

        } else {

            if (payoutCurrency != null) {
                mCurrentCalculatorData.setPayoutCurrency(payoutCurrency);
            }

            // Notify data available
            notifyBeneficiaryInfoAvailable();

            if (getCurrencyType().equals(TOTALSALE)) {
                // You Pay
                if (!TextUtils.isEmpty(getCurrentYouPayAmount())) {
                    mCurrentCalculatorData.setYouPay(AmountFormatter.normalizeDoubleString(getCurrentYouPayAmount()));
                }
            } else {
                //Beneficiary receive
                if (!TextUtils.isEmpty(getBeneficiaryReceives())) {
                    mCurrentCalculatorData.setAmount(AmountFormatter.normalizeDoubleString(getBeneficiaryReceives()));
                }
            }

            //Operation type
            if (!TextUtils.isEmpty(operationType)) {
                mCurrentCalculatorData.setOperationType(operationType);
            } else {
                mCurrentCalculatorData.setOperationType(CALCULATOR.DEFAULT_OPERATION);
            }

            //Currency type
            setCurrencyType(getCurrencyType());

            //Check representative code not null value
            if (mCurrentCalculatorData.getRepresentativeCode() == null) {
                mCurrentCalculatorData.setRepresentativeCode("");
            }

            if (mCurrentCalculatorData.getListMethods() != null) {
                for (Method method : mCurrentCalculatorData.getListMethods()) {
                    if (method != null && mCurrentCalculatorData != null && mCurrentCalculatorData.getDeliveryMethod() != null &&
                            mCurrentCalculatorData.getDeliveryMethod().first != null &&
                            method.getMethod().firstKey().equals(mCurrentCalculatorData.getDeliveryMethod().first)) {
                        buildAndRequestRate();
                        return;
                    }
                }
            }

            propagateCalculatingRateError();
        }

        if (mCurrentCalculatorData != null) {
            cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
        }
    }

    private void updateMethodsValues(ArrayList<Method> listMethods, Pair<String, String> payoutCurrency, String methodKeyUsedByBeneficiary) {
        mCurrentCalculatorData.setListMethods(listMethods);

        // Payout Method, 0 as default
        if (mCurrentCalculatorData.getListMethods().get(0) != null) {
            if (TextUtils.isEmpty(methodKeyUsedByBeneficiary)) {
                mCurrentCalculatorData.setPayoutMethod(mCurrentCalculatorData.getListMethods().get(0));

                // Delivery Method, first 0 as default
                if (mCurrentCalculatorData.getListMethods().get(0).getMethod() != null) {
                    mCurrentCalculatorData.setDeliveryMethod(new Pair<>(mCurrentCalculatorData.getListMethods().get(0).getMethod().firstEntry().getKey(),
                            Objects.requireNonNull(mCurrentCalculatorData.getListMethods().get(0).getMethod().firstEntry()).getValue()));

                }

            } else {
                boolean added = false;
                for (Method methodAvailable : mCurrentCalculatorData.getListMethods()) {
                    if (methodAvailable.getMethod().firstKey().equalsIgnoreCase(methodKeyUsedByBeneficiary)) {
                        mCurrentCalculatorData.setPayoutMethod(methodAvailable);

                        mCurrentCalculatorData.setDeliveryMethod(new Pair<>(Objects.requireNonNull(methodAvailable.getMethod().firstEntry()).getKey(),
                                methodAvailable.getMethod().firstEntry().getValue()));

                        added = true;
                        break;
                    }
                }

                if (!added) {
                    // Avoid wrong key like Euros
                    mCurrentCalculatorData.setPayoutMethod(mCurrentCalculatorData.getListMethods().get(0));
                }
            }

            if (mCurrentCalculatorData.getPayoutMethod().getCurrencies().get(0) != null) {
                if (payoutCurrency != null) {
                    mCurrentCalculatorData.setPayoutCurrency(payoutCurrency);
                } else {
                    // Default value
                    mCurrentCalculatorData.setPayoutCurrency(new Pair<>(mCurrentCalculatorData.getPayoutMethod().getCurrencies().get(0).firstEntry().getKey(),
                            mCurrentCalculatorData.getPayoutMethod().getCurrencies().get(0).firstEntry().getValue()));

                }
            }

            cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
        }
    }

    /**
     * Synchronize Calculator Beneficiary if user change delivery method inside transactional and return prev status
     */
    public void synchronizeCalculatorBeneficiaryMethod(String deliveryMethod) {
        ArrayList<Method> aMethods = CalculatorInteractorImpl.getInstance().getListMethods();
        if (aMethods != null)
            for (Method methodAvailable : aMethods) {
                if (methodAvailable.getMethod().firstKey().equalsIgnoreCase(deliveryMethod)) {
                    mCurrentCalculatorData.setPayoutMethod(methodAvailable);
                    mCurrentCalculatorData.setDeliveryMethod(new Pair<>(methodAvailable.getMethod().firstEntry().getKey(),
                            methodAvailable.getMethod().firstEntry().getValue()));
                    break;
                }
            }
        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
    }

    public void saveLastCalculatorData() {
        this.mLastCalculatorData = new CalculatorData(mCurrentCalculatorData);
    }

    public String getAmount(String type) {
        final String amount;
        if (type.equals(TOTALSALE)) {
            amount = mCurrentCalculatorData.getYouPay();
        } else {
            amount = mCurrentCalculatorData.getAmount();
        }
        if (amount == null || amount.equals("0")) {
            return CALCULATOR.DEFAULT_AMOUNT;
        }
        return amount;
    }

    private void buildAndRequestRate() {
        // Get promotions
        final ServerCalculateRequest serverCalculateRequest = createRateRequest();
        if (serverCalculateRequest != null) {
            requestRate(serverCalculateRequest, new Subscriber<Response<RateResponse>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    onNext(null);
                }

                public void onNext(retrofit2.Response<RateResponse> response) {
                    if (response != null && response.body() != null && response.body().getResult() != null) {
                        checkIfExistPromotion(response.body().getResult());
                        updateRate(response.body().getResult());
                    } else {
                        propagateCalculatingRateError();
                    }
                }
            });
        }

    }

    private void requestRate(@NonNull final ServerCalculateRequest serverCalculateRequest,
                             @NonNull final Subscriber<Response<RateResponse>> subscriber) {
        mCompositeSubscription.add(CalculatorRepositoryLegacy.getInstance()
                .calculateRate(serverCalculateRequest)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(subscriber));
    }

    private void checkIfExistPromotion(RateValues result) {
        // Evaluate if receive added promotion in operation
        if (result != null && !TextUtils.isEmpty(result.getPromotionName()) && result.getPromotionAmount() > 0) {

            // Valid promotion mandatory by server
            PromotionsRepository.getInstance().setCalculatorPromotion(result.getPromotionName(),
                    result.getDiscount(),
                    result.getPromotionNumber(),
                    result.isAuto(),
                    result.getPromotionType(),
                    result.getPromotionCode());
        } else {
            PromotionsRepository.getInstance().clearCalculatorPromotion();
        }
    }

    private void updateRate(RateValues newRate) {
        mCurrentCalculatorData.setCurrentCalculator(newRate);

        // propagatePassiveChanges();
        // Calculate values
        mCurrentCalculatorData.setYouPay(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(newRate.getTotalSale())));
        mCurrentCalculatorData.setAmount(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(newRate.getPayoutPrincipal())));
        if (getCurrencyType().equals(CALCULATOR.TOTALSALE)) {
            calculateAmount(newRate.getPayoutPrincipal());
        } else {
            calculateAmount(newRate.getTotalSale());
        }

        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
    }

    private void calculateAmount(double amount) {
        // Get avaiable promotions
        Promotion currentSelectedPromotion = PromotionsRepository.getInstance().getPromotionSelected();
        final CalculatorPromotion autoAssignedPromotion = PromotionsRepository.getInstance().getCalculatorPromotion();

        if (autoAssignedPromotion != null) {
            currentSelectedPromotion = null;
        }

        // Set Ok status
        mStatus = CalculatorStatus.OK;

        if (mCallbacks != null && mHandler != null) {
            final double finalAmountCalculated = amount;
            final Promotion finalCurrentSelectedPromotion = currentSelectedPromotion;

            mHandler.post(() -> {
                if (mCallbacks != null && !mCallbacks.isEmpty()) {
                    for (Callback callback : mCallbacks) {
                        callback.totalSaleCalculated(finalAmountCalculated, mCurrentCalculatorData.getCurrentCalculator(),
                                mCurrentCalculatorData.getOperationType(), mCurrentCalculatorData.getSendingCurrency(), finalCurrentSelectedPromotion, autoAssignedPromotion);
                    }
                }
            });
        }

        propagatePassiveChanges();
    }

    private void propagatePassiveChanges() {
        if (mPasiveCallbacks != null && mHandler != null && !mPasiveCallbacks.isEmpty()) {
            mHandler.post(() -> {
                if (mPasiveCallbacks != null && mHandler != null) {
                    for (PassiveCallback passiveCallback : mPasiveCallbacks) {
                        if (mCurrentCalculatorData != null && mCurrentCalculatorData.getPayoutCurrency() != null) {
                            passiveCallback.onCalculatorChanges(mCurrentCalculatorData.getAmount(), mCurrentCalculatorData.getYouPay(), mCurrentCalculatorData.getPayoutCurrency().first);
                        }
                    }
                }
            });
        }
    }

    private void propagateBeneficiaryMethodsError() {
        if (mHandler != null && mCallbacks != null && !mCallbacks.isEmpty()) {
            mHandler.post(() -> {
                if (mCallbacks != null && !mCallbacks.isEmpty()) {
                    for (Callback callback : mCallbacks) {
                        callback.onRetrievingBeneficiaryMethodsError();
                    }
                }
            });
        }
    }

    private void propagateCalculatorUpdatedWithBeneficiary(final BeneficiaryUIModel beneficiary) {
        if (mHandler != null && mPasiveCallbacks != null && !mPasiveCallbacks.isEmpty()) {
            mHandler.post(() -> {
                if (mPasiveCallbacks != null && !mPasiveCallbacks.isEmpty()) {
                    for (PassiveCallback callback : mPasiveCallbacks) {
                        callback.onCalculatorUpdatedWithBeneficiary(beneficiary);
                    }
                }
            });
        }
    }

    private void propagateCalculatingRateError() {
        mStatus = CalculatorStatus.ERROR;

        if (mHandler != null && mCallbacks != null && !mCallbacks.isEmpty()) {
            mHandler.post(() -> {
                if (mCallbacks != null && !mCallbacks.isEmpty()) {
                    for (Callback callback : mCallbacks) {
                        callback.onRetrievingRateError();
                    }
                    for (PassiveCallback callback : mPasiveCallbacks) {
                        callback.onCalculatorError();
                    }
                }
            });
        }
    }

    public double localCalculate(String amount, String payoutMethod) {
        if (mStatus != CalculatorStatus.ERROR) {
            return calculate(true, AmountFormatter.normalizeDoubleString(amount), payoutMethod);
        } else return 0.0;
    }

    private double calculate(boolean isLocal, String amount, String payoutOperation) {
        try {
            if (!TextUtils.isEmpty(amount)
                    && mCurrentCalculatorData != null
                    && mCurrentCalculatorData.getCurrentCalculator() != null
                    && mCurrentCalculatorData.getCurrentCalculator().getRate() != null) {

                DecimalFormat formatter = new DecimalFormat();
                String decimalSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getDecimalSeparator());
                String groupingSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getGroupingSeparator());
                String amountFormmatted = amount.replace(groupingSeparator, "");
                amountFormmatted = amountFormmatted.replace(decimalSeparator, ".");

                final double amountValue = Double.valueOf(amountFormmatted);

                if (payoutOperation.equals(CURRENCY_TYPE_PAYOUT_PRINCIPAL))
                    return payoutPrincipalOperation(amountValue);
                else
                    return totalSaleOperation(isLocal, amountValue);
            }
        } catch (Exception e) {
            Log.e(TAG, "calculate:e:----------------------------------------------------------", e);
        }
        return 0;
    }

    public void calculateTransactionalAmount(@NonNull final String amount) {
        final ServerCalculateRequest serverCalculateRequest = createRateRequest();
        if (serverCalculateRequest != null) {
            requestRate(serverCalculateRequest, new Subscriber<Response<RateResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    propagateCalculatingRateError();
                }

                @Override
                public void onNext(Response<RateResponse> response) {
                    if (response != null && response.body() != null && response.body().getResult() != null) {
                        checkIfExistPromotion(response.body().getResult());
                        updateRate(response.body().getResult());
                    } else {
                        propagateCalculatingRateError();
                    }
                }
            });
        }
    }

    private ServerCalculateRequest createRateRequest() {
        if ((mCurrentCalculatorData != null) &&
                (mCurrentCalculatorData.getOriginCountry() != null) &&
                (mCurrentCalculatorData.getSendingCurrency() != null) &&
                (mCurrentCalculatorData.getPayoutCountry() != null) &&
                (mCurrentCalculatorData.getPayoutCurrency() != null) &&
                (mCurrentCalculatorData.getDeliveryMethod() != null)) {

            ServerCalculateRequest request = new ServerCalculateRequest(
                    mCurrentCalculatorData.getOriginCountry().first,
                    mCurrentCalculatorData.getSendingCurrency().first,
                    mCurrentCalculatorData.getPayoutCountry().first,
                    mCurrentCalculatorData.getPayoutCurrency().first,
                    mCurrentCalculatorData.getBeneficiaryId(),
                    mCurrentCalculatorData.getOperationType(),
                    getCurrencyType(),
                    AmountFormatter.normalizeAmountToSend(getAmount(getCurrencyType())),
                    mCurrentCalculatorData.getDeliveryMethod().first,
                    PromotionsRepository.getInstance().getPromotionCodePresent(),
                    mCurrentCalculatorData.getRepresentativeCode()
            );

            User user = userDataRepository.retrieveUser();
            if (user != null && !TextUtils.isEmpty(user.getId())) {
                request.put(Constants.USER_PARAMS.USER_ID, user.getId());
            }
            return request;
        }
        return null;
    }

    private double payoutPrincipalOperation(double amount) {

        // AMOUNT_WITHOUT_FEE = ROUND(BENEFICIARY_RECEIVE) / RATE
        // and
        // AMOUNT = AMOUNT_WITHOUT_FEE + FEE - TOTALDISCOUNT

        // First calculate totalDiscount
        // CalculatorPromotion
        double totalDiscount = 0.0;
        String discountType = "";

        Pair<String, Double> promotionValues = PromotionsRepository.getInstance().getDiscountAndTypePromotion();
        if (promotionValues != null) {
            discountType = promotionValues.first;
            totalDiscount = promotionValues.second;
        }

        totalDiscount = getTotalDiscountByPromotion(discountType, totalDiscount, amount, CURRENCY_TYPE_PAYOUT_PRINCIPAL);
        this.totalDiscount = totalDiscount;

        double quantity = (amount / mCurrentCalculatorData.getCurrentCalculator().getRate()) + mCurrentCalculatorData.getCurrentCalculator().getTotalFee() - totalDiscount;

        // Check non < 0 values
        if (quantity < 0) {
            quantity = 0.0;
        }

        // Update last value to keep current youpay amount
        mCurrentCalculatorData.setYouPay(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(quantity)));
        mCurrentCalculatorData.setAmount(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(amount)));

        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));

        propagatePassiveChanges();

        int scale = (int) Math.pow(10, 2);
        return (double) Math.round(quantity * scale) / scale;
    }

    private double totalSaleOperation(boolean islocal, double amount) {

        // BENEFICIARY_RECEIVE = (AMOUNT - FEE + TOTALDISCOUNT) * RATE

        // First calculate totalDiscount
        // CalculatorPromotion
        double totalDiscount = 0.0;
        String discountType = "";

        Pair<String, Double> promotionValues = PromotionsRepository.getInstance().getDiscountAndTypePromotion();
        if (promotionValues != null) {
            discountType = promotionValues.first;
            totalDiscount = promotionValues.second;
        }

        totalDiscount = getTotalDiscountByPromotion(discountType, totalDiscount, amount, CALCULATOR.TOTALSALE);
        this.totalDiscount = totalDiscount;

        double quantity = 0.0;

        if (!discountType.equalsIgnoreCase(Constants.PROMOTIONS.TOTAL_PERCENT)) {
            quantity = (amount - mCurrentCalculatorData.getCurrentCalculator().getTotalFee() + totalDiscount) * mCurrentCalculatorData.getCurrentCalculator().getRate();
        } else {
            double amountWithoutFee = amount - mCurrentCalculatorData.getCurrentCalculator().getTotalFee();
            double percent = totalDiscount;
            quantity = amountWithoutFee / (1 - percent);
        }

        // Check non < 0 values
        if (quantity < 0) {
            quantity = 0.0;
        }

        // Update last value to keep current beneficiary amount
        if (islocal) {
            mCurrentCalculatorData.setAmount(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(quantity)));
            mCurrentCalculatorData.setYouPay(AmountFormatter.normalizeDoubleString(AmountFormatter.formatDoubleAmountNumber(amount)));
        }

        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));

        propagatePassiveChanges();

        int scale = (int) Math.pow(10, 2);
        return (double) Math.round(quantity * scale) / scale;
    }

    private double getTotalDiscountByPromotion(String discountType, Double discount, double amount, String operationType) {
        try {
            if (!TextUtils.isEmpty(discountType) && discount > 0) {
                switch (discountType) {
                    case Constants.PROMOTIONS.FEE_PERCENT:
                        // TOTALDISCOUNT =  ROUND((FEE*DISCOUNT)/100)
                        return (mCurrentCalculatorData.getCurrentCalculator().getTotalFee() * discount / 100);
                    case Constants.PROMOTIONS.FEE_AMOUNT:
                        // TOTALDISCOUNT = DISCOUNT
                        if (mCurrentCalculatorData.getCurrentCalculator().getTotalFee() > discount) {
                            return discount;
                        } else return 0.0;
                    case Constants.PROMOTIONS.TOTAL_PERCENT:
                        // TOTALDISCOUNT = ROUND((AMOUNT*DISCOUNT)/100)
                        if (operationType.equals(CURRENCY_TYPE_PAYOUT_PRINCIPAL)) {
                            return (amount * discount) / 100;
                        } else {
                            // Return percent
                            return (discount / 100);
                        }
                    case Constants.PROMOTIONS.TOTAL_AMOUNT:
                        // TOTALDISCOUNT = DISCOUNT
                        return discount;
                    default:
                        return 0.0;
                }
            } else return 0.0;
        } catch (Exception e) {
            Log.e("STACK", "----------------------", e);
            return 0.0;
        }
    }

    public double getTotalDiscount() {
        if (totalDiscount != null) {
            return totalDiscount;
        }
        return 0;
    }

    /**
     * Get -- Set Methods
     */

    public Pair<String, String> getCurrentPayoutCountry() {
        return mCurrentCalculatorData.getPayoutCountry();
    }

    private void notifyBeneficiaryInfoAvailable() {
        if (mCallbacks != null && !mCallbacks.isEmpty() && mHandler != null) {
            mHandler.post(() -> {
                if (mCallbacks != null && !mCallbacks.isEmpty()) {
                    for (Callback callback : mCallbacks) {
                        callback.onBeneficiaryInfoAvailable();
                    }
                }
            });
        }
    }

    public ArrayList<Method> getListMethods() {
        return mCurrentCalculatorData.getListMethods();
    }

    public String getCurrentBeneficiaryAmount() {
        if (TextUtils.isEmpty(mCurrentCalculatorData.getAmount())) {
            return "";
        } else {
            return mCurrentCalculatorData.getAmount();
        }
    }

    public String getCurrentYouPayAmount() {
        if (TextUtils.isEmpty(mCurrentCalculatorData.getYouPay())) {
            return "";
        } else {
            return mCurrentCalculatorData.getYouPay();
        }
    }

    public String getSendingCurrency() {
        if (mCurrentCalculatorData.getSendingCurrency() != null) {
            return mCurrentCalculatorData.getSendingCurrency().first;
        } else {
            return "";
        }
    }

    public String getPayoutCountryKey() {
        if (mCurrentCalculatorData.getPayoutCountry() != null) {
            return mCurrentCalculatorData.getPayoutCountry().first;
        } else {
            return "";
        }
    }

    public String getPayoutCurrency() {
        if (mCurrentCalculatorData.getPayoutCurrency() != null) {
            return mCurrentCalculatorData.getPayoutCurrency().first;
        } else {
            return "";
        }
    }

    public String getCurrencyType() {
        if (this.mCurrencyType == null) {
            return Constants.CALCULATOR.DEFAULT_CURRENCY_TYPE;
        }
        return this.mCurrencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.mCurrencyType = currencyType;
        cache.updateCurrencyType(currencyType);
    }

    public Method getCurrentDeliveryMethod() {
        return mCurrentCalculatorData.getPayoutMethod();
    }

    public String getBeneficiaryReceives() {
        return mCurrentCalculatorData.getAmount();
    }

    public void setYouPayAmount(String amount) {
        mCurrentCalculatorData.setYouPay(AmountFormatter.normalizeDoubleString(amount));
        cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
    }

    public CalculatorStatus getCalculatorStatus() {
        return this.mStatus;
    }

    public void setCalculatorStatus(CalculatorStatus status) {
        this.mStatus = status;
    }

    public CalculatorData getCalculatorData() {
        return this.mCurrentCalculatorData;
    }

    public void setCalculatorData(@NonNull final CalculatorData calculatorData) {
        mCurrentCalculatorData = calculatorData;
        cache.setCalculatorData(calculatorData.mapToDTO(mCurrencyType));
    }

    public void recoveryLastStatusCalculator(boolean refreshCalculator) {
        if (mLastCalculatorData != null) {
            mCurrentCalculatorData = new CalculatorData(mLastCalculatorData);
            cache.setCalculatorData(mCurrentCalculatorData.mapToDTO(mCurrencyType));
            mLastCalculatorData = null;
            mStatus = CalculatorStatus.OK;
        }

        if (refreshCalculator) {
            refreshCalculator();
        }
    }

    /**
     * Manage Listener Suscriptions
     */
    public void addCallback(CalculatorInteractor.Callback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    public void removeCallback(CalculatorInteractor.Callback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }

    private void removeAllCallbacks() {
        if (mCallbacks != null) {
            mCallbacks.clear();
        }
    }

    private void removeAllPassiveCallbacks() {
        if (mPasiveCallbacks != null) {
            mPasiveCallbacks.clear();
        }
    }

    public void addPassiveCallback(CalculatorInteractor.PassiveCallback callback) {
        if (mPasiveCallbacks != null && !mPasiveCallbacks.contains(callback)) {
            mPasiveCallbacks.add(callback);
        }
    }

    public void removePassiveCallback(CalculatorInteractor.PassiveCallback callback) {
        if (mPasiveCallbacks != null) {
            mPasiveCallbacks.remove(callback);
        }
    }

    public boolean isUploadDialogShown() {
        return userDataRepository.isUploadDialogShown();
    }

    public void setUploadDialogShown(boolean shown) {
        userDataRepository.setUploadDialogShown(shown);
    }

    // Calculator Status
    public enum CalculatorStatus {
        LOADING_DATA, ERROR, OK, RESET_VALUE
    }
}

