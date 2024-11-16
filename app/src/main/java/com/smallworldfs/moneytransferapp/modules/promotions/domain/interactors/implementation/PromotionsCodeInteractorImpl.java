package com.smallworldfs.moneytransferapp.modules.promotions.domain.interactors.implementation;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateValues;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCalculateRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.repository.CalculatorRepositoryLegacy;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.interactors.PromotionsCodeInteractor;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.utils.Constants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by luis on 29/6/17
 */
@Singleton
public class PromotionsCodeInteractorImpl extends AbstractInteractor implements PromotionsCodeInteractor {

    private CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private static PromotionsCodeInteractorImpl sInstance = null;
    private ArrayList<PromotionsCodeInteractor.Callback> mListCallback;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public PromotionsCodeInteractorImpl() {
        super(AndroidSchedulers.mainThread(), Schedulers.io());
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCompositeSubscription = new CompositeSubscription();

        PromotionsCodeInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        PromotionsCodeInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    public static PromotionsCodeInteractorImpl getInstance() {
        if (sInstance == null) {
            sInstance = new PromotionsCodeInteractorImpl();
            sInstance.init();
        }

        return sInstance;
    }

    private void init() {
        mListCallback = new ArrayList<>();
    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
    }

    public void addCallback(PromotionsCodeInteractor.Callback callback) {
        if (mListCallback != null && !mListCallback.contains(callback)) {
            mListCallback.add(callback);
        }
    }

    public void removeCallback(PromotionsCodeInteractor.Callback callback) {
        if (mListCallback != null && mListCallback.contains(callback)) {
            mListCallback.remove(callback);
        }
    }

    public String getSendingCurrency() {
        if ((CountryRepository.getInstance() != null) &&
                (CountryRepository.getInstance().getCountryUserInfo() != null) &&
                (CountryRepository.getInstance().getCountryUserInfo().getCurrencies() != null) &&
                (CountryRepository.getInstance().getCountryUserInfo().getCurrencies() != null) &&
                (CountryRepository.getInstance().getCountryUserInfo().getCurrencies().firstEntry() != null)) {
            return CountryRepository.getInstance().getCountryUserInfo().getCurrencies().firstEntry().getKey();
        }

        return "";

    }

    /**
     * Get list promotions availables
     *
     * @return
     */
    public ArrayList<Promotion> getListPromotions() {
        return PromotionsRepository.getInstance().getListPromotionsAvailables();
    }


    public void selectPromotion(Promotion promotion) {
        PromotionsRepository.getInstance().setPromotionSelectedByUser(promotion);
        PromotionsRepository.getInstance().clearCalculatorPromotion();

        if (mListCallback != null && mListCallback.size() > 0) {
            for (PromotionsCodeInteractor.Callback callback : mListCallback) {
                callback.onUserChangeSelectedPromotion();
            }
        }
    }


    public Promotion getSelectedPromotion() {
        return PromotionsRepository.getInstance().getPromotionSelected();
    }

    public void checkPromotion(String code) {
        clearComposite();

        CalculatorData calculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();
        User user = userDataRepository.retrieveUser();

        if (calculatorData != null && user != null) {

            String amount = "";
            String currencyType = "";

            if (calculatorData != null) {
                if (CalculatorInteractorImpl.getInstance().getCurrencyType().equals(Constants.CALCULATOR.TOTALSALE)) {
                    amount = calculatorData.getYouPay();
                } else {
                    amount = calculatorData.getAmount();
                }
            }

            if (amount == null || amount.equals("")) {
                amount = Constants.CALCULATOR.DEFAULT_AMOUNT;
            }

            if (CalculatorInteractorImpl.getInstance() != null) {
                currencyType = CalculatorInteractorImpl.getInstance().getCurrencyType();
            }

            String originCountry = calculatorData.getOriginCountry() != null ? calculatorData.getOriginCountry().first : "";
            String sendingCurrency = calculatorData.getSendingCurrency() != null ? calculatorData.getSendingCurrency().first : "";
            String payoutCountry = calculatorData.getPayoutCountry() != null ? calculatorData.getPayoutCountry().first : "";
            String payoutCurrency = calculatorData.getPayoutCurrency() != null ? calculatorData.getPayoutCurrency().first : "";
            String deliveryMethod = calculatorData.getDeliveryMethod() != null ? calculatorData.getDeliveryMethod().first : "";

            ServerCalculateRequest request = new ServerCalculateRequest(
                    originCountry,
                    sendingCurrency,
                    payoutCountry,
                    payoutCurrency,
                    calculatorData.getBeneficiaryId(),
                    calculatorData.getOperationType(),
                    currencyType,
                    amount,
                    deliveryMethod,
                    code,
                    calculatorData.getRepresentativeCode());

            request.put(Constants.USER_PARAMS.USER_ID, user.getId());

            mCompositeSubscription.add(CalculatorRepositoryLegacy.getInstance()
                    .calculateRate(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<RateResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(retrofit2.Response<RateResponse> response) {
                            clearComposite();
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postCheckPromotionError();
                            } else {
                                processAndPostPromotion(response.body());
                            }
                        }
                    }));
        }
    }


    private void processAndPostPromotion(RateResponse promotion) {
        RateValues promotionResult = promotion.getResult();
        if (promotionResult != null && checkIfExistPromotion(promotionResult)) {
            String discount = "" + promotionResult.getDiscount();
            PromotionsRepository.getInstance().setPromotionSelectedByUser(new Promotion(promotionResult.getPromotionNumber(), promotionResult.getPromotionType(), "", discount));
            if (mHandler != null && mListCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListCallback != null && mListCallback.size() > 0) {
                            for (PromotionsCodeInteractor.Callback callback : mListCallback) {
                                callback.onPromotionCheckedSusccessfull();
                            }
                        }
                    }
                });
            }
        } else {
            postCheckPromotionError();
        }
    }

    private boolean checkIfExistPromotion(RateValues result) {
        if (result != null && !TextUtils.isEmpty(result.getPromotionName()) && result.getPromotionAmount() > 0) {

            // Valid promotion mandatory or not by server
            PromotionsRepository.getInstance().setCalculatorPromotion(
                    result.getPromotionName(),
                    result.getDiscount(),
                    result.getPromotionNumber(),
                    result.isAuto(),
                    result.getPromotionType(),
                    result.getPromotionCode());
            return true;
        } else {
            PromotionsRepository.getInstance().clearCalculatorPromotion();
            return false;
        }
    }

    private void postCheckPromotionError() {
        if (mHandler != null && mListCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mListCallback != null && mListCallback.size() > 0) {
                        for (PromotionsCodeInteractor.Callback callback : mListCallback) {
                            callback.onPromotionInvalid();
                        }
                    }
                }
            });
        }
    }


    private void clearComposite() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }

    public String getDestinationCountry() {
        User currentUser = userDataRepository.retrieveUser();
        if (currentUser != null) {
            return currentUser.getDestinationCountry().firstKey();
        } else {
            return "";
        }
    }
}
