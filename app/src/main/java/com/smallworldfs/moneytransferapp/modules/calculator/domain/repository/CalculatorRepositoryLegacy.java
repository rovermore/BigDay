package com.smallworldfs.moneytransferapp.modules.calculator.domain.repository;

import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CurrenciesResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.DeliveryMethodsResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.RateResponse;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCalculateRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerCurrencieRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server.ServerDeliveryMethodsRequest;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.service.CalculatorNetworkDatasourceLegacy;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by luismiguel on 27/6/17
 */
public class CalculatorRepositoryLegacy {

    private static CalculatorRepositoryLegacy sInstance = null;
    public CalculatorNetworkDatasourceLegacy calculatorNetworkDatasourceLegacy;
    private TreeMap<String, String> mDeliveryMethods = new TreeMap<>();

    public CalculatorRepositoryLegacy() {
        CalculatorRepositoryLegacy.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        CalculatorRepositoryLegacy.DaggerHiltEntryPoint.class);
        calculatorNetworkDatasourceLegacy = hiltEntryPoint.providesCalculatorNetworkDatasource();
    }

    public static CalculatorRepositoryLegacy getInstance() {
        if (sInstance == null) {
            sInstance = new CalculatorRepositoryLegacy();
        }
        return sInstance;
    }

    public Observable<Response<CurrenciesResponse>> getCurrencies(final ServerCurrencieRequest request) {
        Observable<Response<CurrenciesResponse>> response = calculatorNetworkDatasourceLegacy.getCurrencies(request);

        return Observable.create(subscriber ->
                response.subscribe(new Subscriber<Response<CurrenciesResponse>>() {
                                       @Override
                                       public void onCompleted() {
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           subscriber.onNext(null);
                                       }

                                       @Override
                                       public void onNext(Response<CurrenciesResponse> response) {
                                           if (response != null) {
                                               if (response.body() != null && response.body().getMethods() != null) {
                                                   ArrayList<Method> methodsAux = new ArrayList<>();
                                                   for (Method m : response.body().getMethods()) {
                                                       if (m.getMethod() != null) {
                                                           Map.Entry<String, String> firstEntry = m.getMethod().firstEntry();
                                                           if (firstEntry != null) {
                                                               String key = firstEntry.getKey();
                                                               if (!TextUtils.isEmpty(key) && !key.equalsIgnoreCase(Constants.DELIVERY_METHODS.CASH_CARD_APP_PHYSICAL)) {
                                                                   methodsAux.add(m);
                                                               }
                                                           }
                                                       }
                                                   }
                                                   response.body().setMethods(methodsAux);
                                               }
                                               subscriber.onNext(response);
                                           } else {
                                               subscriber.onError(null);
                                           }
                                           subscriber.onCompleted();
                                       }
                                   }
                ));
    }

    public Observable<Response<RateResponse>> calculateRate(final ServerCalculateRequest request) {
        return calculatorNetworkDatasourceLegacy.calculateRate(request);
    }

    public Observable<Response<TreeMap<String, String>>> requestDeliveryMethods(final ServerDeliveryMethodsRequest request) {
        Observable<Response<DeliveryMethodsResponse>> response = calculatorNetworkDatasourceLegacy.requestDeliveryMethods(request);
        return Observable.create(subscriber ->
                response.subscribe(new Subscriber<Response<DeliveryMethodsResponse>>() {
                                       @Override
                                       public void onCompleted() {
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           subscriber.onNext(null);
                                       }

                                       @Override
                                       public void onNext(Response<DeliveryMethodsResponse> response) {
                                           if (response != null) {
                                               if (response.body() != null) {
                                                   mDeliveryMethods = response.body().getDeliveryMethods();
                                               }

                                               subscriber.onNext(null);
                                           } else {
                                               subscriber.onError(null);
                                           }
                                           subscriber.onCompleted();
                                       }
                                   }
                ));
    }

    public String getTranslatedDeliveryMethod(String deliveryMethod) {
        if (mDeliveryMethods != null && mDeliveryMethods.containsKey(deliveryMethod)) {
            return mDeliveryMethods.get(deliveryMethod);
        }
        return ConstantsKt.STRING_EMPTY;
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        CalculatorNetworkDatasourceLegacy providesCalculatorNetworkDatasource();
    }
}
