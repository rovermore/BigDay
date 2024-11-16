package com.smallworldfs.moneytransferapp.modules.country.domain.interactors.implementation;

import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.CONTRY_PREFIX_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.ORIGIN_COUNTRIES_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.PAYOUT_COUNTRIES_TYPE;

import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.interactors.CountryInteractor;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.server.ServerCountryRequest;
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;

import java.util.ArrayList;
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

@Singleton
public class CountryInteractorImpl extends AbstractInteractor implements CountryInteractor {

    private static CountryInteractorImpl sInstance = null;
    private final Handler mHandler;
    public LoginRepository loginRepository;
    private CompositeSubscription mCompositeSubscription;
    private CompositeSubscription mCompositeSubscription2;
    private ArrayList<CountryInteractor.Callback> mListCallbacks;
    private ArrayList<CountryInteractor.CountryCodesCallback> mCodesCallbacks;

    @Inject
    public CountryInteractorImpl(Scheduler observeOn, Scheduler subscribeOn) {
        super(observeOn, subscribeOn);
        this.mHandler = new Handler(Looper.getMainLooper());

        CountryInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CountryInteractorImpl.DaggerHiltEntryPoint.class);
        loginRepository = hiltEntryPoint.provideLoginRepository();
    }

    public static CountryInteractorImpl getInstance() {
        if (sInstance == null) {
            sInstance = new CountryInteractorImpl(AndroidSchedulers.mainThread(), Schedulers.io());
            sInstance.init();
        }
        return sInstance;
    }

    private void init() {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription2 = new CompositeSubscription();
        mListCallbacks = new ArrayList<>();
        mCodesCallbacks = new ArrayList<>();
    }

    @Override
    public void run() {
    }

    @Override
    public void removeCallbacks() {
    }

    @Override
    public void destroy() {
        if (mListCallbacks != null) {
            mListCallbacks.clear();
            mListCallbacks = null;
        }
        if (mCodesCallbacks != null) {
            mCodesCallbacks.clear();
            mCodesCallbacks = null;
        }
        clearComposite();
        clearComposite2();
    }

    /**
     * Call request to show origin countries
     */
    public void getOriginCountries() {
        ServerCountryRequest request = new ServerCountryRequest(ORIGIN_COUNTRIES_TYPE);
        mCompositeSubscription.add(CountryRepository.getInstance()
                .requestOriginCountries(request)
                .subscribeOn(mSubscriberOn).observeOn(mObserveOn)
                .subscribe(new Subscriber<Response<Country>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNext(null);
                    }

                    @Override
                    public void onNext(retrofit2.Response<Country> response) {
                        if (response != null && response.body() != null) {
                            postOriginCountryReceived(response.body());
                        }
                    }
                }));
    }

    /**
     * Call request to show all countries by origin
     */
    public void getPayoutCountries(Pair<String, String> fromCountry) {
        if (fromCountry != null) {
            ServerCountryRequest request = new ServerCountryRequest(PAYOUT_COUNTRIES_TYPE, fromCountry.first);
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

                        @Override
                        public void onNext(retrofit2.Response<Country> response) {
                            if (response != null && response.body() != null && response.body().getCountries() != null) {
                                postPayoutCountriesReceived(response.body());
                            } else {
                                postPayoutCountriesError();
                            }
                        }
                    }));
        }
    }

    /**
     * Call request to show all countries as they come from the server
     */
    public void getPayoutCountriesNotSorted(Pair<String, String> fromCountry) {
        if (fromCountry != null) {
            ServerCountryRequest request = new ServerCountryRequest(PAYOUT_COUNTRIES_TYPE, fromCountry.first, true);
            mCompositeSubscription.add(CountryRepository.getInstance().requestPayoutCountriesNotSorted(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<CountryResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        @Override
                        public void onNext(Response<CountryResponse> response) {
                            if (response != null && response.body() != null && response.body().getCountries() != null) {
                                postPayoutCountriesReceivedNotSorted(response.body());
                            } else {
                                postPayoutCountriesError();
                            }
                        }
                    }));
        }
    }

    /**
     * Call request to get all countries prefix
     */
    public void getCountryPrefix() {
        final ServerCountryRequest request = new ServerCountryRequest(CONTRY_PREFIX_TYPE);
        mCompositeSubscription.add(CountryRepository.getInstance()
                .requestCountryPrefix(request)
                .subscribeOn(this.mSubscriberOn)
                .observeOn(this.mObserveOn)
                .subscribe(new Subscriber<Response<Country>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNext(null);
                    }

                    @Override
                    public void onNext(retrofit2.Response<Country> response) {
                        if (response != null && response.body() != null && response.body().getCountries() != null) {
                            CountryRepository.getInstance().setCountryCodes(response.body());
                            postCountryCodes(response.body().getSortedCountries());
                        }
                    }
                }));
    }

    private void clearComposite() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) mCompositeSubscription.clear();
    }

    private void clearComposite2() {
        if (mCompositeSubscription2 != null && mCompositeSubscription2.hasSubscriptions()) mCompositeSubscription2.clear();
    }

    private void postCountryCodes(final TreeMap<String, String> countries) {
        if (mHandler != null && mCodesCallbacks != null) {
            mHandler.post(() -> {
                if (mCodesCallbacks != null) {
                    for (CountryCodesCallback callback : mCodesCallbacks) {
                        callback.onCountryCodesReady(countries);
                    }
                }
            });
        }
    }

    private void postOriginCountryReceived(final Country country) {
        if (mListCallbacks != null && mHandler != null) {
            mHandler.post(() -> {
                if (mListCallbacks != null) {
                    for (Callback callback : new ArrayList<>(mListCallbacks)) {
                        callback.onOriginCountriesReceived(country);
                    }
                }
            });
        }
    }

    private void postPayoutCountriesReceived(final Country countries) {
        if (mHandler != null && mListCallbacks != null) {
            mHandler.post(() -> {
                if (mListCallbacks != null) {
                    for (Callback callback : new ArrayList<>(mListCallbacks)) {
                        callback.onPayoutCountriesReceived(countries);
                    }
                }
            });
        }
    }

    private void postPayoutCountriesReceivedNotSorted(final CountryResponse countries) {
        if (mHandler != null && mListCallbacks != null) {
            mHandler.post(() -> {
                if (mListCallbacks != null) {
                    for (Callback callback : new ArrayList<>(mListCallbacks)) {
                        callback.onPayoutCountriesReceivedNotSorted(countries);
                    }
                }
            });
        }
    }

    private void postPayoutCountriesError() {
        if (mHandler != null && mListCallbacks != null) {
            mHandler.post(() -> {
                if (mListCallbacks != null) {
                    for (Callback callback : new ArrayList<>(mListCallbacks)) {
                        callback.onPayoutCountriesError();
                    }
                }
            });
        }
    }

    public void addCallback(CountryInteractor.Callback callback) {
        if (mListCallbacks != null && !mListCallbacks.contains(callback)) {
            mListCallbacks.add(callback);
        }
    }

    public void removeCallback(CountryInteractor.Callback callback) {
        if (mListCallbacks != null) {
            mListCallbacks.remove(callback);
        }
    }

    public void addCodesCallback(CountryInteractor.CountryCodesCallback callback) {
        if (mCodesCallbacks != null && !mCodesCallbacks.contains(callback)) {
            mCodesCallbacks.add(callback);
        }
    }

    public void removeCodesCallback(CountryInteractor.CountryCodesCallback callback) {
        if (mCodesCallbacks != null) {
            mCodesCallbacks.remove(callback);
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        LoginRepository provideLoginRepository();
    }
}
