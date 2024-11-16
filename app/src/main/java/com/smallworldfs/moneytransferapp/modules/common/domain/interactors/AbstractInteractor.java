package com.smallworldfs.moneytransferapp.modules.common.domain.interactors;


import rx.Scheduler;

/**
 * Created by luis on 13/2/17
 */
public abstract class AbstractInteractor implements Interactor {

    protected Scheduler mObserveOn;
    protected Scheduler mSubscriberOn;

    public AbstractInteractor(Scheduler observeOn, Scheduler subscribeOn) {
        mObserveOn = observeOn;
        mSubscriberOn = subscribeOn;
    }
}
