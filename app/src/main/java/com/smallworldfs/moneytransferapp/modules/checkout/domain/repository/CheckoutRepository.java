package com.smallworldfs.moneytransferapp.modules.checkout.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.server.CheckoutRequest;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.service.CheckoutService;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by luismiguel on 26/9/17
 */
public class CheckoutRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        CheckoutService providesCheckoutNetworkDatasource();
    }

    public CheckoutService checkoutService;

    public static CheckoutRepository sInstance = null;

    public static CheckoutRepository getInstance() {
        if (sInstance == null) {
            sInstance = new CheckoutRepository();
        }
        return sInstance;
    }

    public CheckoutRepository(){
        CheckoutRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CheckoutRepository.DaggerHiltEntryPoint.class);
        checkoutService = hiltEntryPoint.providesCheckoutNetworkDatasource();
    }

    public CheckoutService getService() {
        return checkoutService;
    }

    public Observable<Response<Checkout>> getCheckoutInformation(final CheckoutRequest request) {
        return Observable.create(subscriber -> {
            Observable<Response<Checkout>> observable = getService().getCheckoutInfo(request);
            if (observable != null) {
                observable.subscribe(new Subscriber<Response<Checkout>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<Checkout> response) {
                        subscriber.onNext(response);
                    }
                });
            } else {
                subscriber.onError(null);
            }
        });
    }

    public Observable<Response<CreateTransactionResponse>> createTransaction(final CheckoutRequest request) {
        return Observable.create(subscriber -> {
            Observable<Response<CreateTransactionResponse>> observable = getService().createTransaction(request);
            if (observable != null) {
                observable.subscribe(new Subscriber<Response<CreateTransactionResponse>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<CreateTransactionResponse> response) {
                        subscriber.onNext(response);
                    }
                });
            } else {
                subscriber.onError(null);
            }
        });
    }
}
