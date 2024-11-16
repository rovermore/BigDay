package com.smallworldfs.moneytransferapp.modules.status.domain.repository;


import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.ContactSupportResponse;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.ContactSupportRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.server.SendEmailRequest;
import com.smallworldfs.moneytransferapp.modules.status.domain.service.ContactSupportService;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by luismiguel on 4/10/17
 */
public class ContactSupportRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        ContactSupportService providesContactSupportNetworkDatasource();
    }

    public ContactSupportService contactSupportService;

    public static ContactSupportRepository sInstance = null;

    // Storaged info
    private ContactSupportResponse mContactSupportResponse;

    public static ContactSupportRepository getInstance() {
        if (sInstance == null) {
            sInstance = new ContactSupportRepository();
        }
        return sInstance;
    }

    public ContactSupportRepository(){
        ContactSupportRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        ContactSupportRepository.DaggerHiltEntryPoint.class);
        contactSupportService = hiltEntryPoint.providesContactSupportNetworkDatasource();
    }

    private ContactSupportService getService() {
        return contactSupportService;
    }

    public Observable<Response<ContactSupportResponse>> getContactSupportInfo(final ContactSupportRequest request) {
        return Observable.create(subscriber -> {
            if (mContactSupportResponse != null) {
                Response<ContactSupportResponse> response = Response.success(mContactSupportResponse);
                subscriber.onNext(response);
                subscriber.onCompleted();
                return;
            }

            Observable<Response<ContactSupportResponse>> observable = getService().getContactSupportInfo(request);
            if (observable != null) {
                observable.subscribe(new Subscriber<Response<ContactSupportResponse>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<ContactSupportResponse> response) {
                        if (response != null) {
                            subscriber.onNext(response);
                        } else {
                            subscriber.onError(null);
                        }
                        subscriber.onCompleted();
                    }
                });
            } else {
                subscriber.onError(null);
            }
        });
    }

    public Observable<Response<Void>> sendEmail(final SendEmailRequest request) {
        return Observable.create(subscriber -> {
            Observable<Response<Void>> observable = getService().sendEmail(request);
            if (observable != null) {
                observable.subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onNext(null);
                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        if (response != null) {
                            subscriber.onNext(response);
                        } else {
                            subscriber.onError(null);
                        }
                        subscriber.onCompleted();
                    }
                });
            } else {
                subscriber.onError(null);
            }
        });
    }

    public void storeContactSupportResponse(ContactSupportResponse data){
        this.mContactSupportResponse = data;
    }

}
