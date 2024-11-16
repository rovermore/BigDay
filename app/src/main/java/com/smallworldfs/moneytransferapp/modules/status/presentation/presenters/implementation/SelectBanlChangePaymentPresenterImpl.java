package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.implementation;

import static com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SelectBankChangePaymentActivity.RESULT_DATA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank;
import com.smallworldfs.moneytransferapp.modules.status.presentation.presenters.SelectBankChangePaymentPresenter;
import com.smallworldfs.moneytransferapp.utils.Constants;

import rx.Scheduler;

/**
 * Created by luismiguel on 5/10/17
 */
public class SelectBanlChangePaymentPresenterImpl extends GenericPresenterImpl implements SelectBankChangePaymentPresenter {

    private SelectBankChangePaymentPresenter.View mView;
    private Activity mActivity;

    public SelectBanlChangePaymentPresenterImpl(Scheduler observeOn, Context context, SelectBankChangePaymentPresenter.View view, Activity activity) {
        super(observeOn, context);
        this.mView = view;
        this.mActivity = activity;
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

    public void onBankSelected(Bank bank) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_DATA, bank);
        returnIntent.putExtra(Constants.REQUEST_CODES.REQUEST_CODE, Constants.REQUEST_CODES.SELECT_BANK_REQUEST_CODE);
        mActivity.setResult(Activity.RESULT_OK, returnIntent);
        mActivity.finish();
    }
}
