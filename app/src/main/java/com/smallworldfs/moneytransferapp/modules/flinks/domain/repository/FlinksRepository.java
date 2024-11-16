package com.smallworldfs.moneytransferapp.modules.flinks.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.FlinksResponse;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.server.FlinksRequest;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.service.FlinksNetworkDatasource;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

public class FlinksRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        FlinksNetworkDatasource providesFlinksNetworkDatasource();
    }

    public FlinksNetworkDatasource flinksNetworkDatasource;

    public  static  FlinksRepository sInstance = null;

    // Storaged info
    private FlinksResponse mFlinksResponse;

    public static FlinksRepository getInstance() {
        if (sInstance == null) {
            sInstance = new FlinksRepository();
        }
        return sInstance;
    }

    public FlinksRepository(){
        FlinksRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        FlinksRepository.DaggerHiltEntryPoint.class);
        flinksNetworkDatasource = hiltEntryPoint.providesFlinksNetworkDatasource();
    }

    public Observable<Response<FlinksResponse>> getFlinksInfo(final FlinksRequest request) {
        return flinksNetworkDatasource.getFlinksInfo(request);
    }

    public Observable<Response<FlinksResponse>> postVerifyFlinksState(final FlinksRequest request) {
        return flinksNetworkDatasource.postVerifyFlinksState(request);
    }

    public void setFlinksResponse(FlinksResponse mFlinksResponse) {
        this.mFlinksResponse = mFlinksResponse;
    }

}
