package com.smallworldfs.moneytransferapp.modules.c2b.domain.interactors.implementation;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.c2b.domain.interactors.C2BInteractor;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.server.C2BRequest;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.repository.C2BRepository;
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class C2BInteractorImpl extends AbstractInteractor {

    private CompositeSubscription mCompositeSubscription;
    private C2BInteractor.Callback mCallback;

    public C2BInteractorImpl(C2BInteractor.Callback callback) {
        super(AndroidSchedulers.mainThread(), Schedulers.io());

        this.mCallback = callback;
        this.mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void run() {

    }

    @Override
    public void removeCallbacks() {

    }

    @Override
    public void destroy() {
        if (mCallback != null) {
            mCallback = null;
        }
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    public void requestC2BForm(final Pair<String, String> country) {
        if (country != null) {
            final C2BRequest request = new C2BRequest(country.first);
            mCompositeSubscription.add(C2BRepository.getInstance()
                    .requestBeneficiaryTypes(request)
                    .subscribeOn(this.mSubscriberOn)
                    .observeOn(this.mObserveOn)
                    .subscribe(new Subscriber<Response<C2BResponse>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            onNext(null);
                        }

                        @Override
                        public void onNext(Response<C2BResponse> response) {
                            if (response != null && response.body() != null && response.body().getTypes() != null
                                    && response.body().getTypes().size() > 0) {

                                mCallback.onSuccessful(response.body());
                            } else {
                                mCallback.onError();
                            }
                        }
                    }));
        } else {
            mCallback.onError();
        }
    }
}
