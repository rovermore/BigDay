package com.smallworldfs.moneytransferapp.modules.home.domain.interactors.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.PAYOUT_COUNTRIES_TYPE;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO;
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryDTOMapper;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.BeneficiariesServer;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server.BeneficiaryRequest;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.repository.BeneficiaryRepository;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.server.ServerCountryRequest;
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository;
import com.smallworldfs.moneytransferapp.modules.customization.domain.repository.AppCustomizationRepository;
import com.smallworldfs.moneytransferapp.modules.home.domain.interactors.SendToInteractor;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.PromotionsResponse;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server.ServerPromotionsRequest;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModelMapper;
import com.smallworldfs.moneytransferapp.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Scheduler;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

@Singleton
public class SendToInteractorImpl extends AbstractInteractor implements SendToInteractor {

    private SendToInteractor.Callback mCallback;
    private final CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private User mUser;

    private Pair<String, String> mUserCountry = null;
    private Pair<String, String> mPayoutCountry = null;
    private ArrayList<BeneficiaryUIModel> mListBeneficiary = null;
    private Pair<String, String> mFirstCountrySelector = null;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
        BeneficiaryDTOMapper provideBeneficiaryDTOMapper();
        BeneficiaryUIModelMapper provideBeneficiaryUIModelMapper();
    }

    public UserDataRepository userDataRepository;
    public BeneficiaryDTOMapper beneficiaryDTOMapper;
    public BeneficiaryUIModelMapper beneficiaryUIModelMapper;

    @Inject
    public SendToInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, SendToInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCallback = callback;
        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());

        SendToInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        SendToInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
        beneficiaryDTOMapper = hiltEntryPoint.provideBeneficiaryDTOMapper();
        beneficiaryUIModelMapper = hiltEntryPoint.provideBeneficiaryUIModelMapper();
        this.mUser = userDataRepository.retrieveUser();

    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {
    }

    @Override
    public void destroy() {

        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }

        mCallback = null;
        mHandler = null;
    }


    public void requestBeneficiaries(ArrayList<BeneficiaryUIModel> listBeneficiaries) {

        if (mUser != null) {

            // First request beneficiaries
            if (listBeneficiaries == null) {

                final BeneficiaryRequest request = new BeneficiaryRequest(this.mUser.getUserToken(), this.mUser.getId());

                mCompositeSubscription.add(BeneficiaryRepository.getInstance()
                        .requestBeneficiaries(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<Response<BeneficiariesServer>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(retrofit2.Response<BeneficiariesServer> response) {
                                NewGenericError error = NewGenericError.processError(response);
                                if (error != null || (response.body() != null && response.body().getBeneficiaries() == null)) {
                                    propagateHeaderError();
                                } else {
                                    if (response.body() != null) {
                                        ArrayList<Beneficiary> beneficiaries = response.body().getBeneficiaries();
                                        List<BeneficiaryDTO> beneficiaryDTO = beneficiaryDTOMapper.map(beneficiaries);
                                        List<BeneficiaryUIModel> beneficiaryUIModel = beneficiaryUIModelMapper.map(beneficiaryDTO);
                                        requestBeneficiaries(new ArrayList<>(beneficiaryUIModel));
                                    }
                                }
                            }
                        }));
            } else {
                // Request Origin and Payout Info
                requestOriginAndPayoutInfo(listBeneficiaries, null);
            }
        }
    }


    public void requestOriginAndPayoutInfo(final ArrayList<BeneficiaryUIModel> listBeneficiaries, Country countries) {

        if (countries != null) {
            // First get origin countries from user
            mUserCountry = new Pair<>(Objects.requireNonNull(mUser.getCountry().firstEntry()).getKey(), Objects.requireNonNull(this.mUser.getCountry().firstEntry()).getValue());

            // Get payout countries
            if (listBeneficiaries != null && listBeneficiaries.size() > 0) {
                mListBeneficiary = listBeneficiaries;

                // Reordenate if recent beneficiary created
                Beneficiary recentBeneficiaryCreated = getRecentCreatedBeneficiary();
                BeneficiaryUIModel toMoveBeneficiary = null;
                if (recentBeneficiaryCreated != null) {
                    int position = 0;
                    for (BeneficiaryUIModel beneficiary : mListBeneficiary) {
                        if (beneficiary.getId().equalsIgnoreCase(recentBeneficiaryCreated.getId())) {
                            toMoveBeneficiary = mListBeneficiary.get(position);
                            break;
                        }
                        position++;
                    }

                    if (toMoveBeneficiary != null && position > 0) {
                        Collections.swap(mListBeneficiary, position, 0);
                    }
                }
                for (BeneficiaryUIModel beneficiary : mListBeneficiary) {
                    if (recentBeneficiaryCreated != null) {
                        if (!beneficiary.getId().equalsIgnoreCase(recentBeneficiaryCreated.getId())) {
                            beneficiary.setNew(false);
                        } else  {
                            beneficiary.setNew(true);
                        }
                    } else  {
                        beneficiary.setNew(false);
                    }
                }
                mPayoutCountry = new Pair<>(
                        Objects.requireNonNull(mListBeneficiary.get(0).getPayoutCountry().getIso3()),
                        Objects.requireNonNull(mListBeneficiary.get(0).getPayoutCountry().getName())
                );
                mFirstCountrySelector = new Pair<>(mPayoutCountry.first, mPayoutCountry.second);

            } else if (AppCustomizationRepository.getInstance().getPayoutCountrySelected() != null) {
                mListBeneficiary = null;
                mPayoutCountry = AppCustomizationRepository.getInstance().getPayoutCountrySelected();
                mFirstCountrySelector = new Pair<>(mPayoutCountry.first, mPayoutCountry.second);

            } else {
                mListBeneficiary = null;
                mPayoutCountry = new Pair<>(Objects.requireNonNull(countries.getSortedCountries().firstEntry()).getKey(), Objects.requireNonNull(countries.getSortedCountries().firstEntry()).getValue());
                mFirstCountrySelector = new Pair<>(mPayoutCountry.first, mPayoutCountry.second);
            }

            // Prevent null set adapter in view
            if (mListBeneficiary == null) {
                mListBeneficiary = new ArrayList<>();
            }

            postHeaderInfoReady();
            requestPromotions(mPayoutCountry.first);

        } else {
            Pair<String, String> originCountryFromUser = new Pair<>(Objects.requireNonNull(mUser.getCountry().firstEntry()).getKey(), Objects.requireNonNull(mUser.getCountry().firstEntry()).getValue());
            mUserCountry = originCountryFromUser;
            final ServerCountryRequest request = new ServerCountryRequest(PAYOUT_COUNTRIES_TYPE, originCountryFromUser.first);
            mCompositeSubscription.add(CountryRepository.getInstance()
                    .requestPayoutCountries(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<retrofit2.Response<Country>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(retrofit2.Response<Country> response) {
                            if (response != null && response.body() != null && response.body().getCountries() != null) {
                                requestOriginAndPayoutInfo(listBeneficiaries, response.body());
                            } else {
                                propagateHeaderError();
                            }
                        }
                    }));
        }
    }

    public void requestPromotions(final String payoutCountry) {

        final String originCountry = Objects.requireNonNull(mUser.getCountry().firstEntry()).getKey();

        ServerPromotionsRequest request = new ServerPromotionsRequest(originCountry, payoutCountry, TextUtils.isEmpty(mUser.getClientId()) ? "" : mUser.getClientId());
        mCompositeSubscription.add(PromotionsRepository.getInstance()
                .requestPromotions(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<PromotionsResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNext(null);
                    }

                    public void onNext(retrofit2.Response<PromotionsResponse> response) {
                        NewGenericError error = NewGenericError.processError(response);
                        if (error == null && response.body() != null) {
                            manageAndStorePromotions(response.body().getPromotions());
                        }
                    }
                }));
    }

    private void manageAndStorePromotions(ArrayList<Promotion> listPromotions) {
        if (listPromotions != null) {
            PromotionsRepository.getInstance().setListPromotionsAvailables(listPromotions);
        }
    }


    /**
     * Check mandatory promotions
     */
    public CalculatorPromotion getCurrentCalculatorPromotion() {
        return PromotionsRepository.getInstance().getCalculatorPromotion();
    }

    public Promotion getUserPromotion() {
        return PromotionsRepository.getInstance().getPromotionSelected();
    }

    public boolean isLimitedUser() {
        User user = userDataRepository.retrieveUser();
        return user != null && user.getStatus().equalsIgnoreCase(Constants.UserType.LIMITED);
    }

    public boolean showWelcome() {
        return isLimitedUser() && !userDataRepository.isWelcomeShown();
    }

    public void setWelcomeShown(boolean shown) {
        userDataRepository.setWelcomeShown(shown);
    }

    public boolean isPendingValidatingEmailUser() {
        User user = userDataRepository.retrieveUser();
        return user != null && user.getStatus().equalsIgnoreCase(Constants.UserType.PDT_MAIL);
    }

    public User getUser() {
        return userDataRepository.retrieveUser();
    }

    public void cleanAllPromotions() {
        clearPromotionSelected();
        PromotionsRepository.getInstance().clearListPromotionsAvailables();
        PromotionsRepository.getInstance().clearCalculatorPromotion();
    }

    public ArrayList<BeneficiaryUIModel> getListBeneficiaries() {
        return this.mListBeneficiary;
    }

    public Beneficiary getRecentCreatedBeneficiary() {
        Beneficiary beneficiary = BeneficiaryRepository.getInstance().getLastCreatedBeneficiary();
        BeneficiaryRepository.getInstance().setLastCreatedBeneficiary(null);
        return beneficiary;
    }

    /**
     * Synchronize Calculator if change delivery method inside Transactional
     */
    public boolean synchronizeCalculatorBeneficiaryMethod(BeneficiaryUIModel beneficiary) {
        if (beneficiary != null && !TextUtils.isEmpty(beneficiary.getDeliveryMethod().getType()) && !TextUtils.isEmpty(Objects.requireNonNull(beneficiary.getDeliveryMethod().getName()))) {
            String deliveryMethod = Objects.requireNonNull(beneficiary.getDeliveryMethod().getType());
            CalculatorInteractorImpl.getInstance().synchronizeCalculatorBeneficiaryMethod(deliveryMethod);
            return true;
        } else {
            return mListBeneficiary == null || mListBeneficiary.size() == 0;
        }
    }

    /**
     * Check if promotions in context availables
     */
    public boolean havePromotionsForContext() {
        return PromotionsRepository.getInstance().getListPromotionsAvailables().size() > 0;
    }

    /**
     * Clear promotion selected by user
     */
    public void clearPromotionSelected() {
        PromotionsRepository.getInstance().setPromotionSelectedByUser(null);
    }


    /**
     * Interactor Callbacks
     */
    private void propagateHeaderError() {
        if (mCallback != null && mHandler != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onRetrievingHeaderInfoError();
                }
            });
        }
    }

    private void postHeaderInfoReady() {
        if (mCallback != null && mHandler != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onHeaderInfoReady(mPayoutCountry, mUserCountry, mFirstCountrySelector);
                }
            });
        }
    }

    public void setPayoutCountrySelected(Pair<String, String> country) {
        AppCustomizationRepository.getInstance().setPayoutCountrySelected(country);
    }
}
