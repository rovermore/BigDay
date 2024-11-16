package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation;

import android.content.Context;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors.implementation.LocationMapInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.LocationMapResponse;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Locations;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.LocationMapPresenter;

import rx.Scheduler;

/**
 * Created by luismiguel on 28/8/17
 */
public class LocationMapPresenterImpl extends GenericPresenterImpl implements LocationMapPresenter, LocationMapInteractorImpl.Callback {

    private LocationMapPresenter.View mView;
    private Payout mPayout;
    private LocationMapInteractorImpl mInteractor;


    public LocationMapPresenterImpl(Scheduler observeOn, Scheduler susbscribeOn, Context context, LocationMapPresenter.View view, Payout payout) {
        super(observeOn, context);
        this.mPayout = payout;
        this.mView = view;
        mInteractor =  new LocationMapInteractorImpl(observeOn, susbscribeOn, this, mPayout);
    }

    @Override
    public void create() {
        super.create();
        mView.configureView();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void refresh() {

    }

    public void getContent(){
        mInteractor.getPayoutLocationsMap();
    }

    public void mapReady() {
        if (mPayout.locations != null && mPayout.locations.size() >0){
            onPayoutLocationsReady(mPayout);
        }else {
            getContent();
        }
    }

    private void onPayoutLocationsReady(Payout payout){
        mPayout = payout;
        mView.drawPoisInMap(mPayout);
        mView.hideLoadingView();
    }
    
    @Override
    public void onLocationsReceived(LocationMapResponse locationMapResponse){
        if (locationMapResponse != null && locationMapResponse.getLocations() != null && locationMapResponse.getLocations().size() > 0){
            mPayout.setLocations(locationMapResponse.getLocations());
            boolean locationsReady = false;
            for (Locations location : mPayout.locations){
                if (location.getLatitude() != null && location.getLongitude() != null){
                    locationsReady = true;
                }
            }
            if (locationsReady){
                onPayoutLocationsReady(mPayout);
            }else{
                onLocationsError();
            }
        }else{
            onLocationsError();
        }
    }

    @Override
    public void onLocationsError(){
        mView.hideLoadingView();
        mView.showMessageError();
    }
}

