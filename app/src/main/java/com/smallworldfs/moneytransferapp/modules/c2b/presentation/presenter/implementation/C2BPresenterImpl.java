package com.smallworldfs.moneytransferapp.modules.c2b.presentation.presenter.implementation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.c2b.C2BContract;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.interactors.C2BInteractor;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.interactors.implementation.C2BInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse;
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.navigator.C2BNavigator;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.FormAbstracPresenter;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class C2BPresenterImpl extends FormAbstracPresenter implements C2BContract.Presenter,
        C2BInteractor.Callback {

    private C2BInteractorImpl mInteractor;
    private C2BContract.View mView;
    private Pair<String, String> country;
    private boolean fromTransactional;

    @Inject
    public C2BPresenterImpl(Context context) {
        super(AndroidSchedulers.mainThread(), context);
        this.mContext = context;
        this.mInteractor = new C2BInteractorImpl(this);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void setView(C2BContract.View mView, GenericActivity activity, Pair<String, String> country, boolean fromTransactional) {
        this.mView = mView;
        this.mActivity = activity;
        this.country = country;
        mView.configureView(country);
        if (country != null && country.first != null && !TextUtils.isEmpty(country.first) &&
                country.second != null && !TextUtils.isEmpty(country.second)) {
            this.country = country;
        } else {
            mView.showErrorView();
        }
        this.fromTransactional = fromTransactional;
        mInteractor.requestC2BForm(country);
    }

    @Override
    public void destroy() {
        if (mInteractor != null) {
            mInteractor.destroy();
        }
        super.destroy();
    }

    @Override
    public void click(String beneficiaryType) {
        if (country != null && country.first != null && country.second != null &&
                beneficiaryType != null && !TextUtils.isEmpty(beneficiaryType)) {
            if (fromTransactional) {
                C2BNavigator.showTransactionalActivity(mActivity, country, beneficiaryType);
            } else {
                C2BNavigator.navigateToNewBeneficiary(mActivity, country, beneficiaryType);
            }
        } else {
            onError();
        }
    }

    @Override
    public void retryC2BClick() {
        mView.hideErrorView();
        mView.showGeneralLoadingView();
        mInteractor.requestC2BForm(country);
    }

    @Override
    public void onSuccessful(C2BResponse c2BResponse) {
        mView.hideAllViews();
        if (c2BResponse != null && c2BResponse.getTypes() != null &&
                c2BResponse.getTypes().size() > 0) {
            int numOfbuttons = c2BResponse.getTypes().size();
            for (int i = 0; i < numOfbuttons; i++) {
                mView.addButton(c2BResponse.getTypes().get(i).getId(), c2BResponse.getTypes().get(i).getText(), numOfbuttons);
            }
        } else {
            onError();
        }
    }

    @Override
    public void onError() {
        mView.showErrorView();
    }

    @Override
    public void backToPreviousScreenWithResultOK() {
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }
}
