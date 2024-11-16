package com.smallworldfs.moneytransferapp.modules.transactional.domain.repository;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.MoreField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.service.GenericDropContentNetworkDatasource;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by luismiguel on 22/8/17
 */
public class GenericDropContentRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        GenericDropContentNetworkDatasource providesGenericDropContentNetworkDatasource();
    }

    public GenericDropContentNetworkDatasource genericDropContentNetworkDatasource;

    public static GenericDropContentRepository sInstance = null;

    public static GenericDropContentRepository getInstance() {
        if (sInstance == null) {
            sInstance = new GenericDropContentRepository();
        }
        return sInstance;
    }

    private GenericDropContentRepository() {
        GenericDropContentRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        GenericDropContentRepository.DaggerHiltEntryPoint.class);
        genericDropContentNetworkDatasource = hiltEntryPoint.providesGenericDropContentNetworkDatasource();
    }

    public Observable<Response<GenericKeyValueDropContent>> getDropContent(final String url) {
        return genericDropContentNetworkDatasource.getDropContent(url);
    }

    public Observable<Response<MoreField>> getMoreFieldsBasedOnRequestData(final String url) {
        return genericDropContentNetworkDatasource.getMoreFieldsBasedOnRequestData(url);
    }
}
