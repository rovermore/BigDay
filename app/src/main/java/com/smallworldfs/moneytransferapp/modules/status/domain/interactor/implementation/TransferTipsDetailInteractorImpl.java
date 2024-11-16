package com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation;

import android.os.Handler;
import android.os.Looper;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.TransferTipsDetailsInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.AdditionalInfo;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TipsRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.repository.StatusRepositoryLegacy;

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

/**
 * Created by luismiguel on 27/10/17
 */
@Singleton
public class TransferTipsDetailInteractorImpl extends AbstractInteractor implements TransferTipsDetailsInteractor {

    private CompositeSubscription mCompositeSubcription;
    private Handler mHandler;
    private String mPaymentMethod;
    private TransferTipsDetailsInteractor.Callback mCallback;


    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public TransferTipsDetailInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, String paymentMethod, TransferTipsDetailsInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCompositeSubcription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mPaymentMethod = paymentMethod;
        this.mCallback = callback;

        TransferTipsDetailInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        TransferTipsDetailInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void run() {
        getTips();
    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
        if (mCompositeSubcription != null && mCompositeSubcription.hasSubscriptions()) {
            mCompositeSubcription.clear();
        }
        mHandler = null;
        mCallback = null;


    }


    private void getTips() {
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            TipsRequest request = new TipsRequest(user.getId(), user.getUserToken(), mPaymentMethod, user.getCountry().firstKey());
            mCompositeSubcription.add(StatusRepositoryLegacy.getInstance()
                    .getTips(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<AdditionalInfo>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        public void onNext(Response<AdditionalInfo> response) {
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postTipsError();
                            } else {
                                postTipsSusccessfull(response.body());
                            }
                        }
                    }));
        }
    }

    private void postTipsSusccessfull(final AdditionalInfo additionalInfo) {
        if (additionalInfo != null && additionalInfo.getTips() != null) {
            if (mHandler != null && mCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) {
                            mCallback.onTipsReceivedSusccessfull(additionalInfo.getTips());
                        }
                    }
                });
            }
        } else {
            postTipsError();
        }
    }

    private void postTipsError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onTipsError();
                    }
                }
            });
        }
    }
}
