package com.smallworldfs.moneytransferapp.modules.c2b.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.server.C2BRequest;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.service.C2BNetworkDatasource;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

public class C2BRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        C2BNetworkDatasource providesC2BNetworkDatasource();
    }

    public C2BNetworkDatasource c2BNetworkDatasource;

    public static C2BRepository sInstance = null;

    public static C2BRepository getInstance() {
        if (sInstance == null) {
            sInstance = new C2BRepository();
        }
        return sInstance;
    }

    public C2BRepository(){
        C2BRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        C2BRepository.DaggerHiltEntryPoint.class);
        c2BNetworkDatasource = hiltEntryPoint.providesC2BNetworkDatasource();
    }

    /**
     * Request transactional structure
     */
    public Observable<Response<C2BResponse>> requestBeneficiaryTypes(final C2BRequest request) {
        return c2BNetworkDatasource.requestBeneficiaryTypes(request);
    }
}
