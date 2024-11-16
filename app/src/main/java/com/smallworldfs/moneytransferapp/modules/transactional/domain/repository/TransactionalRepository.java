package com.smallworldfs.moneytransferapp.modules.transactional.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.LocationMapResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ClearTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.LocationMapRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.QuickReminderRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ServerTransactionalRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.ValidateTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.TransactionalStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.ValidationStepResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.TransactionalNetworkDatasource;

import java.util.ArrayList;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by luismiguel on 18/7/17
 */
public class TransactionalRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        TransactionalNetworkDatasource providesTransactionalNetworkDatasource();
    }

    public TransactionalNetworkDatasource transactionalNetworkDatasource;

    public static TransactionalRepository sInstance = null;

    private ArrayList<Field> mPayoutLocations = null;

    public static TransactionalRepository getInstance() {
        if (sInstance == null) {
            sInstance = new TransactionalRepository();
        }
        return sInstance;
    }

    public TransactionalRepository(){
        TransactionalRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        TransactionalRepository.DaggerHiltEntryPoint.class);
        transactionalNetworkDatasource = hiltEntryPoint.providesTransactionalNetworkDatasource();
    }

    /**
     * Request transactional structure
     *
     */
    public Observable<Response<TransactionalStepResponse>> requestTransactionalStructureSteps(final ServerTransactionalRequest request) {
        return transactionalNetworkDatasource.requestTransactionalStructureSteps(request);
    }

    public Observable<Response<ValidationStepResponse>> validateStep(final ValidateTransactionRequest request) {
        return transactionalNetworkDatasource.validateStep(request);
    }

    public Observable<Response<QuickReminderResponse>> requestQuickReminderInfo(final QuickReminderRequest request) {
        return transactionalNetworkDatasource.requestQuickReminderInfo(request);
    }

    public Observable<Response<LocationMapResponse>> requestLocationMapInfo(final LocationMapRequest request) {
        return transactionalNetworkDatasource.requestLocationMapInfo(request);
    }

    public Observable<Response<Void>> clearTransaction(final ClearTransactionRequest request) {
        return transactionalNetworkDatasource.clearTransaction(request);
    }

    public void persistPayoutLocations(ArrayList<Field> locations) {
        if (mPayoutLocations == null) {
            mPayoutLocations = new ArrayList<>();
        }
        mPayoutLocations.clear();
        mPayoutLocations.addAll(locations);
    }

    public void clearPayoutLocations() {
        if (mPayoutLocations != null) {
            mPayoutLocations.clear();
        }
        mPayoutLocations = null;
    }
}
