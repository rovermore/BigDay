package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation;

import android.content.Context;

import androidx.annotation.NonNull;

import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.PayoutLocationSelectorPresenter;

import java.util.ArrayList;

import rx.Scheduler;

/**
 * Created by luismiguel on 25/8/17
 */
public class PayoutLocationSelectorPresenterImpl extends GenericPresenterImpl implements PayoutLocationSelectorPresenter {

    private PayoutLocationSelectorPresenter.View mView;

    public PayoutLocationSelectorPresenterImpl(Scheduler observeOn, Context context, PayoutLocationSelectorPresenter.View view) {
        super(observeOn, context);
        this.mView = view;
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

    @Override
    public void onPayouLocationsLoaded(@NonNull ArrayList<Field> payoutLocation) {
        if (!payoutLocation.isEmpty()) {
            mView.configureDataInAdapter(payoutLocation,
                    CalculatorInteractorImpl.getInstance().getSendingCurrency());
        }
    }
}
