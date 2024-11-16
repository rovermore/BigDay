package com.smallworldfs.moneytransferapp.modules.country.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryInfo;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.server.ServerCountryRequest;
import com.smallworldfs.moneytransferapp.modules.country.domain.service.CountryNetworkDatasource;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

public class CountryRepository {

    public static CountryRepository sInstance = null;
    public CountryNetworkDatasource countryNetworkDatasource;
    private Country mSendCountriesList = null;
    private Country mCountryPrefix = null;
    private CountryInfo mUserCountryInfo = null;

    public CountryRepository() {
        CountryRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CountryRepository.DaggerHiltEntryPoint.class);
        countryNetworkDatasource = hiltEntryPoint.providesCountryNetworkDatasource();
    }

    public static CountryRepository getInstance() {
        if (sInstance == null) {
            sInstance = new CountryRepository();
        }
        return sInstance;
    }

    public Observable<Response<Country>> requestOriginCountries(final ServerCountryRequest request) {
        return Observable.create(subscriber -> {
            if (mSendCountriesList != null && mSendCountriesList.getCountries().size() != 0) {
                Response<Country> response = Response.success(mSendCountriesList);
                subscriber.onNext(response);
                subscriber.onCompleted();
                return;
            }

            Observable<Response<Country>> response = countryNetworkDatasource.requestOriginCountries(request);
            response.subscribe(new Subscriber<Response<Country>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onNext(null);
                }

                @Override
                public void onNext(Response<Country> response) {
                    if (response != null) {
                        // Persist locally origin countries
                        if (response.body() != null && response.body().getCountries() != null) {
                            setPersistedOriginCountries(response.body());
                        }
                        subscriber.onNext(response);
                    } else {
                        subscriber.onError(null);
                    }
                    subscriber.onCompleted();
                }
            });
        });
    }

    public Observable<Response<Country>> requestPayoutCountries(final ServerCountryRequest request) {
        Observable<Response<Country>> response = countryNetworkDatasource.requestPayoutCountries(request);
        return Observable.create(subscriber ->
                response.subscribe(new Subscriber<Response<Country>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<Country> response) {
                        if (response != null) {
                            subscriber.onNext(response);
                        } else {
                            subscriber.onError(null);
                        }
                        onCompleted();
                    }
                })
        );
    }

    /**
     * Request payout countries with corridors with the new country model
     */
    public Observable<Response<CountryResponse>> requestPayoutCountriesNotSorted(final ServerCountryRequest request) {
        return countryNetworkDatasource.requestPayoutCountriesNotSorted(request);
    }

    public Observable<Response<Country>> requestCountryPrefix(final ServerCountryRequest request) {
        return Observable.create(subscriber -> {
            if (mCountryPrefix != null && mCountryPrefix.getCountries() != null && mCountryPrefix.getCountries().size() > 0) {
                Response<Country> response = Response.success(mCountryPrefix);
                subscriber.onNext(response);
                subscriber.onCompleted();
                return;
            }

            countryNetworkDatasource.requestCountryPrefix(request);
        });
    }

    public void setPersistedOriginCountries(Country countries) {
        this.mSendCountriesList = countries;
    }

    public void setCountryCodes(Country countryCodes) {
        if (this.mCountryPrefix == null) {
            this.mCountryPrefix = countryCodes;
        }
    }

    public void setCurrentUserCountryUserInfo(CountryInfo countryInfo) {
        this.mUserCountryInfo = countryInfo;
    }

    public CountryInfo getCountryUserInfo() {
        return this.mUserCountryInfo;
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        CountryNetworkDatasource providesCountryNetworkDatasource();
    }
}
