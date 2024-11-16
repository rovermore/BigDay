package com.smallworldfs.moneytransferapp.modules.status.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.AdditionalInfo;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ChangePaymentMethodResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PayNowUrlResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionDetailResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionsResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ChangePaymentRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PayTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TipsRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionsRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.service.StatusNetworkDatasourceLegacy;

import java.io.File;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by luis on 29/9/17
 */
public class StatusRepositoryLegacy {

    public static StatusRepositoryLegacy sInstance = null;
    public StatusNetworkDatasourceLegacy statusNetworkDatasourceLegacy;

    public StatusRepositoryLegacy() {
        StatusRepositoryLegacy.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        StatusRepositoryLegacy.DaggerHiltEntryPoint.class);
        statusNetworkDatasourceLegacy = hiltEntryPoint.providesStatusNetworkDatasource();
    }

    public static StatusRepositoryLegacy getInstance() {
        if (sInstance == null) {
            sInstance = new StatusRepositoryLegacy();
        }
        return sInstance;
    }

    public Observable<Response<TransactionsResponse>> getTransactions(final TransactionsRequest request) {
        return statusNetworkDatasourceLegacy.getTransactions(request);
    }

    public Observable<Response<TransactionDetailResponse>> getTransactionDetail(final TransactionDetailRequest request) {
        return statusNetworkDatasourceLegacy.getTransactionDetail(request);
    }

    public Observable<Response<ChangePaymentMethodResponse>> changePayment(final ChangePaymentRequest request) {
        return statusNetworkDatasourceLegacy.changePaymentMethod(request);
    }

    public Observable<File> getReceipt(final ReceiptRequest request) {
        return statusNetworkDatasourceLegacy.getReceipt(request);
    }

    public Observable<Response<PayNowUrlResponse>> payTransaction(final PayTransactionRequest request) {
        return statusNetworkDatasourceLegacy.payTransaction(request);
    }

    public Observable<Response<AdditionalInfo>> getTips(final TipsRequest request) {
        return statusNetworkDatasourceLegacy.getTips(request);
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        StatusNetworkDatasourceLegacy providesStatusNetworkDatasource();
    }

}
