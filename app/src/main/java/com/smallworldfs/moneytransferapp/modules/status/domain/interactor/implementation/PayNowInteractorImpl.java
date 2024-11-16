package com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.PayNowInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PayNowUrlResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.PaymentUrl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TransactionDetailResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.PayTransactionRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ReceiptRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.TransactionDetailRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.repository.StatusRepositoryLegacy;

import java.io.File;

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
 * Created by luismiguel on 9/10/17
 */
@Singleton
public class PayNowInteractorImpl extends AbstractInteractor implements PayNowInteractor {

    private Handler mHandler;
    private CompositeSubscription mCompositeSubcription;
    private String mTransactionMtn;
    private PayNowInteractor.Callback mCallback;

    public enum HttpMethod {
        GET, POST;

        public static HttpMethod fromKey(String key) {
            for (HttpMethod method : values()) {
                if (method.name().equals(key)) return method;
            }
            return GET;
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public PayNowInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, String transactionMtn, PayNowInteractor.Callback callback) {
        super(observeOn, subscribeOn);

        this.mTransactionMtn = transactionMtn;
        this.mCallback = callback;

        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCompositeSubcription = new CompositeSubscription();

        PayNowInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        PayNowInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void run() {
        requestPaymentUrl(mTransactionMtn);
    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
        mHandler = null;
        clearComposite();
        mCompositeSubcription = null;
    }

    private void requestPaymentUrl(String transactionMtn) {

        if (!TextUtils.isEmpty(transactionMtn)) {

            User user = userDataRepository.retrieveUser();
            if (user != null) {

                PayTransactionRequest request = new PayTransactionRequest(user.getUserToken(), user.getId(), transactionMtn);

                mCompositeSubcription.add(StatusRepositoryLegacy.getInstance()
                        .payTransaction(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<Response<PayNowUrlResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(Response<PayNowUrlResponse> response) {
                                clearComposite();
                                NewGenericError error = NewGenericError.processError(response);
                                if (error != null) {
                                    postPaymentUrlError();
                                } else {
                                    postPaymentUrl(response.body().getPaymentUrl());
                                }
                            }
                        }));
            }
        }
    }


    private void clearComposite() {
        if (mCompositeSubcription != null) {
            mCompositeSubcription.clear();
        }
    }

    private void postPaymentUrl(final PaymentUrl paymentUrl) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onGetPaymentUrlSusccessfull(paymentUrl);
                }
            });
        }
    }

    private void postPaymentUrlError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onGetPaymentUrlError();
                }
            });
        }
    }

    public void requestTransactionData(String mtn) {
        if (mtn != null && !mtn.isEmpty()) {
            User user = userDataRepository.retrieveUser();
            if (user != null) {
                clearComposite();
                TransactionDetailRequest request = new TransactionDetailRequest(user.getUserToken(), user.getId(), mtn);
                mCompositeSubcription.add(StatusRepositoryLegacy.getInstance()
                        .getTransactionDetail(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<Response<TransactionDetailResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(Response<TransactionDetailResponse> response) {
                                clearComposite();
                                NewGenericError error = NewGenericError.processError(response);
                                if ((error != null) || (response == null) || (response.body() == null)) {
                                    postGetTransactionDetailError();
                                } else {
                                    postTransactionDetail(response.body().getTransaction());
                                }
                            }
                        }));
            }
        }
    }

    public void postGetTransactionDetailError() {}

    public void postTransactionDetail(Transaction transaction) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onTransactionReceived(transaction);
                }
            });
        }
    }


    public void getReceipt(String mtn, boolean offline, boolean pre) {
        clearComposite();

        User user = userDataRepository.retrieveUser();
        if (user != null) {
            if (!TextUtils.isEmpty(mtn)) {
                ReceiptRequest request = new ReceiptRequest(user.getId(), user.getUserToken(), mtn, offline, pre);

                mCompositeSubcription.add(new StatusRepositoryLegacy()
                        .getReceipt(request)
                        .subscribeOn(this.mSubscriberOn)
                        .observeOn(this.mObserveOn)
                        .subscribe(new Subscriber<File>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                onNext(null);
                            }

                            public void onNext(File file) {
                                clearComposite();
                                if (file != null) {
                                    postFilePdfReceived(file);
                                } else {
                                    postFileError();
                                }
                            }
                        }));
            }
        }
    }

    private void postFilePdfReceived(final File file) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onPdfReceiptReceived(file);
                    }
                }
            });
        }
    }

    private void postFileError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onPdfReceiptError();
                    }
                }
            });
        }
    }
}
