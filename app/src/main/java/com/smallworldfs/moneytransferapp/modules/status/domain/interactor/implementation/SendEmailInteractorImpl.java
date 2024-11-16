package com.smallworldfs.moneytransferapp.modules.status.domain.interactor.implementation;

import android.os.Handler;
import android.os.Looper;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.status.domain.interactor.SendEmailInteractor;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.SendEmailRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.repository.ContactSupportRepository;

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
 * Created by luismiguel on 4/10/17
 */
@Singleton
public class SendEmailInteractorImpl extends AbstractInteractor implements SendEmailInteractor {

    private CompositeSubscription mCompositeSubcription;
    private Handler mHandler;
    private SendEmailInteractor.Callback mCallback;


    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        UserDataRepository provideUserDataRepository();
    }

    public UserDataRepository userDataRepository;

    @Inject
    public SendEmailInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, SendEmailInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCompositeSubcription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCallback = callback;

        SendEmailInteractorImpl.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        SendEmailInteractorImpl.DaggerHiltEntryPoint.class);
        userDataRepository = hiltEntryPoint.provideUserDataRepository();
    }

    @Override
    public void run() { }

    @Override
    public void removeCallbacks() { }

    @Override
    public void destroy() {
        mHandler = null;
        mCallback = null;
        if (mCompositeSubcription != null) {
            mCompositeSubcription.clear();
            mCompositeSubcription = null;
        }
    }

    private void clearComposite() {
        if (mCompositeSubcription.hasSubscriptions()) {
            mCompositeSubcription.clear();
        }
    }

    public String getUserId() {
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            return user.getId();
        }
        return "";
    }

    public String getUserName(){
        User user = userDataRepository.retrieveUser();
        if (user != null) {
            return user.getName();
        }
        return "";
    }


    public void sendEmail(String subject, String message, String email) {
        clearComposite();
        User user = userDataRepository.retrieveUser();
        if (user != null) {

            SendEmailRequest request = new SendEmailRequest(subject, message, email, user.getUserToken(), user.getId());

            mCompositeSubcription.add(ContactSupportRepository.getInstance()
                    .sendEmail(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<Void>>() {
                        @Override
                        public void onCompleted() { }
                        @Override
                        public void onError(Throwable e) { onNext(null); }
                        @Override
                        public void onNext(Response<Void> response) {
                            clearComposite();
                            NewGenericError error = NewGenericError.processError(response);
                            if (error != null) {
                                postSendingEmailError();
                            } else {
                                postSendEmailSusccessfull();
                            }
                        }
                    }));
        }
    }

    private void postSendEmailSusccessfull() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onSendEmailSusccessfull();
                    }
                }
            });
        }
    }

    private void postSendingEmailError() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onSendEmailError();
                    }
                }
            });
        }
    }


}
