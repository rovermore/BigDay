package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation;

import android.os.Handler;
import android.os.Looper;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.GenericSelectDropContentInteractor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server.GenericKeyValueDropContent;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.repository.GenericDropContentRepository;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class GenericSelectDropContentInteractorImpl extends AbstractInteractor implements GenericSelectDropContentInteractor {

    private final CompositeSubscription mCompositeSubscription;
    private Handler mHandler;
    private GenericSelectDropContentInteractor.Callback mCallback;


    public GenericSelectDropContentInteractorImpl(Scheduler observeOn, Scheduler subscribeOn, GenericSelectDropContentInteractor.Callback callback) {
        super(observeOn, subscribeOn);
        this.mCompositeSubscription = new CompositeSubscription();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCallback = callback;
    }

    @Override
    public void run() {
    }

    @Override
    public void removeCallbacks() {
    }

    @Override
    public void destroy() {
        clearComposite();
        mHandler = null;
        mCallback = null;
    }

    private static final HashMap<String, Observable<Response<GenericKeyValueDropContent>>> observableCache = new HashMap<>();

    public void requestData(String urlApi) {
        Observable<Response<GenericKeyValueDropContent>> observable;
        Log.e("TAG", "----------------------------------requestData: N CACHE: " + observableCache.size());
        if (observableCache.containsKey(urlApi)) {
            observable = observableCache.get(urlApi);
        } else {
            if (observableCache.size() > 10) observableCache.clear();//MAX OBSERVES
            observable = GenericDropContentRepository.getInstance()
                    .getDropContent(urlApi)
                    .cache()
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn);
            observableCache.put(urlApi, observable);
        }
        if (observable != null)
            mCompositeSubscription.add(observable.subscribe(new Subscriber<Response<GenericKeyValueDropContent>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    onNext(null);
                }

                @Override
                public void onNext(Response<GenericKeyValueDropContent> response) {
                    clearComposite();
                    NewGenericError error = NewGenericError.processError(response);
                    if (error == null && response.body() != null && response.body().getData() != null && response.body().getData().size() != 0) {
                        postDataReceived(response.body().getData());
                    } else {
                        postErrorRetrievingData();
                    }
                }
            }));
    }

    private void clearComposite() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    private void postDataReceived(final ArrayList<TreeMap<String, String>> data) {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onDataReceived(data);
                }
            });
        }
    }

    private void postErrorRetrievingData() {
        if (mHandler != null && mCallback != null) {
            mHandler.post(() -> {
                if (mCallback != null) {
                    mCallback.onErrorRetrievingData();
                }
            });
        }
    }
}
