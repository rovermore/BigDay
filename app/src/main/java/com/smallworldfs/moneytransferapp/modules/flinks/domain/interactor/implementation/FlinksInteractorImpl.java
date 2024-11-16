package com.smallworldfs.moneytransferapp.modules.flinks.domain.interactor.implementation;

import android.os.Looper;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.interactor.FlinksInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.FlinksResponse;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.model.server.FlinksRequest;
import com.smallworldfs.moneytransferapp.modules.flinks.domain.repository.FlinksRepository;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;

import android.os.Handler;

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
public class FlinksInteractorImpl extends AbstractInteractor implements FlinksInteractor {

    private CompositeSubscription mCompositeSubscription;
    private FlinksInteractor.Callback mCallback;
    private Handler mHandler;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public FlinksInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, FlinksInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCompositeSubscription = new CompositeSubscription();

        this.mCallback = callback;
        this.mHandler = new Handler(Looper.getMainLooper());

        FlinksInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        FlinksInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void run() {
        requestFlinksInformation();
    }

    @Override
    public void removeCallbacks() {
        mCallback = null;
    }

    @Override
    public void destroy() {

    }

    private void clearComposite() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    private void requestFlinksInformation(){

        User user = userDataRepository.retrieveUser();

        if (user != null){
            FlinksRequest request = new FlinksRequest(user.getId(), user.getUserToken());

            mCompositeSubscription.add(FlinksRepository.getInstance()
                    .getFlinksInfo(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<FlinksResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<FlinksResponse> response) {
                            clearComposite();
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postFlinksResponseError();
                            } else {
                                postFlinksResponseSuccess(response.body());
                            }
                        }
                    }));
        }
    }

    public void  verifyFlinksState(final String url){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String newFlinksState =  url.substring(url.length() - 1);
                updateFlinksState(newFlinksState);
                verifyFlinksState();
            }
        }, 10000);

    }

    public void verifyFlinksState () {

        User user = userDataRepository.retrieveUser();

        if (user != null){
            FlinksRequest request = new FlinksRequest(user.getId(), user.getUserToken());

            mCompositeSubscription.add(FlinksRepository.getInstance()
                    .postVerifyFlinksState(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<FlinksResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<FlinksResponse> response) {
                            clearComposite();
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postFlinksVerificationError();
                            } else {
                                postFlinksVerificationFinished(response.body().getData());
                            }
                        }
                    }));
        }
    }

    private void updateFlinksState(String newFlinksState) {
        User currenteUser = userDataRepository.retrieveUser();
        currenteUser.setFlinksState(newFlinksState);
    }

    //Responses

    private void postFlinksResponseSuccess(final FlinksResponse flinksResponse) {
        if (mHandler != null && this.mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        setFlinksResponse(flinksResponse);
                        mCallback.onFlinksUrlReady(flinksResponse.getData());
                    }
                }
            });
        }
    }

    private void postFlinksResponseError() {
        if (mHandler != null && this.mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        setFlinksResponse(null);
                        mCallback.onFlinksUrlError();
                    }
                }
            });
        }
    }

    private void postFlinksVerificationFinished(final String newFlinksState) {
        if (mHandler != null && this.mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        updateFlinksState(newFlinksState);
                        mCallback.onFlinksVerificationFinished();
                    }
                }
            });
        }
    }

    private void postFlinksVerificationError() {
        if (mHandler != null && this.mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onFlinksVerificationError();
                    }
                }
            });
        }
    }

    public void setFlinksResponse (FlinksResponse flinksResponse){
        FlinksRepository.getInstance().setFlinksResponse(flinksResponse);
    }


}
