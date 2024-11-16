package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation;

import android.os.Handler;
import android.os.Looper;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.LocationMapInteractor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.LocationMapResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.LocationMapRequest;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.TransactionalRepository;


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
public class LocationMapInteractorImpl extends AbstractInteractor implements LocationMapInteractor {

    private CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private LocationMapInteractorImpl.Callback mCallback;
    private Payout mPayout;


    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public LocationMapInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, LocationMapInteractorImpl.Callback callback, Payout payout) {
        super(observeOn, subscribeOn);
        this.mCallback = callback;

        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mPayout = payout;

        LocationMapInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        LocationMapInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {

        if (mHandler != null) {
            mHandler = null;
        }
        if (mCallback != null) {
            mCallback = null;
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
        if (mPayout != null){
            mPayout = null;
        }
    }

    public void getPayoutLocationsMap() {
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            LocationMapRequest request = new LocationMapRequest(mPayout, user);
            mCompositeSubscription.add(TransactionalRepository.getInstance()
                    .requestLocationMapInfo(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<LocationMapResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onResponseErrorLocationsError();
                        }

                        public void onNext(Response<LocationMapResponse> locationMapResponse) {
                            NewGenericError error = NewGenericError.processError(locationMapResponse);
                            if (error == null) {
                                postLocationMapInfoReceived(locationMapResponse.body());
                            } else {
                                onResponseErrorLocationsError();
                            }
                        }
                    }));
        }
    }

    private void postLocationMapInfoReceived(final LocationMapResponse locationMapResponse) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onLocationsReceived(locationMapResponse);
                    }
                }
            });
        }
    }

    private void onResponseErrorLocationsError(){
        mCallback.onLocationsError();
    }

}
